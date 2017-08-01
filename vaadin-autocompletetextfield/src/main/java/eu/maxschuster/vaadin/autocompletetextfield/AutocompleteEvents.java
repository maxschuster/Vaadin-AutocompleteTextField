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
package eu.maxschuster.vaadin.autocompletetextfield;

import com.vaadin.event.ConnectorEventListener;
import com.vaadin.event.EventRouter;
import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;
import java.lang.reflect.Method;

/**
 * Interface that serves as a wrapper for autocomplete field events.
 * 
 * @author Max Schuster
 */
public interface AutocompleteEvents {

    /**
     * Interface for components that can have {@link SelectListener}s.
     * 
     * @since 3.0
     */
    public interface SelectNotifier {

        /**
         * Adds a {@link SelectListener} to this component.
         * 
         * @param listener A {@link SelectListener} that should be added to this
         * component.
         * @return The listener registration.
         * @since 3.0
         */
        Registration addSelectListener(SelectListener listener);
        
        /**
         * Adds a {@link SelectListener} to this component.
         * 
         * Keep in mind that you can not remove a listener added by this method
         * as it does not return a registration. If you later have to remove the
         * listener please use
         * {@link #addSelectListener(eu.maxschuster.vaadin.autocompletetextfield.AutocompleteEvents.SelectListener) }
         * 
         * @param listener A {@link SelectListener} that should be added to this
         * component.
         * @return this (for method chaining)
         * @since 3.0
         * @see #addSelectListener(eu.maxschuster.vaadin.autocompletetextfield.AutocompleteEvents.SelectListener) 
         */
        SelectNotifier withSelectListener(SelectListener listener);

    }

    /**
     * This event is triggered when the user selects a suggestion.
     * 
     * @since 3.0
     */
    public static class SelectEvent extends Component.Event {
        
        /**
         * Identifier for event that can be used in {@link EventRouter}
         */
        public static final String EVENT_ID = "autocompletefield-select";

        /**
         * The selected {@link AutocompleteSuggestion}.
         */
        private final AutocompleteSuggestion suggestion;

        public SelectEvent(AbstractTextField source, AutocompleteSuggestion suggestion) {
            super(source);
            this.suggestion = suggestion;
        }

        @Override
        public AbstractTextField getComponent() {
            return (AbstractTextField) super.getComponent();
        }

        /**
         * Gets the selected {@link AutocompleteSuggestion}.
         * 
         * @return The selected {@link AutocompleteSuggestion}.
         */
        public AutocompleteSuggestion getSuggestion() {
            return suggestion;
        }

    }

    /**
     * Listener for the {@link SelectEvent}.
     * 
     * @since 3.0
     */
    @FunctionalInterface
    public interface SelectListener extends ConnectorEventListener {

        public static final Method METHOD = ReflectTools.findMethod(
                SelectListener.class, "select", SelectEvent.class);

        /**
         * A suggestion has been selected by the user.
         * 
         * @param event The {@link SelectEvent}.
         */
        void select(SelectEvent event);

    }

}
