package pers.ketikai.common.command.test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import pers.ketikai.common.command.CommandContext;
import pers.ketikai.common.command.CommandLine;
import pers.ketikai.common.command.CommandResult;
import pers.ketikai.common.command.CommandSender;
import pers.ketikai.common.command.example.ExampleCommand;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TestCommand {


    @Test
    public void test() {
        TestCommandSender sender = new TestCommandSender(UUID.randomUUID(), true, Collections.emptySet());
        CommandLine commandLine = CommandLine.of(getClass().getSimpleName(), new ExampleCommand());
        CommandResult result;
        result = execute(commandLine, sender, "reload");
        System.out.println(result.isSuccess() + " " + result.getMessage());
        System.out.println(complete(commandLine, sender, ""));
        System.out.println(complete(commandLine, sender, "re"));
        System.out.println(complete(commandLine, sender, "$"));

        result = execute(commandLine, sender, "reload a");
        System.out.println(result.isSuccess() + " " + result.getMessage());
        System.out.println(complete(commandLine, sender, "reload "));
        System.out.println(complete(commandLine, sender, "reload a"));
        System.out.println(complete(commandLine, sender, "reload $"));

        result = execute(commandLine, sender, "reload a b");
        System.out.println(result.isSuccess() + " " + result.getMessage());
        System.out.println(complete(commandLine, sender, "reload a b"));

        result = execute(commandLine, sender, "fuck");
        System.out.println(result.isSuccess() + " " + result.getMessage());

        result = execute(commandLine, sender, "fuck you");
        System.out.println(result.isSuccess() + " " + result.getMessage());

        result = execute(commandLine, sender, "");
        System.out.println(result.isSuccess() + " " + result.getMessage());

        result = execute(commandLine, sender, "test");
        System.out.println(result.isSuccess() + " " + result.getMessage());
    }

    private CommandResult execute(CommandLine commandLine, CommandSender sender, String command) {
        String[] arguments = command.split(" ", -1);
        CommandContext context = CommandContext.of(sender);
        return commandLine.execute(context, arguments);
    }

    private List<String> complete(CommandLine commandLine, CommandSender sender, String command) {
        String[] arguments = command.split(" ", -1);
        CommandContext context = CommandContext.of(sender);
        return commandLine.complete(context, arguments);
    }

    @Data
    @AllArgsConstructor
    public static final class TestCommandSender implements CommandSender {
        @NonNull
        private final UUID uniqueId;
        private boolean administrator;
        @NonNull
        private Set<String> permissions;

        @Override
        public boolean hasPermission(@NotNull String permission) {
            return permissions.contains(permission);
        }
    }
}
