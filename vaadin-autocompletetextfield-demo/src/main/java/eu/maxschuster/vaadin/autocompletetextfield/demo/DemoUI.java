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

package eu.maxschuster.vaadin.autocompletetextfield.demo;

import com.vaadin.annotations.PreserveOnRefresh;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.ArraySuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import java.util.Collection;

@Theme("demo")
@Title("AutocompleteTextField Add-on Demo")
@SuppressWarnings("serial")
@PreserveOnRefresh
public class DemoUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = true, ui = DemoUI.class, widgetset = "eu.maxschuster.vaadin.autocompletetextfield.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    private final DemoUILayout layout = new DemoUILayout();

    @Override
    protected void init(VaadinRequest request) {
        
        final ArraySuggestionProvider languageProvider
                = new ArraySuggestionProvider(
                        ProgrammingLanguages.ARRAY, MatchMode.CONTAINS, true);

        final WikipediaSuggestionProvider wikipediaSuggestionProvider
                = new WikipediaSuggestionProvider() {
            @Override
            public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
                Collection<AutocompleteSuggestion> suggestions = super.querySuggestions(query);
                suggestions.stream().forEach((suggestion) -> {
                    suggestion.addStyleName("item-class1 item-class2");
                });
                return suggestions;
            }

        };
        wikipediaSuggestionProvider.setAddDescription(true);
        wikipediaSuggestionProvider.setAddIcon(true);

        final AutocompleteTextField atf = layout.wikipediaField;
        atf.setSuggestionLimit(5);
        atf.setItemAsHtml(true);
        atf.setDelay(500);
        atf.setSuggestionProvider(wikipediaSuggestionProvider);
        atf.addTextChangeListener(event -> {
            Notification.show("Text changed to:\n" + event.getText(), Notification.Type.TRAY_NOTIFICATION);
        });
        atf.addMenuStyleName("menu-class1 menu-class2");

        final AutocompleteTextField languageAutocompleteTextField
                = layout.languageField;
        languageAutocompleteTextField.setSuggestionProvider(languageProvider);
        languageAutocompleteTextField.setMinChars(1);
        languageAutocompleteTextField.setSuggestionLimit(4);

        setContent(layout);

    }

}
