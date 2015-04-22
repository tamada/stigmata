package com.github.stigmata.birthmarks.fuc;

import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.spi.BirthmarkExtractorService;
import com.github.stigmata.spi.BirthmarkService;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class FrequencyUsedClassesBirthmarkExtractorService implements BirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "fuc";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkService service){
        return new FrequencyUsedClassesBirthmarkExtractor(service);
    }

    @Override
    public String getDescription(){
        return "Frequency of Used Classes birthmark";
    }
}