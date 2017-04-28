/*
 * Copyright 2016 Max Schuster.
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

import com.vaadin.v7.ui.VerticalLayout;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;

/**
 *
 * @author Max Schuster
 */
public class DemoOverlayTest extends VerticalLayout {
    
    private final AutocompleteTextField windowTestField = new AutocompleteTextField();

    public DemoOverlayTest() {
        init();
    }

    private void init() {
        setMargin(true);
        windowTestField.setMinChars(1);
        windowTestField.setSuggestionLimit(5);
        addComponent(windowTestField);
    }

    public void setSuggestionProvider(AutocompleteSuggestionProvider suggestionProvider) {
        windowTestField.setSuggestionProvider(suggestionProvider);
    }
    
}
