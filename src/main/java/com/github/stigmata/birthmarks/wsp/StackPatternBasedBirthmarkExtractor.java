package com.github.stigmata.birthmarks.wsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.ExtractionUnit;
import com.github.stigmata.birthmarks.ASMBirthmarkExtractor;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;
import com.github.stigmata.cflib.BirthmarkElementBuilder;
import com.github.stigmata.cflib.LabelOpcode;
import com.github.stigmata.cflib.Opcode;
import com.github.stigmata.cflib.OpcodeExtractVisitor;
import com.github.stigmata.spi.BirthmarkService;

/**
 *
 * @author Haruaki Tamada
 */
public class StackPatternBasedBirthmarkExtractor
        extends ASMBirthmarkExtractor{
    public StackPatternBasedBirthmarkExtractor(BirthmarkService service){
        super(service);
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(
            ClassWriter writer, Birthmark birthmark,
	    BirthmarkContext context){

        return new OpcodeExtractVisitor(
            writer, birthmark, context, new WSPBirthmarkElementBuilder()
        );
    }

    @Override
    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] {
            ExtractionUnit.CLASS,
        };
    }

    private static class WSPBirthmarkElementBuilder
            implements BirthmarkElementBuilder{
        @Override
        public BirthmarkElement[] buildElements(List<Opcode> opcodes,
                                                BirthmarkContext context){
            List<CurrentDepth> pattern = buildStackPattern(opcodes, context);
            List<BirthmarkElement> elements =
                new ArrayList<BirthmarkElement>();

            List<CurrentDepth> subPattern = new ArrayList<CurrentDepth>();
            for(CurrentDepth depth: pattern){
                subPattern.add(depth);
                if(depth.getDepth() == 0){
                    elements.add(
                        new StackPatternBasedBirthmarkElement(
                            subPattern.toArray(
                                new CurrentDepth[subPattern.size()]
                            )
                        )
                    );
                    subPattern.clear();
                }
            }
            elements.add(
                new StackPatternBasedBirthmarkElement(
                    subPattern.toArray(new CurrentDepth[subPattern.size()])
                )
            );

            return elements.toArray(new BirthmarkElement[elements.size()]);
        }

        @SuppressWarnings("unchecked")
        private List<CurrentDepth> buildStackPattern(List<Opcode> opcodes,
                BirthmarkContext context){
            Map<Label, Integer> tableMap = new HashMap<Label, Integer>();
            List<CurrentDepth> pattern = new ArrayList<CurrentDepth>();
            Map<Integer, Integer> weights =
                (Map<Integer, Integer>)context.getProperty(
                    "birthmarks.wsp.weights"
                );

            int currentDepth = 0;
            Integer forwardedStatus = null;
            for(Opcode opcode: opcodes){
                if(opcode.getCategory() == Opcode.Category.TARGETER){
                    forwardedStatus =
                        tableMap.get(((LabelOpcode)opcode).getLabel());
                }
                else{
                    WSPOpcode wspOpcode = new WSPOpcode(
                        opcode, weights.get(opcode.getOpcode())
                    );
                    if(forwardedStatus == null){
                        currentDepth += opcode.getAct();
                    }
                    else{
                        currentDepth = forwardedStatus + opcode.getAct();
                    }
                    forwardedStatus = null;

                    pattern.add(new CurrentDepth(currentDepth, wspOpcode));
                    if(opcode.getCategory() == Opcode.Category.BRANCH){
                        for(Label label: opcode.getLabels()){
                            tableMap.put(label, currentDepth);
                        }
                    }
                }
            }
            return pattern;
        }
    }

    @Override
    public BirthmarkElement buildElement(String value){
        return new StackPatternBasedBirthmarkElement(value);
    };
}
