package com.github.stigmata.birthmarks.wsp;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.birthmarks.comparators.AbstractBirthmarkComparator;
import com.github.stigmata.spi.BirthmarkService;

/**
 * 
 * @author Haruaki Tamada
 */
public class StackPatternBasedBirthmarkComparator
        extends AbstractBirthmarkComparator{
    public StackPatternBasedBirthmarkComparator(BirthmarkService spi){
        super(spi);
    }

    @Override
    public double compare(Birthmark b1, Birthmark b2,
			  BirthmarkContext context){
        int[][] wcs = createMatrix(b1, b2);
        int weightOfWcs = new WeightCalculator().calculateWeight(wcs);
        int weightOfBirthmark1 = 0;
        for(BirthmarkElement element: b1){
            weightOfBirthmark1 +=
		((StackPatternBasedBirthmarkElement)element).getWeight();
        }

        return (double)weightOfWcs / (double)weightOfBirthmark1;
    }

    private int[][] createMatrix(Birthmark b1, Birthmark b2){
        BirthmarkElement[] elementsA = b1.getElements();
        BirthmarkElement[] elementsB = b2.getElements();

        int[][] matrix = new int[elementsA.length][elementsB.length];
        for(int i = 0; i < elementsA.length; i++){
            for(int j = 0; j < elementsB.length; j++){
                StackPatternBasedBirthmarkElement wsp1 = 
		    (StackPatternBasedBirthmarkElement)elementsA[i];
                StackPatternBasedBirthmarkElement wsp2 =
		    (StackPatternBasedBirthmarkElement)elementsB[j];
                matrix[i][j] = wsp1.getWeight(wsp2);
            }
        }
        return matrix;
    }
}
