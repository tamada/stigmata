package com.github.stigmata.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author Haruaki TAMADA
 */
public class LocalizedDescriptionManager{
    public static enum ServiceCategory{
        comparator, extractor, birthmark, formatter, filter,
    };
    private Map<Locale, ResourceBundle> resources = new HashMap<Locale, ResourceBundle>();

    /**
     * only one instance of singleton pattern.
     */
    private static LocalizedDescriptionManager manager = new LocalizedDescriptionManager();

    private LocalizedDescriptionManager(){
    }

    public String getDisplayType(Locale locale, String type){
        return getDisplayType(locale, type, ServiceCategory.birthmark);
    }

    public String getDisplayType(Locale locale, String type, ServiceCategory category){
        try{
            return getBundle(locale).getString(category.name() + "." + type + ".display.type");
        } catch(MissingResourceException e){
            return null;
        }
    }

    public String getDescription(Locale locale, String birthmarkType){
        return getDescription(locale, birthmarkType, ServiceCategory.birthmark);
    }

    public String getDescription(Locale locale, String type, ServiceCategory category){
        try{
            return getBundle(locale).getString(category.name() + "." + type + ".description");
        } catch(MissingResourceException e){
            return null;
        }
    }

    private ResourceBundle getBundle(Locale locale){
        ResourceBundle bundle = resources.get(locale);
        if(bundle == null){
            bundle = ResourceBundle.getBundle("resources.description", locale);
            resources.put(locale, bundle);
        }
        return bundle;
    }

    public static LocalizedDescriptionManager getInstance(){
        return manager;
    }
}
