package com.github.stigmata.birthmarks.fmc;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;
import com.github.stigmata.birthmarks.comparators.CosineSimilarityBirthmarkComparator;
import com.github.stigmata.spi.BirthmarkService;

/**
 * 
 * 
 * 
 * @author Haruaki TAMADA
 */
public class FrequencyMethodCallBirthmarkService implements BirthmarkService{
    private BirthmarkComparator comparator = new CosineSimilarityBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new FrequencyMethodCallBirthmarkExtractor(this);

    @Override
    public String getType(){
        return "fmc";
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
