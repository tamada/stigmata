package com.github.stigmata.utils;

/**
 * A rule for checking wellknown class or not.
 * 
 * @author Haruaki TAMADA
 */
public final class WellknownClassJudgeRule{
    public enum MatchType{
        PREFIX, SUFFIX, EXACT, NOT_MATCH,
    };
    public enum MatchPartType{
        FULLY_NAME, PACKAGE_NAME, CLASS_NAME,
    };

    private MatchType matchType;
    private MatchPartType partType;
    private boolean excludeFlag;

    private String pattern;

    public WellknownClassJudgeRule(String pattern, MatchType matchType, MatchPartType partType){
        this(pattern, matchType, partType, false);
    }

    public WellknownClassJudgeRule(String pattern, MatchType matchType, MatchPartType partType, boolean excludeFlag){
        this.pattern = pattern;
        this.matchType = matchType;
        this.partType = partType;
        this.excludeFlag = excludeFlag;
    }

    public void setExclude(boolean excludeFlag){
        this.excludeFlag = excludeFlag;
    }

    public boolean isExclude(){
        return excludeFlag;
    }

    public String getPattern(){
        return pattern;
    }

    public MatchType getMatchType(){
        return matchType;
    }

    public MatchPartType getMatchPartType(){
        return partType;
    }

    @Override
    public int hashCode(){
        int m = getMatchType().hashCode();
        int p = getMatchPartType().hashCode();
        int s = getPattern().hashCode();

        return m + p + s;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof WellknownClassJudgeRule){
            WellknownClassJudgeRule wcjr = (WellknownClassJudgeRule)o;
            return getPattern().equals(wcjr.getPattern())
                && getMatchPartType() == wcjr.getMatchPartType()
                && getMatchType() == wcjr.getMatchType();
        }
        return false;
    }

    @Override
    public String toString(){
        String string = null;
        switch(getMatchType()){
        case PREFIX:
            string = String.format("<prefix>%s</prefix>", getPattern());
            break;
        case SUFFIX:
            string = String.format("<suffix>%s</suffix>", getPattern());
            break;
        case EXACT:
            string = String.format("<match>%s</match>", getPattern());
            break;
        case NOT_MATCH:
            string = String.format("<not-match>%s</not-match>", getPattern());
            break;
        }
        switch(getMatchPartType()){
        case CLASS_NAME:
            string = String.format("<class-name>%s</class-name>", string);
            break;
        case FULLY_NAME:
            string = String.format("<fully-name>%s</fully-name>", string);
            break;
        case PACKAGE_NAME:
            string = String.format("<package-name>%s</package-name>", string);
            break;
        }
        if(isExclude()){
            string = "<exclude>" + string + "</exclude>";
        }
        return string;
    }
}
