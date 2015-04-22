package com.github.stigmata.filter;

import java.util.ArrayList;
import java.util.List;

import com.github.stigmata.ComparisonPair;
import com.github.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class BirthmarkElementCountComparisonPairFilter extends AbstractComparisonPairFilter{
    private static final List<Criterion> CRITERIA = new ArrayList<Criterion>();

    private int threshold = 0;
    private FilterTarget target;
    private String birthmarkType;

    static{
        CRITERIA.add(Criterion.GREATER_EQUALS);
        CRITERIA.add(Criterion.GREATER_THAN);
        CRITERIA.add(Criterion.LESS_EQUALS);
        CRITERIA.add(Criterion.LESS_THAN);
        CRITERIA.add(Criterion.EQUALS_AS);
        CRITERIA.add(Criterion.NOT_EQUALS_AS);
    }

    public BirthmarkElementCountComparisonPairFilter(ComparisonPairFilterService service){
        super(service);
    }

    public String getBirthmarkType(){
        return birthmarkType;
    }

    public void setBirthmarkType(String birthmarkType){
        this.birthmarkType = birthmarkType;
    }

    public static Criterion[] getValidCriteria(){
        return CRITERIA.toArray(new Criterion[CRITERIA.size()]);
    }

    @Override
    public Criterion[] getAcceptableCriteria(){
        return getValidCriteria();
    }

    private boolean isFilteredTwo(ComparisonPair pair){
        boolean flag = false;

        String type = getBirthmarkType();
        if(pair.getTarget1().hasBirthmark(type) && pair.getTarget2().hasBirthmark(type)){
            int elem1 = pair.getTarget1().getBirthmark(type).getElementCount();
            int elem2 = pair.getTarget2().getBirthmark(type).getElementCount();

            switch(getCriterion()){
            case GREATER_EQUALS:
                flag = (target == FilterTarget.BOTH_TARGETS && elem1 >= threshold && elem2 >= threshold) ||
                (target == FilterTarget.ONE_OF_TARGETS && (elem1 >= threshold || elem2 >= threshold));
                break;
            case GREATER_THAN:
                flag = (target == FilterTarget.BOTH_TARGETS && elem1 > threshold && elem2 > threshold) ||
                    (target == FilterTarget.ONE_OF_TARGETS && (elem1 > threshold || elem2 > threshold));
                break;
            case LESS_EQUALS:
                flag = (target == FilterTarget.BOTH_TARGETS && elem1 <= threshold && elem2 <= threshold) ||
                (target == FilterTarget.ONE_OF_TARGETS && (elem1 <= threshold || elem2 <= threshold));
                break;
            case LESS_THAN:
                flag = (target == FilterTarget.BOTH_TARGETS && elem1 < threshold && elem2 < threshold) ||
                    (target == FilterTarget.ONE_OF_TARGETS && (elem1 < threshold || elem2 < threshold));
                break;
            case EQUALS_AS:
                flag = (target == FilterTarget.BOTH_TARGETS && elem1 == threshold && elem2 == threshold) ||
                (target == FilterTarget.ONE_OF_TARGETS && (elem1 == threshold || elem2 == threshold));
                break;
            case NOT_EQUALS_AS:
                flag = (target == FilterTarget.BOTH_TARGETS && elem1 != threshold && elem2 != threshold) ||
                    (target == FilterTarget.ONE_OF_TARGETS && (elem1 != threshold || elem2 != threshold));
                break;
            default:
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public boolean isFiltered(ComparisonPair pair){
        if(target == FilterTarget.BOTH_TARGETS || target == FilterTarget.ONE_OF_TARGETS){
            return isFilteredTwo(pair);
        }
        boolean flag = false;
        String type = getBirthmarkType();
        if(pair.getTarget1().hasBirthmark(type) && pair.getTarget2().hasBirthmark(type)){
            int total = 0;
            int threshold = getThreshold();
            if(target == FilterTarget.TARGET_1){
                total = pair.getTarget1().getBirthmark(type).getElementCount();
            }
            if(target == FilterTarget.TARGET_2){
                total = pair.getTarget2().getBirthmark(type).getElementCount();
            }
            switch(getCriterion()){
            case GREATER_EQUALS:
                flag = total >= threshold;
                break;
            case GREATER_THAN:
                flag = total > threshold;
                break;
            case LESS_EQUALS:
                flag = total <= threshold;
                break;
            case LESS_THAN:
                flag = total < threshold;
                break;
            case EQUALS_AS:
                flag = total == threshold;
                break;
            case NOT_EQUALS_AS:
                flag = total != threshold;
                break;
            default:
                flag = false;
                break;
            }
        }
        return flag;
    }

    public int getThreshold(){
        return threshold;
    }

    public void setThreshold(int threshold){
        if(threshold < 0){
            throw new IllegalArgumentException("threshold must be positive value: " + threshold);
        }
        this.threshold = threshold;
    }

    public FilterTarget getTarget(){
        return target;
    }

    public void setTarget(FilterTarget target){
        this.target = target;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        switch(getTarget()){
        case TARGET_1:       sb.append("target1"); break;
        case TARGET_2:       sb.append("target2"); break;
        case BOTH_TARGETS:   sb.append("(target1&target2)");    break;
        case ONE_OF_TARGETS: sb.append("(target1|target2)");
        }
        sb.append(".").append(birthmarkType);
        sb.append(".size");
        switch(getCriterion()){
        case GREATER_EQUALS: sb.append(" >= "); break;
        case GREATER_THAN:   sb.append(" >  "); break;
        case LESS_EQUALS:    sb.append(" <= "); break;
        case LESS_THAN:      sb.append(" <  "); break;
        case EQUALS_AS:      sb.append(" == "); break;
        case NOT_EQUALS_AS:  sb.append(" != "); break;
        // The followings are not used.
        case ENDS_WITH:
        case MATCH:
        case NOT_ENDS_WITH:
        case NOT_MATCH:
        case NOT_STARTS_WITH:
        case STARTS_WITH:
        default:
            break;
        }
        sb.append(Integer.toString(getThreshold()));

        return new String(sb);
    }
}
