package com.github.stigmata.digger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;

/**
 * This interface is for the class manages the location of an archive,
 * and the set of a class file included in the archive.
 *
 * @author Haruaki TAMADA
 */
public interface ClassFileArchive extends Iterable<ClassFileEntry>{
    String CLASS_FILE_EXTENSION = ".class";

    /**
     * returns the location of this archive.
     */
    URL getLocation();

    /**
     * returns the InputStream object from given entry.
     */
    InputStream getInputStream(ClassFileEntry entry) throws IOException;

    /**
     * returns an entries of this archive.
     */
    Iterator<ClassFileEntry> iterator();

    /**
     * returns this archive has given class entry or not.
     */
    boolean hasEntry(String className);

    /**
     * returns an entry of given class name.
     */
    ClassFileEntry getEntry(String className) throws ClassNotFoundException;

    /**
     * returns the name of this archive.
     */
    String getName();
}
