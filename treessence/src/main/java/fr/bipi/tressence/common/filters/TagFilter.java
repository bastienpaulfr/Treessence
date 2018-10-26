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
package fr.bipi.tressence.common.filters;

public class TagFilter implements Filter {

    private final String tagRegex;

    public TagFilter(String tagRegex) {
        this.tagRegex = tagRegex;
    }

    @Override
    public boolean skipLog(int priority, String tag, String message, Throwable t) {
        return !tag.matches(tagRegex);
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return tag.matches(tagRegex);
    }

    public String getTagRegex() {
        return tagRegex;
    }
}
