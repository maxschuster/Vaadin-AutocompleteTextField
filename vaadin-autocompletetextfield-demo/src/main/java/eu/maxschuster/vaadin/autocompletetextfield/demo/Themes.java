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

import java.util.Locale;

/**
 *
 * @author Max Schuster
 */
 enum Themes {
    VALO("Valo"),
    REINDEER("Reindeer (Compatibility)"),
    RUNO("Runo (Compatibility)"),
    CHAMELEON("Chameleon (Compatibility)");
    
    public final String caption;
    public final String themeName;

    private Themes(String caption) {
        this.caption = caption;
        this.themeName = name().toLowerCase(Locale.US).concat("-demo");
    }
    
}
