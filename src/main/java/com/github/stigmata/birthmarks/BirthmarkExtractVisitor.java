package com.github.stigmata.birthmarks;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.BirthmarkEnvironment;

/**
 * Abstract visitor class of extracting birthmarks from class file.
 *
 * @author Haruaki TAMADA
 */
public abstract class BirthmarkExtractVisitor extends ClassVisitor{
    private Birthmark birthmark;
    private BirthmarkContext context;
    private List<Throwable> causes = new ArrayList<Throwable>();

    public BirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(Opcodes.ASM4, visitor);
        this.birthmark = birthmark;
        this.context = context;
    }

    protected BirthmarkEnvironment getEnvironment(){
        return context.getEnvironment();
    }

    protected BirthmarkContext getContext(){
        return context;
    }

    protected void addElement(BirthmarkElement element){
        birthmark.addElement(element);
    }

    public Birthmark getBirthmark(){
        return birthmark;
    }

    public synchronized void addFailur(Throwable e){
        causes.add(e);
    }

    public boolean isSuccess(){
       return causes.size() == 0;
    }

    public synchronized Throwable[] getCauses(){
       return causes.toArray(new Throwable[causes.size()]);
    }
}
