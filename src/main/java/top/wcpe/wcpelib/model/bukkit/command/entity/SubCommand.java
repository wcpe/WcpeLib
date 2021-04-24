package top.wcpe.wcpelib.model.bukkit.command.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import top.wcpe.wcpelib.model.bukkit.command.intel.ExecuteComponentFunctional;
import top.wcpe.wcpelib.model.bukkit.command.intel.TabCompleterFunctional;

/**
 * 子命令类
 * 
 * @author WCPE
 * @date 2021年4月23日 下午4:47:57
 */
@Data
public class SubCommand {
	/** 名称 */
	private String name;
	/** 参数 */
	private List<SubCommandArgument> args;
	/** 是否隐藏无权限帮助 */
	private boolean hideNoPermissionHelp;
	/** 忽略大小写 */
	private boolean ignoreCase;
	/** 介绍 */
	private String describe;
	/** 权限 */
	private String permission;
	/** 无权限提示 */
	private String noPermissionMessage;
	/** 是否只能玩家用 */
	private boolean onlyPlayerUse;
	/** 不是玩家使用提示 */
	private String noPlayerMessage;
	/** 命令执行组件 */
	private ExecuteComponentFunctional executeComponent;
	/** tab补全器 */
	private TabCompleterFunctional tabCompleter;

	private SubCommand(Builder builder) {
		this.name = builder.name;
		this.args = builder.args;
		this.hideNoPermissionHelp = builder.hideNoPermissionHelp;
		this.ignoreCase = builder.ignoreCase;
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
		private List<SubCommandArgument> args = new ArrayList<>();
		private boolean hideNoPermissionHelp = false;
		private boolean ignoreCase = false;
		private final String describe;
		private String permission;
		private String noPermissionMessage = "§c你莫得权限!";
		private boolean onlyPlayerUse = false;
		private String noPlayerMessage = "§c该指令只能玩家使用!";
		private ExecuteComponentFunctional executeComponent;
		private TabCompleterFunctional tabCompleter;

		public Builder(String name, String describe) {
			this.name = name;
			this.describe = describe;
		}

		public Builder args(List<SubCommandArgument> args) {
			this.args = args;
			return this;
		}

		public Builder hideNoPermissionHelp(boolean hideNoPermissionHelp) {
			this.hideNoPermissionHelp = hideNoPermissionHelp;
			return this;
		}

		public Builder ignoreCase(boolean ignoreCase) {
			this.ignoreCase = ignoreCase;
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

		public SubCommand build() {
			return new SubCommand(this);
		}
	}
}
