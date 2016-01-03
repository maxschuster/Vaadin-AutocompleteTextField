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
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        setContent(layout);

        final CollectionSuggestionProvider languageProvider
                = new CollectionSuggestionProvider(Arrays.asList(
                        ProgrammingLanguages.ARRAY), MatchMode.CONTAINS, true);
        
        final AutocompleteTextField languageField = layout.languageField;
        languageField.setSuggestionProvider(languageProvider);
        languageField.setMinChars(1);
        languageField.setSuggestionLimit(5);
        
        languageField.addTextChangeListener(e -> {
            String text = "Text changed to: " + e.getText();
            Notification.show(text, Notification.Type.TRAY_NOTIFICATION);
        });
        
        languageField.addValueChangeListener(e -> {
            String text = "Value changed to: " + e.getProperty().getValue();
            Notification notification = new Notification(
                    text, Notification.Type.TRAY_NOTIFICATION);
            notification.setPosition(Position.BOTTOM_LEFT);
            notification.show(Page.getCurrent());
        });
        
        final BeanFieldGroup<AutocompleteTextField> optionsGroup =
                new BeanFieldGroup<>(AutocompleteTextField.class);
        optionsGroup.setItemDataSource(languageField);
        optionsGroup.bind(layout.delay, "delay");
        optionsGroup.bind(layout.minChars, "minChars");
        optionsGroup.bind(layout.suggestionLimit, "suggestionLimit");
        optionsGroup.bind(layout.inputPrompt, "inputPrompt");
        optionsGroup.bind(layout.cache, "cache");
        
        layout.apply.addClickListener(e -> {
            try {
                optionsGroup.commit();
            } catch (FieldGroup.CommitException ex) {
                getLogger().log(Level.SEVERE, null, ex);
                Notification notification = new Notification(
                        "Error applying changes!",
                        Notification.Type.ERROR_MESSAGE);
                notification.setDelayMsec(2000);
                notification.show(Page.getCurrent());
            }
        });
        
        layout.reset.addClickListener(e -> {
            optionsGroup.discard();
            Notification.show("Options reseted!");
        });
        
        optionsGroup.addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                
            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent) throws FieldGroup.CommitException {
                Notification.show("Options applied!");
            }
        });

    }
    
    private Logger getLogger() {
        return Logger.getLogger(DemoUI.class.getName());
    }

}
