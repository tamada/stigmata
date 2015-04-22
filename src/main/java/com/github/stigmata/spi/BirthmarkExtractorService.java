package com.github.stigmata.spi;

import com.github.stigmata.BirthmarkExtractor;

/**
 * Service provider interface for extracting birhtmark from given class files.
 *
 * @author Haruaki TAMADA
 */
public interface BirthmarkExtractorService{
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
    public BirthmarkExtractor getExtractor(BirthmarkService service);
}

