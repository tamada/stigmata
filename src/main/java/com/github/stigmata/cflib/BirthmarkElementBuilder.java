package com.github.stigmata.cflib;

import java.util.List;

import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;

/**
 *
 *
 * @author tamada
 * @version $Revision$
 */
public interface BirthmarkElementBuilder {
    public BirthmarkElement[] buildElements(List<Opcode> opcodes, BirthmarkContext context);
}
