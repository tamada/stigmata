package com.github.stigmata;

/**
 * This enum type represents birthmark extraction unit.
 * 
 * The name of
 * {@link com.github.stigmata.BirthmarkSet <code>BirthmarkSet</code>} will be
 * class name, method name, package name, or product name.
 * 
 * @author Haruaki TAMADA
 */
public enum ExtractionUnit{
    CLASS,
    PACKAGE,
    ARCHIVE,
    @Deprecated
    METHOD,
}
