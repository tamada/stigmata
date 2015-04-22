package com.github.stigmata.birthmarks.kgram;

import com.github.stigmata.BirthmarkComparator;
import com.github.stigmata.BirthmarkExtractor;
import com.github.stigmata.BirthmarkPreprocessor;
import com.github.stigmata.birthmarks.AbstractBirthmarkService;
import com.github.stigmata.birthmarks.comparators.LogicalAndBirthmarkComparator;


/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class KGramBasedBirthmarkService extends AbstractBirthmarkService{
    private int lastK;
    private BirthmarkComparator comparator = new LogicalAndBirthmarkComparator(this);
    private KGramBasedBirthmarkExtractor extractor = new KGramBasedBirthmarkExtractor(this);

    public KGramBasedBirthmarkService(){
        super("kgram");
    }

    @Override
    public boolean isType(String givenType){
        return super.isType(givenType) || givenType.equals("ngram") || isKGram(givenType);
    }

    private boolean isKGram(String givenType){
        givenType = givenType.toLowerCase();
        if(givenType.endsWith("gram")){
            try{
                String k = givenType.substring(0, givenType.length() - 4);
                int value = Integer.parseInt(k);
                if(value > 0){
                    this.lastK = value;
                    return true;
                }
            } catch(NumberFormatException e){
            }
        }
        return false;
    }

    @Override
    public String getDescription(){
        return "k-gram based birthmark.";
    }

    @Override
    public BirthmarkExtractor getExtractor(){
        if(lastK > 0){
            extractor.setKValue(lastK);
        }
        return extractor;
    }

    @Override
    public BirthmarkComparator getComparator(){
        return comparator;
    }

    @Override
    public boolean isExperimental(){
        return false;
    }

    @Override
    public boolean isUserDefined(){
        return false;
    }

    @Override
    public BirthmarkPreprocessor getPreprocessor(){
        return null;
    }
}
