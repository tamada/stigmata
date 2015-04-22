package com.github.stigmata.birthmarks.kgram;

import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.spi.BirthmarkExtractorService;
import com.github.stigmata.spi.BirthmarkService;

/**
 * Birthmark Service Provider Interface.
 *
 * @author Haruaki TAMADA
 */
public class KGramBasedBirthmarkExtractorService 
        implements BirthmarkExtractorService{

    /**
     * returns a type of the birthmark this service provides.
     */
    @Override
    public String getType(){
        return "kgram";
    }

    /**
     * returns a extractor for the birthmark of this service.
     */
    @Override
    public BirthmarkExtractor getExtractor(BirthmarkService service){
        return new KGramBasedBirthmarkExtractor(service);
    }

    @Override
    public String getDescription(){
        return "extract k-gram based birthmark";
    }
}