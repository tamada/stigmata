package com.github.stigmata.birthmarks.smc;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;
import com.github.stigmata.birthmarks.AbstractBirthmarkService;
import com.github.stigmata.birthmarks.comparators.PlainBirthmarkComparator;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class SequentialMethodCallBirthmarkService extends AbstractBirthmarkService{
    private BirthmarkComparator comparator = new PlainBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new SequentialMethodCallBirthmarkExtractor(this);

    public SequentialMethodCallBirthmarkService(){
        super("smc");
    }

    @Override
    public String getDescription(){
        return "Sequence of method call which order is appeared in method definition.";
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
