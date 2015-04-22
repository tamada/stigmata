package com.github.stigmata;

/**
 * This class represents comparing two birthmarks.
 * @author  Haruaki TAMADA
 */
public class ComparisonPairElement{
    private Birthmark birthmark1;
    private Birthmark birthmark2;
    private BirthmarkComparator comparator;
    private double similarity;
    private int compareCount = -1;

    public ComparisonPairElement(Birthmark birthmark1, Birthmark birthmark2,
            BirthmarkComparator comparator, BirthmarkContext context){
        this.birthmark1 = birthmark1;
        this.birthmark2 = birthmark2;
        this.comparator = comparator;

        if(!birthmark1.getType().equals(birthmark2.getType())){
            throw new IllegalArgumentException("birthmark type mismatch");
        }
        // cached
        similarity = comparator.compare(birthmark1, birthmark2, context);
    }

    public synchronized int getComparisonCount(){
        // cached
        if(compareCount < 0){
            compareCount = comparator.getCompareCount(birthmark1, birthmark2);
        }
        return compareCount;
    }

    /**
     * returns a type of birthmarks.
     */
    public String getType(){
        return birthmark1.getType();
    }

    /**
     * returns similarity between two birthmarks.
     */
    public double getSimilarity(){
        return similarity;
    }
}
