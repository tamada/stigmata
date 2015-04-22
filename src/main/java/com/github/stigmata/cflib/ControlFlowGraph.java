package com.github.stigmata.cflib;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TryCatchBlockNode;

import com.github.stigmata.utils.ArrayIterator;

/**
 * Control Flow Graph.
 * 
 * @author tamada
 */
public class ControlFlowGraph implements Iterable<BasicBlock>{
    private BasicBlock[] blocks;
    private boolean includeException;
    private MethodNode method;
    private String name;

    public ControlFlowGraph(String name, BasicBlock[] initBlocks){
        this.name = name;
        blocks = new BasicBlock[initBlocks.length];
        System.arraycopy(initBlocks, 0, blocks, 0, initBlocks.length);
    }

    public ControlFlowGraph(String name, MethodNode node){
        this(name, node, false);
    }

    public ControlFlowGraph(String name, MethodNode node, boolean includeException){
        this.includeException = includeException;
        this.name = name;
        this.method = node;
        parse(method);
    }

    private void buildExceptionFlow(AbstractInsnNode inst, Set<Label> exceptionFlows, TryCatchBlockNode[] tryCatches){
        if(inst.getType() == AbstractInsnNode.LABEL){
            Label label = ((LabelNode)inst).getLabel();

            for(TryCatchBlockNode node: tryCatches){
                if(node.start.getLabel() == label){
                    exceptionFlows.add(node.handler.getLabel());
                }
                else if(node.end.getLabel() == label){
                    exceptionFlows.remove(node.handler.getLabel());
                }
            }
        }
    }

    /**
     * TryCatchブロックの一覧を返します．
     * Try Catchブロックが含まれていない場合や，
     * {@link isIncludingExceptionFlow}がfalseを返す場合は長さ0の配列を返します．
     * @param node
     * @return
     */
    private TryCatchBlockNode[] buildTryCatchBlockNode(MethodNode node){
        TryCatchBlockNode[] nodes = new TryCatchBlockNode[0];
        if(isIncludingExceptionFlow()){
            nodes = new TryCatchBlockNode[node.tryCatchBlocks.size()];
            for(int i = 0; i < nodes.length; i++){
                nodes[i] = (TryCatchBlockNode)node.tryCatchBlocks.get(i);
            }
        }
        return nodes;
    }

    private Set<LabelNode> collectLabels(MethodNode node){
        Set<LabelNode> jumpTarget = new HashSet<LabelNode>();
        int size = node.instructions.size();
        for(int i = 0; i < size; i++){
            AbstractInsnNode inst = node.instructions.get(i);
            switch(inst.getType()){
            case AbstractInsnNode.JUMP_INSN:
            {
                JumpInsnNode jump = (JumpInsnNode)inst;
                jumpTarget.add(jump.label);
                break;
            }
            case AbstractInsnNode.LOOKUPSWITCH_INSN:
            {
                LookupSwitchInsnNode lookup = (LookupSwitchInsnNode)inst;
                jumpTarget.add(lookup.dflt);
                for(Object label: lookup.labels){
                    jumpTarget.add((LabelNode)label);
                }
                break;
            }
            case AbstractInsnNode.TABLESWITCH_INSN:
            {
                TableSwitchInsnNode lookup = (TableSwitchInsnNode)inst;
                jumpTarget.add(lookup.dflt);
                for(Object label: lookup.labels){
                    jumpTarget.add((LabelNode)label);
                }
                break;
            }
            }
        }
        if(isIncludingExceptionFlow()){
            for(Object object: node.tryCatchBlocks){
                jumpTarget.add(((TryCatchBlockNode)object).handler);
            }
        }
        return jumpTarget;
    }

    public Iterator<BasicBlock> iterator(){
        return new ArrayIterator<BasicBlock>(blocks);
    }

    public int getBasicBlockSize(){
        return blocks.length;
    }

    public int[][] getGraphMatrix(){
        int[][] matrix = new int[blocks.length][blocks.length];

        for(int i = 0; i < blocks.length; i++){
            for(int j = 0; j < blocks.length; j++){
                int nextValue = 0;
                for(Iterator<BasicBlock> iter = blocks[i].nextIterator(); iter.hasNext(); ){
                    BasicBlock nextBlock = iter.next();
                    if(nextBlock == blocks[j]){
                        nextValue = 1;
                        break;
                    }
                }
                matrix[i][j] = nextValue;
            }
        }

        return matrix;
    }

    public String getName(){
        return name;
    }

    public boolean isIncludingExceptionFlow(){
        return includeException;
    }

    private BasicBlock[] joinBasicBlocks(BasicBlock[] blocks){
        for(int i = 0; i < blocks.length; i++){
            Label[] labels = blocks[i].getTargets();
            for(int j = 0; j < labels.length; j++){
                for(int k = 0; k < blocks.length; k++){
                    if(i != k && blocks[k].hasLabel(labels[j])){
                        blocks[i].setNext(blocks[k]);
                        break;
                    }
                }
            }
            if((i + 1) < blocks.length && blocks[i].isFlowNext()){
                blocks[i].setNext(blocks[i + 1]);
            }
        }

        return blocks;
    }

    private void parse(MethodNode node){
        Set<LabelNode> jumpTarget = collectLabels(node);
        BasicBlock[] blocks = separateBasicBlock(node, jumpTarget);
        this.blocks = joinBasicBlocks(blocks);
    }

    private BasicBlock[] separateBasicBlock(MethodNode node, Set<LabelNode> jumpTarget){
        int size = node.instructions.size();

        List<BasicBlock> blockList = new ArrayList<BasicBlock>();
        Set<Label> exceptionFlows = new HashSet<Label>();
        TryCatchBlockNode[] tryCatchBlocks = buildTryCatchBlockNode(node);

        BasicBlock block = new BasicBlock();
        for(int i = 0; i < size; i++){
            AbstractInsnNode inst = node.instructions.get(i);
            block.exceptionFlows.addAll(exceptionFlows);

            if(jumpTarget.contains(inst)){
                if(!block.isEmpty()){
                    blockList.add(block);
                    block = new BasicBlock();
                    block.exceptionFlows.addAll(exceptionFlows);
                }
            }
            block.addNode(inst);
            buildExceptionFlow(inst, exceptionFlows, tryCatchBlocks);
            if(inst.getType() == AbstractInsnNode.JUMP_INSN
                    || inst.getType() == AbstractInsnNode.TABLESWITCH_INSN
                    || inst.getType() == AbstractInsnNode.LOOKUPSWITCH_INSN
                    || inst.getOpcode() == Opcodes.RETURN
                    || inst.getOpcode() == Opcodes.ATHROW
                    || inst.getOpcode() == Opcodes.RET){
                if(!block.isEmpty()){
                    blockList.add(block);
                    BasicBlock block2 = new BasicBlock();
                    if(inst.getOpcode() != Opcodes.GOTO && inst.getOpcode() != Opcodes.JSR){
                        block2.setPrev(block);
                    }
                    block = block2;
                    block.exceptionFlows.addAll(exceptionFlows);
                }
            }
        }
        if(!block.isEmpty()){
            blockList.add(block);
        }
        return blockList.toArray(new BasicBlock[blockList.size()]);
    }

    public void setIncludingExceptionFlow(boolean includeException){
        boolean oldvalue = this.includeException;
        this.includeException = includeException;
        if(oldvalue != includeException){
            parse(method);
        }
    }
}
