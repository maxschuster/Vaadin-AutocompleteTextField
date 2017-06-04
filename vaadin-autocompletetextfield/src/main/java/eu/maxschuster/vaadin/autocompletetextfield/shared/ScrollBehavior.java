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
package eu.maxschuster.vaadin.autocompletetextfield.shared;

import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextFieldExtension;

/**
 * Defines how the {@link AutocompleteTextFieldExtension} should handle
 * scrolling when the suggestion box is visible.
 * 
 * @author Max Schuster
 */
public enum ScrollBehavior {

    /**
     * Scrolling does not cause any action.
     */
    NONE,

    /**
     * Scrolling the page closes the suggestion box.
     */
    CLOSE,

    /**
     * Scrolling the page updates the position of the suggestion box.
     */
    REFRESH
    
}
