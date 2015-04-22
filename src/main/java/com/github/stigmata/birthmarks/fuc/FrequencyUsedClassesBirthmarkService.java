package com.github.stigmata.birthmarks.fuc;

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
public class FrequencyUsedClassesBirthmarkService extends AbstractBirthmarkService{
    private BirthmarkComparator comparator = new CosineSimilarityBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new FrequencyUsedClassesBirthmarkExtractor(this);

    public FrequencyUsedClassesBirthmarkService(){
        super("fuc");
    }

    @Override
    public String getDescription(){
        return "Frequency of used classes in target class.";
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
    public BirthmarkPreprocessor getPreprocessor(){
        return null;
    }
}
