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

import com.vaadin.server.Resource;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A suggestion of the {@link AutocompleteTextFieldExtension}.
 * <p>
 * Is can contain the following informations:
 * <ul>
 * <li>{@code value} - The actual value of the suggestion</li>
 * <li>{@code description} - An optional description</li>
 * <li>{@code icon} - An optional icon {@link Resource}</li>
 * <li>{@code styleNames} - An optional list of style names</li>
 * </ul>
 * <p>
 * <strong>
 * Because {@code value} contains the actual value of this suggestion, the
 * methods {@link #hashCode()} and {@link #equals(java.lang.Object)} only take
 * the {@code value} property into account!
 * </strong>
 *
 * @author Max Schuster
 * @see AutocompleteSuggestionProvider
 * @see AutocompleteTextFieldExtension
 */
public final class AutocompleteSuggestion implements Serializable {

    private static final long serialVersionUID = 3L;

    /**
     * The actual value of the suggestion.
     */
    private String value;
    
    /**
     * Optional data associated with this suggestion
     */
    private Object data;

    /**
     * Optional description of the suggestion.
     */
    private String description;

    /**
     * Optional icon of the suggestion.
     */
    private Resource icon;

    /**
     * Optional style names of the suggestion.
     */
    private List<String> styleNames;

    /**
     * Creates a new {@link AutocompleteSuggestion} with the given
     * {@code value}.
     *
     * @param value The actual {@code value} of the suggestion.
     * @throws NullPointerException If {@code value} is {@code null}.
     */
    public AutocompleteSuggestion(String value) throws NullPointerException {
        this(value, null);
    }

    /**
     * Creates a new {@link AutocompleteSuggestion} with the given {@code value}
     * and {@code description}.
     *
     * @param value The actual {@code value} of the suggestion.
     * @param description The description {@link String} or {@code null}.
     * @throws NullPointerException If {@code value} is {@code null}.
     */
    public AutocompleteSuggestion(String value, String description) throws NullPointerException {
        this(value, description, null);
    }

    /**
     * Creates a new {@link AutocompleteSuggestion} with the given
     * {@code value}, {@code description} and {@code icon}.
     *
     * @param value The actual {@code value} of the suggestion.
     * @param description The description {@link String} or {@code null}.
     * @param icon The icon {@link Resource} or {@code null}.
     * @throws NullPointerException If {@code value} is {@code null}.
     */
    public AutocompleteSuggestion(String value, String description, Resource icon) throws NullPointerException {
        validateValue(value);
        this.value = value;
        this.description = description;
        this.icon = icon;
    }

    /**
     * Validates the given {@code value}.
     *
     * @param value The actual {@code value} of the suggestion.
     * @throws NullPointerException If {@code value} is {@code null}.
     */
    private void validateValue(String value) throws NullPointerException {
        if (value == null) {
            throw new NullPointerException("value mustn't be null!");
        }
    }
    
    /**
     * Gets the data of the suggestion.
     * 
     * @return The data of the suggestion or {@code null}.
     * @since 3.0
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the data of the suggestion.
     * 
     * @param data The data of the suggestion or {@code null}.
     * @since 3.0
     */
    public void setData(Object data) {
        this.data = data;
    }
    
    /**
     * Sets the data of the suggestion.
     * 
     * @param data The data of the suggestion or {@code null}.
     * @return this (for method chaining)
     * @since 3.0
     * @see #setData(java.lang.Object) 
     */
    public AutocompleteSuggestion withData(Object data) {
        setData(data);
        return this;
    }

    /**
     * Gets the actual {@code value} of the suggestion.
     *
     * @return The actual {@code value} of the suggestion or {@code null}.
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the actual value of the suggestion.
     * <p>
     * A {@code null value} is not allowed!
     * </p>
     *
     * @param value The value of the suggestion.
     * @throws NullPointerException If {@code value} is {@code null}.
     */
    public void setValue(String value) throws NullPointerException {
        validateValue(value);
        this.value = value;
    }

    /**
     * Sets the actual value of the suggestion.
     * <p>
     * A {@code null value} is not allowed!
     * </p>
     *
     * @param value The value of the suggestion.
     * @throws NullPointerException If {@code value} is {@code null}.
     * @return this (for method chaining)
     * @see #setValue(java.lang.String)
     */
    public AutocompleteSuggestion withValue(String value) throws NullPointerException {
        setValue(value);
        return this;
    }

    /**
     * Gets the description.
     *
     * @return The description {@link String} or {@code null}.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description The description {@link String} or {@code null}.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description.
     *
     * @param description The description {@link String} or {@code null}.
     * @return this (for method chaining)
     * @see #setDescription(java.lang.String)
     */
    public AutocompleteSuggestion withDescription(String description) {
        setDescription(description);
        return this;
    }

    /**
     * Gets the icon.
     *
     * @return The icon {@link Resource} or {@code null}.
     */
    public Resource getIcon() {
        return icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon The icon {@link Resource} or {@code null}.
     */
    public void setIcon(Resource icon) {
        this.icon = icon;
    }

    /**
     * Sets the icon.
     *
     * @param icon The icon {@link Resource} or {@code null}.
     * @return this (for method chaining)
     * @see #setIcon(com.vaadin.server.Resource)
     */
    public AutocompleteSuggestion withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }

    /**
     * Gets the style names as unmodifiable {@link List}.
     *
     * @return The style names as unmodifiable {@link List} or {@code null}.
     */
    public List<String> getStyleNames() {
        return styleNames != null
                ? Collections.unmodifiableList(styleNames) : null;
    }

    /**
     * Gets all user-defined CSS style names of the suggestion. If the component
     * has multiple style names defined, the return string is a space-separated
     * list of style names.
     *
     * @return The style name or a space-separated list of user-defined style
     * names of the suggestion.
     */
    public String getStyleName() {
        String styleName = "";
        if (styleNames != null || !styleNames.isEmpty()) {
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
     * Adds one or more style names to the suggestion. Multiple styles can be
     * specified as a space-separated list of style names. The style name will
     * be rendered as a HTML class name, which can be used in a CSS definition.
     *
     * @param styleName The new style to be added to the suggestion.
     */
    public void addStyleName(String styleName) {
        if (styleName == null || styleName.isEmpty()) {
            return;
        }
        if (styleName.contains(" ")) {
            StringTokenizer tokenizer = new StringTokenizer(styleName, " ");
            while (tokenizer.hasMoreTokens()) {
                addStyleName(tokenizer.nextToken());
            }
            return;
        }
        if (styleNames == null) {
            styleNames = new ArrayList<>();
        }
        styleNames.add(styleName);
    }
    
    /**
     * Adds one or more style names to the suggestion. Multiple styles can be
     * specified as a space-separated list of style names. The style name will
     * be rendered as a HTML class name, which can be used in a CSS definition.
     *
     * @param styleNames The new styles to be added to the suggestion.
     * @return this (for method chaining)
     * @see #addStyleName(java.lang.String)
     */
    public AutocompleteSuggestion withStyleName(String... styleNames) {
        for (String styleName : styleNames) {
            addStyleName(styleName);
        }
        return this;
    }

    /**
     * Removes one or more style names from the suggestion. Multiple styles can
     * be specified as a space-separated list of style names.
     *
     * @param styleName The style name or style names to be removed.
     */
    public void removeStyleName(String styleName) {
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

    @Override
    public String toString() {
        return "AutocompleteSuggestion{" + "value=" + value + ", description="
                + description + '}';
    }

}
