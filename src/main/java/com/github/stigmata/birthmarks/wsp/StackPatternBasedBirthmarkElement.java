package com.github.stigmata.birthmarks.wsp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.cflib.Opcode;
import com.github.stigmata.cflib.OpcodeManager;

/**
 *
 * @author Haruaki TAMADA
 */
public class StackPatternBasedBirthmarkElement
        extends BirthmarkElement implements Iterable<CurrentDepth>{
    private static final long serialVersionUID = 7965456413167854L;

    private List<CurrentDepth> list = new ArrayList<CurrentDepth>();
    private int weight = -1;

    public StackPatternBasedBirthmarkElement(CurrentDepth[] depthArray){
        super(getStringRepresentation(depthArray));
        for(CurrentDepth depth: depthArray){
            list.add(depth);
        }
    }
    public StackPatternBasedBirthmarkElement(String value){
        super(value);
        String[] depthList = value.split(" ");
        for(int i = 0; i < depthList.length; i++){
            String[] depthStringArray = depthList[i].split(":");
            if(depthStringArray.length == 4){
                int opcode = Integer.parseInt(depthStringArray[0]);
                int depth = Integer.parseInt(depthStringArray[1]);
                int weight = Integer.parseInt(depthStringArray[2]);
                int act = Integer.parseInt(depthStringArray[3]);

                WSPOpcode o = new WSPOpcode(
                    OpcodeManager.getInstance().getOpcode(opcode), weight
                );
                if(o.getCategory() == Opcode.Category.FIELD
                        || o.getCategory() == Opcode.Category.INVOKE){
                    o.setAct(act);
                }
                list.add(new CurrentDepth(depth, o));
            }
        }
    }

    public int getLength(){
        return list.size();
    }

    public CurrentDepth getDepth(int index){
        return list.get(index);
    }

    @Override
    public Iterator<CurrentDepth> iterator(){
        return Collections.unmodifiableList(list).iterator();
    }

    public int getWeight(StackPatternBasedBirthmarkElement element){
        int[][] matrix = new int[element.getLength() + 1][getLength() + 1];

        for(int i = 0; i <= element.getLength(); i++){
            for(int j = 0; j <= getLength(); j++){
                if(i == 0 || j == 0){
                    matrix[i][j] = 0;
                }
                else if(element.getDepth(i - 1).getOpcode().getOpcode()
                        == getDepth(j - 1).getOpcode().getOpcode()){
                    matrix[i][j] = (int)(
                        matrix[i - 1][j - 1]
                        + getDepth(j - 1).getOpcode().getWeight()
                    );
                }
                else{
                    matrix[i][j] =
                        Math.max(matrix[i - 1][j], matrix[i][j - 1]);
                }
            }
        }

        int max = 0;
        int last = element.getLength();
        for(int i = 0; i < matrix[last].length; i++){
            if(matrix[last][i] > max){
                max = matrix[last][i];
            }
        }
        return max;
    }

    public int getWeight(){
        if(weight < 0){
            int w = 0;
            for(CurrentDepth depth: this){
                w += depth.getOpcode().getWeight();
            }
            this.weight = w;
        }
        return weight;
    }

    private static String getStringRepresentation(CurrentDepth[] depth){
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < depth.length; i++){
            if(i != 0){
                builder.append(" ");
            }
            builder.append(depth[i]);
        }
        return new String(builder);
    }
}
