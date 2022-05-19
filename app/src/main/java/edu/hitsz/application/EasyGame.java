package edu.hitsz.application;

import android.content.Context;

public class EasyGame extends Game {

    public EasyGame(String grade , boolean musicOpenFlag, Context context, Object lock) {
        super( context);
        setEnemyMaxNumber(4);
        setEnemySpeedX(0,10,0);
        setEnemySpeedY(10,10,0);
        setEnemyHp(30,30,0);
        setCycleDurationOfEnemyGeneration(600);
        setEliteEnemyPossibility(0.25);
        setSupplyPossibility(0.75);
        setMiniumScoreOfBossGeneration(100);
    }

    @Override
    public boolean ifBossGenerationFlag() {
        return false;
    }

    @Override
    public boolean ifGameIsHarder(){
        return false;
    }
}
