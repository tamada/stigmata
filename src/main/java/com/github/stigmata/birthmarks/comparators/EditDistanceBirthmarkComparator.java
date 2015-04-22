package com.github.stigmata.birthmarks.comparators;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.spi.BirthmarkService;

/**
 * calculate similarities between two birthmarks by edit distance
 * algorithm (levenshtein distance).
 *
 * @author Haruaki TAMADA
 */
public class EditDistanceBirthmarkComparator extends AbstractBirthmarkComparator{
    public EditDistanceBirthmarkComparator(BirthmarkService spi){
        super(spi);
    }

    @Override
    public double compare(Birthmark b1, Birthmark b2, BirthmarkContext context){
        if(!b1.getType().equals(b2.getType())){
            return Double.NaN;
        }

        BirthmarkElement[] element1 = b1.getElements();
        BirthmarkElement[] element2 = b2.getElements();
        int[][] distance = createDistanceMatrics(element1, element2);

        int length = element1.length;
        if(length < element2.length){
            length = element2.length;
        }
        int d = distance[element1.length][element2.length];

        if(element1.length == 0 && element2.length == 0){
            return 1d;
        }
        return (double)(length - d) / length;
    }

    protected int[][] createDistanceMatrics(BirthmarkElement[] element1,
                                            BirthmarkElement[] element2){
        int[][] distance = new int[element1.length + 1][element2.length + 1];
        for(int i = 0; i <= element1.length; i++) distance[i][0] = i;
        for(int i = 0; i <= element2.length; i++) distance[0][i] = i;

        for(int i = 1; i <= element1.length; i++){
            for(int j = 1; j <= element2.length; j++){
                int cost = 1;
                if(element1[i - 1] == null){
                    if(element2[j - 1] == null) cost = 0;
                    else                        cost = 1;
                }
                else{
                    if(element1[i - 1].equals(element2[j - 1])) cost = 0;
                    else                                        cost = 1;
                }
                int insertion = distance[i - 1][j    ] + 1;
                int deletion  = distance[i    ][j - 1] + 1;
                int replace   = distance[i - 1][j - 1] + cost;

                if(insertion <= deletion && insertion <= replace) distance[i][j] = insertion;
                else if(deletion <= replace)                      distance[i][j] = deletion;
                else                                              distance[i][j] = replace;
            }
        }
        return distance;
    }
}
