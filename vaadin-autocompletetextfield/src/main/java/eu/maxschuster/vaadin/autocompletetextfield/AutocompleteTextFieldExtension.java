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

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.server.AbstractJavaScriptExtension;
import com.vaadin.server.ClientConnector;
import com.vaadin.server.JsonCodec;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.JavaScriptFunction;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import elemental.json.JsonValue;
import eu.maxschuster.vaadin.autocompletetextfield.shared.AutocompleteTextFieldExtensionState;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Extends an {@link AbstractTextField} with autocomplete (aka word completion)
 * functionality.
 * <p>
 * Uses a modified version of
 * <a href="https://goodies.pixabay.com/javascript/auto-complete/demo.html">
 * autoComplete</a> originally developed by
 * <a href="https://pixabay.com/users/Simon/">Simon Steinberger</a>
 * </p>
 * <p>
 * {@code autoComplete} is released under the MIT License.
 * </p>
 *
 * @author Max Schuster
 * @see <a href="https://github.com/Pixabay/JavaScript-autoComplete">
 * https://github.com/Pixabay/JavaScript-autoComplete</a>
 * @see <a href="https://github.com/maxschuster/JavaScript-autoComplete">
 * https://github.com/maxschuster/JavaScript-autoComplete</a>
 */
@JavaScript({
    "vaadin://addons/autocompletetextfield/dist/AutocompleteTextFieldExtension.min.js"
})
@StyleSheet({
    "vaadin://addons/autocompletetextfield/dist/AutocompleteTextFieldExtension.css"
})
public class AutocompleteTextFieldExtension extends AbstractJavaScriptExtension {

    private static final long serialVersionUID = 2L;

    /**
     * The max amount of suggestions send to the client-side
     */
    private int suggestionLimit = 0;

    /**
     * The suggestion provider queried for suggesions
     */
    protected AutocompleteSuggestionProvider suggestionProvider = null;

    /**
     * Construct a new {@link AutocompleteTextFieldExtension}.
     */
    public AutocompleteTextFieldExtension() {
        init(null);
    }

    /**
     * Construct a new {@link AutocompleteTextFieldExtension} and extends the
     * given {@link AbstractTextField}.
     *
     * @param target The textfield to extend.
     */
    public AutocompleteTextFieldExtension(AbstractTextField target) {
        init(target);
    }

    /**
     * Init stuff
     *
     * @param target The textfield to extend.
     */
    private void init(AbstractTextField target) {
        addFunctions();
        if (target != null) {
            extend(target);
        }
    }

    /**
     * Extends the given textfield.
     *
     * @param target The textfield to extend.
     */
    public void extend(AbstractTextField target) {
        super.extend(target);
    }

    @Override
    public AbstractTextField getParent() {
        return (AbstractTextField) super.getParent();
    }

    @Override
    protected Class<? extends ClientConnector> getSupportedParentType() {
        return AbstractTextField.class;
    }

    @Override
    protected AutocompleteTextFieldExtensionState getState() {
        return (AutocompleteTextFieldExtensionState) super.getState();
    }

    @Override
    protected AutocompleteTextFieldExtensionState getState(boolean markAsDirty) {
        return (AutocompleteTextFieldExtensionState) super.getState(markAsDirty);
    }

    @Override
    public Class<? extends AutocompleteTextFieldExtensionState> getStateType() {
        return AutocompleteTextFieldExtensionState.class;
    }

    /**
     * Adds all {@link JavaScriptFunction}s
     */
    private void addFunctions() {
        addFunction("serverQuerySuggestions", this::jsQuerySuggestions);
    }

    /**
     * Receives a search term from the client-side, executes the query and sends
     * the results to the JavaScript method "setSuggestions".
     * <p>
     * <b>Parameters:</b>
     * <ul>
     * <li>{@link JsonValue} {@code requestId} - Request id to send back to the
     * client-side.</li>
     * <li>{@link String} {@code term} - The search term.</li>
     * </ul>
     */
    private void jsQuerySuggestions(JsonArray arguments) {
        JsonValue requestId = arguments.get(0);
        String term = arguments.getString(1);
        Set<AutocompleteSuggestion> suggestions = querySuggestions(term);
        JsonValue suggestionsAsJson = suggestionsToJson(suggestions);
        callFunction("setSuggestions", requestId, suggestionsAsJson);
    };

    /**
     * Creates an {@link AutocompleteQuery} from the given search term and the
     * internal {@link #suggestionLimit} and executes it.
     *
     * Returns a {@link Set} of {@link AutocompleteSuggestion}s with a
     * predictable iteration order.
     *
     * @param term The search term.
     * @return Result {@link Set} of {@link AutocompleteSuggestion}s with a
     * predictable iteration order.
     */
    protected Set<AutocompleteSuggestion> querySuggestions(String term) {
        AutocompleteQuery autocompleteQuery
                = new AutocompleteQuery(this, term, suggestionLimit);
        return querySuggestions(autocompleteQuery);
    }

