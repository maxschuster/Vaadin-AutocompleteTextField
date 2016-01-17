# AutocompleteTextField Add-on for Vaadin 7.4+

AutocompleteTextField is an UI component add-on for Vaadin 7.4 or later.

## Online demo

Try the add-on demo at https://maxschuster.jelastic.servint.net/AutocompleteTextField/

## Download release

Official releases of this add-on are available at Vaadin Directory. For Maven instructions, download and reviews, go to <...>

## Building and running demo

git clone https://github.com/maxschuster/Vaadin-AutocompleteTextField.git
mvn clean install
cd demo
mvn jetty:run

To see the demo, navigate to http://localhost:8080/

## Development with Eclipse IDE

For further development of this add-on, the following tool-chain is recommended:
- Eclipse IDE
- m2e wtp plug-in (install it from Eclipse Marketplace)
- Vaadin Eclipse plug-in (install it from Eclipse Marketplace)
- JRebel Eclipse plug-in (install it from Eclipse Marketplace)
- Chrome browser

### Importing project

Choose File > Import... > Existing Maven Projects

Note that Eclipse may give "Plugin execution not covered by lifecycle configuration" errors for pom.xml. Use "Permanently mark goal resources in pom.xml as ignored in Eclipse build" quick-fix to mark these errors as permanently ignored in your project. Do not worry, the project still works fine. 

### Debugging server-side

If you have not already compiled the widgetset, do it now by running vaadin:install Maven target for Vaadin-AutocompleteTextField-root project.

If you have a JRebel license, it makes on the fly code changes faster. Just add JRebel nature to your Vaadin-AutocompleteTextField-demo project by clicking project with right mouse button and choosing JRebel > Add JRebel Nature

To debug project and make code modifications on the fly in the server-side, right-click the Vaadin-AutocompleteTextField-demo project and choose Debug As > Debug on Server. Navigate to http://localhost:8080/Vaadin-AutocompleteTextField-demo/ to see the application.

### Debugging client-side

The most common way of debugging and making changes to the client-side code is dev-mode. To create debug configuration for it, open Vaadin-AutocompleteTextField-demo project properties and click "Create Development Mode Launch" button on the Vaadin tab. Right-click newly added "GWT development mode for Vaadin-AutocompleteTextField-demo.launch" and choose Debug As > Debug Configurations... Open up Classpath tab for the development mode configuration and choose User Entries. Click Advanced... and select Add Folders. Choose Java and Resources under Vaadin-AutocompleteTextField/src/main and click ok. Now you are ready to start debugging the client-side code by clicking debug. Click Launch Default Browser button in the GWT Development Mode in the launched application. Now you can modify and breakpoints to client-side classes and see changes by reloading the web page. 

Another way of debugging client-side is superdev mode. To enable it, uncomment devModeRedirectEnabled line from the end of DemoWidgetSet.gwt.xml located under Vaadin-AutocompleteTextField-demo resources folder and compile the widgetset once by running vaadin:compile Maven target for Vaadin-AutocompleteTextField-demo. Refresh Vaadin-AutocompleteTextField-demo project resources by right clicking the project and choosing Refresh. Click "Create SuperDevMode Launch" button on the Vaadin tab of the Vaadin-AutocompleteTextField-demo project properties panel to create superder mode code server launch configuration and modify the class path as instructed above. After starting the code server by running SuperDevMode launch as Java application, you can navigate to http://localhost:8080/Vaadin-AutocompleteTextField-demo/?superdevmode. Now all code changes you do to your client side will get compiled as soon as you reload the web page. You can also access Java-sources and set breakpoints inside Chrome if you enable source maps from inspector settings. 

 
## Release notes

### Version 1.0-alpha-1
- Initial version

## Roadmap

This component is developed as a hobby with no public roadmap or any guarantees of upcoming releases. That said, the following features are planned for upcoming releases:
- Nothing yet

## Issue tracking

The issues for this add-on are tracked on its github.com page. All bug reports and feature requests are appreciated. 

## Contributions

Contributions are welcome, but there are no guarantees that they are accepted as such. Process for contributing is the following:
- Fork this project
- Create an issue to this project about the contribution (bug or feature) if there is no such issue about it already. Try to keep the scope minimal.
- Develop and test the fix or functionality carefully. Only include minimum amount of code needed to fix the issue.
- Refer to the fixed issue in commit
- Send a pull request for the original project
- Comment on the original issue that you have implemented a fix for it

## License & Author

Add-on is distributed under Apache License 2.0. For license terms, see LICENSE.txt.

AutocompleteTextField is written by Max Schuster

autoComplete was originally developed by [Simon Steinberger](https://pixabay.com/users/Simon/)
and is released under the MIT License

# Developer Guide

## Getting started

Here is a simple example on how to try out the add-on component:

```java

Collection<String> theJavas = Arrays.asList(new String[] {
    "Java",
    "JavaScript",
    "Join Java",
    "JavaFX Script"
});

AutocompleteSuggestionProvider suggestionProvider = new CollectionSuggestionProvider(theJavas, MatchMode.CONTAINS, true, Locale.US);

AutocompleteTextField field = new AutocompleteTextField();
field.setSuggestionProvider(suggestionProvider);
field.addTextChangeListener(e -> {
    String text = "Text changed to: " + e.getText();
    Notification.show(text, Notification.Type.TRAY_NOTIFICATION);
});
field.addValueChangeListener(e -> {
    String text = "Value changed to: " + e.getProperty().getValue();
    Notification notification = new Notification(
            text, Notification.Type.TRAY_NOTIFICATION);
    notification.setPosition(Position.BOTTOM_LEFT);
    notification.show(Page.getCurrent());
});

```

For a more comprehensive example, see vaadin-autocompletetextfield-demo/src/main/java/eu/maxschuster/vaadin/autocompletetextfield/demo/DemoUI.java

## Features

### TextField features

You can use all the features of the normal Vaadin TextField.
At least the ones I'm aware of ;-).

This includes:
- Text change events
- Data binding 

### Extended suggestion attributes

Every suggestion can be extended by a description and an icon

## API

AutocompleteTextField JavaDoc is currently not available online.