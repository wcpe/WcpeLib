package top.wcpe.wcpelib.bukkit.command.entity;

import lombok.Data;
import top.wcpe.wcpelib.bukkit.command.intel.ExecuteComponentFunctional;
import top.wcpe.wcpelib.bukkit.command.intel.TabCompleterFunctional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 子命令类
 *
 * @author WCPE
 * @date 2021年4月23日 下午4:47:57
 */
@Data
public class Command {
    /**
     * 名称
     */
    private String name;
    /**
     * 参数
     */
    private List<CommandArgument> args;
    /**
     * 是否隐藏无权限帮助
     */
    private boolean hideNoPermissionHelp;
    /**
     * 介绍
     */
    private String describe;
    /**
     * 权限
     */
    private String permission;
    /**
     * 无权限提示
     */
    private String noPermissionMessage;
    /**
     * 是否只能玩家用
     */
    private boolean onlyPlayerUse;
    /**
     * 不是玩家使用提示
     */
    private String noPlayerMessage;
    /**
     * 命令执行组件
     */
    private ExecuteComponentFunctional executeComponent;
    /**
     * tab补全器
     */
    private TabCompleterFunctional tabCompleter;


    private Command(Command.Builder builder) {
        this.name = builder.name;
        this.hideNoPermissionHelp = builder.hideNoPermissionHelp;
        this.args = builder.args;
        this.describe = builder.describe;
        this.permission = builder.permission;
        this.noPermissionMessage = builder.noPermissionMessage;
        this.onlyPlayerUse = builder.onlyPlayerUse;
        this.noPlayerMessage = builder.noPlayerMessage;
        this.executeComponent = builder.executeComponent;
        this.tabCompleter = builder.tabCompleter;
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
