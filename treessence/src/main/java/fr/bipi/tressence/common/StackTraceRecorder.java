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
package fr.bipi.tressence.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import timber.log.Timber;

/**
 * Records stack trace without timber code. Used by {@link fr.bipi.tressence.crash.CrashlyticsLogTree} to purify report.
 */
public class StackTraceRecorder extends Throwable {
    public StackTraceRecorder(String detailMessage) {
        super(detailMessage);
    }

    @Override
    public Throwable fillInStackTrace() {
        super.fillInStackTrace();
        StackTraceElement[] original = getStackTrace();
        Iterator<StackTraceElement> iterator = Arrays.asList(original).iterator();
        List<StackTraceElement> filtered = new ArrayList<>();

        // heading to top of Timber stack trace
        while (iterator.hasNext()) {
            StackTraceElement stackTraceElement = iterator.next();
            if (isTimber(stackTraceElement)) {
                break;
            }
        }

        // copy all
        boolean isReachedApp = false;
        while (iterator.hasNext()) {
            StackTraceElement stackTraceElement = iterator.next();
            // skip Timber
            if (!isReachedApp && isTimber(stackTraceElement)) {
                continue;
            }
            isReachedApp = true;
            filtered.add(stackTraceElement);
        }

        setStackTrace(filtered.toArray(new StackTraceElement[filtered.size()]));
        return this;
    }

    private boolean isTimber(StackTraceElement stackTraceElement) {
        return stackTraceElement.getClassName().equals(Timber.class.getName());
    }
}
