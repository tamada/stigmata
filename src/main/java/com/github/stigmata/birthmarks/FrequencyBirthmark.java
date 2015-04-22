package com.github.stigmata.birthmarks;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.stigmata.BirthmarkElement;
import com.github.stigmata.utils.ArrayIterator;

/**
 * 
 * @author Haruaki Tamada
 */
public class FrequencyBirthmark extends AbstractBirthmark{
    private static final long serialVersionUID = 1905526895627693908L;

    private Map<String, FrequencyBirthmarkElement> counts = new HashMap<String, FrequencyBirthmarkElement>();
    private String type;

    public FrequencyBirthmark(String type){
        this.type = type;
    }

    @Override
    public int getElementCount(){
        return counts.size();
    }

    @Override
    public synchronized BirthmarkElement[] getElements(){
        FrequencyBirthmarkElement[] elements = new FrequencyBirthmarkElement[counts.size()];
        int index = 0;
        for(Map.Entry<String, FrequencyBirthmarkElement> entry: counts.entrySet()){
            elements[index] = entry.getValue();
            index++;
        }
        Arrays.sort(elements, new Comparator<FrequencyBirthmarkElement>(){
            @Override
            public int compare(FrequencyBirthmarkElement o1, FrequencyBirthmarkElement o2){
                return o1.getValueName().compareTo(o2.getValueName());
            }
        });
        
        return elements;
    }

    @Override
    public Iterator<BirthmarkElement> iterator(){
        return new ArrayIterator<BirthmarkElement>(getElements());
    }

    @Override
    public void addElement(BirthmarkElement element){
        String value;
        if(element instanceof FrequencyBirthmarkElement){
            FrequencyBirthmarkElement e = (FrequencyBirthmarkElement)element;
            value = e.getValueName();
        }
        else{
            value = (String)element.getValue();
        }
        FrequencyBirthmarkElement foundElement = counts.get(value);
        if(foundElement != null){
            foundElement.incrementValueCount();
        }
        else{
            foundElement = new FrequencyBirthmarkElement(value);
        }
        counts.put(value, foundElement);
    }

    @Override
    public String getType(){
        return type;
    }
}
