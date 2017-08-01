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
import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Window;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteEvents;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;

@Theme("valo-demo")
@Title("AutocompleteTextField Add-on Demo")
@SuppressWarnings("serial")
@PreserveOnRefresh
@Viewport(value = "width=device-width, initial-scale=1.0, user-scalable=no")
public class DemoUI extends DemoUILayout {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = true, ui = DemoUI.class, widgetset = "eu.maxschuster.vaadin.autocompletetextfield.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }

    private final CustomSuggestionProvider suggestionProvider
            = new CustomSuggestionProvider();
    
    private final Binder<CustomSuggestionProvider> suggestionProviderBinder
            = new Binder<>(CustomSuggestionProvider.class);

    private final Binder<AutocompleteTextField> binder
            = new Binder<>(AutocompleteTextField.class);
    
    private Registration selectRegistration;
    
    @Override
    protected void init(VaadinRequest request) {
        suggestionProviderBinder.forField(addIcon)
                .bind(CustomSuggestionProvider::getAddIcon, CustomSuggestionProvider::setAddIcon);
        suggestionProviderBinder.forField(addDescription)
                .bind(CustomSuggestionProvider::isAddDescription, CustomSuggestionProvider::setAddDescription);
        suggestionProviderBinder.forField(addStyleName)
                .bind(CustomSuggestionProvider::getAddStyleNames, CustomSuggestionProvider::setAddStyleNames);
        suggestionProviderBinder.setBean(suggestionProvider);

        languageField
                .withSuggestionProvider(suggestionProvider)
                .withMinChars(1)
                .withSuggestionLimit(5)
                .withValueChangeListener(this::onAutocompleteValueChange);

        scrollBehavior.setItems(ScrollBehavior.values());
        scrollBehavior.setEmptySelectionAllowed(false);
        scrollBehavior.setItemCaptionGenerator(item ->
                item.getClass().getSimpleName() + "." + item.toString());

        theme.setItems(Themes.values());
        theme.setValue(Themes.VALO);
        theme.setItemCaptionGenerator(item -> item.caption);
        theme.addValueChangeListener(this::onThemeValueChange);

        addIcon.setItems(Icons.values());
        addIcon.setValue(Icons.NONE);
        addIcon.setItemCaptionGenerator(item -> item.caption);

        visible.setValue(true);
        visible.addValueChangeListener(e -> languageField.setVisible(e.getValue()));

        enabled.setValue(true);
        enabled.addValueChangeListener(e -> languageField.setEnabled(e.getValue()));
        
        addSelectListeners.setValue(false);
        addSelectListeners.addValueChangeListener(this::onAddSelectListenersValueChange);
        
        binder.forField(delay)
                .asRequired("This value is required")
                .withConverter(new StringToIntegerConverter("Please enter a valid integer"))
                .withValidator(value -> value >= 0, "delay must be >= 0")
                .bind(AutocompleteTextField::getDelay, AutocompleteTextField::setDelay);
        binder.forField(minChars)
                .asRequired("This value is required")
                .withConverter(new StringToIntegerConverter("Please enter a valid integer"))
                .withValidator(value -> value >= 0, "minChars must be >= 0")
                .bind(AutocompleteTextField::getMinChars, AutocompleteTextField::setMinChars);
        binder.forField(suggestionLimit)
                .asRequired("This value is required")
                .withConverter(new StringToIntegerConverter("Please enter a valid integer"))
                .bind(AutocompleteTextField::getSuggestionLimit, AutocompleteTextField::setSuggestionLimit);
        binder.forField(placeholder)
                .bind(AutocompleteTextField::getPlaceholder, AutocompleteTextField::setPlaceholder);
        binder.forField(scrollBehavior)
                .bind(AutocompleteTextField::getScrollBehavior, AutocompleteTextField::setScrollBehavior);
        binder.forField(typeSearch)
                .bind(AutocompleteTextField::isTypeSearch, AutocompleteTextField::setTypeSearch);
        binder.setBean(languageField);

        windowTest.addClickListener(e -> openTestWindow());

        demoOverlayTest.setSuggestionProvider(suggestionProvider);
    }
    
    private void openTestWindow() {
        Window testWindow = new Window();
        DemoOverlayTest test = new DemoOverlayTest();
        test.setSuggestionProvider(suggestionProvider);
        testWindow.setContent(test);
        testWindow.setCaption("Window Demo");
        testWindow.center();
        getUI().addWindow(testWindow);
    }
    
    private void onAutocompleteValueChange(HasValue.ValueChangeEvent<String> event) {
        String text = "Value changed to: " + event.getValue();
        Notification notification = new Notification(
                text, Notification.Type.TRAY_NOTIFICATION);
        notification.setPosition(Position.BOTTOM_LEFT);
        notification.show(Page.getCurrent());
    }
    
    private void onThemeValueChange(HasValue.ValueChangeEvent<Themes> event) {
        setTheme(event.getValue().themeName);
    }
    
    private void onAutocompleteSelect(AutocompleteEvents.SelectEvent event) {
        AutocompleteSuggestion suggestion = event.getSuggestion();
        String caption = "Suggestion selected: " + suggestion.getValue();
        Notification notification = new Notification(
                caption, Notification.Type.TRAY_NOTIFICATION);
        notification.setDescription(suggestion.getData().toString());
        notification.setPosition(Position.BOTTOM_RIGHT);
        notification.show(Page.getCurrent());
    }
    
    private void onAddSelectListenersValueChange(HasValue.ValueChangeEvent<Boolean> event) {
        Boolean value = event.getValue();
        if (!value && selectRegistration != null) {
            selectRegistration.remove();
            selectRegistration = null;
            languageField.markAsDirty();
        } else if (value && selectRegistration == null) {
            selectRegistration = languageField
                    .addSelectListener(this::onAutocompleteSelect);
        }
    }
}
