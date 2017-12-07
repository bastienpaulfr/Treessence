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
package fr.bipi.tressence.crash;

import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import fr.bipi.tressence.base.FormatterPriorityTree;
import timber.log.Timber;

/**
 * An implementation of {@link Timber.Tree} which sends log to Crashlytics.
 *
 * @author ypresto
 * @see CrashlyticsLogExceptionTree
 */
public class CrashlyticsLogTree extends FormatterPriorityTree {
    /**
     * Create instance with default log priority of WARN.
     */
    public CrashlyticsLogTree() {
        this(Log.WARN);
    }

    /**
     * @param logPriority Minimum log priority to send log. Expects one of constants defined in {@link Log}.
     */
    public CrashlyticsLogTree(int logPriority) {
        super(logPriority);
        Crashlytics.class.getCanonicalName();
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }

        Crashlytics.log(format(priority, tag, message));
    }
}
