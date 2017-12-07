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
import fr.bipi.tressence.common.StackTraceRecorder;
import timber.log.Timber;

/**
 * An implementation of {@link Timber.Tree} which sends exception to Crashlytics.
 *
 * @author ypresto
 * @see CrashlyticsLogExceptionTree
 */
public class CrashlyticsLogExceptionTree extends FormatterPriorityTree {

    private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
    private static final String CRASHLYTICS_KEY_TAG = "tag";
    private static final String CRASHLYTICS_KEY_MESSAGE = "message";

    /**
     * Create instance with default log priority of ERROR.
     */
    public CrashlyticsLogExceptionTree() {
        this(Log.ERROR);
    }

    /**
     * @param logPriority Minimum log priority to send exception. Expects one of constants defined in {@link Log}.
     */
    public CrashlyticsLogExceptionTree(int logPriority) {
        super(logPriority);
        Crashlytics.class.getCanonicalName();
    }

    @Override
    protected void log(int priority, String tag, @NonNull String message, Throwable t) {
        if (skipLog(priority, tag, message, t)) {
            return;
        }

        Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
        Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
        Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);

        if (t != null) {
            Crashlytics.logException(t);
        } else {
            Crashlytics.logException(new StackTraceRecorder(format(priority, tag, message)));
        }
    }
}
