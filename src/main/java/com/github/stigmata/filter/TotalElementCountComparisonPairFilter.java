package com.github.stigmata.filter;

import java.util.ArrayList;
import java.util.List;

import com.github.stigmata.ComparisonPair;
import com.github.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class TotalElementCountComparisonPairFilter extends AbstractComparisonPairFilter{
    private static final List<Criterion> CRITERIA = new ArrayList<Criterion>();
    static{
        CRITERIA.add(Criterion.GREATER_EQUALS);
        CRITERIA.add(Criterion.GREATER_THAN);
        CRITERIA.add(Criterion.LESS_EQUALS);
        CRITERIA.add(Criterion.LESS_THAN);
        CRITERIA.add(Criterion.EQUALS_AS);
        CRITERIA.add(Criterion.NOT_EQUALS_AS); 
        CRITERIA.add(Criterion.MATCH);
        CRITERIA.add(Criterion.NOT_MATCH);
    };

    private int threshold = 0;
    private FilterTarget target;

    public TotalElementCountComparisonPairFilter(ComparisonPairFilterService service){
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
        boolean flag;
        if(getTarget() == FilterTarget.TARGET_1){
            flag = checkFiltered(pair.getTarget1().getSumOfElementCount());
        }
        else if(getTarget() == FilterTarget.TARGET_2){
            flag = checkFiltered(pair.getTarget2().getSumOfElementCount());
        }
        else{
            flag = checkFiltered(pair.getTarget1().getSumOfElementCount(), pair.getTarget2().getSumOfElementCount());
        }
        return flag;
    }

    private boolean checkFiltered(int e1, int e2){
        boolean flag1;
        boolean flag2;
        int t = getThreshold();
        switch(getCriterion()){
        case GREATER_EQUALS:
            flag1 = e1 >= t; flag2 = e2 >= t;
            break;
        case GREATER_THAN:
            flag1 = e1 > t;  flag2 = e2 > t;
            break;
        case LESS_EQUALS:
            flag1 = e1 <= t; flag2 = e2 <= t;
            break;
        case LESS_THAN:
            flag1 = e1 < t;  flag2 = e2 < t;
            break;
        case EQUALS_AS:
            flag1 = e1 == t; flag2 = e2 == t;
            break;
        case NOT_EQUALS_AS:
            flag1 = e1 != t; flag2 = e2 != t;
            break;
        case MATCH:
            flag1 = e1 == e2; flag2 = flag1;
            break;
        case NOT_MATCH:
            flag1 = e1 != e2; flag2 = flag1;
            break;
        default:
            flag1 = false;
            flag2 = false;
            break;
        }
        return (getTarget() == FilterTarget.BOTH_TARGETS && flag1 && flag2) ||
            (getTarget() == FilterTarget.ONE_OF_TARGETS && (flag1 || flag2));
    }

    private boolean checkFiltered(int total){
        boolean flag = false;
        switch(getCriterion()){
        case GREATER_EQUALS:
            flag = total >= getThreshold();
            break;
        case GREATER_THAN:
            flag = total > getThreshold();
            break;
        case LESS_EQUALS:
            flag = total <= getThreshold();
            break;
        case LESS_THAN:
            flag = total < getThreshold();
            break;
        case EQUALS_AS:
            flag = total == getThreshold();
            break;
        case NOT_EQUALS_AS:
            flag = total != getThreshold();
            break;
        default:
            flag = false;
            break;
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
        case BOTH_TARGETS:   sb.append("(target1&target2)"); break;
        case ONE_OF_TARGETS: sb.append("(target1|target2)"); break;
        }
        sb.append(".element");
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
