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
package fr.bipi.tressence.base;

import org.jetbrains.annotations.NotNull;

import fr.bipi.tressence.formatter.Formatter;
import fr.bipi.tressence.formatter.NoTagFormatter;

/**
 * Base class to filter logs by priority
 */
public class FormatterPriorityTree extends PriorityTree {
    private Formatter formatter = getDefaultFormatter();

    public FormatterPriorityTree(int priority) {
        super(priority);
    }

    /**
     * Set {@link Formatter}
     *
     * @param f formatter
     * @return itself
     */
    public FormatterPriorityTree withFormatter(Formatter f) {
        this.formatter = f;
        return this;
    }

    /**
     * Use its formatter to format log
     *
     * @param priority Priority
     * @param tag      Tag
     * @param message  Message
     * @return Formatted log
     */
    protected String format(int priority, String tag, @NotNull String message) {
        return formatter.format(priority, tag, message);
    }

    /**
     * @return Default log {@link Formatter}
     */
    protected Formatter getDefaultFormatter() {
        return NoTagFormatter.INSTANCE;
    }

    @Override
    protected void log(int priority, String tag, @NotNull String message, Throwable t) {
        super.log(priority, tag, format(priority, tag, message), t);
    }
}
