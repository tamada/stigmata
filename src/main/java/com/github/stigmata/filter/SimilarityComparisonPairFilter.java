package com.github.stigmata.filter;

import java.util.ArrayList;
import java.util.List;

import com.github.stigmata.ComparisonPair;
import com.github.stigmata.spi.ComparisonPairFilterService;

/**
 * 
 * @author Haruaki TAMADA
 */
public class SimilarityComparisonPairFilter extends AbstractComparisonPairFilter{
    private static final double EQUALS_THRESHOLD = 5E-5;

    private static List<Criterion> CRITERIA = new ArrayList<Criterion>();
    static{
        CRITERIA.add(Criterion.GREATER_EQUALS);
        CRITERIA.add(Criterion.GREATER_THAN);
        CRITERIA.add(Criterion.LESS_EQUALS);
        CRITERIA.add(Criterion.LESS_THAN);
        CRITERIA.add(Criterion.EQUALS_AS);
        CRITERIA.add(Criterion.NOT_EQUALS_AS); 
    };

    private double threshold;

    public SimilarityComparisonPairFilter(ComparisonPairFilterService service){
        super(service);
        setThreshold(0.8d);
    }
    
    @Override
    public boolean isFiltered(ComparisonPair pair){
        double similarity = pair.calculateSimilarity();
        boolean flag;
        switch(getCriterion()){
        case GREATER_EQUALS:
            flag = similarity >= getThreshold();
            break;
        case GREATER_THAN:
            flag = similarity > getThreshold();
            break;
        case LESS_EQUALS:
            flag = similarity <= getThreshold();
            break;
        case LESS_THAN:
            flag = similarity < getThreshold();
            break;
        case EQUALS_AS:
            flag = (similarity - getThreshold()) <= EQUALS_THRESHOLD;
            break;
        case NOT_EQUALS_AS:
            flag = (similarity - getThreshold()) > EQUALS_THRESHOLD;
            break;
        default:
            flag = false;
            break;
        }
        return flag;
    }

    public static Criterion[] getValidCriteria(){
        return CRITERIA.toArray(new Criterion[CRITERIA.size()]);
    }

    @Override
    public Criterion[] getAcceptableCriteria(){
        return getValidCriteria();
    }

    public double getThreshold(){
        return threshold;
    }

    public void setThreshold(double threshold){
        if(threshold < 0d || threshold >= 1.0d){
            throw new IllegalArgumentException("threshold must be 0.0-1.0");
        }
        this.threshold = threshold;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        sb.append("similarity");
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
        sb.append(getThreshold());
        return new String(sb);
    }
}
