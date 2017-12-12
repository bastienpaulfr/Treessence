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

import android.annotation.SuppressLint;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import fr.bipi.tressence.utils.TimeUtils;

public class LogcatFormatter implements Formatter {

    private static final String SEP = " ";

    @SuppressLint("UseSparseArrays")
    private final HashMap<Integer, String> prioPrefixes = new HashMap<>();

    public LogcatFormatter() {
        prioPrefixes.put(Log.VERBOSE, "V/");
        prioPrefixes.put(Log.DEBUG, "D/");
        prioPrefixes.put(Log.INFO, "I/");
        prioPrefixes.put(Log.WARN, "W/");
        prioPrefixes.put(Log.ERROR, "E/");
        prioPrefixes.put(Log.ASSERT, "WTF/");
    }

    @Override
    public String format(int priority, String tag, @NotNull String message) {
        String prio = prioPrefixes.get(priority);
        if (prio == null) {
            prio = "";
        }
        return TimeUtils.timestampToDate(System.currentTimeMillis(), "MM-dd HH:mm:ss:SSS")
               + SEP
               + prio
               + (tag == null ? "" : tag)
               + "(" + Thread.currentThread().getId() + ") :"
               + SEP
               + message
               + "\n";
    }
}
