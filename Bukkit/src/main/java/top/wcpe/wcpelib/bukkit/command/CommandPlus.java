package top.wcpe.wcpelib.bukkit.command;

import java.util.*;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import top.wcpe.wcpelib.bukkit.command.entity.Command;
import top.wcpe.wcpelib.bukkit.command.entity.CommandArgument;
import top.wcpe.wcpelib.bukkit.command.intel.ExecuteComponentFunctional;
import top.wcpe.wcpelib.bukkit.command.intel.TabCompleterFunctional;
import top.wcpe.wcpelib.common.utils.collector.ListUtil;
import top.wcpe.wcpelib.common.utils.string.StringUtil;

/**
 * 命令增强类
 * <p>
 * </p>
 *
 * @author WCPE
 * @date 2021年4月23日 下午4:48:48
 */
public class CommandPlus extends org.bukkit.command.Command implements PluginIdentifiableCommand {
    public CommandPlus registerThis() {
        CommandManager.registerCommandPlus(this);
        return this;
    }

    @Getter
    private final Plugin plugin;
    @Getter
    private final List<String> aliases;
    @Getter
    private LinkedHashMap<String, Command> subCommandMap = new LinkedHashMap<>();

    private Command mainCommand;

    public String getSubPermission(Command subCommand) {
        return subCommand.getPermission() == null ? this.getName() + "." + subCommand.getName() + ".use"
                : subCommand.getPermission();
    }


    public CommandPlus registerSubCommand(Command subCommand) {
        subCommandMap.put(subCommand.getName(), subCommand);
        return this;
    }

    private CommandPlus(Builder builder) {
        super(builder.name);
        this.aliases = builder.aliases;
        this.plugin = builder.plugin;
        this.mainCommand = builder.mainCommand;
    }

    private boolean executeJudge(Command mainCommand, CommandSender sender) {
        if (mainCommand.isOnlyPlayerUse() && !(sender instanceof Player)) {
            sender.sendMessage(mainCommand.getNoPlayerMessage());
            return false;
        }
        String permission = mainCommand.getPermission();
        if (permission == null) {
            if (mainCommand instanceof Command) {
                permission = getName() + "." + ((Command) mainCommand).getName() + ".use";
            } else {
                permission = getName() + ".use";
            }
        }
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(mainCommand.getNoPermissionMessage());
            return false;
        }
        return true;
    }


    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.plugin.isEnabled()) {
            sender.sendMessage("§c插件§e " + this.plugin.getName() + " §c已被卸载 无法使用命令!");
            return true;
        }
        if (mainCommand != null) {
            if (!executeJudge(mainCommand, sender)) return true;
            int validArg = 0;
            for (CommandArgument arg : mainCommand.getArgs()) {
                if (arg.getIgnoreArg() == null)
                    validArg++;
            }
            if (args.length < validArg) {
                sender.sendMessage("§c指令执行错误 请填写必填参数!");
                return true;
            }
            ExecuteComponentFunctional executeComponent = mainCommand.getExecuteComponent();
            if (executeComponent != null) executeComponent.execute(sender, args);
            return true;
        }
        if (args.length > 0)
            for (Command subCommand : subCommandMap.values()) {
                if (subCommand.isIgnoreCase() ? !subCommand.getName().equalsIgnoreCase(args[0])
                        : !subCommand.getName().equals(args[0]))
                    continue;
                String[] subArgs;
                if (args.length == 1)
                    subArgs = new String[]{};
                else
                    subArgs = Arrays.copyOfRange(args, 1, args.length);
                int validArg = 0;
                List<CommandArgument> subArgLists = subCommand.getArgs();
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
                if (subArgLists != null && !subArgLists.isEmpty() && subArgLists.get(subArgLists.size() - 1).getIgnoreArg() != null) {
                    validArg--;
                }
                if (args.length >= validArg) {
                    for (String s : subArgs) {
                        if (s == null) {
                            sender.sendMessage("§c指令执行错误 请填写必填参数!");
                            return true;
                        }
                    }
                    if (!executeJudge(subCommand, sender)) return true;
                    ExecuteComponentFunctional executeComponent = subCommand.getExecuteComponent();
                    if (executeComponent != null)
                        executeComponent.execute(sender, subArgs);
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
            List<List<Command>> splitSubCommandList = ListUtil.splitList(subCommandMap.values().stream().collect(Collectors.toList()), 5);
            page = page > splitSubCommandList.size() ? splitSubCommandList.size() : page;
            sender.sendMessage("§6===== §e" + getName() + " §a指令帮助 §e" + page + "§a/§e" + splitSubCommandList.size()
                    + " §a页 §6=====");
            sender.sendMessage("§b/" + getName());
            for (String alias : aliases) {
                sender.sendMessage("§b/" + alias);
            }
            for (Command subCommand : splitSubCommandList.get(page - 1)) {
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
        if (mainCommand != null) {
            TabCompleterFunctional tabCompleter = mainCommand.getTabCompleter();
            if (tabCompleter != null)
                return tabCompleter.onTabComplete(sender, args);
        }
        if (args.length == 1) {
            return subCommandMap.values().stream()
                    .filter(sub -> sub.isHideNoPermissionHelp() ? sender.hasPermission(getSubPermission(sub)) : true)
                    .map(Command::getName).collect(Collectors.toList());
        }
        return subCommandMap.values().stream()
                .filter(sub -> (sub.isIgnoreCase() ? sub.getName().equalsIgnoreCase(args[0])
                        : sub.getName().equals(args[0])) && (sub.isOnlyPlayerUse() ? sender instanceof Player : true))
                .findFirst().map(sub -> {
                    TabCompleterFunctional tabCompleter = sub.getTabCompleter();
                    if (tabCompleter != null)
                        return tabCompleter.onTabComplete(sender, args);
                    return new ArrayList<String>();
                }).orElse(null);
    }

    public static class Builder {
        private final String name;
        private List<String> aliases = new ArrayList<>();
        private final Plugin plugin;
        private Command mainCommand;

        public Builder(String name, Plugin plugin) {
            super();
            this.name = name;
            this.plugin = plugin;
        }

        public Builder mainCommand(Command mainCommand) {
            this.mainCommand = mainCommand;
            return this;
        }

        public Builder aliases(String... aliases) {
            for (String s : aliases) {
                this.aliases.add(s);
            }
            return this;
        }

        public CommandPlus build() {
            return new CommandPlus(this);
        }
    }

}
