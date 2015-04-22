package com.github.stigmata.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Utility{
    /**
     * no instance is created
     */
    private Utility(){
    }

    public static void deleteDirectory(File dir){
        File[] files = dir.listFiles();
        for(File file: files){
            if(file.isDirectory()){
                deleteDirectory(file);
            }
            else{
                file.delete();
            }
        }
        dir.delete();
    }

    public static String array2String(String[] values){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < values.length; i++){
            if(i != 0)
                builder.append(", ");
            builder.append(values[i]);
        }
        return new String(builder);
    }

    public static boolean isStigmataPluginJarFile(File pluginFile){
        return isStigmataPluginJarFile(pluginFile, new ArrayList<String>());
    }

    public static boolean isStigmataPluginJarFile(File pluginFile, List<String> messages){
        boolean flag = true;
        if(pluginFile == null){
            flag = false;
        }
        if(!pluginFile.getName().endsWith(".jar")){
            messages.add("install.error.notjarfile");
            flag = false;
        }
        if(!pluginFile.exists()){
            messages.add("install.error.file.missing");
            flag = false;
        }

        // check service descriptor.
        if(flag){
            try{
                JarFile jarfile = new JarFile(pluginFile);
                JarEntry entry = jarfile.getJarEntry("META-INF/services/jp.sourceforge.stigmata.spi.BirthmarkService");
                if(entry == null){
                    messages.add("install.error.servicedescriptor.missing");
                    flag = false;
                }
                jarfile.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        return flag;
    }
}
