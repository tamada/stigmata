package com.github.stigmata.birthmarks.kgram;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;
import com.github.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator;
import com.github.stigmata.spi.BirthmarkService;


/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class KGramBasedBirthmarkService implements BirthmarkService{
    private BirthmarkComparator comparator =
        new LogicalAndBirthmarkComparator(this);
    private BirthmarkExtractor extractor =
        new KGramBasedBirthmarkExtractor(this);

    @Override
    public String getType(){
        return "kgram";
    }

    @Override
    public String getDescription(){
        return "k-gram based birthmark.";
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
