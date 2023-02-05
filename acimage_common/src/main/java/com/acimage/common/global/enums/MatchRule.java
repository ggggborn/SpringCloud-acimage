package com.acimage.common.global.enums;

/**
 * interface表中匹配规则
 */
public enum MatchRule {
    /**
     * 精确匹配
     */
    EXACT(1),
    /**
     * 匹配前缀
     */
    PREFIX(2);

    private final int key;

    MatchRule(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public static MatchRule from(int key){
        for(MatchRule matchRule:MatchRule.values()){
            if(matchRule.getKey()==key){
                return matchRule;
            }
        }
        return null;
    }
}
