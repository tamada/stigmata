package com.github.stigmata.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * File filter by file extension.
 * 
 * @author Haruaki TAMADA
 */
public class ExtensionFilter extends FileFilter implements java.io.FileFilter{
    private List<String> extensions = new ArrayList<String>();

    private String description = null;

    public ExtensionFilter(){
    }

    public ExtensionFilter(String ext){
        this(new String[] { ext }, null);
    }

    public ExtensionFilter(String ext, String description){
        this(new String[] { ext }, description);
    }

    public ExtensionFilter(String[] exts){
        this(exts, null);
    }

    public ExtensionFilter(String[] exts, String description){
        if(exts != null){
            for(String ext: exts){
                addExtension(ext);
            }
        }
        setDescription(description);
    }

    public void addExtension(String ext){
        extensions.add(ext);
    }

    public boolean hasExtension(){
        return extensions.size() > 0;
    }

    public synchronized String[] getExtensions(){
        return extensions.toArray(new String[extensions.size()]);
    }

    @Override
    public boolean accept(File f){
        String fileName = f.getName();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);

        boolean flag = false;
        for(String ext: extensions){
            if(ext.equals(extension)){
                flag = true;
                break;
            }
        }
        if(extensions.size() == 0){
            flag = true;
        }

        return flag || f.isDirectory();
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public String getDescription(){
        return description;
    }
}
