package com.github.stigmata.birthmarks.cvfv;

import java.util.LinkedHashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;

/**
 * 
 * 
 *
 * @author Haruaki TAMADA
 */
public class ConstantValueOfFieldVariableBirthmarkExtractVisitor extends BirthmarkExtractVisitor{
    private Map<String, TypeAndValueBirthmarkElement> elements = new LinkedHashMap<String, TypeAndValueBirthmarkElement>();
    private String className;

    public ConstantValueOfFieldVariableBirthmarkExtractVisitor(ClassVisitor visitor, Birthmark birthmark, BirthmarkContext context){
        super(visitor, birthmark, context);
    }

    @Override
    public void visitEnd(){
        for(String key: elements.keySet()){
            addElement(elements.get(key));
        }
        super.visitEnd();
    }

    @Override
    public void visit(int version, int access, String name, String signature,
                      String superName, String[] interfaces){
        this.className = name;

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value){

        FieldVisitor visitor = super.visitField(access, name, desc, signature, value);

        TypeAndValueBirthmarkElement e = elements.get(name);
        if(e == null){
            e = new TypeAndValueBirthmarkElement(desc, value);
        }
        else{
            if(value != null){
                e.setValue(value);
            }
        }
        elements.put(name, e);

        return visitor;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature,
                                     String[] exceptions){
        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        if(name.equals("<init>") || name.equals("<clinit>")){
            visitor = new MethodVisitor(Opcodes.ASM4, visitor){
                private Object constant = null;

                @Override
                public void visitIntInsn(int opcode, int operand){
                    if(opcode == Opcodes.BIPUSH || opcode == Opcodes.SIPUSH){
                        constant = new Integer(operand);
                    }
                    super.visitIntInsn(opcode, operand);
                }

                @Override
                public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf){
                    Type type = Type.getReturnType(desc);
                    if(!type.equals(Type.VOID_TYPE)){
                        constant = null;
                    }
                    super.visitMethodInsn(opcode, owner, name, desc, itf);
                }

                @Override
                public void visitInsn(int opcode){
                    if(opcode == Opcodes.ICONST_M1)     constant = new Integer(-1);
                    else if(opcode == Opcodes.ICONST_0) constant = new Integer(0);
                    else if(opcode == Opcodes.ICONST_1) constant = new Integer(1);
                    else if(opcode == Opcodes.ICONST_2) constant = new Integer(2);
                    else if(opcode == Opcodes.ICONST_3) constant = new Integer(3);
                    else if(opcode == Opcodes.ICONST_4) constant = new Integer(4);
                    else if(opcode == Opcodes.ICONST_5) constant = new Integer(5);
                    else if(opcode == Opcodes.LCONST_0) constant = new Long(0L);
                    else if(opcode == Opcodes.LCONST_1) constant = new Long(1L);
                    else if(opcode == Opcodes.DCONST_0) constant = new Double(0D);
                    else if(opcode == Opcodes.DCONST_1) constant = new Double(1D);
                    else if(opcode == Opcodes.FCONST_0) constant = new Float(0F);
                    else if(opcode == Opcodes.FCONST_1) constant = new Float(1F);
                    else if(opcode == Opcodes.FCONST_2) constant = new Float(2F);

                    super.visitInsn(opcode);
                }

                @Override
                public void visitLdcInsn(Object object){
                    constant = object;
                    super.visitLdcInsn(object);
                }

                @Override
                public void visitFieldInsn(int opcode, String owner, String name, String desc){
                    if(className.equals(owner) && opcode == Opcodes.PUTFIELD){
                        TypeAndValueBirthmarkElement e = elements.get(name);
                        if(e == null){
                            e = new TypeAndValueBirthmarkElement(desc, constant);
                        }

                        if(e.getValue() == null && constant != null){
                            if(!checkCast(desc, constant)){
                                constant = null;
                            }
                            e.setValue(constant);
                        }
                    }
                    super.visitFieldInsn(opcode, owner, name, desc);
                }
            };
        }

        return visitor;
    }

    private boolean checkCast(String desc, Object constant){
        if(constant instanceof Integer){
            return desc.equals("Ljava/lang/Integer;") ||
                desc.equals("I") || desc.equals("S") || desc.equals("Z") ||
                desc.equals("C") || desc.equals("B");
        }
        else if(constant instanceof Float){
            return desc.equals("Ljava/lang/Float;") || desc.equals("F");
        }
        else if(constant instanceof Double){
            return desc.equals("Ljava/lang/Double;") || desc.equals("D");
        }
        else if(constant instanceof Long){
            return desc.equals("Ljava/lang/Long;") || desc.equals("J");
        }
        else if(constant instanceof String){
            return desc.equals("Ljava/lang/String;");
        }
        return false;
    }
}
