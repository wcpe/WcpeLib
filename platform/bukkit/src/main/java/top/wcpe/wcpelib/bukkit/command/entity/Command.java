package top.wcpe.wcpelib.bukkit.command.entity;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.wcpe.wcpelib.bukkit.command.BukkitCommand;
import top.wcpe.wcpelib.bukkit.command.intel.ExecuteComponentFunctional;
import top.wcpe.wcpelib.bukkit.command.intel.TabCompleterFunctional;
import top.wcpe.wcpelib.common.command.v2.ChildCommand;
import top.wcpe.wcpelib.common.command.v2.ParentCommand;
import top.wcpe.wcpelib.common.command.v2.SingleCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 子命令类
 *
 * @author WCPE
 * @date 2021年4月23日 下午4:47:57
 * @deprecated This class is deprecated. Use the {@link SingleCommand} Or {@link ParentCommand} And {@link ChildCommand}.
 */
@Deprecated
public class Command implements BukkitCommand {
    /**
     * 名称
     */
    private final String name;
    /**
     * 参数
     */
    private final List<top.wcpe.wcpelib.common.command.CommandArgument> args = new ArrayList<>();
    /**
     * 是否隐藏无权限帮助
     */
    private final boolean hideNoPermissionHelp;
    /**
     * 介绍
     */
    private final String describe;
    /**
     * 权限
     */
    private final String permission;
    /**
     * 无权限提示
     */
    private final String noPermissionMessage;
    /**
     * 是否只能玩家用
     */
    private final boolean onlyPlayerUse;
    /**
     * 不是玩家使用提示
     */
    private final String noPlayerMessage;
    /**
     * 命令执行组件
     */
    private final top.wcpe.wcpelib.common.command.CommandExecute executeComponent;
    /**
     * tab补全器
     */
    private final TabCompleterFunctional tabCompleter;


    private Command(Command.Builder builder) {
        this.name = builder.name;
        this.hideNoPermissionHelp = builder.hideNoPermissionHelp;
        this.args.addAll(builder.args);
        this.describe = builder.describe;
        this.permission = builder.permission;
        this.noPermissionMessage = builder.noPermissionMessage;
        this.onlyPlayerUse = builder.onlyPlayerUse;
        this.noPlayerMessage = builder.noPlayerMessage;
        this.executeComponent = (sender, args) -> builder.executeComponent.execute((CommandSender) sender.getAdapter(), args);
        this.tabCompleter = builder.tabCompleter;
    }

    @Nullable
    @Override
    public TabCompleterFunctional getTabCompleter() {
        return tabCompleter;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getDescribe() {
        return describe;
    }

    @NotNull
    @Override
    public List<top.wcpe.wcpelib.common.command.CommandArgument> getArgs() {
        return args;
    }

    @Override
    public boolean getHideNoPermissionHelp() {
        return hideNoPermissionHelp;
    }

    @Nullable
    @Override
    public String getPermission() {
        return permission;
    }

    @NotNull
    @Override
    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    @Override
    public boolean getOnlyPlayerUse() {
        return onlyPlayerUse;
    }

    @NotNull
    @Override
    public String getNoPlayerMessage() {
        return noPlayerMessage;
    }

    @Nullable
    @Override
    public top.wcpe.wcpelib.common.command.CommandExecute getExecuteComponent() {
        return executeComponent;
    }


    public static class Builder {
        private final String name;
        private final String describe;
        private final List<CommandArgument> args = new ArrayList<>();
        private boolean hideNoPermissionHelp = false;
        private String permission;
        private String noPermissionMessage = "§c你莫得 §6%permission% §c权限!";
        private boolean onlyPlayerUse = false;
        private String noPlayerMessage = "§c该指令只能玩家使用!";
        private ExecuteComponentFunctional executeComponent;
        private TabCompleterFunctional tabCompleter;

        public Builder(String name, String describe) {
            this.name = name;
            this.describe = describe;
        }

        public Builder args(CommandArgument... args) {
            Collections.addAll(this.args, args);
            return this;
        }

        public Builder hideNoPermissionHelp(boolean hideNoPermissionHelp) {
            this.hideNoPermissionHelp = hideNoPermissionHelp;
            return this;
        }

        public Builder permission(String permission) {
            this.permission = permission;
            return this;
        }

        public Builder noPermissionMessage(String noPermissionMessage) {
            this.noPermissionMessage = noPermissionMessage;
            return this;
        }

        public Builder onlyPlayerUse(boolean onlyPlayerUse) {
            this.onlyPlayerUse = onlyPlayerUse;
            return this;
        }

        public Builder noPlayerMessage(String noPlayerMessage) {
            this.noPlayerMessage = noPlayerMessage;
            return this;
        }

        public Builder executeComponent(ExecuteComponentFunctional executeComponent) {
            this.executeComponent = executeComponent;
            return this;
        }

        public Builder tabCompleter(TabCompleterFunctional tabCompleter) {
            this.tabCompleter = tabCompleter;
            return this;
        }

        public Command build() {
            return new Command(this);
        }
    }
}
