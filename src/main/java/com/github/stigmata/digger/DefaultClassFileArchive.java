package com.github.stigmata.digger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class DefaultClassFileArchive implements ClassFileArchive{
    private File file;
    private String className;
    
    public DefaultClassFileArchive(String file){
        this(new File(file));
    }
    
    public DefaultClassFileArchive(File file){
        this.file = file;
        try{
            parseClassName();
        } catch(ParseClassNameFailedException e){
            className = null;
        }
    }
    
    public DefaultClassFileArchive(String file, String className){
        this(new File(file), className);
    }
    
    public DefaultClassFileArchive(File file, String className){
        this.file = file;
        this.className = className;
    }

    public URL getLocation(){
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public InputStream getInputStream(ClassFileEntry entry) throws IOException{
        return new FileInputStream(file);
    }

    public Iterator<ClassFileEntry> iterator(){
        List<ClassFileEntry> list = new ArrayList<ClassFileEntry>();
        list.add(new ClassFileEntry(className, getLocation()));

        return list.iterator();
    }

    public boolean hasEntry(String className){
        return this.className.equals(className);
    }

    public ClassFileEntry getEntry(String className) throws ClassNotFoundException{
        return new ClassFileEntry(className, getLocation());
    }

    public String getName(){
        return className;
    }

    private void parseClassName() throws ParseClassNameFailedException{
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            ClassReader reader = new ClassReader(in);
            ClassNameExtractVisitor visitor = new ClassNameExtractVisitor();
            reader.accept(visitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            this.className = visitor.getClassName();
        } catch (FileNotFoundException ex) {
            throw new ParseClassNameFailedException(ex);
        } catch (IOException ex) {
            throw new ParseClassNameFailedException(ex);
        } finally{
            if(in != null){
                try{
                    in.close();
                } catch(IOException e){
                    throw new ParseClassNameFailedException(e);
                }
            }
        }
    }

    private static class ClassNameExtractVisitor extends ClassVisitor{
        private String className;

        public ClassNameExtractVisitor(){
            super(Opcodes.ASM4);
        }

        public String getClassName(){
            return className;
        }

        @Override
        public void visit(int version, int access, String name, String signature, 
                String superClassName, String[] interfaces){
            className = name;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String arg0, boolean arg1){
            return null;
        }

        @Override
        public void visitAttribute(Attribute arg0){
        }

        @Override
        public void visitEnd(){
        }

        @Override
        public FieldVisitor visitField(int arg0, String arg1, String arg2,
                String arg3, Object arg4){
            return null;
        }

        @Override
        public void visitInnerClass(String arg0, String arg1, String arg2,
                int arg3){
        }

        @Override
        public MethodVisitor visitMethod(int arg0, String arg1, String arg2,
                String arg3, String[] arg4){
            return null;
        }

        @Override
        public void visitOuterClass(String arg0, String arg1, String arg2){
        }

        @Override
        public void visitSource(String arg0, String arg1){

        }
    }
}
