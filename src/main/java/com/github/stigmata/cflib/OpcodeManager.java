package com.github.stigmata.cflib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.sourceforge.talisman.csvio.CsvLine;
import jp.sourceforge.talisman.csvio.CsvParser;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

/** 
 * 
 * @author Haruaki Tamada
 */
public class OpcodeManager{
    private Map<Integer, Opcode> opcodeMap = new HashMap<Integer, Opcode>();
    private static OpcodeManager manager = new OpcodeManager();

    /**
     * private constructor for singleton pattern.
     */
    private OpcodeManager(){
        try{
            URL location = OpcodeManager.class.getResource("/META-INF/bytecode.def");
            BufferedReader in = new BufferedReader(new InputStreamReader(location.openStream()));
            CsvParser parser = new CsvParser(in);
            while(parser.hasNext()){
                CsvLine line = parser.next();
                String[] values = line.getValues();
                if(values.length == 5){
                    Opcode def = new Opcode(
                        Integer.parseInt(values[0]), values[1],
                        Integer.parseInt(values[2]),
                        Integer.parseInt(values[3]), values[4]
                    );
                    opcodeMap.put(def.getOpcode(), def);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            throw new InternalError(e.getMessage());
        }
    }

    public static OpcodeManager getInstance(){
        return manager;
    }

    public Opcode getOpcode(int opcode){
        return opcodeMap.get(opcode);
    }

    private Map<LabelNode, LabelOpcode> labelMap = new HashMap<LabelNode, LabelOpcode>();

    public Opcode getOpcode(AbstractInsnNode node){
        Opcode opcode = getOpcode(node.getOpcode());
        if(opcode == null && node instanceof LabelNode){
            opcode = labelMap.get(node);
            if(opcode == null){
                opcode = new LabelOpcode(((LabelNode)node).getLabel());
                labelMap.put((LabelNode)node, (LabelOpcode)opcode);
            }
        }
        if(opcode != null){
            switch(opcode.getCategory()){
            case BRANCH:
                opcode = constructBranchOpcode(opcode, node);
                break;
            case INVOKE:
                opcode = constructMethodOpcode(opcode, node);
                break;
            case FIELD:
                opcode = constructObjectOpcode(opcode, node);
                break;
             // Not needs conversion.
            case CONSTANT: case ARRAY:  case ADD:      case LOAD:
            case SUBTRACT: case DIVIDE: case MULTIPLY: case SHIFT_RIGHT:
            case AND:      case OR:     case XOR:      case SHIFT_LEFT:
            case NEGATE:   case REMAIN: case STORE:    case USHIFT_RIGHT:
            case NEW:      case CAST:   case COMPARE:  case RETURN:
            case OTHERS:   case THROW:  case STACK:  
            case TARGETER: // Already convert above.
            default:
                break;
            }
        }

        return opcode;
    }

    private Opcode constructMethodOpcode(Opcode o, AbstractInsnNode node){
        Opcode methodOpcode = new Opcode(o);
        MethodInsnNode methodNode = (MethodInsnNode)node;
        Type[] types = Type.getArgumentTypes(methodNode.desc);
        int argumentSize = 0;
        for(Type type: types){
            argumentSize += type.getSize();
        }
        int size = Type.getReturnType(methodNode.desc).getSize();
        switch(o.getOpcode()){
        case Opcodes.INVOKESTATIC:
            size = size - argumentSize;
            break;
        case Opcodes.INVOKEINTERFACE:
        case Opcodes.INVOKESPECIAL:
        case Opcodes.INVOKEVIRTUAL:
            size = size - argumentSize - 1;
            break;
        }
        methodOpcode.setAct(size);

        return methodOpcode;
    }

    private Opcode constructObjectOpcode(Opcode o, AbstractInsnNode node){
        Opcode opcode = new Opcode(o);
        FieldInsnNode field = (FieldInsnNode)node;
        
        int size = Type.getType(field.desc).getSize();
        switch(opcode.getOpcode()){
        case Opcodes.PUTFIELD:
            size = -1 - size;
            break;
        case Opcodes.PUTSTATIC:
            size = 0 - size;
            break;
        case Opcodes.GETFIELD:
            size = -1 + size;
            break;
        case Opcodes.GETSTATIC:
            size = 0 + size;
            break;
        }
        opcode.setAct(size);
        return opcode;
    }

    private Opcode constructBranchOpcode(Opcode defaultOpcode, AbstractInsnNode node){
        Opcode opcode = new Opcode(defaultOpcode);
        if(node instanceof TableSwitchInsnNode){
            TableSwitchInsnNode table = (TableSwitchInsnNode)node;
            List<Label> labels = new ArrayList<Label>();
            labels.add(table.dflt.getLabel());
            for(Object o: table.labels){
                labels.add(((LabelNode)o).getLabel());
            }
        }
        else if(node instanceof LookupSwitchInsnNode){
            LookupSwitchInsnNode lookup = (LookupSwitchInsnNode)node;
            List<Label> labels = new ArrayList<Label>();
            labels.add(lookup.dflt.getLabel());
            for(Object o: lookup.labels){
                labels.add(((LabelNode)o).getLabel());
            }
        }
        else{
            opcode.addLabel(((JumpInsnNode)node).label.getLabel());
        }
        return opcode;
    }
}
