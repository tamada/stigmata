package com.github.stigmata.birthmarks.is;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.BirthmarkElementClassNotFoundException;
import com.github.stigmata.BirthmarkExtractionFailedException;
import com.github.stigmata.ExtractionUnit;
import com.github.stigmata.birthmarks.AbstractBirthmarkExtractor;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;
import com.github.stigmata.spi.BirthmarkService;


/**
 * 
 * @author Haruaki TAMADA
 */
public class InheritanceStructureBirthmarkExtractor extends AbstractBirthmarkExtractor{
    public InheritanceStructureBirthmarkExtractor(BirthmarkService spi){
        super(spi);
    }

    @SuppressWarnings("deprecation")
    public InheritanceStructureBirthmarkExtractor(){
        super();
    }

    @Override
    public Birthmark extract(Birthmark birthmark, InputStream in, BirthmarkContext context) throws BirthmarkExtractionFailedException{
        BirthmarkElementClassNotFoundException e = new BirthmarkElementClassNotFoundException();

        try{
            ClassReader reader = new ClassReader(in);
            ClassWriter writer = new ClassWriter(0);
            BirthmarkExtractVisitor visitor = new InheritanceStructureBirthmarkExtractVisitor(writer, birthmark, context);
            reader.accept(visitor, 0);

            if(!visitor.isSuccess()){
                for(Throwable t: visitor.getCauses()){
                    if(t instanceof ClassNotFoundException){
                        e.addClassName(t.getMessage());
                    }
                    else{
                        e.addCause(t);
                    }
                }
            }
        } catch(IOException ee){
            e.addCause(ee);
        } finally{
            if(e.isFailed()){
                System.out.printf("fail: %s%n", e.getMessage());
                throw e;
            }
        }
        return birthmark;
     }

    @Override
    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] { ExtractionUnit.CLASS, };
    }

    @Override
    public BirthmarkElement buildElement(String value){
        return new BirthmarkElement(value);
    }
}
