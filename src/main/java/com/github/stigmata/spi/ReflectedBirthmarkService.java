package com.github.stigmata.spi;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;

public class ReflectedBirthmarkService implements BirthmarkService, Serializable{
    private static final long serialVersionUID = 4090172591560046236L;

    private String type;
    private String description;
    private String extractorClass;
    private String comparatorClass;
    private String preprocessorClass;

    private transient BirthmarkExtractor extractor;
    private transient BirthmarkComparator comparator;
    private transient BirthmarkPreprocessor preprocessor;

    public ReflectedBirthmarkService(String type, String description,
            String extractorClass, String comparatorClass){
        this(type, description, extractorClass, comparatorClass, null);
    }

    public String toString(){
        return String.format("%s, extractor=%s, comparator=%s", type, extractorClass, comparatorClass);
    }

    /**
     * 
     * @param type
     * @param description
     * @param extractorClass
     * @param comparatorClass
     * @param preprocessorClass
     * @throws NullPointerException one of type, extractorClass, and comparatorClass is null. 
     */
    public ReflectedBirthmarkService(String type, String description,
            String extractorClass, String comparatorClass, String preprocessorClass){
        if(type == null || extractorClass == null || comparatorClass == null){
            throw new NullPointerException();
        }
        this.type = type;
        this.description = description;
        this.extractorClass = extractorClass;
        this.comparatorClass = comparatorClass;
        this.preprocessorClass = preprocessorClass;
    }

    @Override
    public String getType(){
        return type;
    }

    @Override
    public String getDescription(){
        return description;
    }

    @Override
    public synchronized BirthmarkPreprocessor getPreprocessor(){
        if(preprocessor == null && preprocessorClass != null){
            preprocessor = instantiateClass(preprocessorClass, BirthmarkPreprocessor.class);
        }
        return preprocessor;
    }

    private <T> T instantiateClass(String name, Class<T> type){
        try{
            Class<? extends T> targetClass = Class.forName(name).asSubclass(type);
            Constructor<? extends T> constructor = targetClass.getConstructor(BirthmarkService.class);
            return constructor.newInstance(this);
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public String getExtractorClassName(){
        return extractorClass;
    }

    @Override
    public BirthmarkExtractor getExtractor(){
        if(extractor == null){
            extractor = instantiateClass(extractorClass, BirthmarkExtractor.class);
        }
        return extractor;
    }

    public String getComparatorClassName(){
        return comparatorClass;
    }

    @Override
    public synchronized BirthmarkComparator getComparator(){
        if(comparator == null){
            comparator = instantiateClass(comparatorClass, BirthmarkComparator.class);
        }
        return comparator;
    }

    @Override
    public boolean isExperimental(){
        return true;
    }

    @Override
    public boolean isUserDefined(){
        return true;
    }

}
