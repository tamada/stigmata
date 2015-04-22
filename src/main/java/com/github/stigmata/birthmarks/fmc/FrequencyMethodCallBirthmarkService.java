package com.github.stigmata.birthmarks.fmc;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;
import com.github.stigmata.birthmarks.AbstractBirthmarkService;
import com.github.stigmata.birthmarks.comparators.CosineSimilarityBirthmarkComparator;

/**
 * 
 * 
 * 
 * @author Haruaki TAMADA
 */
public class FrequencyMethodCallBirthmarkService extends AbstractBirthmarkService{
    private BirthmarkComparator comparator = new CosineSimilarityBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new FrequencyMethodCallBirthmarkExtractor(this);

    public FrequencyMethodCallBirthmarkService(){
        super("fmc");
    }

    @Override
    public BirthmarkExtractor getExtractor(){
        return extractor;
    }

    @Override
    public BirthmarkComparator getComparator(){
        return comparator;
    }

    @Override
    public boolean isExperimental(){
        return false;
    }

    @Override
    public boolean isUserDefined(){
        return false;
    }

    @Override
    public String getDescription(){
        return "Frequency of Method Calls";
    }

    @Override
    public BirthmarkPreprocessor getPreprocessor(){
        return null;
    }
}
