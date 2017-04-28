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
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.fieldgroup.BeanFieldGroup;
import com.vaadin.v7.data.fieldgroup.FieldGroup;
import com.vaadin.v7.data.util.BeanItemContainer;
import com.vaadin.v7.event.FieldEvents;
import com.vaadin.server.ClassResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
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
            boolean addDescription = l.addDescription.getValue();
            Icons addIcon = (Icons) l.addIcon.getValue();
            if (!addDescription && addIcon == Icons.NONE) {
                return suggestions;
            }
            int i = 0;
            for (AutocompleteSuggestion suggestion : suggestions) {
                if (addDescription) {
                    suggestion.setDescription("This is a description for "
                            + suggestion.getValue() + " ...");
                }
                switch (addIcon) {
                    case IMAGE:
                        suggestion.setIcon(imageIcon);
                        break;
                    case FONT_ICON:
                        suggestion.setIcon(VaadinIcons.values()[i]);
                        break;
                    default:
                        break;
                }
                ++i;
            }
            return suggestions;
        }

    };

    private final DemoUILayout l = new DemoUILayout();

    private final Resource imageIcon = new ClassResource(DemoUI.class, "vaadin.png");
    
    private final BeanFieldGroup<AutocompleteTextField> optionsGroup
            = new BeanFieldGroup<>(AutocompleteTextField.class);

    @Override
    protected void init(VaadinRequest request) {

        setContent(l);

        final AutocompleteTextField languageField = l.languageField
                .withSuggestionProvider(languageProvider)
                .withMinChars(1)
                .withSuggestionLimit(5)
                .withTextChangeListener(this::onAutocompleteTextChange)
                .withValueChangeListener(this::onAutocompleteValueChange);

        l.scrollBehavior.setContainerDataSource(
                new BeanItemContainer<>(ScrollBehavior.class,
                        Arrays.asList(ScrollBehavior.values())));

        l.theme.addItems(Arrays.asList(Themes.values()));
        l.theme.setValue(Themes.VALO);
        l.theme.addValueChangeListener(this::onThemeValueChange);

        l.addIcon.addItems(Arrays.asList(Icons.values()));
        l.addIcon.setValue(Icons.NONE);

        l.visible.setValue(true);
        l.visible.addValueChangeListener(e -> {
            languageField.setVisible((boolean) e.getProperty().getValue());
        });

        l.enabled.setValue(true);
        l.enabled.addValueChangeListener(e -> {
            languageField.setEnabled((boolean) e.getProperty().getValue());
        });

        optionsGroup.setItemDataSource(languageField);
        optionsGroup.bind(l.delay, "delay");
        optionsGroup.bind(l.minChars, "minChars");
        optionsGroup.bind(l.suggestionLimit, "suggestionLimit");
        optionsGroup.bind(l.inputPrompt, "inputPrompt");
        optionsGroup.bind(l.scrollBehavior, "scrollBehavior");
        optionsGroup.bind(l.cache, "cache");

        l.apply.addClickListener(e -> {
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

        l.reset.addClickListener(e -> reset());

        optionsGroup.addCommitHandler(new FieldGroup.CommitHandler() {
            @Override
            public void preCommit(FieldGroup.CommitEvent commitEvent)
                    throws FieldGroup.CommitException {

            }

            @Override
            public void postCommit(FieldGroup.CommitEvent commitEvent)
                    throws FieldGroup.CommitException {
                Notification.show("Options applied!");
            }
        });

        l.windowTest.addClickListener(e -> openTestWindow());

        l.demoOverlayTest.setSuggestionProvider(languageProvider);

    }

    private Logger getLogger() {
        return Logger.getLogger(DemoUI.class.getName());
    }
    
    private void reset() {
        optionsGroup.discard();
        Notification.show("Options reseted!");
    }
    
    private void openTestWindow() {
        Window testWindow = new Window();
        DemoOverlayTest test = new DemoOverlayTest();
        test.setSuggestionProvider(languageProvider);
        testWindow.setContent(test);
        testWindow.setCaption("Window Demo");
        testWindow.center();
        getUI().addWindow(testWindow);
    }
    
    private void onAutocompleteTextChange(FieldEvents.TextChangeEvent event) {
        String text = "Text changed to: " + event.getText();
        Notification.show(text, Notification.Type.TRAY_NOTIFICATION);
    }
    
    private void onAutocompleteValueChange(Property.ValueChangeEvent event) {
        String text = "Value changed to: " + event.getProperty().getValue();
        Notification notification = new Notification(
                text, Notification.Type.TRAY_NOTIFICATION);
        notification.setPosition(Position.BOTTOM_LEFT);
        notification.show(Page.getCurrent());
    }
    
    private void onThemeValueChange(Property.ValueChangeEvent event) {
        setTheme(((Themes) l.theme.getValue()).themeName);
    }

}
