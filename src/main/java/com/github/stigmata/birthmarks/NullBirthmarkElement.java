package com.github.stigmata.birthmarks;

import com.github.stigmata.BirthmarkElement;

/**
 * Null birthmark element.
 *
 * @author Haruaki TAMADA
 */
public class NullBirthmarkElement extends BirthmarkElement{
    private static final long serialVersionUID = -92345638932523L;

    private static final NullBirthmarkElement ELEMENT = new NullBirthmarkElement();

    private NullBirthmarkElement(){
        super(null);
    }

    public static BirthmarkElement getInstance(){
        return ELEMENT;
    }

    @Override
    public int hashCode(){
        return 0;
    }

    @Override
    public boolean equals(Object o){
        return o instanceof NullBirthmarkElement;
    }

    @Override
    public String toString(){
        return "<null>";
    }
}
