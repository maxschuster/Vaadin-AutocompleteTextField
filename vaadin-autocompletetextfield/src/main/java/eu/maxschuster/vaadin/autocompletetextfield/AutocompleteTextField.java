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

import com.vaadin.data.Property;
import com.vaadin.server.Extension;
import com.vaadin.ui.TextField;

/**
 * A {@link TextField} with autocomplete (aka word completion) functionality.
 * <p>
 * Uses a modified version of
 * <a href="https://goodies.pixabay.com/javascript/auto-complete/demo.html">
 * autoComplete</a> originally developed by
 * <a href="https://pixabay.com/users/Simon/">Simon Steinberger</a>
 * </p>
 *
 * @author Max Schuster
 * @see AutocompleteTextFieldExtension
 * @see <a href="https://github.com/Pixabay/JavaScript-autoComplete">
 * https://github.com/Pixabay/JavaScript-autoComplete</a>
 * @see <a href="https://github.com/maxschuster/JavaScript-autoComplete">
 * https://github.com/maxschuster/JavaScript-autoComplete</a>
 */
public class AutocompleteTextField extends TextField {

    private static final long serialVersionUID = 1L;

    /**
     * The {@link Extension} providing the autocomplete functionality
     */
    private final AutocompleteTextFieldExtension extension;

    /**
     * Constructs an empty {@link AutocompleteTextField} with no caption and
     * autocomplete (aka word completion) functionality.
     */
    public AutocompleteTextField() {
        super();
        this.extension = extend();
    }

    /**
     * Constructs an empty {@link AutocompleteTextField} with given caption and
     * autocomplete (aka word completion) functionality.
     *
     * @param caption The caption {@link String} for the editor.
     */
    public AutocompleteTextField(String caption) {
        super(caption);
        this.extension = extend();
    }

    /**
     * Constructs a new {@link AutocompleteTextField} that's bound to the
     * specified {@link Property}, has no caption and autocomplete (aka word 
     * completion) functionality.
     *
     * @param dataSource The {@link Property} to be edited with this editor.
     */
    public AutocompleteTextField(Property<?> dataSource) {
        super(dataSource);
        this.extension = extend();
    }

    /**
     * Constructs a new {@link AutocompleteTextField} that's bound to the
     * specified {@link Property}, has the given caption {@link String} and 
     * autocomplete (aka word completion) functionality.
     *
     * @param caption The caption {@link String} for the editor.
     * @param dataSource The {@link Property} to be edited with this editor.
     */
    public AutocompleteTextField(String caption, Property<?> dataSource) {
        super(caption, dataSource);
        this.extension = extend();
    }

    /**
     * Constructs a new {@link AutocompleteTextField} with the given caption, 
     * initial text contents and autocomplete (aka word completion) 
     * functionality. The editor constructed this way will not be bound
     * to a {@link Property} unless
     * {@link com.vaadin.data.Property.Viewer#setPropertyDataSource(Property)}
     * is called to bind it.
     *
     * @param caption The caption {@link String} for the editor.
     * @param value The initial text content of the editor.
     */
    public AutocompleteTextField(String caption, String value) {
        super(caption, value);
        this.extension = extend();
    }

    /**
     * Extends this {@link AutocompleteTextField} with
     * {@link AutocompleteTextFieldExtension}
     * @return The {@link AutocompleteTextFieldExtension} instance.
     */
    private AutocompleteTextFieldExtension extend() {
        return new AutocompleteTextFieldExtension(this);
    }

    public AutocompleteTextFieldExtension getExtension() {
        return extension;
    }
    
    /**
     * Gets the active {@link AutocompleteSuggestionProvider}.
     *
     * @return The active {@link AutocompleteSuggestionProvider}.
     */
    public AutocompleteSuggestionProvider getSuggestionProvider() {
        return extension.getSuggestionProvider();
    }

    /**
     * Sets the active {@link AutocompleteSuggestionProvider}.
     *
     * @param suggestionProvider The active
     * {@link AutocompleteSuggestionProvider}.
     */
    public void setSuggestionProvider(AutocompleteSuggestionProvider suggestionProvider) {
        extension.setSuggestionProvider(suggestionProvider);
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
        return extension.getSuggestionLimit();
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
        extension.setSuggestionLimit(suggestionLimit);
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
        return extension.isItemAsHtml();
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
        extension.setItemAsHtml(itemAsHtml);
    }

    /**
     * Gets the minimum number of characters (&gt;=1) a user must type before a
     * search is performed.
     *
     * @return Minimum number of characters.
     */
    public int getMinChars() {
        return extension.getMinChars();
    }

    /**
     * Sets the minimum number of characters (&gt;=1) a user must type before a
     * search is performed.
     *
     * @param minChars Minimum number of characters.
     */
    public void setMinChars(int minChars) {
        extension.setMinChars(minChars);
    }

    /**
     * Gets the delay in milliseconds between when a keystroke occurs and when a
     * search is performed. A zero-delay is more responsive, but can produce a
     * lot of load.
     *
     * @return Search delay in milliseconds.
     */
    public int getDelay() {
        return extension.getDelay();
    }

    /**
     * Sets the delay in milliseconds between when a keystroke occurs and when a
     * search is performed. A zero-delay is more responsive, but can produce a
     * lot of load.
     *
     * @param delay Search delay in milliseconds.
     */
    public void setDelay(int delay) {
        extension.setDelay(delay);
    }

    /**
     * Checks if performed searches should be cached.
     *
     * @return Cache performed searches.
     */
    public boolean isCache() {
        return extension.isCache();
    }

    /**
     * Sets if performed searches should be cached.
     *
     * @param cache Cache performed searches.
     */
    public void setCache(boolean cache) {
        extension.setCache(cache);
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
        return extension.getMenuStyleName();
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
        extension.addMenuStyleName(styleName);
    }

    /**
     * Removes one or more style names from the dropdown menu container.
     * Multiple styles can be specified as a space-separated list of style
     * names.
     *
     * @param styleName The style name or style names to be removed.
     */
    public void removeMenuStyleName(String styleName) {
        extension.removeMenuStyleName(styleName);
    }

}
