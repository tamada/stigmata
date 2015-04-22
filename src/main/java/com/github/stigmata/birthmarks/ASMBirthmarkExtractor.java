package com.github.stigmata.birthmarks;

import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkExtractionFailedException;
import com.github.stigmata.spi.BirthmarkService;

/**
 * Abstract birthmark extractor using ASM.
 *
 * @author Haruaki TAMADA
 */
public abstract class ASMBirthmarkExtractor extends AbstractBirthmarkExtractor{
    public ASMBirthmarkExtractor(BirthmarkService spi){
        super(spi);
    }

    @SuppressWarnings("deprecation")
    public ASMBirthmarkExtractor(){
        super();
    }

    public abstract BirthmarkExtractVisitor
        createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context);

    @Override
    public Birthmark extract(Birthmark birthmark, InputStream in,
            BirthmarkContext context) throws BirthmarkExtractionFailedException{
        BirthmarkExtractionFailedException bee = new BirthmarkExtractionFailedException();

        try{
            ClassReader reader = new ClassReader(in);
            ClassWriter writer = new ClassWriter(0);
            BirthmarkExtractVisitor visitor = createExtractVisitor(writer, birthmark, context);
            reader.accept(visitor, 0);

            if(!visitor.isSuccess()){
                bee.addCauses(visitor.getCauses());
            }

            return visitor.getBirthmark();
        } catch(IOException e){
            bee.addCause(e);
            throw bee;
        }
    }
}
