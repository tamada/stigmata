package com.github.stigmata.birthmarks.cvfv;

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
public class ConstantValueOfFieldVariableBirthmarkService extends AbstractBirthmarkService{
	private BirthmarkComparator comparator = new PlainBirthmarkComparator(this);
    private BirthmarkExtractor extractor = new ConstantValueOfFieldVariableBirthmarkExtractor(this);

    public ConstantValueOfFieldVariableBirthmarkService(){
        super("cvfv");
    }

    @Override
    public String getDescription(){
        return "Field type and its initial value.";
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
