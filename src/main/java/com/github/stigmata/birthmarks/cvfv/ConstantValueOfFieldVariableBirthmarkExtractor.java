package com.github.stigmata.birthmarks.cvfv;

import org.objectweb.asm.ClassWriter;

import com.github.stigmata.Birthmark;
import com.github.stigmata.BirthmarkContext;
import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.ExtractionUnit;
import com.github.stigmata.birthmarks.ASMBirthmarkExtractor;
import com.github.stigmata.birthmarks.BirthmarkExtractVisitor;
import com.github.stigmata.spi.BirthmarkService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class ConstantValueOfFieldVariableBirthmarkExtractor extends ASMBirthmarkExtractor{
    public ConstantValueOfFieldVariableBirthmarkExtractor(BirthmarkService spi){
        super(spi);
    }

    public ConstantValueOfFieldVariableBirthmarkExtractor(){
        super();
    }

    @Override
    public BirthmarkExtractVisitor createExtractVisitor(ClassWriter writer, Birthmark birthmark, BirthmarkContext context) {
        return new ConstantValueOfFieldVariableBirthmarkExtractVisitor(writer, birthmark, context);
    }

    @Override
    public ExtractionUnit[] getAcceptableUnits(){
        return new ExtractionUnit[] {
            ExtractionUnit.CLASS, ExtractionUnit.PACKAGE, ExtractionUnit.ARCHIVE, 
        };
    }

    @Override
    public BirthmarkElement buildElement(String value) {
    	String signature = value.substring(0, value.indexOf('='));
    	String subValue = value.substring(value.indexOf('=') + 1);
    	Object elementValue = subValue;
    
        if(subValue.equals("null")){
            elementValue = null;
        }
        else{
            switch(signature.charAt(0)){
            case 'Z':{
                if(value.equals("true")) elementValue = Boolean.TRUE;
                else                     elementValue = Boolean.FALSE;
                break;
            }
            case 'C': elementValue = new Character(subValue.charAt(0)); break;
            case 'D': elementValue = new Double(subValue);  break;
            case 'F': elementValue = new Float(subValue);   break;
            case 'S': elementValue = new Short(subValue);   break;
            case 'B': elementValue = new Byte(subValue);    break;
            case 'I': elementValue = new Integer(subValue); break;
            default:  elementValue = value; break;
            }
    	}
    	return new TypeAndValueBirthmarkElement(signature, elementValue);
    }
}
