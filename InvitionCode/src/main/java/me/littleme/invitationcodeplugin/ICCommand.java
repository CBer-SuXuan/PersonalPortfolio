package me.littleme.invitationcodeplugin;

import me.clip.placeholderapi.PlaceholderAPI;
import me.littleme.invitationcodeplugin.commands.*;
import me.littleme.invitationcodeplugin.config.ConfigMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ICCommand implements CommandExecutor {

    private final InvitationCodePlugin main;

    public ICCommand(InvitationCodePlugin main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length >= 1) {
                if (args[0].equalsIgnoreCase("reset")) {

                    if (args.length == 3) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        UUID uuid = offlinePlayer.getUniqueId();
                        if (!isOldPlayer((Player) offlinePlayer)) {main.getDatabase().setCodeZero(player);}
                        ResetCommand resetCommand = new ResetCommand(main);
                        if (args[2].equalsIgnoreCase("used")) {
                            resetCommand.resetUsed(player, uuid);
                        } else if (args[2].equalsIgnoreCase("times")) {
                            resetCommand.resetUseTimes(player, uuid);
                        }
                    } else {
                        onError(player);
                    }

                }
                else if (args[0].equalsIgnoreCase("old")) {

                    if (args.length == 2) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        UUID uuid = offlinePlayer.getUniqueId();
                        if (main.getDatabase().isForceNewPlayer(uuid)) {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.ALREADY_NEW_MESSAGE));
                            return true;
                        }
                        if (main.getDatabase().isForceOldPlayer(uuid)) {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.CANCEL_FORCE_OLD_MESSAGE));
                            main.getDatabase().setCodeZero(player);
                            main.getDatabase().setForceOld(uuid, 0);
                        } else {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.FORCE_OLD_MESSAGE));
                            main.getDatabase().setForceOld(uuid, 1);
                        }
                    } else {
                        onError(player);
                    }

                }
                else if (args[0].equalsIgnoreCase("new")) {

                    if (args.length == 2) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        UUID uuid = offlinePlayer.getUniqueId();
                        if (main.getDatabase().isForceOldPlayer(uuid)) {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.ALREADY_OLD_MESSAGE));
                            return true;
                        }
                        if (main.getDatabase().isForceNewPlayer(uuid)) {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.CANCEL_FORCE_NEW_MESSAGE));
                            main.getDatabase().setCodeZero(player);
                            main.getDatabase().setForceNew(uuid, 0);
                        } else {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.FORCE_NEW_MESSAGE));
                            main.getDatabase().setForceNew(uuid, 1);
                        }
                    } else {
                        onError(player);
                    }

                }
                else if (args[0].equalsIgnoreCase("get")) {

                    GetCommand getCommand = new GetCommand(main);
                    getCommand.useCommand(player, args);

                }
                else if (args[0].equalsIgnoreCase("test")) {

                    if (args.length == 2) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (!isOldPlayer((Player) offlinePlayer)) {main.getDatabase().setCodeZero((Player) offlinePlayer);}
                        UUID uuid = offlinePlayer.getUniqueId();
                        if (main.getDatabase().isPlayerCodeInDatabase(uuid)) {
                            main.getDatabase().setTimes(uuid);
                            player.sendMessage(PlaceholderAPI.setPlaceholders(offlinePlayer, main.manager.getMessage(ConfigMessage.PLUS_ONE_MESSAGE)));
                            rewardPlayer(offlinePlayer, player);
                        } else {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.NOCODE_NOEXIST_MESSAGE));
                        }
                    } else {
                        onError(player);
                    }

                }
                else if (args[0].equalsIgnoreCase("add")) {

                    if (args.length == 3) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        UUID uuid = offlinePlayer.getUniqueId();
                        if (!isOldPlayer((Player) offlinePlayer)) {main.getDatabase().setCodeZero(player);}
                        if (main.getDatabase().isPlayerCodeInDatabase(uuid)) {
                            try {
                                int num = Integer.parseInt(args[2]);
                                main.getDatabase().addTimes(uuid, num);
                                player.sendMessage(PlaceholderAPI.setPlaceholders(offlinePlayer, main.manager.getMessage(ConfigMessage.ADD_SUCCESS_MESSAGE)));
                            } catch (Exception e) {
                                player.sendMessage(main.manager.getMessage(ConfigMessage.NUMBER_WRONG_MESSAGE));
                            }
                        } else {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.NOCODE_NOEXIST_MESSAGE));
                        }
                    } else if (args.length == 2) {
                        player.sendMessage(main.manager.getMessage(ConfigMessage.ADD_WRONG_MESSAGE));
                    } else {
                        onError(player);
                    }

                }
                else if (args[0].equalsIgnoreCase("take")) {

                    if (args.length == 3) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        UUID uuid = offlinePlayer.getUniqueId();
                        if (!isOldPlayer((Player) offlinePlayer)) {main.getDatabase().setCodeZero(player);}
                        if (main.getDatabase().isPlayerCodeInDatabase(uuid)) {
                            try {
                                int num = Integer.parseInt(args[2]);
                                main.getDatabase().removeTimes(uuid, num);
                                if (main.getDatabase().getTimes(uuid) < 0) {
                                    player.sendMessage(PlaceholderAPI.setPlaceholders(offlinePlayer, main.manager.getMessage(ConfigMessage.TAKE_OVER_MESSAGE)));
                                    main.getDatabase().addTimes(uuid, num);
                                } else {
                                    player.sendMessage(PlaceholderAPI.setPlaceholders(offlinePlayer, main.manager.getMessage(ConfigMessage.TAKE_SUCCESS_MESSAGE)));
                                }
                            } catch (Exception e) {
                                player.sendMessage(main.manager.getMessage(ConfigMessage.NUMBER_WRONG_MESSAGE));
                            }
                        } else {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.NOCODE_NOEXIST_MESSAGE));
                        }
                    } else if (args.length == 2) {
                        player.sendMessage(main.manager.getMessage(ConfigMessage.TAKE_WRONG_MESSAGE));
                    } else {
                        onError(player);
                    }

                }
                else if (args[0].equalsIgnoreCase("remove")) {
                    if (args.length == 2) {
                        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[1]);
                        if (main.getDatabase().isPlayerUUIDInDatabase(offlinePlayer.getUniqueId())) {
                            main.getDatabase().setCodeZero((Player) offlinePlayer);
                            player.sendMessage(main.manager.getMessage(ConfigMessage.REMOVE_MESSAGE));
                        } else {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.NOCODE_NOEXIST_MESSAGE));
                        }
                    } else {
                        onError(player);
                    }
                }
                else if (args[0].equalsIgnoreCase("help")) {

                    if (args.length == 1) {
                        if (player.hasPermission("ic")) {
                            player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "----------------------------------------");
                            player.sendMessage(main.manager.getMessage(ConfigMessage.HELP_OP_MESSAGE));
                            player.sendMessage(ChatColor.GRAY + "Plugin Author: Little_Me666");
                            player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "----------------------------------------");
                        } else {
                            player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "----------------------------------------");
                            player.sendMessage(main.manager.getMessage(ConfigMessage.HELP_PLAYER_MESSAGE));
                            player.sendMessage(ChatColor.GRAY + "Plugin Author: Little_Me666");
                            player.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD + "----------------------------------------");
                        }
                    } else {
                        onError(player);
                    }

                }
                else if (args[0].equalsIgnoreCase("reload")) {

                    if (player.hasPermission("ic")) {
                        main.reloadConfig();
                        main.onEnable();
                        sender.sendMessage(main.manager.getMessage(ConfigMessage.RELOAD_MESSAGE));
                    }

                }
                else {
                    if (args.length == 1) {
                        try {
                            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(main.getDatabase().getPlayerFromCode(args[0]));
                            int is = main.getDatabase().getUsed(player.getUniqueId());
                            if (isOldPlayer(player)) {
                                player.sendMessage(main.manager.getMessage(ConfigMessage.ALREADY_OLD_PLAYER_WRONG));
                            } else {
                                if (is == 1) {
                                    player.sendMessage(main.manager.getMessage(ConfigMessage.ALREADY_NEW_PLAYER_WRONG));
                                } else {
                                    if (main.getDatabase().getPlayerFromCode(args[0]) != null && isOldPlayer((Player) offlinePlayer)) {
                                        String message = PlaceholderAPI.setPlaceholders(offlinePlayer, main.manager.getMessage(ConfigMessage.INPUT_CODE_MESSAGE));
                                        player.sendMessage(message);
                                        main.getDatabase().setUsed(player);
                                        main.getDatabase().setTimes(offlinePlayer.getUniqueId());
                                        rewardPlayer(offlinePlayer, player);
                                    } else {
                                        main.getDatabase().setCodeZero(player);
                                        player.sendMessage(main.manager.getMessage(ConfigMessage.CODE_WRONG_MESSAGE));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            player.sendMessage(main.manager.getMessage(ConfigMessage.NOCODE_NOEXIST_MESSAGE));
                        }
                    } else {
                        onError(player);
                    }
                }
            } else {
                onError(player);
            }
        }
        return true;
    }

    public void onError(Player player) {
        player.sendMessage(main.manager.getMessage(ConfigMessage.COMMAND_WRONG_MESSAGE));
    }

    public void rewardPlayer(OfflinePlayer oldPlayer, Player newPlayer) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        List<String> reward_commands = main.getConfig().getStringList("rewards");
        for (String reward_command : reward_commands) {
            String reward;
            if (reward_command.contains("%ic_new%")) {
                reward = PlaceholderAPI.setPlaceholders(newPlayer, reward_command);
            } else {
                reward = PlaceholderAPI.setPlaceholders(oldPlayer, reward_command);
            }
            Bukkit.dispatchCommand(console, reward);
        }
        newPlayer.sendMessage(main.manager.getMessage(ConfigMessage.REWARD_MESSAGE));
    }

    public boolean isOldPlayer(Player player) {
        if (main.getDatabase().isForceOldPlayer(player.getUniqueId())) {
            return true;
        } else if (main.getDatabase().isForceNewPlayer(player.getUniqueId())) {
            return false;
        } else {
            double first = Bukkit.getOfflinePlayer(player.getUniqueId()).getFirstPlayed();
            double now = new Date().getTime();
            return ((now - first) / 1000 / (60 * 60)) >= main.getConfig().getInt("time_as_old_player");
        }
    }

}
