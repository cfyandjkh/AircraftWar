package edu.hitsz.application;

import android.content.Context;

public class SimpleGame extends Game{

    public SimpleGame(String grade , boolean musicOpenFlag, Context context, Object lock) {
        super( context);
        setEnemyMaxNumber(6);
        setEnemySpeedX(0,10,10);
        setEnemySpeedY(10,10,0);
        setEnemyHp(30,60,200);
        setCycleDurationOfEnemyGeneration(500);
        setEliteEnemyPossibility(0.33);
        setSupplyPossibility(0.5);
        setMiniumScoreOfBossGeneration(300);
        setSupplyPossibilityMin(0.25);
        setEliteEnemyPossibilityMax(0.5);
    }
}
