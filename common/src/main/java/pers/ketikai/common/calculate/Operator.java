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

package pers.ketikai.common.calculate;

import lombok.Getter;

@Getter
public enum Operator implements Symbol {
    ADD('+', 0),
    SUBTRACT('-', 0),
    MULTIPLY('*', 1),
    DIVIDE('/', 1),
    MOD('%', 1),
    POWER('^', 1);

    public static final int MIN_PRIORITY = Integer.MIN_VALUE;
    private final char symbol;
    private final int priority;
    Operator(char symbol, int priority) {
        validateSymbol(symbol);
        validatePriority(priority);
        this.symbol = symbol;
        this.priority = priority;
    }

    private static void validateSymbol(char symbol) {
        if (Variable.isNameContent(symbol)) {
            throw new IllegalArgumentException("Invalid symbol '" + symbol + "', because it is a variable keyword.");
        }
    }

    private static void validatePriority(int priority) {
        if (priority == MIN_PRIORITY) {
            throw new IllegalArgumentException("Priority must be greater than " + MIN_PRIORITY);
        }
    }

    public Number calculate(Number first, Number second) {
        double firstVal = first.doubleValue();
        double secondVal = second.doubleValue();
        switch (this) {
            case ADD:
                return firstVal + secondVal;
            case SUBTRACT:
                return firstVal - secondVal;
            case MULTIPLY:
                return firstVal * secondVal;
            case DIVIDE:
                return firstVal / secondVal;
            case MOD:
                return firstVal % secondVal;
            case POWER:
                return Math.pow(firstVal, secondVal);
            default:
                throw new IllegalArgumentException("Unknown operator: " + this);
        }
    }
}
