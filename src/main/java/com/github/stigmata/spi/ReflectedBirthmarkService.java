package com.github.stigmata.spi;

import java.io.Serializable;
import java.lang.reflect.Constructor;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;
import com.github.stigmata.birthmarks.AbstractBirthmarkService;

public class ReflectedBirthmarkService extends AbstractBirthmarkService implements Serializable{
    private static final long serialVersionUID = 4090172591560046236L;

    private transient BirthmarkComparator comparator;
    private String comparatorClass;
    private String description;
    private transient BirthmarkExtractor extractor;

    private String extractorClass;
    private transient BirthmarkPreprocessor preprocessor;
    private String preprocessorClass;

    public ReflectedBirthmarkService(String type, String description,
            String extractorClass, String comparatorClass){
        this(type, description, extractorClass, comparatorClass, null);
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
        super(type);
        if(type == null || extractorClass == null || comparatorClass == null){
            throw new NullPointerException();
        }
        this.description = description;
        this.extractorClass = extractorClass;
        this.comparatorClass = comparatorClass;
        this.preprocessorClass = preprocessorClass;
    }

    @Override
    public synchronized BirthmarkComparator getComparator(){
        if(comparator == null){
            comparator = instantiateClass(comparatorClass, BirthmarkComparator.class);
        }
        return comparator;
    }

    public String getComparatorClassName(){
        return comparatorClass;
    }

    @Override
    public String getDescription(){
        return description;
    }

    @Override
    public BirthmarkExtractor getExtractor(){
        if(extractor == null){
            extractor = instantiateClass(extractorClass, BirthmarkExtractor.class);
        }
        return extractor;
    }

    public String getExtractorClassName(){
        return extractorClass;
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

    @Override
    public boolean isExperimental(){
        return true;
    }

    @Override
    public boolean isUserDefined(){
        return true;
    }

    public String toString(){
        return String.format("%s, extractor=%s, comparator=%s", getType(), extractorClass, comparatorClass);
    }

}
