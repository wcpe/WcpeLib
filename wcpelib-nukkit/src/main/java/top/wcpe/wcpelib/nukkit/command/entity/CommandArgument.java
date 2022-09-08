package top.wcpe.wcpelib.nukkit.command.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 子命令参数实体
 *
 * @author WCPE
 * @date 2021年4月24日 下午3:36:31
 */
public class CommandArgument implements top.wcpe.wcpelib.common.command.CommandArgument {
    /**
     * 参数名称
     */
    private final String name;
    /**
     * 参数介绍
     */
    private final String describe;
    /**
     * 参数忽略默认值
     */
    private final String ignoreArg;

    private CommandArgument(Builder builder) {
        this.name = builder.name;
        this.describe = builder.describe;
        this.ignoreArg = builder.ignoreArg;
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

    @Nullable
    @Override
    public String getIgnoreArg() {
        return ignoreArg;
    }


    public static class Builder {
        private final String name;
        private String describe = "";
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

        public CommandArgument build() {
            return new CommandArgument(this);
        }
    }
}
