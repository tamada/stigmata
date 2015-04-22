package com.github.stigmata.birthmarks.uc;

import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.birthmarks.PlainBirthmark;

/**
 * 
 * @author Haruaki Tamada
 */
class UsedClassesBirthmark extends PlainBirthmark{
    private static final long serialVersionUID = -1043130948373105655L;

    public UsedClassesBirthmark(String type){
        super(type);
    }

    @Override
    public synchronized void addElement(BirthmarkElement element){
        int c = getElementCount();
        String s = (String)element.getValue();
        boolean addFlag = false;
        for(int i = 0; i < c; i++){
            BirthmarkElement e = getElement(i);
            String v = (String)e.getValue();
            if(s.equals(v)){
                addFlag = true;
                break;
            }
        }
        if(!addFlag){
            for(int i = 0; i < c; i++){
                if(s.compareTo((String)getElement(i).getValue()) < 0){
                    super.addElement(i, element);
                    break;
                }
            }
        }
        if(!addFlag && c == getElementCount()){
            super.addElement(element);
        }
    }
}
