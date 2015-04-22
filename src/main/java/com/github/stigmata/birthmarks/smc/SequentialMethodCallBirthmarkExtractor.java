package com.github.stigmata.birthmarks.smc;

import org.objectweb.asm.ClassWriter;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.ExtractionUnit;
import com.github.stigmata.birthmarks.ASMBirthmarkExtractor;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;
import com.github.stigmata.spi.BirthmarkService;

/**
 * @author Haruaki TAMADA
 */
public class SequentialMethodCallBirthmarkExtractor extends ASMBirthmarkExtractor{
    public SequentialMethodCallBirthmarkExtractor(BirthmarkService spi){
        super(spi);
    }

    public SequentialMethodCallBirthmarkExtractor(){
        super();
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context){
        return new SequentialMethodCallBirthmarkExtractVisitor(writer, birthmark, context);
    }

    @Override
    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] { ExtractionUnit.CLASS, ExtractionUnit.ARCHIVE, ExtractionUnit.PACKAGE, };
    }

    @Override
    public BirthmarkElement buildElement(String value) {
        String className = value.substring(0, value.indexOf('#'));
        String methodName = value.substring(value.indexOf('#') + 1, value.lastIndexOf('!'));
        String signature = value.substring(value.lastIndexOf('!') + 1);

        return new MethodCallBirthmarkElement(className, methodName, signature);
    }
}
