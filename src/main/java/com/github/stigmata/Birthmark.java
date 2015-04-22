package com.github.stigmata;

import java.util.Iterator;

/**
 * This interface represents a birthmark.
 * The birthmark is native characteristics of a program.
 * 
 * @author Haruaki TAMADA
 */
public interface Birthmark extends Iterable<BirthmarkElement>{
    /**
     * return all elements of this birthmark.
     * 
     * @return all elements.
     */
    public BirthmarkElement[] getElements();

    /**
     * returns the iterator for all elements of this birthmark.
     * @return iterator for accessing to elements of this birthmark.
     */
    @Override
    public Iterator<BirthmarkElement> iterator();

    /**
     * returns the number of elements of this birthmark.
     * 
     * @return element count
     */
    public int getElementCount();

    /**
     * add element to this birthmark.
     * 
     * @param element new element
     */
    public void addElement(BirthmarkElement element);

    /**
     * return the type of this birthmark.
     * 
     * @return birthmark type
     */
    public String getType();

    /**
     * This method check types of this birthmark and given birthmark are matched.
     * 
     * @param birthmark check target.
     * @return true: same type, false: not same type.
     */
    public boolean isSameType(Birthmark birthmark);
}
