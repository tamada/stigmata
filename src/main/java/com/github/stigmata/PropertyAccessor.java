package com.github.stigmata;

import java.util.Iterator;

/**
 * Accessor for properties of extractors, comparators, and filters.
 * 
 * @author Haruaki Tamada
 *
 */
public interface PropertyAccessor {
    /**
     * returns type of corresponding birthmark type.
     * @return
     */
    public String getType();
    /**
     * returns key list of this property holder.
     * @return iterator of key list.
     */
    public Iterator<String> getPropertyKeys();

    /**
     * sets given property.
     * @param key property key.
     * @param value property value.
     */
    public void setProperty(String key, Object value);

    /**
     * returns property value of given key.
     * @param key property key.
     * @return property value corresponding given key.
     */
    public Object getProperty(String key);
}
