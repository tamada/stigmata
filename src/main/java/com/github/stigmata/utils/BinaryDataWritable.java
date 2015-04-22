package com.github.stigmata.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author Haruaki Tamada
 */
public interface BinaryDataWritable{
    public void writeBinaryData(OutputStream out, String format) throws IOException, UnsupportedFormatException;
}
