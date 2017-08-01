/*
 * Copyright 2017 Max Schuster.
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Keeps track of visible suggestions.
 * 
 * @author Max Schuster
 * @since 3.0
 */
public class AutocompleteSuggestionTracker implements Serializable {
    
    /**
     * Key to suggestion mapping.
     */
    private final Map<String, AutocompleteSuggestion> suggestions
            = new HashMap<>();
    
    /**
     * Tracks the given suggestion and returns a key to later identify it.
     * 
     * @param suggestion The suggestion.
     * @return The key of the suggestion.
     */
    public String addSuggestion(AutocompleteSuggestion suggestion) {
        String key = generateKey(suggestion);
        if (suggestions.containsKey(key)) {
            throw new IllegalArgumentException("Key collision");
        }
        suggestions.put(key, suggestion);
        return key;
    }
    
    /**
     * Generates a key for the given suggestion.
     * 
     * The key can be random.
     * 
     * @param suggestion The suggestion.
     * @return A tracker key.
     */
    protected String generateKey(AutocompleteSuggestion suggestion) {
        return UUID.randomUUID().toString();
    }

    /**
     * Gets an optional suggestion by the given key.
     * 
     * @param key Key to retrieve.
     * @return An {@link Optional} containing an {@link AutocompleteSuggestion}
     * or {@code null}.
     */
    public Optional<AutocompleteSuggestion> getSuggestion(String key) {
        return Optional.ofNullable(suggestions.get(key));
    }
    
    /**
     * Removes the suggestion with the given key.
     * 
     * @param key Key to remove
     * @return {@code true} if a suggestion was removed.
     */
    public boolean removeKey(String key) {
        return suggestions.remove(key) != null;
    }
    
    /**
     * Removes all tracked suggestions.
     */
    public void clear() {
        suggestions.clear();
    }
    
}
