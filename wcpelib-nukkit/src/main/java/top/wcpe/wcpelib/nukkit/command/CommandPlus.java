package top.wcpe.wcpelib.nukkit.command;


import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.wcpe.wcpelib.nukkit.command.entity.Command;

import java.util.*;

/**
 * 命令增强类
 * <p>
 * </p>
 *
 * @author WCPE
 * @date 2021年4月23日 下午4:48:48
 */
public class CommandPlus extends cn.nukkit.command.Command implements top.wcpe.wcpelib.common.command.CommandPlus, PluginIdentifiableCommand {
    private final Plugin plugin;
    private final LinkedHashMap<String, top.wcpe.wcpelib.common.command.Command> subCommandMap = new LinkedHashMap<>();
    private top.wcpe.wcpelib.common.command.Command mainCommand;
    private boolean hideNoPermissionHelp;

    private CommandPlus(Builder builder) {
        super(builder.name);
        setAliases(builder.aliases.toArray(new String[]{}));
        this.plugin = builder.plugin;
        this.mainCommand = builder.mainCommand;
        this.hideNoPermissionHelp = builder.hideNoPermissionHelp;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.plugin.isEnabled()) {
            sender.sendMessage("§c插件§e " + this.plugin.getName() + " §c已被卸载 无法使用命令!");
            return true;
        }
        execute(new NukkitCommandSenderImpl(sender), args);
        return true;
    }


    @Override
    public Plugin getPlugin() {
        return plugin;
    }


    @NotNull
    @Override
    public CommandPlus registerThis() {
        CommandManager.registerCommandPlus(this);
        return this;
    }

    @NotNull
    @Override
    public List<String> getAliasList() {
        return Arrays.asList(getAliases());
    }

    @NotNull
    @Override
    public LinkedHashMap<String, top.wcpe.wcpelib.common.command.Command> getSubCommandMap() {
        return subCommandMap;
    }

    @Nullable
    @Override
    public top.wcpe.wcpelib.common.command.Command getMainCommand() {
        return mainCommand;
    }

    @Override
    public void setMainCommand(@Nullable top.wcpe.wcpelib.common.command.Command command) {
        mainCommand = command;
    }

    @Override
    public boolean getHideNoPermissionHelp() {
        return hideNoPermissionHelp;
    }

    @Override
    public void setHideNoPermissionHelp(boolean hideNoPermissionHelp) {
        this.hideNoPermissionHelp = hideNoPermissionHelp;
    }

    public CommandPlus registerSubCommand(@NotNull Command subCommand) {
        return (CommandPlus) top.wcpe.wcpelib.common.command.CommandPlus.super.registerSubCommand(subCommand);
    }


    public static class Builder {
        private final String name;
        private final Plugin plugin;
        private final List<String> aliases = new ArrayList<>();
        private Command mainCommand;
        private boolean hideNoPermissionHelp = false;

        public Builder(String name, Plugin plugin) {
            this.name = name;
            this.plugin = plugin;
        }

        public Builder(Command mainCommand, Plugin plugin) {
            this.mainCommand = mainCommand;
            this.name = mainCommand.getName();
            this.plugin = plugin;
        }

        public Builder mainCommand(Command mainCommand) {
            this.mainCommand = mainCommand;
            return this;
        }

        public Builder aliases(String... aliases) {
            Collections.addAll(this.aliases, aliases);
            return this;
        }

        public Builder hideNoPermissionHelp(boolean hideNoPermissionHelp) {
            this.hideNoPermissionHelp = hideNoPermissionHelp;
            return this;
        }

        public CommandPlus build() {
            return new CommandPlus(this);
        }
    }

}
