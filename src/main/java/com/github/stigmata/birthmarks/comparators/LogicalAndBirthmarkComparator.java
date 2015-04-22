package com.github.stigmata.birthmarks.comparators;

import java.util.HashSet;
import java.util.Set;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.spi.BirthmarkService;

/**
 * this comparator calculate following formula.
 * let f(p) and f(q) be given birthmarks, then
 * similarity of those birthmarks are defined by |f(p) and f(q)|/|f(p) or f(q)|.
 * 
 * @author Haruaki TAMADA
 */
public class LogicalAndBirthmarkComparator extends AbstractBirthmarkComparator{
    public LogicalAndBirthmarkComparator(BirthmarkService spi){
        super(spi);
    }

    @Override
    public double compare(Birthmark b1, Birthmark b2, BirthmarkContext context){
        if(!b1.getType().equals(b2.getType())){
            return Double.NaN;
        }

        BirthmarkElement[] element1 = b1.getElements();
        BirthmarkElement[] element2 = b2.getElements();
        Set<BirthmarkElement> set1 = new HashSet<BirthmarkElement>();
        for(int i = 0; i < element1.length; i++) set1.add(element1[i]);
        Set<BirthmarkElement> set2 = new HashSet<BirthmarkElement>();
        for(int i = 0; i < element2.length; i++) set2.add(element2[i]);

        Set<BirthmarkElement> set = new HashSet<BirthmarkElement>();
        for(BirthmarkElement elem: set1){
            if(set2.contains(elem)) set.add(elem);
        }
        for(BirthmarkElement elem: set2){
            if(set1.contains(elem)) set.add(elem);
        }

        int len = set1.size() + set2.size();
        int frac = set.size() * 2;

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
