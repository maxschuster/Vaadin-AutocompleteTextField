/*
 * Copyright 2015 Max Schuster.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.maxschuster.vaadin.autocompletetextfield.provider;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;

/**
 * Common match modes that can be used in {@link AutocompleteSuggestionProvider}
 * implementations.
 *
 * @author Max Schuster
 * @see AutocompleteSuggestionProvider
 */
public enum MatchMode {

    /**
     * Should match if the value begins with the search string.
     */
    BEGINS,
    /**
     * Should match if the value contains the search string.
     */
    CONTAINS

}
