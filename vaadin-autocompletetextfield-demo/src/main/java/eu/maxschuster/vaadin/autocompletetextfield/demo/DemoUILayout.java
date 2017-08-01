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

import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.declarative.Design;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;

/**
 *
 * @author Max Schuster
 */
@DesignRoot
abstract class DemoUILayout extends UI {
    
    protected AutocompleteTextField languageField;
    protected TextField delay;
    protected TextField minChars;
    protected TextField suggestionLimit;
    protected TextField placeholder;
    protected CheckBox cache;
    protected NativeSelect<ScrollBehavior> scrollBehavior;
    protected CheckBox addDescription;
    protected NativeSelect<Icons> addIcon;
    protected TextField addStyleName;
    protected NativeSelect<Themes> theme;
    protected CheckBox visible;
    protected CheckBox enabled;
    protected CheckBox typeSearch;
    protected Button windowTest;
    protected DemoOverlayTest demoOverlayTest;
    protected Button getValueTest;
    
    public DemoUILayout() {
        Design.read(this);
    }
    
}
