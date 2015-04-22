package com.github.stigmata.hook;

/**
 * 
 * @author Haruaki Tamada
 */
public enum Phase{
    SETUP,
    TEAR_DOWN,
    BEFORE_PREPROCESS,
    AFTER_PREPROCESS,
    BEFORE_EXTRACTION,
    AFTER_EXTRACTION,
    BEFORE_COMPARISON,
    AFTER_COMPARISON,
    BEFORE_FILTERING,
    AFTER_FILTERING,
}