package com.github.stigmata.birthmarks.comparators;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.spi.BirthmarkComparatorService;
import com.github.stigmata.spi.BirthmarkService;

/**
 * SPI of {@link BirthmarkComparator <code>BirthmarkComparator</code>}.
 * Comparing birthmarks by default matching algorithm.
 *
 * @author Haruaki TAMADA
 */
public class PlainBirthmarkComparatorService implements BirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "plain";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkComparator getComparator(BirthmarkService service){
        return new PlainBirthmarkComparator(service);
    }

    @Override
    public String getDescription(){
        return "Plain Comparator";
    }
}

