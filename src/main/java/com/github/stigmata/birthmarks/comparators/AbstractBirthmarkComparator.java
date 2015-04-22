package com.github.stigmata.birthmarks.comparators;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.spi.BirthmarkService;

/**
 * abstract birthmark comparator.
 *
 * @author Haruaki Tamada
 */
public abstract class AbstractBirthmarkComparator implements BirthmarkComparator{
    private BirthmarkService spi;

    @Deprecated
    public AbstractBirthmarkComparator(){
    }

    public AbstractBirthmarkComparator(BirthmarkService spi){
        this.spi = spi;
    }

    public BirthmarkService getProvider(){
        return spi;
    }

    @Override
    public String getType(){
        return spi.getType();
    }

    @Override
    public abstract double compare(Birthmark b1, Birthmark b2, BirthmarkContext context);

    @Override
    public int getCompareCount(Birthmark b1, Birthmark b2){
        return b1.getElementCount() + b2.getElementCount();
    }
}
