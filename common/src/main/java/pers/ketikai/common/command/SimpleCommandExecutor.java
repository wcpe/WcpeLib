/*
 * Copyright 2025 ketikai
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pers.ketikai.common.command;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import pers.ketikai.common.command.annotation.CommandArgument;
import pers.ketikai.common.command.exception.CommandException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class SimpleCommandExecutor implements CommandExecutor {

    @NonNull
    private final Object command;
    @NonNull
    private final Method method;

    @Override
    public @NotNull CommandResult execute(@NotNull CommandContext context, @Range(from = 0, to = Integer.MAX_VALUE) int current, @NotNull String @NotNull ... arguments) {
        Parameter[] parameters = method.getParameters();
        Object[] parametersValues = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            CommandArgument commandArgument = parameter.getDeclaredAnnotation(CommandArgument.class);
            if (commandArgument == null) {
                parametersValues[i] = context;
                continue;
            }
            String value = commandArgument.value();
            if (value.isEmpty()) {
                value = parameter.getName();
            }
            parametersValues[i] = context.get(value);
        }
        try {
            CommandResult result = (CommandResult) method.invoke(command, parametersValues);
            return Objects.requireNonNull(result, "command result cannot be null.");
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new CommandException(e);
        }
    }
}
