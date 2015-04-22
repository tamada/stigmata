package com.github.stigmata.birthmarks.uc;

import org.objectweb.asm.ClassWriter;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.ExtractionUnit;
import com.github.stigmata.birthmarks.ASMBirthmarkExtractor;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;
import com.github.stigmata.spi.BirthmarkService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class UsedClassesBirthmarkExtractor extends ASMBirthmarkExtractor{
    public UsedClassesBirthmarkExtractor(BirthmarkService spi){
        super(spi);
    }

    public UsedClassesBirthmarkExtractor(){
        super();
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context){
        return new UsedClassesBirthmarkExtractVisitor(writer, birthmark, context);
    }

    @Override
    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] {
            ExtractionUnit.CLASS, ExtractionUnit.PACKAGE, ExtractionUnit.ARCHIVE, 
        };
    }

    @Override
    public Birthmark createBirthmark(){
        return new UsedClassesBirthmark(getProvider().getType());
    }

    @Override
    public BirthmarkElement buildElement(String value){
        return new BirthmarkElement(value);
    }
}
