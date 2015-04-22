package com.github.stigmata.birthmarks.comparators;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.spi.BirthmarkService;

/**
 * calculate similarities between two birthmarks by edit distance
 * algorithm (levenshtein distance).
 *
 * @author Haruaki TAMADA
 */
public class ExtendedEditDistanceBirthmarkComparator extends EditDistanceBirthmarkComparator{
    public ExtendedEditDistanceBirthmarkComparator(BirthmarkService spi){
        super(spi);
    }

    public double compare(Birthmark b1, Birthmark b2) {
        if(!b1.getType().equals(b2.getType())){
            return Double.NaN;
        }

        BirthmarkElement[] element1 = b1.getElements();
        BirthmarkElement[] element2 = b2.getElements();
        int[][] distance = createDistanceMatrics(element1, element2);

        int length = element1.length;
        if(length > element2.length){
            length = element2.length;
        }
        int d = distance[element1.length][element2.length];

        return (double)(1 - (d + Math.abs(element1.length - element2.length))) / length;
    }
}
