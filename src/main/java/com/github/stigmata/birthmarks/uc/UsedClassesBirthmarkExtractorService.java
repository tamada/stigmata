package com.github.stigmata.birthmarks.uc;

import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.spi.BirthmarkExtractorService;
import com.github.stigmata.spi.BirthmarkService;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class UsedClassesBirthmarkExtractorService implements BirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "uc";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkService service){
        return new UsedClassesBirthmarkExtractor(service);
    }

    @Override
    public String getDescription(){
        return "Used classes birthmark";
    }
}