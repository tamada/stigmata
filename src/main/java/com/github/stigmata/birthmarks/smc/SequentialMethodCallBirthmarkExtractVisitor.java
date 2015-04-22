package com.github.stigmata.birthmarks.smc;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class SequentialMethodCallBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    public SequentialMethodCallBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(visitor, birthmark, context);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                      String signature, String[] exceptions){

        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        return new MethodVisitor(Opcodes.ASM4, visitor){
            @Override
            public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf){
                String className = owner.replace('/', '.');
                if(getEnvironment().getWellknownClassManager().isWellKnownClass(className)){
                    addElement(className, name, desc);
                }
                super.visitMethodInsn(opcode, owner, name, desc, itf);
            }
        };
    }

    protected void addElement(String className, String methodName, String description){
        addElement(new MethodCallBirthmarkElement(className, methodName, description));
    }
}
