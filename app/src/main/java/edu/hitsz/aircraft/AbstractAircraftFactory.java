package edu.hitsz.aircraft;

import edu.hitsz.supply.BombSupply;

import java.util.Random;

public abstract class AbstractAircraftFactory {
    private static int rd;
    private static int MOB_ENEMY_SPEEDX;
    private static int ELITE_ENEMY_SPEEDX;
    private static int BOSS_ENEMY_SPEEDX;
    private static int MOB_ENEMY_SPEEDY;
    private static int ELITE_ENEMY_SPEEDY;
    private static int BOSS_ENEMY_SPEEDY;
    private static int MOB_ENEMY_HP;
    private static int ELITE_ENEMY_HP;
    private static int BOSS_ENEMY_HP;
    private static int limit;

    public static void setEnemySpeedX(int mobEnemySpeedX,int eliteEnemySpeedX,int bossEnemySpeedX){
        MOB_ENEMY_SPEEDX=mobEnemySpeedX;
        ELITE_ENEMY_SPEEDX=eliteEnemySpeedX;
        BOSS_ENEMY_SPEEDX=bossEnemySpeedX;
    }

    public static void setEnemySpeedY(int mobEnemySpeedY,int eliteEnemySpeedY,int bossEnemySpeedY){
        MOB_ENEMY_SPEEDY=mobEnemySpeedY;
        ELITE_ENEMY_SPEEDY=eliteEnemySpeedY;
        BOSS_ENEMY_SPEEDY=bossEnemySpeedY;
    }

    public static void setEnemyHp(int mobEnemyHp,int eliteEnemyHp,int bossEnemyHp){
        MOB_ENEMY_HP=mobEnemyHp;
        ELITE_ENEMY_HP=eliteEnemyHp;
        BOSS_ENEMY_HP=bossEnemyHp;
    }

    public static AbstractAircraft order(double elitePossibility,boolean bossGenerationFlag){
        AbstractAircraft enemy;
        if(bossGenerationFlag){
            enemy = new BossEnemyFactory().create(BOSS_ENEMY_SPEEDX,BOSS_ENEMY_SPEEDY,BOSS_ENEMY_HP);
        }else {
            rd=new Random().nextInt(100);
            limit=(int)(elitePossibility*100-1);
            if (rd<=limit ) {
                enemy = new EliteEnemyFactory().create(ELITE_ENEMY_SPEEDX,ELITE_ENEMY_SPEEDY,ELITE_ENEMY_HP);
            } else {
                enemy = new MobEnemyFactory().create(MOB_ENEMY_SPEEDX,MOB_ENEMY_SPEEDY,MOB_ENEMY_HP);
            }
        }
        return enemy;
    }
    public abstract AbstractAircraft create(int speedX,int speedY,int hp);
}
