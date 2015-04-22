package com.github.stigmata.birthmarks.fmc;

import org.objectweb.asm.ClassWriter;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.ExtractionUnit;
import com.github.stigmata.birthmarks.ASMBirthmarkExtractor;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;
import com.github.stigmata.birthmarks.FrequencyBirthmark;
import com.github.stigmata.birthmarks.FrequencyBirthmarkElement;
import com.github.stigmata.birthmarks.smc.SequentialMethodCallBirthmarkExtractVisitor;
import com.github.stigmata.spi.BirthmarkService;

/**
 * @author Haruaki TAMADA
 */
public class FrequencyMethodCallBirthmarkExtractor extends ASMBirthmarkExtractor{
    public FrequencyMethodCallBirthmarkExtractor(BirthmarkService spi){
        super(spi);
    }

    public FrequencyMethodCallBirthmarkExtractor(){
        super();
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context){
        return new SequentialMethodCallBirthmarkExtractVisitor(writer, birthmark, context){
            @Override
            protected void addElement(String className, String methodName, String description){
                addElement(new FrequencyBirthmarkElement(className + "#" + methodName + description));
            }
        };
    }

    @Override
    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] {
            ExtractionUnit.CLASS, ExtractionUnit.PACKAGE, ExtractionUnit.ARCHIVE, 
        };
    }

    @Override
    public Birthmark createBirthmark(){
        return new FrequencyBirthmark(getProvider().getType());
    }


    @Override
    public BirthmarkElement buildElement(String value) {
        return new FrequencyBirthmarkElement(value);
    }
}
