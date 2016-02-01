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
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.ClassResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteTextField;
import eu.maxschuster.vaadin.autocompletetextfield.provider.CollectionSuggestionProvider;
import eu.maxschuster.vaadin.autocompletetextfield.provider.MatchMode;
import eu.maxschuster.vaadin.autocompletetextfield.shared.ScrollBehavior;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.teemu.VaadinIcons;

@Theme("valo-demo")
@Title("AutocompleteTextField Add-on Demo")
@SuppressWarnings("serial")
@PreserveOnRefresh
@Viewport(value = "width=device-width, initial-scale=1.0, user-scalable=no")
public class DemoUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class, widgetset = "eu.maxschuster.vaadin.autocompletetextfield.demo.DemoWidgetSet")
    public static class Servlet extends VaadinServlet {
    }
    
    private enum Themes {
        
        VALO,
        REINDEER,
        RUNO,
        CHAMELEON;
        
        public final String themeName;
        
        Themes() {
            themeName = name().toLowerCase(Locale.US).concat("-demo");
        }
        
    }
    
    private enum Icons {
        
        NONE,
        IMAGE,
        FONT_ICON
        
    }
    
    private final CollectionSuggestionProvider languageProvider
            = new CollectionSuggestionProvider(Arrays.asList(
                    ProgrammingLanguages.ARRAY), MatchMode.CONTAINS, true) {
        @Override
        public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
            Collection<AutocompleteSuggestion> suggestions = super.querySuggestions(query);
            boolean addDescription = layout.addDescription.getValue();
            Icons addIcon = (Icons) layout.addIcon.getValue();
            if (!addDescription && addIcon == Icons.NONE) {
                return suggestions;
            }
            int i = 0;
            for (AutocompleteSuggestion suggestion : suggestions) {
                if (addDescription) {
                    suggestion.setDescription("This is a description for " +
                            suggestion.getValue() + " ...");
                }
                switch (addIcon) {
                    case IMAGE:
                        suggestion.setIcon(imageIcon);
                        break;
                    case FONT_ICON:
                        suggestion.setIcon(VaadinIcons.values()[i]);
                        break;
                    default: break;
                }
                ++i;
            } 
            return suggestions;
        }

    };

    private final DemoUILayout layout = new DemoUILayout();
    
    private final Resource imageIcon = new ClassResource(DemoUI.class, "vaadin.png");

    @Override
    protected void init(VaadinRequest request) {

        setContent(layout);
        
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

        layout.scrollBehavior.setContainerDataSource(
                new BeanItemContainer<>(ScrollBehavior.class,
                        Arrays.asList(ScrollBehavior.values())));
        
        layout.theme.addItems(Arrays.asList(Themes.values()));
        layout.theme.setValue(Themes.VALO);
        layout.theme.addValueChangeListener(e -> {
            setTheme(((Themes) e.getProperty().getValue()).themeName);
        });
        
        layout.addIcon.addItems(Arrays.asList(Icons.values()));
        layout.addIcon.setValue(Icons.NONE);
        
        layout.visible.setValue(true);
        layout.visible.addValueChangeListener(e -> {
            languageField.setVisible((boolean) e.getProperty().getValue());
        });
        
        layout.enabled.setValue(true);
        layout.enabled.addValueChangeListener(e -> {
            languageField.setEnabled((boolean) e.getProperty().getValue());
        });

        final BeanFieldGroup<AutocompleteTextField> optionsGroup
                = new BeanFieldGroup<>(AutocompleteTextField.class);
        optionsGroup.setItemDataSource(languageField);
        optionsGroup.bind(layout.delay, "delay");
        optionsGroup.bind(layout.minChars, "minChars");
        optionsGroup.bind(layout.suggestionLimit, "suggestionLimit");
        optionsGroup.bind(layout.inputPrompt, "inputPrompt");
        optionsGroup.bind(layout.scrollBehavior, "scrollBehavior");
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
        
        layout.windowTest.addClickListener(e -> {
            Window testWindow = new Window();
            DemoOverlayTest test = new DemoOverlayTest();
            test.setSuggestionProvider(languageProvider);
            testWindow.setContent(test);
            testWindow.setCaption("Window Demo");
            testWindow.center();
            getUI().addWindow(testWindow);
        });
        
        layout.demoOverlayTest.setSuggestionProvider(languageProvider);

    }

    private Logger getLogger() {
        return Logger.getLogger(DemoUI.class.getName());
    }
    

}
