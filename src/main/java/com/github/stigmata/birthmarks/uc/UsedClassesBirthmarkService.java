package com.github.stigmata.birthmarks.uc;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;
import com.github.stigmata.birthmarks.AbstractBirthmarkService;
import com.github.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class UsedClassesBirthmarkService extends AbstractBirthmarkService{
    private BirthmarkComparator comparator = new LogicalAndBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new UsedClassesBirthmarkExtractor(this);

    public UsedClassesBirthmarkService(){
        super("uc");
    }

    @Override
    public String getDescription(){
        return "Used classes birthmark";
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
