package com.github.stigmata.birthmarks;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkPreprocessor;
import com.github.stigmata.digger.ClassFileArchive;
import com.github.stigmata.spi.BirthmarkService;

public abstract class AbstractBirthmarkPreprocessor implements BirthmarkPreprocessor{
    private BirthmarkService service;

    /**
     * default constructor.
     */
    @Deprecated
    public AbstractBirthmarkPreprocessor(){
    }

    public AbstractBirthmarkPreprocessor(BirthmarkService service){
        this.service = service;
    }

    public BirthmarkService getProvider(){
        return service;
    }

    @Override
    public abstract void preprocess(ClassFileArchive[] targets, BirthmarkContext context);
}
