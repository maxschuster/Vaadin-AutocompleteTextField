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
package eu.maxschuster.vaadin.autocompletetextfield.demo;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.Resource;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author Max Schuster
 */
public class CustomSuggestionProvider extends CollectionSuggestionProvider {
    
    private final Resource imageIcon
            = new ClassResource(DemoUI.class, "vaadin.png");
    
    private boolean addDescription;
    
    private Icons addIcon;
    
    private String addStyleNames;

    public CustomSuggestionProvider() {
        super(Arrays.asList(ProgrammingLanguages.ARRAY), MatchMode.CONTAINS, true);
    }
    
    @Override
    public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
        Collection<AutocompleteSuggestion> suggestions = super.querySuggestions(query);
        int i = 0;
        for (AutocompleteSuggestion suggestion : suggestions) {
            if (addDescription) {
                suggestion.setDescription("This is a description for "
                        + suggestion.getValue() + " ...");
            }
            switch (addIcon) {
                case IMAGE:
                    suggestion.setIcon(imageIcon);
                    break;
                case FONT_ICON:
                    suggestion.setIcon(VaadinIcons.values()[i]);
                    break;
                default:
                    break;
            }
            if (addStyleNames != null) {
                suggestion.addStyleName(addStyleNames);
            }
            suggestion.setData(new DemoSuggestionData(
                    "Data of '" + suggestion.getValue() + "'"));
            ++i;
        }
        return suggestions;
    }

    public boolean isAddDescription() {
        return addDescription;
    }

    public void setAddDescription(boolean addDescription) {
        this.addDescription = addDescription;
    }

    public Icons getAddIcon() {
        return addIcon;
    }

    public void setAddIcon(Icons addIcon) {
        this.addIcon = addIcon;
    }

    public String getAddStyleNames() {
        return addStyleNames;
    }

    public void setAddStyleNames(String addStyleNames) {
        this.addStyleNames = addStyleNames;
    }
    
}
