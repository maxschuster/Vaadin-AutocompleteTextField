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

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author Max Schuster
 */
public class ArraySuggestionProvider implements AutocompleteSuggestionProvider {
    
    private static final long serialVersionUID = 1L;

    private final String[] values;
    
    private final String[] search;
    
    private final MatchMode matchMode;
    
    private final boolean ignoreCase;
    
    private final Locale locale;
    
    public ArraySuggestionProvider(Collection<String> values) {
        this(values, MatchMode.BEGINS);
    }
    
    public ArraySuggestionProvider(Collection<String> values, MatchMode matchMode) {
        this(values, matchMode, false);
    }
    
    public ArraySuggestionProvider(Collection<String> values, MatchMode matchMode, boolean ignoreCase) {
        this(toArray(values), matchMode, ignoreCase, Locale.getDefault());
    }
    
    public ArraySuggestionProvider(Collection<String> values, MatchMode matchMode, boolean ignoreCase, Locale locale) {
        this(toArray(values), matchMode, ignoreCase, locale);
    }
    
    public ArraySuggestionProvider(String[] values) {
        this(values, MatchMode.BEGINS);
    }
    
    public ArraySuggestionProvider(String[] values, MatchMode matchMode) {
        this(values, matchMode, false);
    }

    public ArraySuggestionProvider(String[] values, MatchMode matchMode, boolean ignoreCase) {
        this(values, matchMode, ignoreCase, Locale.getDefault());
    }
    
    public ArraySuggestionProvider(String[] values, MatchMode matchMode, boolean ignoreCase, Locale locale) {
        if (values == null) {
            throw new NullPointerException("array is null!");
        }
        if (matchMode == null) {
            throw new NullPointerException("matchMode is null!");
        }
        if (locale == null) {
            throw new NullPointerException("locale is null!");
        }
        this.values = arrayCopy(values);
        this.matchMode = matchMode;
        this.ignoreCase = ignoreCase;
        this.locale = locale;
        if (ignoreCase) {
            this.search = arrayToLowerCase(this.values, locale);
        } else {
            this.search = this.values;
        }
    }
    
    private static String[] toArray(Collection<String> values) {
        if (values == null) {
            throw new NullPointerException("collection is null!");
        }
        return values.toArray(new String[values.size()]);
    }
    
    private static String[] arrayCopy(String[] array) {
        String[] copyArray = new String[array.length];
        System.arraycopy(array, 0, copyArray, 0, array.length);
        return copyArray;
    }
    
    private static String[] arrayToLowerCase(String[] array, Locale locale) {
        int length = array.length;
        String[] lowerCase = new String[length];
        for (int i = 0; i < length; ++i) {
            lowerCase[i] = array[i].toLowerCase(locale);
        }
        return lowerCase;
    }

    @Override
    public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
        String term = query.getTerm();
        if (values.length == 0 || term == null || term.isEmpty()) {
            return Collections.emptyList();
        }
        if (ignoreCase) {
            term = term.toLowerCase(locale);
        }
        final List<AutocompleteSuggestion> suggestions =
                new ArrayList<AutocompleteSuggestion>();
        int length = search.length;
        int limit = query.getLimit();
        int added = 0;
        for (int i = 0; i < length; ++i) {
            if (limit > 0 && added >= limit) {
                break; // limit reached, exit loop
            }
            if (MatchMode.BEGINS == matchMode && search[i].startsWith(term)) {
                suggestions.add(new AutocompleteSuggestion(values[i]));
                ++added;
            }
            if (MatchMode.CONTAINS == matchMode && search[i].contains(term)) {
                suggestions.add(new AutocompleteSuggestion(values[i]));
                ++added;
            }
        }
        return suggestions;
    }

    public String[] getValues() {
        return values;
    }

    public MatchMode getMatchMode() {
        return matchMode;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public Locale getLocale() {
        return locale;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Arrays.deepHashCode(this.values);
        hash = 47 * hash + (this.matchMode != null ? this.matchMode.hashCode() : 0);
        hash = 47 * hash + (this.ignoreCase ? 1 : 0);
        hash = 47 * hash + (this.locale != null ? this.locale.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        final ArraySuggestionProvider other = (ArraySuggestionProvider) obj;
        if (this.ignoreCase != other.ignoreCase) {
            return false;
        } else if (!Arrays.deepEquals(this.values, other.values)) {
            return false;
        } else if (this.matchMode != other.matchMode) {
            return false;
        } else if (this.locale != other.locale && (this.locale == null || !this.locale.equals(other.locale))) {
            return false;
        }
        return true;
    }
    
}
