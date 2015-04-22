package com.github.stigmata.utils;

import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Haruaki TAMADA
 */
public interface AsciiDataWritable{
    public void writeAsciiData(PrintWriter out, String format) throws IOException, UnsupportedFormatException;
}
