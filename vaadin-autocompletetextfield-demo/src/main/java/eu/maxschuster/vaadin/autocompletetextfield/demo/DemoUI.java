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
import com.vaadin.annotations.Viewport;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import java.util.Arrays;

@Theme("demo")
@Title("AutocompleteTextField Add-on Demo")
@SuppressWarnings("serial")
@PreserveOnRefresh
@Viewport(value = "width=device-width, initial-scale=1.0, user-scalable=no")
public class DemoUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = true, ui = DemoUI.class, widgetset = "eu.maxschuster.vaadin.autocompletetextfield.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    private final DemoUILayout layout = new DemoUILayout();

    @Override
    protected void init(VaadinRequest request) {

        final CollectionSuggestionProvider languageProvider
                = new CollectionSuggestionProvider(Arrays.asList(
                        ProgrammingLanguages.ARRAY), MatchMode.CONTAINS, true);

        final AutocompleteTextField languageField = layout.languageField;
        languageField.setSuggestionProvider(languageProvider);
        languageField.setMinChars(1);
        languageField.setSuggestionLimit(5);

        setContent(layout);

    }

}
