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

/**
 * 
 * @constructor
 * @class eu_maxschuster_vaadin_autocompletetextfield_AutocompleteTextFieldExtension
 */
function eu_maxschuster_vaadin_autocompletetextfield_AutocompleteTextFieldExtension() {

    /* jshint validthis:true */
    "use strict";
    
    var AutoComplete = window.autoComplete,
            self = this,
            fontIconPrefix = "fonticon://",
            EVENT_SELECT = "autocompletefield-select";
    
    function CustomAutoComplete() {
        AutoComplete.apply(this, arguments);
    }
    
    CustomAutoComplete.prototype = Object.create(AutoComplete.prototype);
    
    CustomAutoComplete.prototype.updateSuggestionsContainer = function(instance, resize, next) {
        AutoComplete.prototype.updateSuggestionsContainer.apply(this, arguments);
        var textField = instance.textField,
                sc = instance.suggestionsContainer,
                scStyle = sc.style,
                found = false,
                zIndexParent = textField;
        // check if the textfield is inside another overlay
        while (zIndexParent && zIndexParent.parentElement && 
                !(found = this.hasClass(zIndexParent.parentElement, 'v-overlay-container'))) {
            zIndexParent = zIndexParent.parentElement;
        }
        if (found) {
            var style = this.getStyle(zIndexParent),
                    zIndex = style.zIndex || 0,
                    newZIndex = parseInt(zIndex, 10) + 1;
            // Adjust the suggestioncontainers z-index to be higher than
            // the z-index of the textfield overlay parent.
            scStyle.zIndex = newZIndex;
        } else if (scStyle.zIndex) {
            // Reset the ajusted z-index. Not sure if this is necessary... ^^
            scStyle.zIndex = "";
        }
    };
    
    CustomAutoComplete.prototype.hide = function(instance) {
        var visible = this.isVisible(instance);
        AutoComplete.prototype.hide.apply(this, arguments);
        if (visible) { // only trigger if the box was actually hidden by this call.
            self.serverOnCloseSuggestionContainer();
        }
    };

    this.init = function () {
        this.lastResponseId = 0;
        this.pendingResponses = {};
        this.textField = this.findTextField();
        this.popupContainer = this.findPopupContainer();
        this.autoComplete = this.createAutoComplete(this.getConfig(this.getState()));
        this.scrollBehavior = null;
        this.scrollListener = false;
    };

    /**
     * Extends and extends the parent component of this extension.
     * @returns {Element} The parent element.
     */
    this.findTextField = function () {
        var textField = this.getElement(this.getParentId());
        return textField;
    };

    this.createAutoComplete = function (config) {
        var autoComplete = new CustomAutoComplete(config);
        this.currentConfig = config;
        return autoComplete;
    };

    this.findPopupContainer = function () {
        return document.querySelector('.v-overlay-container');
    };

    this.getConfig = function (state) {
        var menuClass = Array.isArray(state.menuStyleNames) ?
                state.menuStyleNames.join(" ") : "";
        return {
            selector: this.textField,
            source: this.source,
            minChars: state.minChars,
            delay: state.delay,
            cache: false, // state.cache,
            menuClass: menuClass,
            renderItem: this.renderItem,
            onSelect: this.onSelect,
            popupContainer: this.popupContainer
        };
    };

    this.compareConfig = function (oldConfig, newConfig) {
        for (var key in oldConfig) {
            if (oldConfig[key] !== newConfig[key]) {
                return false;
            }
        }
        return true;
    };

    this.onSelect = function (event, value, item) {
        var textField = self.textField,
                key = item.getAttribute('data-key');
        self.triggerEvent(textField, "change");
        // Also check if key is set, because for some reason the event name does
        // not get removed once it has been added
        if (self.hasServerSideListener(EVENT_SELECT) && key) {
            self.serverOnSelect(key);
        }
    };
    
    this.hasServerSideListener = function (event) {
        var listeners = self.getState().registeredEventListeners;
        return listeners && listeners.indexOf(event) > -1;
    };

    this.onStateChange = function () {
        var state = this.getState(),
                oldConfig = this.currentConfig,
                newConfig = this.getConfig(state),
                autoComplete = this.autoComplete;
        if (!this.compareConfig(oldConfig, newConfig)) {
            if (typeof autoComplete === "object") {
                autoComplete.destroy();
            }
            this.autoComplete = this.createAutoComplete(newConfig);
        }
        this.setScrollBehavoir(state.scrollBehavior);
        this.setTypeSearch(state.typeSearch);
    };
    
    this.setTypeSearch = function (typeSearch) {
        var textField = this.textField,
            type = typeSearch ? "search" : "text";
        if (textField.tagName.toLowerCase() !== "input") {
            return;
        }
        if (textField.getAttribute("type") !== type) {
            textField.setAttribute("type", type);
        }
    };

    this.setScrollBehavoir = function (scrollBehavior) {
        switch (scrollBehavior) {
            case "REFRESH":
            case "CLOSE":
                if (!this.scrollListener) {
                    window.addEventListener("scroll", this.onScroll, true);
                    this.scrollListener = true;
                }
                break;
            default: // including "NONE"
                if (this.scrollListener) {
                    window.removeEventListener("scroll", this.onScroll, true);
                    this.scrollListener = false;
                }
                break;
        }
        this.scrollBehavior = scrollBehavior;
    };

    this.onScroll = function (ev) {
        var scrollBehavior = self.scrollBehavior,
                autoComplete = self.autoComplete,
                autoCompleteInstance = self.textField.autoCompleteInstance,
                suggestionsContainer = autoCompleteInstance.suggestionsContainer;
        
        if (!autoComplete.isVisible(autoCompleteInstance)) {
            return;
        }
        
        // ignore scroll events from inside the suggestionsContainer
        var found, el = ev.target || ev.srcElement;
        while (el && !(found = (el === suggestionsContainer))) {
            el = el.parentElement;
        }
        if (found) {
            return;
        }
        
        switch (scrollBehavior) {
            case "REFRESH":
                autoComplete.updateSuggestionsContainer(autoCompleteInstance, true);
                break;
            case "CLOSE":
                autoComplete.hide(autoCompleteInstance);
                break;
            default:
                // Do nothing
                break;
        }
    };

    this.onUnregister = function () {
        this.autoComplete.destroy();
        if (this.scrollListener) {
            window.removeEventListener("scroll", this.onScroll, true);
            this.scrollListener = false;
        }
    };

    this.setSuggestions = function (responseId, suggestions) {
        var pendingResponses = this.pendingResponses;
        if (typeof pendingResponses[responseId] === "function") {
            pendingResponses[responseId](suggestions);
            delete pendingResponses[responseId];
        }
    };

    /**
     * @param {type} unsafe
     * @returns {String}
     * @see http://stackoverflow.com/a/6234804
     */
    this.escapeHtml = function (unsafe) {
        if (!unsafe || typeof unsafe !== "string") {
            // Is no string or empty. Do nothing with it...
            return unsafe;
        }
        return unsafe.replace(/&/g, "&amp;")
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;")
                .replace(/"/g, "&quot;")
                .replace(/'/g, "&#039;");
    };

    /**
     * Trigger an Event
     * 
     * @param {Element} element
     * @param {string} event
     * @returns {undefined}
     * @see http://stackoverflow.com/a/2856602
     */
    this.triggerEvent = function (element, event) {
        var evt = document.createEvent("HTMLEvents");
        evt.initEvent(event, false, true);
        element.dispatchEvent(evt);
    };

    this.source = function (term, response) {
        var responseId = ++self.lastResponseId;
        response.term = term;
        self.serverQuerySuggestions(responseId, term);
        self.pendingResponses[responseId] = response;
    };

    this.renderItem = function (item, search) {
        var escape = self.getState().itemsAsHtml,
                valueEscaped = self.escapeHtml(item.value),
                value = item.value,
                key = item.key,
                description = item.description,
                icon = item.icon,
                itemClass = 'autocomplete-suggestion',
                classes = [itemClass],
                styleNames = item.styleNames;

        if (escape) {
            value = valueEscaped;
            description = self.escapeHtml(description);
        }

        if (description) {
            classes.push('has-description');
        }

        if (icon) {
            classes.push('has-icon');
        }

        //  Add the item styles
        if (Array.isArray(styleNames)) {
            classes.push.apply(classes, styleNames);
        }

        var rendered = '<div class="' + classes.join(' ') + // classes
                '" data-val="' + valueEscaped + '"' + // value
                (key ? ' data-key="' + self.escapeHtml(key) + '"' : '') + '>'; // id
        rendered += '<div class="' + itemClass + '-content">';
        if (icon) {
            rendered += '<div class="' + itemClass + '-icon">';
            rendered += self.getIconHtml(icon);
            rendered += '</div>';
        }
        rendered += '<div class="' + itemClass + '-text">';
        rendered += '<div class="' + itemClass + '-value"><span>' +
                value +
                '</span></div>';
        if (description) {
            rendered += '<div class="' + itemClass + '-description"><span>' +
                    description +
                    '</span></div>';
        }
        rendered += '</div>'; // itemClass + '-value'
        rendered += '</div>'; // itemClass + '-text'
        rendered += '</div>'; // itemClass + '-content'

        return rendered;
    };

    this.getResource = function (resourceKey) {
        var resources = this.getState().resources;
        if (typeof resources[resourceKey] !== "object") {
            return null;
        }
        return resources[resourceKey];
    };

    this.getIconHtml = function (resourceKey) {
        var resource = this.getResource(resourceKey);
        if (typeof resource !== "object") {
            return false;
        }
        var vaadinUri = resource.uRL;
        if (vaadinUri.substr(0, fontIconPrefix.length) === fontIconPrefix) {
            // is fonticon
            var parts = vaadinUri.substr(fontIconPrefix.length).split("/"),
                    fontFamily = decodeURI(parts[0]),
                    codepoint = String.fromCharCode(parseInt(parts[1], 16));
            return '<span class="v-icon ' + fontFamily + '">' +
                    codepoint +
                    '</span>';
        } else {
            return '<img class="v-icon" src="' + this.translateVaadinUri(vaadinUri) + '" />';
        }
    };

    this.init();

}