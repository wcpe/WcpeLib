package top.wcpe.wcpelib.model.bukkit.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import top.wcpe.wcpelib.model.bukkit.command.entity.SubCommand;
import top.wcpe.wcpelib.model.bukkit.command.entity.SubCommandArgument;
import top.wcpe.wcpelib.utils.ListUtil;
import top.wcpe.wcpelib.utils.StringUtil;

/**
 * 命令增强类
 * <p>使用例子
 * 	new CommandPlus.Builder("test", this).aliases(Arrays.asList("t")).build()
 * 				.registerSubCommand(new SubCommand.Builder("subCommand", "子命令1")
 * 						.args(Arrays.asList(new SubCommandArgument.Builder("参数1").ignoreArg("忽略填充1").build(),
 * 								new SubCommandArgument.Builder("参数2").build(),
 * 								new SubCommandArgument.Builder("参数3").ignoreArg("忽略填充3").build()))
 * 						.executeComponent((sender, args) -> {
 * 							sender.sendMessage(Arrays.toString(args));
 *                                                }).tabCompleter((sender, args) -> {
 * 							return Arrays.asList("子命令1");
 *                        }).build())
 * 				.registerThis();
 * 	</p>
 * @author WCPE
 * @date 2021年4月23日 下午4:48:48
 */
public class CommandPlus extends Command implements PluginIdentifiableCommand {
    public CommandPlus registerThis() {
        CommandManager.registerCommandPlus(this);
        return this;
    }

    @Getter
    private final Plugin plugin;
    @Getter
    private final List<String> aliases;
    @Getter
    private List<SubCommand> subCommandList = new ArrayList<>();

    public String getSubPermission(SubCommand subCommand) {
        return subCommand.getPermission() == null ? this.getName() + "." + subCommand.getName() + ".use"
                : subCommand.getPermission();
    }

    ;

    public CommandPlus registerSubCommand(SubCommand subCommand) {
        subCommandList.add(subCommand);
        return this;
    }

    private CommandPlus(Builder builder) {
        super(builder.name);
        this.aliases = builder.aliases;
        this.plugin = builder.plugin;
    }


    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.plugin.isEnabled()) {
            sender.sendMessage("§c插件§e " + this.plugin.getName() + " §c已被卸载 无法使用命令!");
            return true;
        }
        if (args.length > 1)
            for (SubCommand subCommand : subCommandList) {
                if (subCommand.isIgnoreCase() ? !subCommand.getName().equalsIgnoreCase(args[0])
                        : !subCommand.getName().equals(args[0]))
                    continue;
                String[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                int validArg = 0;
                List<SubCommandArgument> subArgLists = subCommand.getArgs();
                for (int i = 0; i < subArgLists.size(); i++) {
                    String ignoreArg = subArgLists.get(i).getIgnoreArg();
                    if (subArgs.length <= i)
                        subArgs = Arrays.copyOf(subArgs, i + 1);
                    if ((subArgs[i] == null || "".equals(subArgs[i]) || " ".equals(subArgs[i])) && ignoreArg != null) {
                        subArgs[i] = ignoreArg;
                    }
                    validArg++;
                }
                // 如果最后一个参数可忽略 减少一位输入参数
                if (subArgLists.get(subArgLists.size() - 1).getIgnoreArg() != null) {
                    validArg--;
                }
                if (args.length >= validArg) {
                    for (String s : subArgs) {
                        if (s == null) {
                            sender.sendMessage("§c指令执行错误 请填写必填参数!");
                            return true;
                        }
                    }
                    subCommand.getExecuteComponent().execute(sender, subArgs);
                    return true;
                }
            }

        if (args.length == 0 || "help".equals(args[0])) {
            int page = 1;
            if (args.length > 1)
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            List<List<SubCommand>> splitSubCommandList = ListUtil.splitList(subCommandList, 5);
            page = page > splitSubCommandList.size() ? splitSubCommandList.size() : page;
            sender.sendMessage("§6===== §e" + getName() + " §a指令帮助 §e" + page + "§a/§e" + splitSubCommandList.size()
                    + " §a页 §6=====");
            sender.sendMessage("§b/" + getName());
            for (SubCommand subCommand : splitSubCommandList.get(page - 1)) {
                sender.sendMessage("§6>" + StringUtil.getRepeatString(" ", getName().length()) + " §e" + subCommand.getName() + " "
                        + subCommand.getArgs().stream().map(arg -> {
                    if (arg.getIgnoreArg() == null) {
                        return "<" + arg.getName() + ">";
                    }
                    return "[" + arg.getName() + "]";
                }).collect(Collectors.joining(" ")) + " §a权限:§6 " + getSubPermission(subCommand));
                sender.sendMessage("§6>" + StringUtil.getRepeatString(" ", getName().length()) + " §a描述:§6 " + subCommand.getDescribe());
            }
            sender.sendMessage("§6> []为选填参数 <>为必填参数");
            return true;
        }
        sender.sendMessage("§c指令不存在或参数错误! §a请输入§e/" + getName() + " help [页码] §a进行查询");

        return true;

    }

    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        if (!this.plugin.isEnabled() || args.length < 1)
            return null;
        if (args.length == 1) {
            return subCommandList.stream()
                    .filter(sub -> sub.isHideNoPermissionHelp() ? sender.hasPermission(getSubPermission(sub)) : true)
                    .map(SubCommand::getName).collect(Collectors.toList());
        }
        return subCommandList.stream()
                .filter(sub -> (sub.isIgnoreCase() ? sub.getName().equalsIgnoreCase(args[0])
                        : sub.getName().equals(args[0])) && (sub.isOnlyPlayerUse() ? sender instanceof Player : true))
                .findFirst().map(sub -> sub.getTabCompleter().onTabComplete(sender, args)).orElse(null);
    }

    public static class Builder {
        private final String name;
        private List<String> aliases = new ArrayList<>();
        private final Plugin plugin;

        public Builder(String name, Plugin plugin) {
            super();
            this.name = name;
            this.plugin = plugin;
        }

        public Builder aliases(List<String> aliases) {
            this.aliases = aliases;
            return this;
        }

        public CommandPlus build() {
            return new CommandPlus(this);
        }
    }

}
