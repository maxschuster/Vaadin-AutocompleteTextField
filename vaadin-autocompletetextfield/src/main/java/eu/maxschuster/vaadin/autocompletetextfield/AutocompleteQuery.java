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
package eu.maxschuster.vaadin.autocompletetextfield;

import java.io.Serializable;
import java.util.Objects;

/**
 * A search query created by {@link AutocompleteTextFieldExtension} and passed
 * to a {@link AutocompleteSuggestionProvider}.
 * 
 * @author Max Schuster
 */
public final class AutocompleteQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The extension that has created this query.
     */
    private final AutocompleteTextFieldExtension extension;

    /**
     * The search term.
     */
    private final String term;

    /**
     * The search result limit. If <code>limit &lt;= 0</code> the query has no
     * limit.
     */
    private final int limit;

    /**
     * Creates a new query for the given search term.
     *
     * @param extension The extension that has created this query.
     * @param term The search term.
     */
    public AutocompleteQuery(AutocompleteTextFieldExtension extension, String term) {
        this(extension, term, 0);
    }

    /**
     * Creates a new query for the given search term with a result limit.
     *
     * @param extension The extension that has created this query.
     * @param term The search term.
     * @param limit Max result limit.
     */
    public AutocompleteQuery(AutocompleteTextFieldExtension extension, String term, int limit) {
        Objects.requireNonNull(extension, "The extension is null!");
        Objects.requireNonNull(term, "The term is null!");
        this.extension = extension;
        this.term = term;
        this.limit = limit;
    }

    /**
     * Gets the extension that has created this query.
     *
     * @return The extension that has created this query.
     */
    public AutocompleteTextFieldExtension getExtension() {
        return extension;
    }

    /**
     * Gets the search term.
     *
     * @return The search term.
     */
    public String getTerm() {
        return term;
    }

    /**
     * Gets the search result limit. If <code>limit &lt;= 0</code> the query has
     * no limit.
     *
     * @return The search result limit. If <code>limit &lt;= 0</code> the query
     * has no limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Checks if this query has a limit.
     *
     * @return {@code true} if this query has a limit.
     */
    public boolean hasLimit() {
        return limit > 0;
    }

    @Override
    public String toString() {
        return "AutocompleteQuery{" + "term=" + term + ", limit=" + limit + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.extension != null ? this.extension.hashCode() : 0);
        hash = 53 * hash + (this.term != null ? this.term.hashCode() : 0);
        hash = 53 * hash + this.limit;
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
        final AutocompleteQuery other = (AutocompleteQuery) obj;
        if (this.limit != other.limit) {
            return false;
        } else if ((this.term == null) ? (other.term != null) : !this.term.equals(other.term)) {
            return false;
        } else if (this.extension != other.extension && (this.extension == null || !this.extension.equals(other.extension))) {
            return false;
        }
        return true;
    }

}
