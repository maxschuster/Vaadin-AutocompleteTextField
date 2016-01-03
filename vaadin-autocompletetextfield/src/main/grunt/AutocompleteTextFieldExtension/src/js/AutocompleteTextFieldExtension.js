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

    var self = this;

    this.init = function () {
        this.lastResponseId = 0;
        this.pendingResponses = {};
        this.textField = this.findTextField();
        this.popupContainer = this.findPopupContainer();
        this.currentConfig = this.getConfig(this.getState());
        this.autoComplete = this.createAutoComplete(this.currentConfig);
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
        var autoComplete = new window.autoComplete(config);
        return autoComplete;
    };

    this.findPopupContainer = function () {
        return document.querySelector('.v-overlay-container');
    };

    this.getConfig = function (state) {
        var menuClass = this.isArray(state.menuStyleNames) ?
                state.menuStyleNames.join(" ") : "";
        return {
            selector: this.textField,
            source: this.source,
            minChars: state.minChars,
            delay: state.delay,
            cache: state.cache,
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
        var textField = self.textField;
        // Fake keydown to force a text change event
        self.triggerEvent(textField, "keydown");
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
    };

    this.onUnregister = function () {
        this.autoComplete.destroy();
    };

    this.getResourceUrl = function (resourceKey) {
        var resources = this.getState().resources;
        if (typeof resources[resourceKey] !== "object") {
            return null;
        }
        var resource = resources[resourceKey],
                resourceUrl = resource.uRL; // uRL == no typo!
        return this.translateVaadinUri(resourceUrl);
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
     * Cross browser add event.
     * Borrowed from https://github.com/samie/Idle
     * 
     * @param {Element} ob
     * @param {string} type
     * @param {Function} fn
     * @returns {Boolean}
     */
    this.addEvent = function (ob, type, fn) {
        if (ob.addEventListener) {
            ob.addEventListener(type, fn, false);
        } else if (ob.attachEvent) {
            ob.attachEvent('on' + type, fn);
        } else {
            type = 'on' + type;
            if (typeof ob[type] === 'function') {
                fn = (function (f1, f2) {
                    return function () {
                        f1.apply(this, arguments);
                        f2.apply(this, arguments);
                    };
                })(ob[type], fn);
            }
            ob[type] = fn;
            return true;
        }
        return false;
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
        if ("createEvent" in document) {
            var evt = document.createEvent("HTMLEvents");
            evt.initEvent(event, false, true);
            element.dispatchEvent(evt);
        } else {
            element.fireEvent("on" + event);
        }
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
        if (self.isArray(styleNames)) {
            classes.push.apply(classes, styleNames);
        }

        var rendered = '<div class="' + classes.join(' ') + '" data-val="' + valueEscaped + '">';
        rendered += '<div class="' + itemClass + '-content">';
        if (icon) {
            var iconSrc = self.getResourceUrl(icon);
            rendered += '<div class="' + itemClass + '-icon">' +
                    '<img src="' + iconSrc + '" alt="' + valueEscaped + '" />' +
                    '</div>';
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

    /**
     * Checks if the given object is an array.
     * @param {Object} object
     * @returns {Boolean}
     */
    this.isArray = function (object) {
        if (Array.isArray) {
            return Array.isArray(object);
        }
        return Object.prototype.toString.call(object) === '[object Array]';
    };

    this.init();

}