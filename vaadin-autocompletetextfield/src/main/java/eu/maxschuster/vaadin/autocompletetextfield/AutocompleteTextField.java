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

import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.Extension;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.TextField;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import java.util.Locale;

/**
 * A {@link TextField} with autocomplete (aka word completion) functionality.
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
    private final AutocompleteTextFieldExtension extension
            = new AutocompleteTextFieldExtension(this);

    /**
     * Constructs an empty {@link AutocompleteTextField} with no caption and
     * autocomplete (aka word completion) functionality.
     */
    public AutocompleteTextField() {
        super();
    }

    /**
     * Constructs an empty {@link AutocompleteTextField} with given caption and
     * autocomplete (aka word completion) functionality.
     *
     * @param caption The caption {@link String} for the editor.
     */
    public AutocompleteTextField(String caption) {
        super(caption);
    }

    /**
     * Constructs a new {@link AutocompleteTextField} with the given caption,
     * initial text contents and autocomplete (aka word completion)
     * functionality.
     * is called to bind it.
     *
     * @param caption The caption {@link String} for the editor.
     * @param value The initial text content of the editor.
     */
    public AutocompleteTextField(String caption, String value) {
        super(caption, value);
    }

    /**
     * Gets the {@link AutocompleteTextFieldExtension} of this
     * {@link TextField}.
     *
     * @return The {@link AutocompleteTextFieldExtension} of this
     * {@link TextField}.
     */
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
     * Sets the active {@link AutocompleteSuggestionProvider}.
     *
     * @param suggestionProvider The active
     * {@link AutocompleteSuggestionProvider}.
     * @return this (for method chaining)
     * @see
     * #setSuggestionProvider(eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider)
     */
    public AutocompleteTextField withSuggestionProvider(AutocompleteSuggestionProvider suggestionProvider) {
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
    public AutocompleteTextField withSuggestionLimit(int suggestionLimit) {
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
    public AutocompleteTextField withItemAsHtml(boolean itemAsHtml) {
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
     * Sets the minimum number of characters (&gt;=1) a user must type before a
     * search is performed.
     *
     * @param minChars Minimum number of characters.
     * @return this (for method chaining)
     * @see #setMinChars(int)
     */
    public AutocompleteTextField withMinChars(int minChars) {
        setMinChars(minChars);
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
     * Sets the delay in milliseconds between when a keystroke occurs and when a
     * search is performed. A zero-delay is more responsive, but can produce a
     * lot of load.
     *
     * @param delay Search delay in milliseconds.
     * @return this (for method chaining)
     * @see #setDelay(int)
     */
    public AutocompleteTextField withDelay(int delay) {
        setDelay(delay);
        return this;
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
     * Sets if performed searches should be cached.
     *
     * @param cache Cache performed searches.
     * @return this (for method chaining)
     * @see #setCache(boolean)
     */
    public AutocompleteTextField withCache(boolean cache) {
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
     * Adds one or more style names to the dropdown menu container. Multiple
     * styles can be specified as a space-separated list of style names. The
     * style name will be rendered as a HTML class name, which can be used in a
     * CSS definition.
     *
     * @param styleNames The new style to be added to the dropdown menu
     * container.
     * @return this (for method chaining)
     * @see #addMenuStyleName(java.lang.String)
     */
    public AutocompleteTextField withMenuStyleName(String... styleNames) {
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
        extension.removeMenuStyleName(styleName);
    }

    /**
     * Gets the {@link ScrollBehavior} that is used when the user scrolls the
     * page while the suggestion box is open.
     *
     * @return The {@link ScrollBehavior}.
     */
    public ScrollBehavior getScrollBehavior() {
        return extension.getScrollBehavior();
    }

    /**
     * Sets the {@link ScrollBehavior} that is used when the user scrolls the
     * page while the suggestion box is open.
     *
     * @param scrollBehavior The {@link ScrollBehavior}.
     */
    public void setScrollBehavior(ScrollBehavior scrollBehavior) {
        extension.setScrollBehavior(scrollBehavior);
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
    public AutocompleteTextField withScrollBehavior(ScrollBehavior scrollBehavior) {
        setScrollBehavior(scrollBehavior);
        return this;
    }

    /**
     * See: {@link #addBlurListener(com.vaadin.event.FieldEvents.BlurListener) }
     *
     * @param listener The new blur listener
     * @return this (for method chaining)
     * @see #addBlurListener(com.vaadin.event.FieldEvents.BlurListener)
     */
    public AutocompleteTextField withBlurListener(FieldEvents.BlurListener listener) {
        addBlurListener(listener);
        return this;
    }

    /**
     * See:
     * {@link #addFocusListener(com.vaadin.event.FieldEvents.FocusListener)}
     *
     * @param listener The new focus listener
     * @return this (for method chaining)
     * @see #addFocusListener(com.vaadin.event.FieldEvents.FocusListener)
     */
    public AutocompleteTextField withFocusListener(FieldEvents.FocusListener listener) {
        addFocusListener(listener);
        return this;
    }

    /**
     * See: {@link #setSelection(int, int)}
     *
     * @param start Start position
     * @param length Selection length
     * @return this (for method chaining)
     * @see #setSelection(int, int)
     */
    public AutocompleteTextField withSelection(int start, int length) {
        setSelection(start, length);
        return this;
    }

    /**
     * See: {@link #setValueChangeTimeout(int)}
     *
     * @param timeout The new value change timeout
     * @return this (for method chaining)
     * @see #setValueChangeTimeout(int)
     */
    public AutocompleteTextField withValueChangeTimeout(int timeout) {
        setValueChangeTimeout(timeout);
        return this;
    }

    /**
     * See:
     * {@link #withTextChangeEventMode(com.vaadin.shared.ui.ValueChangeMode)}
     *
     * @param mode The new input event mode
     * @return this (for method chaining)
     * @see #withTextChangeEventMode(com.vaadin.shared.ui.ValueChangeMode)
     */
    public AutocompleteTextField withTextChangeEventMode(ValueChangeMode mode) {
        setValueChangeMode(mode);
        return this;
    }

    /**
     * See: {@link #setValue(java.lang.String)}
     *
     * @param value The new value
     * @return this (for method chaining)
     * @see #setValue(java.lang.String)
     * @throws NullPointerException If value is {@code null}
     */
    public AutocompleteTextField withValue(String value) throws NullPointerException {
        setValue(value);
        return this;
    }

    /**
     * See: {@link #setPlaceholder(java.lang.String)}
     *
     * @param placeholder The new placeholder
     * @return this (for method chaining)
     * @see #setPlaceholder(java.lang.String)
     */
    public AutocompleteTextField withPlaceholder(String placeholder) {
        setPlaceholder(placeholder);
        return this;
    }

    /**
     * See: {@link #setMaxLength(int)}
     *
     * @param maxLength The new text max length
     * @return this (for method chaining)
     * @see #setMaxLength(int)
     */
    public AutocompleteTextField withMaxLength(int maxLength) {
        setMaxLength(maxLength);
        return this;
    }
    
    /**
     * See: {@link #setRequiredIndicatorVisible(boolean)}
     * @param visible Set {@code true} to make required indicator visible,
     * {@code false} to not show it.
     * @return this (for method chaining)
     * @see #setRequiredIndicatorVisible(boolean) 
     */
    public AutocompleteTextField withRequiredIndicatorVisible(boolean visible) {
        setRequiredIndicatorVisible(visible);
        return this;
    }

    /**
     * See: {@link #setLocale(java.util.Locale)}
     *
     * @param locale The new locale
     * @return this (for method chaining)
     * @see #setLocale(java.util.Locale)
     */
    public AutocompleteTextField withLocale(Locale locale) {
        setLocale(locale);
        return this;
    }

    /**
     * See: {@link #setTabIndex(int)}
     *
     * @param tabIndex The new tab index
     * @return this (for method chaining)
     * @see #setTabIndex(int)
     */
    public AutocompleteTextField withTabIndex(int tabIndex) {
        setTabIndex(tabIndex);
        return this;
    }

    /**
     * See: {@link #setReadOnly(boolean)}
     *
     * @param readOnly Is read only
     * @return this (for method chaining)
     * @see #setReadOnly(boolean)
     */
    public AutocompleteTextField withReadOnly(boolean readOnly) {
        setReadOnly(readOnly);
        return this;
    }

    /**
     * See:
     * {@link #addValueChangeListener(com.vaadin.data.Property.ValueChangeListener)}
     *
     * @param listener The new value change listener
     * @return this (for method chaining)
     * @see
     * #addValueChangeListener(com.vaadin.data.Property.ValueChangeListener)
     */
    public AutocompleteTextField withValueChangeListener(ValueChangeListener<String> listener) {
        addValueChangeListener(listener);
        return this;
    }

    /**
     * See: {@link #addShortcutListener(com.vaadin.event.ShortcutListener)}
     *
     * @param shortcut The new shortcut listener
     * @return this (for method chaining)
     * @see #addShortcutListener(com.vaadin.event.ShortcutListener)
     */
    public AutocompleteTextField withShortcutListener(ShortcutListener shortcut) {
        addShortcutListener(shortcut);
        return this;
    }

    /**
     * See: {@link #setHeight(java.lang.String)}
     *
     * @param height The new height
     * @return this (for method chaining)
     * @see #setHeight(java.lang.String)
     */
    public AutocompleteTextField withHeight(String height) {
        setHeight(height);
        return this;
    }

    /**
     * See: {@link #setWidth(java.lang.String)}
     *
     * @param width The new width
     * @return this (for method chaining)
     * @see #setWidth(java.lang.String)
     */
    public AutocompleteTextField withWidth(String width) {
        setWidth(width);
        return this;
    }

    /**
     * See: {@link #setWidth(float, com.vaadin.server.Sizeable.Unit)}
     *
     * @param width The new width
     * @param unit The unit of the new {@code width} value
     * @return this (for method chaining)
     * @see #setWidth(float, com.vaadin.server.Sizeable.Unit)
     */
    public AutocompleteTextField withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    /**
     * See: {@link #setHeightUndefined()}
     *
     * @return this (for method chaining)
     * @see #setHeightUndefined()
     */
    public AutocompleteTextField withHeightUndefined() {
        setHeightUndefined();
        return this;
    }

    /**
     * See: {@link #setWidthUndefined()}
     *
     * @return this (for method chaining)
     * @see #setWidthUndefined()
     */
    public AutocompleteTextField withWidthUndefined() {
        setWidthUndefined();
        return this;
    }

    /**
     * Sets the {@code height} of this component to {@code 100%}.
     *
     * @return this (for method chaining)
     * @see #setHeight(float, com.vaadin.server.Sizeable.Unit)
     */
    public AutocompleteTextField withHeightFull() {
        return withHeight(100f, Unit.PERCENTAGE);
    }

    /**
     * Sets the {@code width} of this component to {@code 100%}.
     *
     * @return this (for method chaining)
     * @see #setWidth(float, com.vaadin.server.Sizeable.Unit)
     */
    public AutocompleteTextField withWidthFull() {
        return withWidth(100f, Unit.PERCENTAGE);
    }

    /**
     * See: {@link #setSizeUndefined()}
     *
     * @return this (for method chaining)
     * @see #setSizeUndefined()
     */
    public AutocompleteTextField withSizeUndefined() {
        setSizeUndefined();
        return this;
    }

    /**
     * See: {@link #setSizeFull()}
     *
     * @return this (for method chaining)
     * @see #setSizeFull()
     */
    public AutocompleteTextField withSizeFull() {
        setSizeFull();
        return this;
    }

    /**
     * See: {@link #setHeight(float, com.vaadin.server.Sizeable.Unit)}
     *
     * @param height The new height
     * @param unit The unit of the new {@code heigt} value
     * @return this (for method chaining)
     * @see #setHeight(float, com.vaadin.server.Sizeable.Unit)
     */
    public AutocompleteTextField withHeight(float height, Unit unit) {
        setHeight(height, unit);
        return this;
    }

    /**
     * See: {@link #setData(java.lang.Object)}
     *
     * @param data The new data
     * @return this (for method chaining)
     * @see #setData(java.lang.Object)
     */
    public AutocompleteTextField withData(Object data) {
        setData(data);
        return this;
    }

    /**
     * See: {@link #setComponentError(com.vaadin.server.ErrorMessage)}
     *
     * @param componentError The new error message
     * @return this (for method chaining)
     * @see #setComponentError(com.vaadin.server.ErrorMessage)
     */
    public AutocompleteTextField withComponentError(ErrorMessage componentError) {
        setComponentError(componentError);
        return this;
    }

    /**
     * See: {@link #setDescription(java.lang.String)}
     *
     * @param description The new description
     * @return this (for method chaining)
     * @see #setDescription(java.lang.String)
     */
    public AutocompleteTextField withDescription(String description) {
        setDescription(description);
        return this;
    }

    /**
     * See: {@link #setVisible(boolean)}
     *
     * @param visible Is visible
     * @return this (for method chaining)
     * @see #setVisible(boolean)
     */
    public AutocompleteTextField withVisible(boolean visible) {
        setVisible(visible);
        return this;
    }

    /**
     * See: {@link #setEnabled(boolean)}
     *
     * @param enabled Is enabled
     * @return this (for method chaining)
     * @see #setEnabled(boolean)
     */
    public AutocompleteTextField withEnabled(boolean enabled) {
        setEnabled(enabled);
        return this;
    }

    /**
     * See: {@link #setIcon(com.vaadin.server.Resource)}
     *
     * @param icon The new icon
     * @return this (for method chaining)
     * @see #setIcon(com.vaadin.server.Resource)
     */
    public AutocompleteTextField withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    /**
     * See: {@link #setCaptionAsHtml(boolean)}
     *
     * @param captionAsHtml Should allow HTML in caption
     * @return this (for method chaining)
     * @see #setCaptionAsHtml(boolean)
     */
    public AutocompleteTextField withCaptionAsHtml(boolean captionAsHtml) {
        setCaptionAsHtml(captionAsHtml);
        return this;
    }

    /**
     * See: {@link #setCaption(java.lang.String) }
     *
     * @param caption The new caption
     * @return this (for method chaining)
     * @see #setCaption(java.lang.String)
     */
    public AutocompleteTextField withCaption(String caption) {
        setCaption(caption);
        return this;
    }

    /**
     * See: {@link #setPrimaryStyleName(java.lang.String)}
     *
     * @param style The new primary style name
     * @return this (for method chaining)
     * @see #setPrimaryStyleName(java.lang.String)
     */
    public AutocompleteTextField withPrimaryStyleName(String style) {
        setPrimaryStyleName(style);
        return this;
    }

    /**
     * See: {@link #addStyleName(java.lang.String)}
     *
     * @param styleNames Additional style names
     * @return this (for method chaining)
     * @see #addStyleName(java.lang.String)
     */
    public AutocompleteTextField withStyleName(String... styleNames) {
        for (String styles : styleNames) {
            addStyleName(styles);
        }
        return this;
    }

    /**
     * See: {@link #setId(java.lang.String)}
     *
     * @param id The new id
     * @return this (for method chaining)
     * @see #setId(java.lang.String)
     */
    public AutocompleteTextField withId(String id) {
        setId(id);
        return this;
    }
    
    /**
     * Gets if the fields type is {@code "search"}.
     * 
     * @return {@code true} the fields type is {@code "search"}.
     */
    public boolean isTypeSearch() {
        return extension.isTypeSearch();
    }
    
    /**
     * Sets if the fields type is {@code "search"}.
     * 
     * @param typeSearch {@code true} will change this fields type to
     * {@code "search"}.
     */
    public void setTypeSearch(boolean typeSearch) {
        extension.setTypeSearch(typeSearch);
    }
    
    /**
     * Sets if the fields type is {@code "search"}.
     * 
     * @param typeSearch {@code true} will change this fields type to
     * {@code "search"}.
     * @return this (for method chaining)
     * @see #setTypeSearch(boolean)
     */
    public AutocompleteTextField withTypeSearch(boolean typeSearch) {
        extension.setTypeSearch(typeSearch);
        return this;
    }

}
