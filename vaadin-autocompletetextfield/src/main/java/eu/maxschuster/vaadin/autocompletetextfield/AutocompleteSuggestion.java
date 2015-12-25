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
 *
 * @author Max Schuster
 */
public class AutocompleteSuggestion implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String value;
    
    private String description;
    
    private Resource icon;
    
    private List<String> styleNames; 

    public AutocompleteSuggestion(String value) {
        this(value, null, null);
    }

    public AutocompleteSuggestion(String value, String description) {
        this(value, description, null);
    }

    public AutocompleteSuggestion(String value, String description, Resource icon) {
        this.value = value;
        this.description = description;
        this.icon = icon;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Resource getIcon() {
        return icon;
    }

    public void setIcon(Resource icon) {
        this.icon = icon;
    }
    
    public List<String> getStyleNames() {
        return styleNames != null ?
                Collections.unmodifiableList(styleNames) : null;
    }

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
            styleNames = new ArrayList<String>();
        }
        styleNames.add(styleName);
    }
    
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
        return "AutocompleteSuggestion{" + "value=" + value + ", description=" +
                description + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.value != null ? this.value.hashCode() : 0);
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
        final AutocompleteSuggestion other = (AutocompleteSuggestion) obj;
        if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value)) {
            return false;
        }
        return true;
    }
    
}
