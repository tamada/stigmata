package com.github.stigmata.birthmarks.is;

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
public class InheritanceStructureBirthmarkService extends AbstractBirthmarkService{
    private BirthmarkComparator comparator = new PlainBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new InheritanceStructureBirthmarkExtractor(this);

    public InheritanceStructureBirthmarkService(){
        super("is");
    }

    @Override
    public String getDescription(){
        return "Inheritance sequence to root class and user classes is replaced to <null>.";
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
