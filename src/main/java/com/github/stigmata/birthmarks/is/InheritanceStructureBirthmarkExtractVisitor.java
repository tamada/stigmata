package com.github.stigmata.birthmarks.is;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;
import com.github.stigmata.birthmarks.NullBirthmarkElement;
import com.github.stigmata.digger.ClassFileEntry;
import com.github.stigmata.digger.ClasspathContext;
import com.github.stigmata.utils.WellknownClassManager;


/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class InheritanceStructureBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    public InheritanceStructureBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(visitor, birthmark, context);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces){
        if((access & Opcodes.ACC_INTERFACE) != Opcodes.ACC_INTERFACE){
            ClasspathContext context = getEnvironment().getClasspathContext();
            name = name.replace('/', '.');
            ClassFileEntry entry = context.findEntry(name);
            if(entry == null){
                superName = superName.replace('/', '.');
                ClassFileEntry parent = context.findEntry(superName);
                if(parent != null){
                    addIsBirthmark(name);
                    addIsBirthmark(superName);
                }
                else{
                    addFailur(new ClassNotFoundException(superName));
                }
            }
            else{
                try{
                    Class<?> clazz = context.findClass(name);
                    addISBirthmark(clazz);
                } catch(ClassNotFoundException e){
                    addFailur(e);
                }
            }
        }
    }

    private void addIsBirthmark(String className){
        WellknownClassManager wcm = getEnvironment().getWellknownClassManager();
        BirthmarkElement element;
        if(wcm.isWellKnownClass(className)){
            element = new BirthmarkElement(className);
        }
        else{
            element = NullBirthmarkElement.getInstance();
        }
        addElement(element);
    }

    private void addISBirthmark(Class<?> c){
        WellknownClassManager wcm = getEnvironment().getWellknownClassManager();
        do{
            String className = c.getName();
            BirthmarkElement element = null;
            if(wcm.isWellKnownClass(className)){
                element = new BirthmarkElement(className);
            }
            else{
                element = NullBirthmarkElement.getInstance();
            }

            addElement(element);
            c = c.getSuperclass();
        } while(!c.getName().equals("java.lang.Object"));
        addElement(new BirthmarkElement("java.lang.Object"));
    }
}
