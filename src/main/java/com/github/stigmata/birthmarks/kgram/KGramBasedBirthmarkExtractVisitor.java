package com.github.stigmata.birthmarks.kgram;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;

/**
 * 
 * @author Haruaki TAMADA
 */
public class KGramBasedBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    private int kvalue;
    private List<Integer> opcodes = new ArrayList<Integer>();

    public KGramBasedBirthmarkExtractVisitor(ClassVisitor visitor,
            Birthmark birthmark, BirthmarkContext context){
        super(visitor, birthmark, context);
    }

    public int getKValue(){
        return kvalue;
    }

    public void setKValue(int kvalue){
        this.kvalue = kvalue;
    }

    @Override
    public void visitEnd(){
        KGramBuilder builder = KGramBuilder.getInstance();
        KGram<Integer>[] kgrams = builder.<Integer>buildKGram(
            opcodes, getKValue()
        );

        for(KGram<Integer> kgram: kgrams){
            addElement(new KGramBasedBirthmarkElement<Integer>(kgram));
        }
    }

    @Override
    public MethodVisitor visitMethod(int arg0, String arg1, String arg2,
				     String arg3, String[] arg4){
        MethodVisitor visitor = super.visitMethod(
            arg0, arg1, arg2, arg3, arg4
        );
        MethodVisitor opcodeVisitor =
            new OpcodeExtractionMethodVisitor(visitor, opcodes);

        return opcodeVisitor;
    }
}
