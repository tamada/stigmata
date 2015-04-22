package com.github.stigmata.birthmarks.cvfv;

import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.spi.BirthmarkExtractorService;
import com.github.stigmata.spi.BirthmarkService;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class ConstantValueOfFieldVariableBirthmarkExtractorService implements BirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "cvfv";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkService service){
        return new ConstantValueOfFieldVariableBirthmarkExtractor(service);
    }

    @Override
    public String getDescription(){
        return "Constant Value and Field Variable birthmark";
    }
}