package com.github.stigmata.birthmarks;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;
import com.github.stigmata.spi.BirthmarkService;

public abstract class AbstractBirthmarkService implements BirthmarkService {
    private String type;

    public AbstractBirthmarkService(String type){
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean isType(String givenType){
        return type.equals(givenType);
    }

    @Override
    public abstract String getDescription();

    @Override
    public abstract BirthmarkPreprocessor getPreprocessor();

    @Override
    public abstract BirthmarkExtractor getExtractor();

    @Override
    public abstract BirthmarkComparator getComparator();

    @Override
    public boolean isExperimental() {
        return true;
    }

    @Override
    public boolean isUserDefined() {
        return true;
    }

}
