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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface CommandExecutor {

    @NotNull
    CommandResult execute(@NotNull CommandContext context, @Range(from = 0, to = Integer.MAX_VALUE) int current, @NotNull String @NotNull ... arguments);
}
