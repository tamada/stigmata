package com.github.stigmata.birthmarks.kgram;

import java.util.Iterator;

import org.objectweb.asm.ClassWriter;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.ExtractionUnit;
import com.github.stigmata.birthmarks.ASMBirthmarkExtractor;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;
import com.github.stigmata.spi.BirthmarkService;
import com.github.stigmata.utils.ArrayIterator;

/**
 * @author Haruaki TAMADA
 */
public class KGramBasedBirthmarkExtractor extends ASMBirthmarkExtractor{
    private int kvalue = 4;

    public KGramBasedBirthmarkExtractor(BirthmarkService spi){
        super(spi);
    }

    public KGramBasedBirthmarkExtractor(){
        super();
    }

    public void setKValue(int kvalue){
        this.kvalue = kvalue;
    }

    public int getKValue(){
        return kvalue;
    }

    @Override
    public Iterator<String> getPropertyKeys(){
        return new ArrayIterator<String>(new String[] { "KValue" });
    }

    @Override
    public void setProperty(String key, Object value){
        if(key.equals("KValue")){
            if(value instanceof Number){
                setKValue(((Number)value).intValue());
            }
            else{
                setKValue(Integer.parseInt(String.valueOf(value)));
            }
        }
    }

    @Override
    public Object getProperty(String key){
        if(key.equals("KValue")){
            return getKValue();
        }
        return null;
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, 
            Birthmark birthmark, BirthmarkContext context){
        KGramBasedBirthmarkExtractVisitor extractor = 
            new KGramBasedBirthmarkExtractVisitor(writer, birthmark, context);
        extractor.setKValue(getKValue());
        return extractor;
    }

    @Override
    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] {
            ExtractionUnit.CLASS, ExtractionUnit.PACKAGE,
            ExtractionUnit.ARCHIVE, 
        };
    }


    @Override
    public BirthmarkElement buildElement(String value) {
        value = value.trim();
        String[] param = value.split(" ");
        KGram<Integer> kgram = new KGram<Integer>(param.length);
        for(int i = 0; i < param.length; i++){
            kgram.set(i, new Integer(param[i].trim()));
        }
        return new KGramBasedBirthmarkElement<Integer>(kgram);
    }
}
