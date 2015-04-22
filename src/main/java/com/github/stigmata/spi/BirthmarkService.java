package com.github.stigmata.spi;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;

/**
 * Birthmark service provider interface.
 *
 * @author Haruaki TAMADA
 */
public interface BirthmarkService{
    /**
     * returns a type of the birthmark this service provides.
     */
    String getType();

    /**
     * returns that given type string is compatible this birthmark type.
     * @param givenType
     * @return
     */
    boolean isType(String givenType);

    /**
     * returns a description of the birthmark this service provides.
     */
    String getDescription();

    /**
     * returns a preprocessor for the birthmark of this service.
     */
    BirthmarkPreprocessor getPreprocessor();

    /**
     * returns a extractor for the birthmark of this service.
     */
    BirthmarkExtractor getExtractor();

    /**
     * returns a comparator for the birthmark of this service.
     */
    BirthmarkComparator getComparator();

    boolean isExperimental();

    boolean isUserDefined();
}

