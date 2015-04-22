package com.github.stigmata.birthmarks;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkEnvironment;
import com.github.stigmata.BirthmarkExtractionFailedException;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.ExtractionUnit;
import com.github.stigmata.spi.BirthmarkService;

/**
 * Abstract class for extracting birthmark.
 * @author  Haruaki TAMADA
 */
public abstract class AbstractBirthmarkExtractor implements BirthmarkExtractor{
    /**
     * provider.
     */
    private BirthmarkService spi;

    /**
     * default constructor.
     * @deprecated this constructor does not support service provider.
     */
    public AbstractBirthmarkExtractor(){
    }

    /**
     * constructor.
     * @param spi service provider.
     */
    public AbstractBirthmarkExtractor(BirthmarkService spi){
        this.spi = spi;
    }

    /**
     * returns the provider of this extractor.
     */
    @Override
    public BirthmarkService getProvider(){
        return spi;
    }

    @Override
    public String getType(){
        return getProvider().getType();
    }

    public final Birthmark extract(InputStream in) throws BirthmarkExtractionFailedException{
        return extract(createBirthmark(), in, new BirthmarkContext(BirthmarkEnvironment.getDefaultEnvironment()));
    }

    /**
     * extract birthmark given stream with given environment.
     */
    @Override
    public final Birthmark extract(InputStream in, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        return extract(createBirthmark(), in, context);
    }

    /**
     * extract birthmark given byte array with given environment.
     */
    @Override
    public final Birthmark extract(Birthmark birthmark, byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        return extract(birthmark, new ByteArrayInputStream(bytecode), context);
    }

    /**
     * extract birthmark given byte array with given environment.
     */
    @Override
    public final Birthmark extract(byte[] bytecode, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        return extract(createBirthmark(), new ByteArrayInputStream(bytecode), context);
    }

    /**
     * extract birthmark given stream with given environment.
     */
    @Override
    public abstract Birthmark extract(Birthmark birthmark, InputStream in, BirthmarkContext context) throws BirthmarkExtractionFailedException;

    /**
     * create birthmark.
     * @see com.github.stigmata.BirthmarkExtractor#createBirthmark()
     */
    @Override
    public Birthmark createBirthmark(){
        return new PlainBirthmark(getProvider().getType());
    }

    @Override
    public abstract ExtractionUnit[] getAcceptableUnits();

    @Override
    public boolean isAcceptable(ExtractionUnit unit){
        ExtractionUnit[] units = getAcceptableUnits();

        for(int i = 0; i < units.length; i++){
            if(units[i] == unit){
                return true;
            }
        }
        return false;
    }


    @Override
    public Iterator<String> getPropertyKeys(){
        return new ArrayList<String>().iterator();
    }

    @Override
    public void setProperty(String key, Object value){
    }

    @Override
    public Object getProperty(String key){
        return null;
    }
}
