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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 *
 * @author Max Schuster
 * @see AutocompleteSuggestionProvider
 */
public class CollectionSuggestionProvider implements AutocompleteSuggestionProvider {

    private static final long serialVersionUID = 1L;

    /**
     * List of available values
     */
    private List<String> values;

    /**
     * List of values used for matching
     */
    private List<String> search;

    /**
     * The match mode
     */
    private MatchMode matchMode;

    /**
     * Ignore case while matching
     */
    private boolean ignoreCase;

    /**
     * Locale used for matching
     */
    private Locale locale;

    public CollectionSuggestionProvider(Collection<String> values) {
        this(values, MatchMode.BEGINS);
    }

    public CollectionSuggestionProvider(Collection<String> values, MatchMode matchMode) {
        this(values, matchMode, false);
    }

    public CollectionSuggestionProvider(Collection<String> values, MatchMode matchMode, boolean ignoreCase) {
        this(values, matchMode, ignoreCase, Locale.getDefault());
    }

    public CollectionSuggestionProvider(Collection<String> values, MatchMode matchMode, boolean ignoreCase, Locale locale) {
        validateValues(values);
        validateMatchMode(matchMode);
        validateLocale(locale);
        this.values = copyToList(values);
        this.matchMode = matchMode;
        this.ignoreCase = ignoreCase;
        this.locale = locale;
        refreshSearchList();
    }

    private void validateValues(Collection<String> values) {
        if (values == null) {
            throw new NullPointerException("array is null!");
        }
    }

    private void validateMatchMode(MatchMode matchMode) {
        if (matchMode == null) {
            throw new NullPointerException("matchMode is null!");
        }
    }

    private void validateLocale(Locale locale) {
        if (locale == null) {
            throw new NullPointerException("locale is null!");
        }
    }

    /**
     * Copies the given {@link Collection} to a new {@link List}
     *
     * @param values {@link Collection} to copy
     * @return New {@link List}
     */
    private static List<String> copyToList(Collection<String> values) {
        if (values == null) {
            throw new NullPointerException("collection is null!");
        }
        return new ArrayList<String>(values);
    }

    /**
     * Converts the given {@link List} of {@link String}s to lower case
     *
     * @param list {@link List} of {@link String}s
     * @param locale {@link Locale} used for converting
     * @return {@link List} containing only lower case {@link String}s
     */
    private static List<String> listToLowerCase(List<String> list, Locale locale) {
        List<String> lowerCase = new ArrayList<String>(list);
        int length = lowerCase.size();
        for (int i = 0; i < length; ++i) {
            String normalCase = lowerCase.get(i);
            lowerCase.set(i, normalCase.toLowerCase(locale));
        }
        return lowerCase;
    }

    @Override
    public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
        String term = query.getTerm();
        if (values.isEmpty() || term == null || term.isEmpty()) {
            return Collections.emptyList();
        }
        if (ignoreCase) {
            // Use lower case version of the term for matching
            term = term.toLowerCase(locale);
        }

        boolean hasLimit = query.hasLimit();
        int limit = query.getLimit();
        int length = search.size();

        Set<AutocompleteSuggestion> suggestions;
        if (hasLimit) {
            suggestions = new LinkedHashSet<AutocompleteSuggestion>(limit);
        } else {
            suggestions = new LinkedHashSet<AutocompleteSuggestion>();
        }

        int added = 0;
        for (int i = 0; i < length; ++i) {
            if (hasLimit && added >= limit) {
                break; // limit reached, exit loop
            }
            String searchValue = search.get(i);
            if (MatchMode.BEGINS == matchMode && searchValue.startsWith(term)
                    || MatchMode.CONTAINS == matchMode && searchValue.contains(term)) {
                if (suggestions.add(new AutocompleteSuggestion(values.get(i)))) {
                    ++added;
                }
            }
        }
        return suggestions;
    }

    /**
     * Refreshes the {@link List} used for matching.
     * <p>
     * It's eigther a {@link List} of lower case values or the {@link #values}
     * {@link List} itself, depending on {@code ignoreCase}.
     * </p>
     */
    private void refreshSearchList() {
        if (ignoreCase) {
            // Use lower case values for matching
            search = listToLowerCase(values, locale);
        } else {
            // Use normal values for matching
            search = values;
        }
    }

    /**
     * Returns an unmodifiable {@link Collection} containing the values used by
     * this provider.
     *
     * @return An unmodifiable {@link Collection}
     */
    public Collection<String> getValues() {
        return Collections.unmodifiableList(values);
    }

    public void setValues(Collection<String> values) {
        validateValues(values);
        this.values = copyToList(values);
        refreshSearchList();
    }

    public MatchMode getMatchMode() {
        return matchMode;
    }

    public void setMatchMode(MatchMode matchMode) {
        validateMatchMode(matchMode);
        this.matchMode = matchMode;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        boolean changed = this.ignoreCase != ignoreCase;
        this.ignoreCase = ignoreCase;
        if (changed) {
            refreshSearchList();
        }
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        validateLocale(locale);
        boolean changed = !this.locale.equals(locale);
        this.locale = locale;
        if (changed) {
            refreshSearchList();
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.values != null ? this.values.hashCode() : 0);
        hash = 97 * hash + (this.matchMode != null ? this.matchMode.hashCode() : 0);
        hash = 97 * hash + (this.ignoreCase ? 1 : 0);
        hash = 97 * hash + (this.locale != null ? this.locale.hashCode() : 0);
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
        final CollectionSuggestionProvider other = (CollectionSuggestionProvider) obj;
        if (this.ignoreCase != other.ignoreCase) {
            return false;
        } else if (this.values != other.values && (this.values == null || !this.values.equals(other.values))) {
            return false;
        } else if (this.matchMode != other.matchMode) {
            return false;
        } else if (this.locale != other.locale && (this.locale == null || !this.locale.equals(other.locale))) {
            return false;
        }
        return true;
    }

}
