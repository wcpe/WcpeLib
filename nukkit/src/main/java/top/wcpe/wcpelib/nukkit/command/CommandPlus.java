package top.wcpe.wcpelib.nukkit.command;


import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.plugin.Plugin;
import lombok.Getter;
import top.wcpe.wcpelib.common.utils.collector.ListUtil;
import top.wcpe.wcpelib.common.utils.string.StringUtil;
import top.wcpe.wcpelib.nukkit.command.entity.Command;
import top.wcpe.wcpelib.nukkit.command.entity.CommandArgument;
import top.wcpe.wcpelib.nukkit.command.intel.ExecuteComponentFunctional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 命令增强类
 * <p>
 * </p>
 *
 * @author WCPE
 * @date 2021年4月23日 下午4:48:48
 */
public class CommandPlus extends cn.nukkit.command.Command implements PluginIdentifiableCommand {
    public CommandPlus registerThis() {
        CommandManager.registerCommandPlus(this);
        return this;
    }

    @Getter
    private final Plugin plugin;

    private final List<String> aliases;

    @Override
    public String[] getAliases() {
        return aliases.toArray(new String[0]);
    }

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


    public int requiredArgs(String[] args, List<CommandArgument> listCommandArgument) {
        if (args.length < listCommandArgument.size()) {
            args = Arrays.copyOf(args, listCommandArgument.size());
        }
        for (int i = 0; i < listCommandArgument.size(); i++) {
            String ignoreArg = listCommandArgument.get(i).getIgnoreArg();
            if (ignoreArg == null && args[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public String[] ignoreArgReplace(String[] args, List<CommandArgument> listCommandArgument) {
        if (args.length < listCommandArgument.size()) {
            args = Arrays.copyOf(args, listCommandArgument.size());
        }
        for (int i = 0; i < listCommandArgument.size(); i++) {
            String ignoreArg = listCommandArgument.get(i).getIgnoreArg();
            if (ignoreArg != null && ((args[i] == null || "".equals(args[i]) || " ".equals(args[i])))) {
                args[i] = ignoreArg;
            }
        }
        return args;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!this.plugin.isEnabled()) {
            sender.sendMessage("§c插件§e " + this.plugin.getName() + " §c已被卸载 无法使用命令!");
            return true;
        }

        if (mainCommand != null) {
            if (!executeJudge(mainCommand, sender)) return true;
            List<CommandArgument> listCommandArgument = mainCommand.getArgs();
            int i = requiredArgs(args, listCommandArgument);
            if (i != -1) {
                sender.sendMessage("§c指令执行错误 请填写必填参数 §6<" + listCommandArgument.get(i).getName() + "> §c!");
                return true;
            }
            args = ignoreArgReplace(args, listCommandArgument);
            ExecuteComponentFunctional executeComponent = mainCommand.getExecuteComponent();
            if (executeComponent != null) executeComponent.execute(sender, args);
            return true;
        }
        if (args.length > 0) {
            Command subCommand = subCommandMap.get(args[0]);
            if (subCommand != null) {
                String[] subArgs;
                if (args.length == 1)
                    subArgs = new String[]{};
                else
                    subArgs = Arrays.copyOfRange(args, 1, args.length);
                List<CommandArgument> listCommandArgument = subCommand.getArgs();
                int i = requiredArgs(subArgs, listCommandArgument);
                if (i != -1) {
                    sender.sendMessage("§c指令执行错误 请填写必填参数 §6<" + listCommandArgument.get(i).getName() + "> §c!");
                    return true;
                }
                subArgs = ignoreArgReplace(subArgs, listCommandArgument);


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
                }).collect(Collectors.joining(" ")));
                sender.sendMessage("§6>" + StringUtil.getRepeatString(" ", getName().length()) + " §a描述:§6 " + subCommand.getDescribe());
                sender.sendMessage("§6>" + StringUtil.getRepeatString(" ", getName().length()) + " §a权限:§c " + getSubPermission(subCommand));
            }
            sender.sendMessage("§6> []为选填参数 <>为必填参数");
            return true;
        }
        sender.sendMessage("§c指令不存在或参数错误! §a请输入§e/" + getName() + " help [页码] §a进行查询");

        return true;

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
