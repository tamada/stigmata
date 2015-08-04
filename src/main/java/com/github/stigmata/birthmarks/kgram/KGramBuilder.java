package com.github.stigmata.birthmarks.kgram;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Haruaki Tamada
 */
public class KGramBuilder{
    private static final KGramBuilder builder = new KGramBuilder();

    /**
     * private constructor for singleton pattern.
     */
    private KGramBuilder(){
    }

    public static KGramBuilder getInstance(){
        return builder;
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> KGram<T>[] buildKGram(T[] values, int kvalue){
        Set<KGram<T>> kgrams = new LinkedHashSet<KGram<T>>();

        if(values.length >= kvalue){
            int max = values.length - (kvalue - 1);
            for(int i = 0; i < max; i++){
                KGram<T> kgram = new KGram<T>(kvalue);
                for(int j = 0; j < kvalue; j++){
                    kgram.set(j, values[i + j]);
                }
                kgrams.add(kgram);
            }
        }
        return kgrams.toArray(new KGram[kgrams.size()]);
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> KGram<T>[] buildKGram(List<T> values, int kvalue){
        Set<KGram<T>> kgrams = new LinkedHashSet<KGram<T>>();

        if(values.size() >= kvalue){
            int max = values.size() - (kvalue - 1);
            for(int i = 0; i < max; i++){
                KGram<T> kgram = new KGram<T>(kvalue);
                for(int j = 0; j < kvalue; j++){
                    kgram.set(j, values.get(i + j));
                }
                kgrams.add(kgram);
            }
        }
        return kgrams.toArray(new KGram[kgrams.size()]);
    }
}