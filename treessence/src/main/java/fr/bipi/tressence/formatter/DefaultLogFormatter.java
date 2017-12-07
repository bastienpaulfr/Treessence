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
package fr.bipi.tressence.formatter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

public class DefaultLogFormatter implements Formatter {

    private final SparseArray<String> prioPrefixes = new SparseArray<>();

    public DefaultLogFormatter() {
        prioPrefixes.append(Log.VERBOSE, "V/");
        prioPrefixes.append(Log.DEBUG, "D/");
        prioPrefixes.append(Log.INFO, "I/");
        prioPrefixes.append(Log.WARN, "W/");
        prioPrefixes.append(Log.ERROR, "E/");
        prioPrefixes.append(Log.ASSERT, "WTF/");
    }

    @Override
    public String format(int priority, String tag, @NonNull String message) {
        String prio = prioPrefixes.get(priority);
        if (prio == null) {
            prio = "";
        }
        return prio + (tag == null ? "" : tag + " : ") + message;
    }
}
