package edu.hitsz.application;

import android.content.Context;

public class HardGame extends Game {
    public HardGame(String grade , boolean musicOpenFlag, Context context, Object lock) {
        super( context);
        setEnemyMaxNumber(8);
        setEnemySpeedX(0,20,30);
        setEnemySpeedY(20,20,0);
        setEnemyHp(30,60,300);
        setCycleDurationOfEnemyGeneration(400);
        setEliteEnemyPossibility(0.5);
        setSupplyPossibility(0.25);
        setMiniumScoreOfBossGeneration(150);
        setSupplyPossibilityMin(0.10);
        setEliteEnemyPossibilityMax(0.75);
    }
    @Override
    public boolean ifBossHpIncrease(){
        return  true;
    }

    @Override
    public  boolean ifMissileGenerates(){
        return true;
    }
}
