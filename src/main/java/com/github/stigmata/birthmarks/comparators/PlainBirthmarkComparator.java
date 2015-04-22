package com.github.stigmata.birthmarks.comparators;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.spi.BirthmarkService;

/**
 * An implementation of {@link BirthmarkComparator
 * <code>BirthmarkComparator</code>}.  Let <i>p</i> and <i>q</i> be
 * programs, <i>f(p)</i> and <i>f(q)</i> be extracted birthmarks from
 * <i>p</i> and <i>q</i>.  Let <i>|f(p)|</i> be a element count of
 * <i>f(p)</i>.  Then, expression of comparing birthmarks algorithm of
 * this class is defined as <i>|f(p) and f(q)|/(|f(p)| + |f(q)|)</i>.
 * 
 * @author Haruaki TAMADA
 */
public class PlainBirthmarkComparator extends AbstractBirthmarkComparator{
    public PlainBirthmarkComparator(BirthmarkService spi){
        super(spi);
    }

    @Override
    public double compare(Birthmark b1, Birthmark b2, BirthmarkContext context){
        if(!b1.getType().equals(b2.getType())){
            return Double.NaN;
        }

        BirthmarkElement[] element1 = b1.getElements();
        BirthmarkElement[] element2 = b2.getElements();
        int len = element1.length + element2.length;
        int frac = 0;
        for(int i = 0; i < element1.length && i < element2.length; i++){
            if(element1[i].equals(element2[i])){
                frac += 2;
            }
        }

        double similarity = (double)frac / (double)len;
        if(len == 0 && frac == 0){
            similarity = 1d;
        }
        return similarity;
    }

    @Override
    public int getCompareCount(Birthmark b1, Birthmark b2){
        return b1.getElementCount() + b2.getElementCount();
    }
}
