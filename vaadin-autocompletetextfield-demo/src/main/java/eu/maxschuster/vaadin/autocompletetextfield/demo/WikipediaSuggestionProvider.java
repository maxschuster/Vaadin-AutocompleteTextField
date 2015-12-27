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

import com.vaadin.server.ClassResource;
import com.vaadin.server.Resource;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteQuery;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestion;
import eu.maxschuster.vaadin.autocompletetextfield.AutocompleteSuggestionProvider;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Max Schuster
 */
public class WikipediaSuggestionProvider implements AutocompleteSuggestionProvider {
    
    private static final long serialVersionUID = 1L;
    
    private final String baseUrl =
            "https://en.wikipedia.org/w/api.php?action=query&list=search&format=json&utf8=";
    
    private final Resource wikipediaIcon =
            new ClassResource(WikipediaSuggestionProvider.class, "wikipedia.gif");
    
    private boolean addDescription = false;
    
    private boolean addIcon = false;

    @Override
    public Collection<AutocompleteSuggestion> querySuggestions(AutocompleteQuery query) {
        try {
            JsonObject result = apiQuery(query);
            JsonArray search = result.getObject("query").getArray("search");
            int length = search.length();
            Set<AutocompleteSuggestion> suggestions = new LinkedHashSet<>(length);
            for (int i = 0; i < length; i++) {
                JsonObject row = search.getObject(i);
                String value = row.getString("title");
                String description = addDescription ? 
                        row.getString("snippet").replaceAll("<.*?>", "") : null;
                Resource icon = addIcon ? wikipediaIcon : null;
                
                suggestions.add(new AutocompleteSuggestion(value, description, icon));
            }
            return suggestions;
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, e.getMessage(), e);
        } finally {
            
        }
        return Collections.emptyList();
    }
    
    private JsonObject apiQuery(AutocompleteQuery query) throws MalformedURLException, UnsupportedEncodingException, IOException {
        String limitStr = query.getLimit() > 0 ? ("&srlimit=" + query.getLimit()) : "";
        URL url = new URL(baseUrl + "&srsearch=" + urlEncode(query.getTerm()) + limitStr);
        String jsonString = IOUtils.toString(url, "UTF-8");
        return Json.parse(jsonString);
    }
    
    private String urlEncode(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, "UTF-8").replace("+", "%20");
    }
    
    private Logger getLogger() {
        return Logger.getLogger(WikipediaSuggestionProvider.class.getName());
    }

    public boolean isAddDescription() {
        return addDescription;
    }

    public void setAddDescription(boolean addDescription) {
        this.addDescription = addDescription;
    }

    public boolean isAddIcon() {
        return addIcon;
    }

    public void setAddIcon(boolean addIcon) {
        this.addIcon = addIcon;
    }
    
}
