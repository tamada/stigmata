package com.github.stigmata.utils;

import java.io.Serializable;

/**
 * This class represents a fully class name.
 * 
 * @author Haruaki Tamada
 */
class FullyClassName implements Serializable{
    private static final long serialVersionUID = 1911603124143509407L;
    private final String fullyName;
    private String className;
    private String packageName;

    /**
     * basic constructor.
     * @param fullyName fully class name
     */
    public FullyClassName(final String fullyName){
        this.fullyName = fullyName.replace('/', '.');
    }

    /**
     * returns fully class name.
     */
    public String getFullyName(){
        return fullyName;
    }

    /**
     * returns a class name exclude package name.
     */
    public String getClassName(){
        if(className == null){
            generateClassAndPackageName();
        }
        return className;
    }

    /**
     * returns a package name. (exclude class name from fully class name)
     */
    public String getPackageName(){
        if(packageName == null){
            generateClassAndPackageName();
        }
        return packageName;
    }

    private void generateClassAndPackageName(){
        final int index = fullyName.lastIndexOf('.');
        this.className = fullyName.substring(index + 1);
        this.packageName = "";
        if(index > 0){
            packageName = fullyName.substring(0, index - 1);
        }
    }
}
