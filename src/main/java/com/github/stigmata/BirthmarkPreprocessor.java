package com.github.stigmata;

import com.github.stigmata.digger.ClassFileArchive;

/**
 * 
 * @author Haruaki Tamada
 */
public interface BirthmarkPreprocessor{
    public void preprocess(ClassFileArchive[] targets, BirthmarkContext context);
}
