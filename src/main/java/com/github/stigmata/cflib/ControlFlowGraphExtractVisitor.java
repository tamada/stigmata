package com.github.stigmata.cflib;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;

/**
 *
 *
 * @author tamada
 */
public class ControlFlowGraphExtractVisitor extends BirthmarkExtractVisitor{
    private Map<String, MethodNode> opcodesMap = new LinkedHashMap<String, MethodNode>();
    private boolean includeExceptionFlows = false;

    public ControlFlowGraphExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(visitor, birthmark, context);
    }

    public ControlFlowGraphExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context, boolean includeExceptionFlows){
        super(visitor, birthmark, context);
        this.includeExceptionFlows = includeExceptionFlows;
    }

    /**
     * Constructor for JUnit.
     * @param visitor
     */
    ControlFlowGraphExtractVisitor(ClassVisitor visitor){
        super(visitor, null, null);
    }

    public Iterator<String> getMethodNames(){
        return opcodesMap.keySet().iterator();
    }

    public ControlFlowGraph getGraph(String name){
        return buildControlFlow(name, opcodesMap.get(name));
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions){
        MethodNode node = new MethodNode(access, name, descriptor, signature, exceptions);

        opcodesMap.put(name + descriptor, node);

        return node;
    }

    private ControlFlowGraph buildControlFlow(String methodName, MethodNode node){
        return new ControlFlowGraph(methodName, node, includeExceptionFlows);
    }
}
