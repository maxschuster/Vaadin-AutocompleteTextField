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
package eu.maxschuster.vaadin.autocompletetextfield.shared;

import com.vaadin.shared.JavaScriptExtensionState;
import com.vaadin.shared.communication.SharedState;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextFieldExtension;
import java.util.List;

/**
 * {@link SharedState} of the {@link AutocompleteTextFieldExtension}.
 *
 * @author Max Schuster
 * @see AutocompleteTextFieldExtension
 */
public class AutocompleteTextFieldExtensionState extends JavaScriptExtensionState {

    /**
     * Item contains safe html.
     */
    public boolean itemAsHtml = false;

    /**
     * Minimum number of characters (&gt;=1) a user must type before a search is
     * performed.
     */
    public int minChars = 3;

    /**
     * The delay in milliseconds between when a keystroke occurs and when a
     * search is performed. A zero-delay is more responsive, but can produce a
     * lot of load.
     */
    public int delay = 150;

    /**
     * Determines if performed searches should be cached.
     */
    public boolean cache = true;

    /**
     * Custom class/es that get/s added to the dropdown menu container.
     */
    public List<String> menuStyleNames;
    
    /**
     * What to do when the page is scrolled by the user.
     */
    public ScrollBehavior scrollBehavior = ScrollBehavior.NONE;

}
