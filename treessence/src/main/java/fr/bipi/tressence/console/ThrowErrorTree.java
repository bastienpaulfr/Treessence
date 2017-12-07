/*
 * Copyright (C) 2017 bastien
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

import android.support.annotation.NonNull;
import android.util.Log;

import fr.bipi.tressence.base.PriorityTree;
import timber.log.Timber;

/**
 * An implementation of {@link Timber.Tree} which throws {@link java.lang.Error} when priority of
 * log is exceeded the limit. Useful for development or test environment.
 */
public class ThrowErrorTree extends PriorityTree {
    /**
     * Create instance with default log priority of ERROR.
     */
    public ThrowErrorTree() {
        this(Log.ERROR);
    }

    /**
     * @param logPriority Minimum log priority to throw error. Expects one of constants defined in {@link Log}.
     */
    public ThrowErrorTree(int logPriority) {
        super(logPriority);
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }

        if (t != null) {
            throw new LogPriorityExceededError(priority, getPriorityFilter().getMinPriority(), t);
        } else {
            throw new LogPriorityExceededError(priority, getPriorityFilter().getMinPriority());
        }
    }

}
