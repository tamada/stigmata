package com.github.stigmata.filter;

import java.util.ArrayList;
import java.util.List;

import com.github.stigmata.ComparisonPair;
import com.github.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class TargetNameComparisonPairFilter extends AbstractComparisonPairFilter{
    private static final List<Criterion> CRITERIA = new ArrayList<Criterion>();

    static{
        CRITERIA.add(Criterion.STARTS_WITH);
        CRITERIA.add(Criterion.NOT_STARTS_WITH); 
        CRITERIA.add(Criterion.ENDS_WITH);
        CRITERIA.add(Criterion.NOT_ENDS_WITH);
        CRITERIA.add(Criterion.EQUALS_AS);
        CRITERIA.add(Criterion.NOT_EQUALS_AS);
        CRITERIA.add(Criterion.MATCH);
        CRITERIA.add(Criterion.NOT_MATCH);
    };
    private FilterTarget target = FilterTarget.BOTH_TARGETS;
    private String value;

    public TargetNameComparisonPairFilter(ComparisonPairFilterService service){
        super(service);
    }

    @Override
    public Criterion[] getAcceptableCriteria(){
        return getValidCriteria();
    }

    public static Criterion[] getValidCriteria(){
        return CRITERIA.toArray(new Criterion[CRITERIA.size()]);
    }

    @Override
    public boolean isFiltered(ComparisonPair pair){
        String v = value;
        if(v == null) v = "";
        boolean flag;
        if(getTarget() == FilterTarget.TARGET_1){
            flag = checkMatch(pair.getTarget1().getName(), v);
        }
        else if(getTarget() == FilterTarget.TARGET_2){
            flag = checkMatch(pair.getTarget2().getName(), v);
        }
        else{
            flag = checkMatch(pair.getTarget1().getName(), pair.getTarget2().getName(), v);
        }
        return flag;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public FilterTarget getTarget(){
        return target;
    }

    public void setTarget(FilterTarget target){
        this.target = target;
    }

    private boolean checkMatch(String name1, String name2, String value){
        boolean flag1;
        boolean flag2;
        switch(getCriterion()){
        case STARTS_WITH:
            flag1 = name1.startsWith(value);
            flag2 = name2.startsWith(value);
            break;
        case ENDS_WITH:
            flag1 = name1.endsWith(value);
            flag2 = name2.endsWith(value);
            break;
        case EQUALS_AS:
            flag1 = name1.equals(value);
            flag2 = name2.equals(value);
            break;
        case NOT_EQUALS_AS:
            flag1 = !name1.equals(value);
            flag2 = !name2.equals(value);
            break;
        case MATCH:
            flag1 = name1.equals(name2);
            flag2 = flag1;
            break;
        case NOT_MATCH:
            flag1 = !name1.equals(name2);
            flag2 = flag1;
            break;
        default:
            flag1 = false;
            flag2 = false;
            break;
        }
        
        boolean flag;
        if(getTarget() == FilterTarget.BOTH_TARGETS){
            flag = flag1 && flag2;
        }
        else{
            flag = flag1 || flag2;
        }
        return flag;
    }

    private boolean checkMatch(String name, String value){
        boolean flag;
        switch(getCriterion()){
        case STARTS_WITH:
            flag = name.startsWith(value);
            break;
        case ENDS_WITH:
            flag = name.endsWith(value);
            break;
        case EQUALS_AS:
            flag = name.equals(value);
            break;
        case NOT_EQUALS_AS:
            flag = !name.equals(value);
            break;
        default:
            flag = false;
            break;
        }
        return flag;
    }

    @Override
    public String toString(){
        if(getCriterion() == Criterion.MATCH || getCriterion() == Criterion.NOT_MATCH){
            String value = " match ";
            if(getCriterion() == Criterion.NOT_MATCH) value = " not match ";
            return "target1.name" + value + "target2.name";
        }
        StringBuilder sb = new StringBuilder();
        switch(getTarget()){
        case TARGET_1:       sb.append("target1.name");           break;
        case TARGET_2:       sb.append("target2.name");           break;
        case BOTH_TARGETS:   sb.append("(target1&target2).name"); break;
        case ONE_OF_TARGETS: sb.append("(target1|target2).name"); break; 
        }
        switch(getCriterion()){
        case STARTS_WITH:     sb.append(" starts with ");        break;
        case NOT_STARTS_WITH: sb.append(" not starts with ");    break;
        case ENDS_WITH:       sb.append(" ends with ");          break;
        case NOT_ENDS_WITH:   sb.append(" not ends with ");      break;
        case EQUALS_AS:       sb.append(" equals as ");          break;
        case NOT_EQUALS_AS:   sb.append(" not equals as ");      break;
        case GREATER_EQUALS:  sb.append(" greater equals than"); break;
        case GREATER_THAN:    sb.append(" greater than ");       break;
        case LESS_EQUALS:     sb.append(" less equals than ");   break;
        case LESS_THAN:       sb.append(" less than ");          break;
        case MATCH:           sb.append(" match ");              break;
        case NOT_MATCH:       sb.append(" not match ");          break;
        default:
            break;
        }
        sb.append(getValue());

        return new String(sb);
    }
}
