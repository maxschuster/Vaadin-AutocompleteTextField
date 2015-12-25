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

/**
 *
 * @author Max Schuster
 */
public class AutocompleteQuery implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final AutocompleteTextFieldExtension extension;
    
    private final String term;
    
    private final int limit;
    
    public AutocompleteQuery(AutocompleteTextFieldExtension extension, String term) {
        this(extension, term, 0);
    }

    public AutocompleteQuery(AutocompleteTextFieldExtension extension, String term, int limit) {
        if (extension == null) {
            throw new NullPointerException("The extension is null!");
        } else if (term == null) {
            throw new NullPointerException("The term is null!");
        }
        this.extension = extension;
        this.term = term;
        this.limit = limit;
    }

    public AutocompleteTextFieldExtension getExtension() {
        return extension;
    }

    public String getTerm() {
        return term;
    }

    public int getLimit() {
        return limit;
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
