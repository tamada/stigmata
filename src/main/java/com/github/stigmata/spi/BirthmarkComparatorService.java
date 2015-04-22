package com.github.stigmata.spi;

import com.github.stigmata.BirthmarkComparator;

/**
 * Service provider interface for comparing birthmarks.
 *
 * @author Haruaki TAMADA
 */
public interface BirthmarkComparatorService{
    /**
     * returns a type of the birthmark this service provides.
     */
    public String getType();

    /**
     * returns a localized description of the birthmark in default locale.
     */
    public String getDescription();

    /**
     * returns a extractor for the birthmark of this service.
     */
    public BirthmarkComparator getComparator(BirthmarkService service);
}

