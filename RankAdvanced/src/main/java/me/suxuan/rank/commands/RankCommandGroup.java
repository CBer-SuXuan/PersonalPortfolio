package me.suxuan.rank.commands;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.annotation.AutoRegister;
import org.mineacademy.fo.command.SimpleCommandGroup;
import org.mineacademy.fo.model.SimpleComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CBer_SuXuan
 * @className RankCommandGroup
 * @date 2023/5/8 19:39
 * @description Command Group of /rank
 */
@AutoRegister
public final class RankCommandGroup extends SimpleCommandGroup {

	private static final RankCommandGroup instance = new RankCommandGroup();

	public RankCommandGroup() {
		super("rank");
	}

	@Override
	protected void registerSubcommands() {
		registerSubcommand(new RankSetCommand(this));
		registerSubcommand(new RankAddCommand(this));
		registerSubcommand(new RankReloadCommand(this));
		registerSubcommand(new RankGetCommand(this));
	}

	@Override
	protected List<String> getHelpLabel() {
		return List.of("help");
	}

	@Override
	protected boolean sendHelpIfNoArgs() {
		return true;
	}

	@Override
	protected String[] getHelpHeader() {
		return new String[]{
				"&7[&e&lR&b&lS&7] Contact author for more help"
		};
	}

	@Override
	protected List<SimpleComponent> getNoParamsHeader() {
		List<String> message = help();
		return Common.convert(message, SimpleComponent::of);
	}

	private List<String> help() {
		List<String> message = new ArrayList<>();
		message.add("&7[&e&lR&b&lS&7] Contact author for more help");
		return message;
	}

}
