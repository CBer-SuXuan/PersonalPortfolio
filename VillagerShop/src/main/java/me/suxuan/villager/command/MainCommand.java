package me.suxuan.villager.command;

import me.suxuan.villager.VillagerShop;
import me.suxuan.villager.cache.TradeCache;
import me.suxuan.villager.menu.TradeConfigMenu;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainCommand extends SimpleCommand {


    public MainCommand() {
        super("villagershop|vgs");
        setAutoHandleHelp(false);
    }

    @Override
    protected void onCommand() {

        List<String> trades = getAllTradesName();

        Common.setTellPrefix("&cVi&6lla&eger&aSh&bop &7>> ");

        if (args.length == 0) return;
        if (args[0].equals("create")) {
            if (args.length == 1) {
                Common.tell(getPlayer(), "&cYou need a name to make trade. Usage: /vgs creat &6<name>&c.");
                return;
            }
            if (args.length == 2) {
                if (trades.contains(args[1])) {
                    Common.tell(getPlayer(), "&cTrade '" + args[1] + "' already created! Use &6/vgs manage " + args[1] + " &cto manage this trade!");
                    return;
                }
                TradeCache.from(args[1]);
                Common.tell(getPlayer(), "&6Trade '" + args[1] + "' create successfully!");
            }
        }
        if (args[0].equals("manage")) {
            if (trades.isEmpty()) {
                Common.tell(getPlayer(), "&cYou need to create a trade. Usage: /vgs creat &6<name>&C.");
                return;
            }
            if (args.length == 1) {
                Common.tell(getPlayer(), "&cYou need a trade name to manager. Usage: /vgs modify &6<name>&c.");
                Common.tell(getPlayer(), "&6All registered trades: " + trades);
                return;
            }
            if (args.length == 2) {
                String name = args[1];
                if (!trades.contains(name)) {
                    Common.tell(getPlayer(), "&cNo trade called '" + name + "', try again!");
                    return;
                }
                if (VillagerShop.getEditing().contains(name)) {
                    Common.tell(getPlayer(), "&cThis trade is editing by others! Can't edit at the same time!");
                } else {
                    TradeConfigMenu.showTo(getPlayer(), name);
                }
            }
        }
        if (args[0].equals("delete")) {
            if (args.length == 1) {
                Common.tell(getPlayer(), "&cYou need a trade name to delete. Usage: /vgs delete &6<name>&c.");
                Common.tell(getPlayer(), "&6All registered trades: " + trades);
                return;
            }
            if (args.length == 2) {
                String name = args[1];
                if (!trades.contains(name)) {
                    Common.tell(getPlayer(), "&cNo trade called '" + name + "', try again!");
                    return;
                }
                TradeCache.remove(name, getPlayer());
            }
        }
        if (args[0].equals("show")) {
            if (args.length == 1) {
                Common.tell(getPlayer(), "&cYou need a trade name to show. Usage: /vgs show &6<name>&c.");
                Common.tell(getPlayer(), "&6All registered trades: " + trades);
                return;
            }
            if (args.length == 2) {
                String name = args[1];
                if (!trades.contains(name)) {
                    Common.tell(getPlayer(), "&cNo trade called '" + name + "', try again!");
                    return;
                }
                getPlayer().addScoreboardTag(name);
                getPlayer().openMerchant(makeMerchant(name), true);
            }
        }
    }

    @Override
    protected List<String> tabComplete() {
        List<String> trades = getAllTradesName();
        if (args.length == 1 && trades.isEmpty()) return completeLastWord("create");
        if (args.length == 1)
            return completeLastWord("create", "manage", "delete", "show");
        if (args.length == 2 && args[0].equals("create"))
            return NO_COMPLETE;
        if (args.length == 2)
            return completeLastWord(trades);
        return NO_COMPLETE;
    }

    private List<String> getAllTradesName() {
        File[] trades_files = Objects.requireNonNull(new File(VillagerShop.getInstance().getDataFolder(), "trades").listFiles());
        List<String> trades = new ArrayList<>();
        for (File trades_file : trades_files)
            trades.add(trades_file.getName().replace(".yml", ""));
        return trades;
    }

    private Merchant makeMerchant(String trade_name) {
        TradeCache cache = TradeCache.from(trade_name);
        Merchant merchant = Bukkit.createMerchant(Common.colorize(cache.getDisplayName()));
        List<MerchantRecipe> recipes = new ArrayList<>();
        for (int i = 0; i < cache.getDeals().size(); i++) {
//			if (cache.getDeals().getMap(String.valueOf(i)).size() == 2) {
//				MerchantRecipe recipe = new MerchantRecipe(cache.getDeals().getMap(String.valueOf(i)).getItemStack("2"), 9999);
//				recipes.add(recipe);
//			}
            if (cache.getDeals().getMap(String.valueOf(i)).size() == 3) {
                MerchantRecipe recipe = new MerchantRecipe(cache.getDeals().getMap(String.valueOf(i)).getItemStack("2"), 9999);
                recipe.addIngredient(cache.getDeals().getMap(String.valueOf(i)).getItemStack("0"));
                recipes.add(recipe);
            }
            if (cache.getDeals().getMap(String.valueOf(i)).size() == 4) {
                MerchantRecipe recipe = new MerchantRecipe(cache.getDeals().getMap(String.valueOf(i)).getItemStack("2"), 9999);
                recipe.addIngredient(cache.getDeals().getMap(String.valueOf(i)).getItemStack("0"));
                recipe.addIngredient(cache.getDeals().getMap(String.valueOf(i)).getItemStack("1"));
                recipes.add(recipe);
            }
        }
        merchant.setRecipes(recipes);
        return merchant;
    }
}
