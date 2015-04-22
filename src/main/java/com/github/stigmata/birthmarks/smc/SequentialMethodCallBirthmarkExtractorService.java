package com.github.stigmata.birthmarks.smc;

import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.spi.BirthmarkExtractorService;
import com.github.stigmata.spi.BirthmarkService;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class SequentialMethodCallBirthmarkExtractorService implements BirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "smc";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkService service){
        return new SequentialMethodCallBirthmarkExtractor(service);
    }

    @Override
    public String getDescription(){
        return "Sequence of Method Calls birthmark";
    }
}