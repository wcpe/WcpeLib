package top.wcpe.wcpelib.model.bukkit.command.entity;

import lombok.Getter;

/**
 * 子命令参数实体
 * 
 * @author WCPE
 * @date 2021年4月24日 下午3:36:31
 */
public class SubCommandArgument {
	/** 参数名称 */
	@Getter
	private final String name;
	/** 参数介绍 */
	@Getter
	private final String describe;
	/** 参数忽略默认值 */
	@Getter
	private final String ignoreArg;

	private SubCommandArgument(Builder builder) {
		this.name = builder.name;
		this.describe = builder.describe;
		this.ignoreArg = builder.ignoreArg;
	}

	public static class Builder {
		private String name;
		private String describe;
		private String ignoreArg;

		public Builder(String name) {
			this.name = name;
		}

		public Builder describe(String describe) {
			this.describe = describe;
			return this;
		}

		public Builder ignoreArg(String ignoreArg) {
			this.ignoreArg = ignoreArg;
			return this;
		}

		public SubCommandArgument build() {
			return new SubCommandArgument(this);
		}
	}
}
