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
 * A simple {@link AutocompleteSuggestionProvider} backed by a
 * {@link Collection}
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

    @SuppressWarnings("unchecked")
    public CollectionSuggestionProvider() {
        this(Collections.EMPTY_LIST);
    }

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
        return new ArrayList<>(values);
    }

    /**
     * Converts the given {@link List} of {@link String}s to lower case
     *
     * @param list {@link List} of {@link String}s
     * @param locale {@link Locale} used for converting
     * @return {@link List} containing only lower case {@link String}s
     */
    private static List<String> listToLowerCase(List<String> list, Locale locale) {
        List<String> lowerCase = new ArrayList<>(list);
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
            suggestions = new LinkedHashSet<>(limit);
        } else {
            suggestions = new LinkedHashSet<>();
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

    /**
     * Sets the values used by this provider.
     *
     * @param values The values used by this provider.
     */
    public void setValues(Collection<String> values) {
        validateValues(values);
        this.values = copyToList(values);
        refreshSearchList();
    }

    /**
     * Sets the values used by this provider.
     *
     * @param values The values used by this provider.
     * @return this (for method chaining)
     * @see #setValues(java.util.Collection)
     */
    public CollectionSuggestionProvider withValues(Collection<String> values) {
        setValues(values);
        return this;
    }

    /**
     * Gets the {@link MatchMode} of this provider.
     *
     * @return The {@link MatchMode} of this provider.
     */
    public MatchMode getMatchMode() {
        return matchMode;
    }

    /**
     * Sets the {@link MatchMode} of this provider.
     *
     * @param matchMode The {@link MatchMode} of this provider.
     */
    public void setMatchMode(MatchMode matchMode) {
        validateMatchMode(matchMode);
        this.matchMode = matchMode;
    }

    /**
     * Sets the {@link MatchMode} of this provider.
     *
     * @param matchMode The {@link MatchMode} of this provider.
     * @return this (for method chaining)
     * @see
     * #setMatchMode(eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode)
     */
    public CollectionSuggestionProvider withMatchMode(MatchMode matchMode) {
        setMatchMode(matchMode);
        return this;
    }

    /**
     * Gets whether this provider is case insensitive.
     *
     * @return This provider is case insensitive.
     */
    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    /**
     * Sets whether this provider is case insensitive.
     *
     * @param ignoreCase This provider is case insensitive.
     */
    public void setIgnoreCase(boolean ignoreCase) {
        boolean changed = this.ignoreCase != ignoreCase;
        this.ignoreCase = ignoreCase;
        if (changed) {
            refreshSearchList();
        }
    }

    /**
     * Sets whether this provider is case insensitive.
     *
     * @param ignoreCase This provider is case insensitive.
     * @return this (for method chaining)
     * @see #setIgnoreCase(boolean)
     */
    public CollectionSuggestionProvider withIgnoreCase(boolean ignoreCase) {
        setIgnoreCase(ignoreCase);
        return this;
    }

    /**
     * Gets the {@link Locale} of this provider.
     *
     * @return The {@link Locale} of this provider.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets the {@link Locale} of this provider.
     *
     * @param locale The {@link Locale} of this provider.
     */
    public void setLocale(Locale locale) {
        validateLocale(locale);
        boolean changed = !this.locale.equals(locale);
        this.locale = locale;
        if (changed) {
            refreshSearchList();
        }
    }

    /**
     * Sets the {@link Locale} of this provider.
     *
     * @param locale The {@link Locale} of this provider.
     * @return this (for method chaining)
     * @see #setLocale(java.util.Locale)
     */
    public CollectionSuggestionProvider withLocale(Locale locale) {
        setLocale(locale);
        return this;
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
