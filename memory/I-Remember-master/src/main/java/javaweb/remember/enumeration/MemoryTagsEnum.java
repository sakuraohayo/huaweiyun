package javaweb.remember.enumeration;

public enum MemoryTagsEnum {

    HAPPY("快乐"),
    HAPPINESS("幸福"),
    SAD("悲伤"),
    CALM("平静"),
    LOSE("失落"),
    GLOOMY("郁闷"),
    ANGRY("生气"),
    RAGE("愤怒"),
    ANXIOUS("焦虑"),
    DEJECTED("沮丧"),

    ;

    private String tag;

    MemoryTagsEnum(String tag){
        this.tag = tag;
    }

    public String getTag(){
        return tag;
    }

    public void setTag(String tag){
        this.tag = tag;
    }

}
