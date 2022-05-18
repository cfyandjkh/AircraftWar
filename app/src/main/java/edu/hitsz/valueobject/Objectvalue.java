package edu.hitsz.valueobject;

import java.util.Comparator;

public class Objectvalue {
    private String name;
    private int score;
    private String time;
    public Objectvalue (String name,int score,String time){
        this.name=name;
        this.score=score;
        this.time=time;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
    public void setScore(int score){
        this.score=score;
    }
    public int getScore(){
        return  score;
    }
    public String getTime(){
        return time;
    }
    public static class Comparators{
        //根据分数进行排序
        public static Comparator<Objectvalue> SCORE=new Comparator<Objectvalue>() {
            @Override
            public int compare(Objectvalue o1, Objectvalue o2) {
                return o2.getScore()-o1.getScore();
            }
        };
    }
}
