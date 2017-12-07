/*
 * Copyright (C) 2015 Yuya Tanaka
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
package fr.bipi.tressence.console;

import java.util.Locale;

/**
 * Exception thrown by {@link ThrowErrorTree}.
 */
class LogPriorityExceededError extends Error {
    private static final String LOG_FORMAT = "Log priority exceeded: actual:%d >= minimum:%d";

    LogPriorityExceededError(int priority, int failFastPriority, Throwable throwable) {
        super(String.format(Locale.US, LOG_FORMAT, priority, failFastPriority), throwable);
    }

    LogPriorityExceededError(int priority, int failFastPriority) {
        super(String.format(Locale.US, LOG_FORMAT, priority, failFastPriority));
    }
}