    /**
     * Executes the given {@link AutocompleteQuery} and makes sure the result is
     * within the boundries of the {@link AutocompleteQuery}'s limit.
     * <p>
     * Returns a {@link Set} of {@link AutocompleteSuggestion}s with a
     * predictable iteration order.
     * </p>
     *
     * @param query The Query.
     * @return Result {@link Set} of {@link AutocompleteSuggestion}s with a
     * predictable iteration order.
     */
    protected Set<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
        if (suggestionProvider == null) {
            // no suggestionProvider set
            return Collections.emptySet();
        }

        Collection<AutocompleteSuggestion> suggestions
                = suggestionProvider.querySuggestions(query);
        if (suggestions == null) {
            // suggestionProvider has returned null
            return Collections.emptySet();
        }

        int limit = query.getLimit();
        if (limit > 0 && limit < suggestions.size()) {
            // suggestionProvider has returned more results than allowed
            Set<AutocompleteSuggestion> subSet = new LinkedHashSet<>(limit);
            for (AutocompleteSuggestion suggestion : suggestions) {
                subSet.add(suggestion);
                if (subSet.size() >= limit) {
                    // size has reached the limit, ignore the following results
                    // TODO: Should we log a message here?
                    break;
                }
            }
            return subSet;
        } else {
            // suggestionProvider has respected the query limit
            return new LinkedHashSet<>(suggestions);
        }
    }

    /**
     * Converts the given {@link AutocompleteSuggestion} into a
     * {@link JsonValue} representation because {@link JsonCodec} can't handle
     * it itself.
     *
     * @param suggestions Suggestions.
     * @return {@link JsonValue} representation.
     */
    protected JsonValue suggestionsToJson(Set<AutocompleteSuggestion> suggestions) {
        JsonArray array = Json.createArray();
        int i = 0;
        for (AutocompleteSuggestion suggestion : suggestions) {
            JsonObject object = Json.createObject();

            String value = suggestion.getValue();
            String description = suggestion.getDescription();
            Resource icon = suggestion.getIcon();
            List<String> styleNames = suggestion.getStyleNames();

            object.put("value", value != null
                    ? Json.create(value) : Json.createNull());
            object.put("description", description != null
                    ? Json.create(description) : Json.createNull());
            if (icon != null) {
                String key = "icon-" + i;
                setResource(key, icon);
                object.put("icon", key);
            } else {
                object.put("icon", Json.createNull());
            }
            if (styleNames != null) {
                JsonArray styleNamesArray = Json.createArray();
                int s = 0;
                for (String styleName : styleNames) {
                    if (styleName == null) {
                        continue;
                    }
                    styleNamesArray.set(s++, styleName);
                }
                object.put("styleNames", styleNamesArray);
            } else {
                object.put("styleNames", Json.createNull());
            }

            array.set(i++, object);
        }
        return array;
    }

    /**
     * Gets the active {@link AutocompleteSuggestionProvider}.
     *
     * @return The active {@link AutocompleteSuggestionProvider}.
     */
    public AutocompleteSuggestionProvider getSuggestionProvider() {
        return suggestionProvider;
    }

    /**
     * Sets the active {@link AutocompleteSuggestionProvider}.
     *
     * @param suggestionProvider The active
     * {@link AutocompleteSuggestionProvider}.
     */
    public void setSuggestionProvider(AutocompleteSuggestionProvider suggestionProvider) {
        this.suggestionProvider = suggestionProvider;
    }

    /**
     * Sets the active {@link AutocompleteSuggestionProvider}.
     *
     * @param suggestionProvider The active
     * {@link AutocompleteSuggestionProvider}.
     * @return this (for method chaining)
     * @see
     * #setSuggestionProvider(eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider)
     */
    public AutocompleteTextFieldExtension withSuggestionProvider(AutocompleteSuggestionProvider suggestionProvider) {
        setSuggestionProvider(suggestionProvider);
        return this;
    }

    /**
     * Gets the maximum number of suggestions that are allowed.
     * <p>
     * If the active {@link AutocompleteSuggestionProvider} returns more
     * suggestions than allowed, the excess suggestions will be ignored!
     * </p>
     * <p>
     * If {@code limit <= 0} the suggestions won't be limited.
     * </p>
     *
     * @return Maximum number of suggestions.
     */
    public int getSuggestionLimit() {
        return suggestionLimit;
    }

    /**
     * Sets the maximum number of suggestions that are allowed.
     * <p>
     * If the active {@link AutocompleteSuggestionProvider} returns more
     * suggestions than allowed, the excess suggestions will be ignored!
     * </p>
     * <p>
     * If limit &lt;= 0 the suggestions won't be limited.
     * </p>
     *
     * @param suggestionLimit Maximum number of suggestions.
     */
    public void setSuggestionLimit(int suggestionLimit) {
        this.suggestionLimit = suggestionLimit;
    }

    /**
     * Sets the maximum number of suggestions that are allowed.
     * <p>
     * If the active {@link AutocompleteSuggestionProvider} returns more
     * suggestions than allowed, the excess suggestions will be ignored!
     * </p>
     * <p>
     * If limit &lt;= 0 the suggestions won't be limited.
     * </p>
     *
     * @param suggestionLimit Maximum number of suggestions.
     * @return this (for method chaining)
     * @see #setSuggestionLimit(int)
     */
    public AutocompleteTextFieldExtension withSuggestionLimit(int suggestionLimit) {
        setSuggestionLimit(suggestionLimit);
        return this;
    }

    /**
     * Checks whether items are rendered as HTML.
     * <p>
     * The default is false, i.e. to render that caption as plain text.
     * </p>
     *
     * @return true if the captions are rendered as HTML, false if rendered as
     * plain text.
     */
    public boolean isItemAsHtml() {
        return getState(false).itemAsHtml;
    }

    /**
     * Sets whether the items are rendered as HTML.
     * <p>
     * If set to true, the items are rendered in the browser as HTML and the
     * developer is responsible for ensuring no harmful HTML is used. If set to
     * false, the caption is rendered in the browser as plain text.
     * </p>
     * <p>
     * The default is false, i.e. to render that caption as plain text.
     * </p>
     *
     * @param itemAsHtml true if the items are rendered as HTML, false if
     * rendered as plain text.
     */
    public void setItemAsHtml(boolean itemAsHtml) {
        getState().itemAsHtml = itemAsHtml;
    }

    /**
     * Sets whether the items are rendered as HTML.
     * <p>
     * If set to true, the items are rendered in the browser as HTML and the
     * developer is responsible for ensuring no harmful HTML is used. If set to
     * false, the caption is rendered in the browser as plain text.
     * </p>
     * <p>
     * The default is false, i.e. to render that caption as plain text.
     * </p>
     *
     * @param itemAsHtml true if the items are rendered as HTML, false if
     * rendered as plain text.
     * @return this (for method chaining)
     * @see #setItemAsHtml(boolean)
     */
    public AutocompleteTextFieldExtension withItemAsHtml(boolean itemAsHtml) {
        setItemAsHtml(itemAsHtml);
        return this;
    }

    /**
     * Gets the minimum number of characters (&gt;=1) a user must type before a
     * search is performed.
     *
     * @return Minimum number of characters.
     */
    public int getMinChars() {
        return getState(false).minChars;
    }

    /**
     * Sets the minimum number of characters (&gt;=1) a user must type before a
     * search is performed.
     *
     * @param minChars Minimum number of characters.
     */
    public void setMinChars(int minChars) {
        getState().minChars = minChars;
    }

    /**
     * Sets the minimum number of characters (&gt;=1) a user must type before a
     * search is performed.
     *
     * @param minChars Minimum number of characters.
     * @return this (for method chaining)
     * @see #setMinChars(int)
     */
    public AutocompleteTextFieldExtension withMinChars(int minChars) {
        getState().minChars = minChars;
        return this;
    }

    /**
     * Gets the delay in milliseconds between when a keystroke occurs and when a
     * search is performed. A zero-delay is more responsive, but can produce a
     * lot of load.
     *
     * @return Search delay in milliseconds.
     */
    public int getDelay() {
        return getState(false).delay;
    }

    /**
     * Sets the delay in milliseconds between when a keystroke occurs and when a
     * search is performed. A zero-delay is more responsive, but can produce a
     * lot of load.
     *
     * @param delay Search delay in milliseconds.
     */
    public void setDelay(int delay) {
        getState().delay = delay;
    }

    /**
     * Sets the delay in milliseconds between when a keystroke occurs and when a
     * search is performed. A zero-delay is more responsive, but can produce a
     * lot of load.
     *
     * @param delay Search delay in milliseconds.
     * @return this (for method chaining)
     * @see #setDelay(int)
     */
    public AutocompleteTextFieldExtension withDelay(int delay) {
        setDelay(delay);
        return this;
    }

    /**
     * Checks if performed searches should be cached.
     *
     * @return Cache performed searches.
     */
    public boolean isCache() {
        return getState(false).cache;
    }

    /**
     * Sets if performed searches should be cached.
     *
     * @param cache Cache performed searches.
     */
    public void setCache(boolean cache) {
        getState().cache = cache;
    }

    /**
     * Sets if performed searches should be cached.
     *
     * @param cache Cache performed searches.
     * @return this (for method chaining)
     * @see #setCache(boolean)
     */
    public AutocompleteTextFieldExtension withCache(boolean cache) {
        setCache(cache);
        return this;
    }

    /**
     * Gets all user-defined CSS style names of the dropdown menu container. If
     * the component has multiple style names defined, the return string is a
     * space-separated list of style names.
     *
     * @return The style name or a space-separated list of user-defined style
     * names of the dropdown menu container.
     */
    public String getMenuStyleName() {
        List<String> styleNames = getState(false).menuStyleNames;
        String styleName = "";
        if (styleNames != null && !styleNames.isEmpty()) {
            Iterator<String> i = styleNames.iterator();
            while (i.hasNext()) {
                styleName += i.next();
                if (i.hasNext()) {
                    styleName += " ";
                }
            }
        }
        return styleName;
    }

    /**
     * Adds one or more style names to the dropdown menu container. Multiple
     * styles can be specified as a space-separated list of style names. The
     * style name will be rendered as a HTML class name, which can be used in a
     * CSS definition.
     *
     * @param styleName The new style to be added to the dropdown menu
     * container.
     */
    public void addMenuStyleName(String styleName) {
        List<String> styleNames = getState().menuStyleNames;
        if (styleName == null || styleName.isEmpty()) {
            return;
        }
        if (styleName.contains(" ")) {
            StringTokenizer tokenizer = new StringTokenizer(styleName, " ");
            while (tokenizer.hasMoreTokens()) {
                addMenuStyleName(tokenizer.nextToken());
            }
            return;
        }
        if (styleNames == null) {
            styleNames = new ArrayList<>();
            getState().menuStyleNames = styleNames;
        }
        styleNames.add(styleName);
    }

    /**
     * Adds one or more style names to the dropdown menu container. Multiple
     * styles can be specified as a space-separated list of style names. The
     * style name will be rendered as a HTML class name, which can be used in a
     * CSS definition.
     *
     * @param styleNames The new styles to be added to the dropdown menu
     * container.
     * @return this (for method chaining)
     * @see #addMenuStyleName(java.lang.String)
     */
    public AutocompleteTextFieldExtension withMenuStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addMenuStyleName(styleName);
        }
        return this;
    }

    /**
     * Removes one or more style names from the dropdown menu container.
     * Multiple styles can be specified as a space-separated list of style
     * names.
     *
     * @param styleName The style name or style names to be removed.
     */
    public void removeMenuStyleName(String styleName) {
        List<String> styleNames = getState().menuStyleNames;
        if (styleName == null || styleName.isEmpty() || styleNames == null) {
            return;
        }
        if (styleName.contains(" ")) {
            StringTokenizer tokenizer = new StringTokenizer(styleName, " ");
            while (tokenizer.hasMoreTokens()) {
                styleNames.remove(tokenizer.nextToken());
            }
        } else {
            styleNames.remove(styleName);
        }
    }

    /**
     * Gets the {@link ScrollBehavior} that is used when the user scrolls the
     * page while the suggestion box is open.
     *
     * @return The {@link ScrollBehavior}.
     */
    public ScrollBehavior getScrollBehavior() {
        return getState(false).scrollBehavior;
    }

    /**
     * Sets the {@link ScrollBehavior} that is used when the user scrolls the
     * page while the suggestion box is open.
     *
     * @param scrollBehavior The {@link ScrollBehavior}.
     */
    public void setScrollBehavior(ScrollBehavior scrollBehavior) {
        getState().scrollBehavior = scrollBehavior;
    }

    /**
     * Sets the {@link ScrollBehavior} that is used when the user scrolls the
     * page while the suggestion box is open.
     *
     * @param scrollBehavior The {@link ScrollBehavior}.
     * @return this (for method chaining)
     * @see
     * #setScrollBehavior(eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior)
     */
    public AutocompleteTextFieldExtension withScrollBehavior(ScrollBehavior scrollBehavior) {
        setScrollBehavior(scrollBehavior);
        return this;
    }
    
    /**
     * Gets if the fields type is {@code "search"}.
     * 
     * @return {@code true} the fields type is {@code "search"}.
     */
    public boolean isTypeSearch() {
        return getState(false).typeSearch;
    }
    
    /**
     * Sets if the fields type is {@code "search"}.
     * 
     * @param typeSearch {@code true} will change this fields type to
     * {@code "search"}.
     */
    public void setTypeSearch(boolean typeSearch) {
        getState().typeSearch = typeSearch;
    }
    
    /**
     * Sets if the fields type is {@code "search"}.
     * 
     * @param typeSearch {@code true} will change this fields type to
     * {@code "search"}.
     * @return this (for method chaining)
     * @see #setTypeSearch(boolean)
     */
    public AutocompleteTextFieldExtension withSearch(boolean typeSearch) {
        setTypeSearch(typeSearch);
        return this;
    }

}
