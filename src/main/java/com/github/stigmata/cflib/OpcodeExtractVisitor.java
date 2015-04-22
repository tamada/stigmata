package com.github.stigmata.cflib;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;

/**
 *
 *
 * @author tamada
 */
public class OpcodeExtractVisitor extends BirthmarkExtractVisitor{
    private List<Opcode> opcodeList = new ArrayList<Opcode>();
    private BirthmarkElementBuilder builder;

    public OpcodeExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context, BirthmarkElementBuilder builder){
        super(visitor, birthmark, context);
        this.builder = builder;
    }

    @Override
    public void visitEnd(){
        BirthmarkElement[] elements = builder.buildElements(opcodeList, getContext());
        for(BirthmarkElement element: elements){
            addElement(element);
        }
    }

    @Override
    public MethodVisitor visitMethod(int arg0, String arg1, String arg2, String arg3, String[] arg4){
        MethodVisitor visitor = super.visitMethod(arg0, arg1, arg2, arg3, arg4);
        OpcodeExtractMethodVisitor opcodeVisitor = new OpcodeExtractMethodVisitor(visitor, opcodeList);

        return opcodeVisitor;
    }
}
