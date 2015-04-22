package com.github.stigmata.cflib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;

/**
 * 基本ブロックを表すクラス．
 * 
 * @author tamada
 */
public class BasicBlock {
    private Set<BasicBlock> nexts = new HashSet<BasicBlock>();
    private List<Opcode> opcodes = new ArrayList<Opcode>();
    private Set<BasicBlock> prevs = new HashSet<BasicBlock>();
    Set<Label> exceptionFlows = new HashSet<Label>();

    BasicBlock(){
    }

    public BasicBlock(InsnNode[] nodeArray){
        for(InsnNode node: nodeArray){
            addNode(node);
        }
    }

    void addNode(AbstractInsnNode node){
        OpcodeManager manager = OpcodeManager.getInstance();
        Opcode opcode = manager.getOpcode(node);
        if(opcode != null){
            addNode(opcode);
        }
    }

    void addNode(Opcode opcode){
        opcodes.add(opcode);
    }

    public Opcode getOpcode(int index){
        return opcodes.get(index);
    }

    public int getSize(){
        return opcodes.size();
    }

    public Label[] getTargets(){
        Set<Label> targets = new HashSet<Label>();
        for(Opcode opcode: opcodes){
            if(opcode.getCategory() != Opcode.Category.TARGETER){
                Label[] labels = opcode.getLabels();
                for(Label label: labels){
                    targets.add(label);
                }
            }
        }
        for(Label label: exceptionFlows){
            targets.add(label);
        }

        return targets.toArray(new Label[targets.size()]);
    }

    public boolean hasLabel(Label label){
        boolean flag = false;
        for(Opcode opcode: opcodes){
            if(flag || opcode.hasLabel(label)){
                flag = true;
            }
        }
        return flag;
    }

    public boolean isEmpty(){
        return opcodes.size() == 0;
    }

    public Iterator<BasicBlock> nextIterator(){
        return Collections.unmodifiableSet(nexts).iterator();
    }

    public Iterator<BasicBlock> previousIterator(){
        return Collections.unmodifiableSet(prevs).iterator();
    }

    public void setNext(BasicBlock block){
        block.prevs.add(this);
        nexts.add(block);
    }

    public void setPrev(BasicBlock block){
        block.nexts.add(this);
        prevs.add(block);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("---- block ----");
        String ln = System.getProperty("line.separator");
        for(Opcode opcode: opcodes){
            sb.append(ln).append(opcode);
        }
        sb.append(ln).append("Targeter: ");
        for(Label label: getTargets()){
            sb.append(label).append(", ");
        }
        return new String(sb);
    }

    public boolean isFlowNext(){
        Opcode opcode = getOpcode(getSize() - 1);
        int op = opcode.getOpcode();

        return op != Opcodes.GOTO && op != Opcodes.RETURN
        && op != Opcodes.RET && op != Opcodes.ATHROW;
    }
}
