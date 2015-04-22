package com.github.stigmata.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.stigmata.utils.WellknownClassJudgeRule.MatchPartType;
import com.github.stigmata.utils.WellknownClassJudgeRule.MatchType;

/**
 * Managing wellknown class checking rule.
 * 
 * @author Haruaki TAMADA
 */
public class WellknownClassManager implements Iterable<WellknownClassJudgeRule>{
    private List<WellknownClassJudgeRule> rules = new ArrayList<WellknownClassJudgeRule>();

    /**
     * public field/method flag defined in JVM specification.
     */
    private static final int OPCODE_ACC_PUBLIC = 1;
    /**
     * private field/method flag defined in JVM specification.
     */
    @SuppressWarnings("unused")
    private static final int OPCODE_ACC_PRIVATE = 2;
    /**
     * protected field/method flag defined in JVM specification.
     */
    @SuppressWarnings("unused")
    private static final int OPCODE_ACC_PROTECTED = 4;
    /**
     * static field/method flag defined in JVM specification.
     */
    private static final int OPCODE_ACC_STATIC = 8;
    /**
     * final field/method flag defined in JVM specification.
     */
    private static final int OPCODE_ACC_FINAL = 16;

    /**
     * default constructor.
     */
    public WellknownClassManager(){
    }

    /**
     * constructor with parent WellknownClassManager.
     */
    public WellknownClassManager(WellknownClassManager manager){
        rules = new ArrayList<WellknownClassJudgeRule>(manager.rules);
    }

    public void remove(WellknownClassJudgeRule rule){
        rules.remove(rule);
    }

    public void remove(String value, MatchType matchType, MatchPartType partType){
        remove(new WellknownClassJudgeRule(value, matchType, partType));
    }

    public void clear(){
        rules.clear();
    }

    @Override
    public synchronized Iterator<WellknownClassJudgeRule> iterator(){
        List<WellknownClassJudgeRule> copiedRules = new ArrayList<WellknownClassJudgeRule>(rules);
        return copiedRules.iterator();
    }

    public synchronized WellknownClassJudgeRule[] getRules(){
        return rules.toArray(new WellknownClassJudgeRule[rules.size()]);
    }

    public void add(WellknownClassJudgeRule rule){
        if(!rules.contains(rule)){
            rules.add(rule);
        }
    }

    private boolean checkSystemClass(String className){
        FullyClassName name = new FullyClassName(className);
        if(isMatch(name, true)){
            return false;
        }
        return isMatch(name, false);
    }

    private boolean isMatch(FullyClassName name, boolean excludeFlag){
        for(Iterator<WellknownClassJudgeRule> i = rules.iterator(); i.hasNext(); ){
            WellknownClassJudgeRule s = i.next();
            if(s.isExclude() == excludeFlag){
                boolean flag = false;
                String partName = name.getFullyName();
                if(s.getMatchPartType() == MatchPartType.CLASS_NAME){
                    partName = name.getClassName();
                }
                else if(s.getMatchPartType() == MatchPartType.PACKAGE_NAME){
                    partName = name.getPackageName();
                }
                switch(s.getMatchType()){
                case PREFIX:
                    flag = partName.startsWith(s.getPattern());
                    break;
                case SUFFIX:
                    flag = partName.endsWith(s.getPattern());
                    break;
                case EXACT:
                    flag = partName.equals(s.getPattern());
                    break;
                case NOT_MATCH:
                    flag = !partName.equals(s.getPattern());
                    break;
                }
                if(flag){
                    return flag;
                }
            }
        }
        return false;
    }

    /**
     * check system defined methods, which are following methods.
     * <ul>
     *   <li><code>public static void main(String[])</code></li>
     *   <li><code>static void &lt;clinit&gt;(void)</code>(static initializer)</li>
     *   <li><code>void &lt;init&gt;</code>(constructor)</li>
     * </ul>
     */
    private boolean checkSystemMethod(int access, String methodName, String signature){
        if(methodName.equals("main")){
            return signature.equals("([Ljava/lang/String;)V")
                && checkAccess(access, OPCODE_ACC_PUBLIC);
        }
        else if(methodName.equals("<clinit>")){
            return signature.equals("()V")
                && checkAccess(access, OPCODE_ACC_STATIC);
        }
        else if(methodName.equals("<init>")){
            return !checkAccess(access, OPCODE_ACC_STATIC);
        }
        return false;
    }

    /**
     * check system defined field, which is following field.
     * <ul>
     *   <code>static final long serialVersionUID</code>
     * </ul>
     */
    private boolean checkSystemField(int access, String fieldName, String signature){
        if(fieldName.equals("serialVersionUID")){
            return checkAccess(access, OPCODE_ACC_STATIC) &&
                checkAccess(access, OPCODE_ACC_FINAL) &&
                signature.equals("J");
        }

        return false;
    }

    public boolean isWellKnownClass(String className){
        return checkSystemClass(className);
    }

    public boolean isSystemMethod(int access, String methodName, String signature){
        return checkSystemMethod(access, methodName, signature);
    }

    public boolean isSystemField(int access, String fieldName, String signature){
        return checkSystemField(access, fieldName, signature);
    }

    private boolean checkAccess(int access, int code){
        return (access & code) == code;
    }
}
