package com.github.stigmata.digger;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class WarClassFileArchive extends JarClassFileArchive{
    private static final String WAR_FILE_CLASSPATH = "WEB-INF/classes/";

    public WarClassFileArchive(String jarfile) throws IOException{
        super(jarfile);
    }

    public InputStream getInputStream(ClassFileEntry entry) throws IOException{
        if(hasEntry(entry.getClassName())){
            return entry.getLocation().openStream();
        }
        return null;
    }

    public Iterator<ClassFileEntry> iterator(){
        List<ClassFileEntry> list = new ArrayList<ClassFileEntry>();

        for(Enumeration<JarEntry> e = jarEntries(); e.hasMoreElements(); ){
            JarEntry entry = e.nextElement();
            if(entry.getName().endsWith(CLASS_FILE_EXTENSION)){
                URL location = null;
                try {
                    location = new URL("jar:" + getLocation() + "!/" + entry.getName());
                    String className = entry.getName();
                    className = className.substring(
                        WAR_FILE_CLASSPATH.length(), className.length() - CLASS_FILE_EXTENSION.length()
                    );
                    className = className.replace('/', '.');

                    list.add(new ClassFileEntry(className, location));
                } catch(MalformedURLException ex){
                    throw new IllegalStateException(ex);
                }
            }
        }
        return list.iterator();
    }

    public boolean hasEntry(String className){
        return hasJarEntry(
            WAR_FILE_CLASSPATH + className.replace('.', '/') + CLASS_FILE_EXTENSION
        );
    }

    public ClassFileEntry getEntry(String className) throws ClassNotFoundException{
        if(hasEntry(className)){
            String entryName = className.replace('.', '/') + CLASS_FILE_EXTENSION;
            try{
                URL location = new URL("jar:" + getLocation() + "!/" + WAR_FILE_CLASSPATH + entryName);

                return new ClassFileEntry(className, location);
            } catch(MalformedURLException e){
                throw new IllegalStateException(e);
            }
        }
        return null;
    }
}
