package edu.hitsz.application;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.List;
import androidx.annotation.NonNull;

import com.example.aircraftwar.MainActivity;
import com.example.aircraftwar.R;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.Basebullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.bullet.Missile;
import edu.hitsz.supply.*;


import java.util.*;
import java.util.concurrent.*;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public  class Game extends SurfaceView implements
        SurfaceHolder.Callback,Runnable {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private  HeroAircraft heroAircraft;
    private static final List<AbstractAircraft> enemyAircrafts= new LinkedList<>();
    private static final List<Basebullet> heroBullets= new LinkedList<>();
    private static final List<Basebullet> enemyBullets= new LinkedList<>();
    private static final List<AbstractSupply> supplies=new LinkedList<>();
    private static final List<Basebullet> missiles=new LinkedList<>();

    /**
     * 三种模式下敌机数量最大值分别为4,6,8
     */
    private int enemyMaxNumber=4;

    /**
     * 表示游戏难度
     */
    private String grade;
    /**
     * 不同难度下设置不同的血量和速度
     * 英雄机子弹伤害为30，因此：
     * 简单模式下：普通敌机X和Y速度为0,10，精英敌机X和Y速度为10,10，血量分别为30,30，不产生BOSS机
     * 普通模式下：普通敌机X和Y速度为0,15，精英敌机X和Y速度为15,15，BOSS机的X和Y速度为20,0，三者血量分别为30,60,200
     * 困难模式下：普通敌机X和Y速度为0,20，精英敌机X和Y速度为20,20，BOSS机的X和Y速度为30,0，三者血量分别为30,60,300,最高到达500滴血
     * 对于普通模式和困难模式，敌机属性每半分钟提高0.02，最高到达2倍。
     */
    private static int MOB_ENEMY_SPEEDX=0;
    private static int ELITE_ENEMY_SPEEDX=10;
    private static int BOSS_ENEMY_SPEEDX=10;
    private static int MOB_ENEMY_SPEEDY=10;
    private static int ELITE_ENEMY_SPEEDY=10;
    private static int BOSS_ENEMY_SPEEDY=0;
    private static int MOB_ENEMY_HP=30;
    private static int ELITE_ENEMY_HP=30;
    private static int BOSS_ENEMY_HP=100;
    private static int BOSS_ENEMY_HP_INCREASEMENT=30;
    private static int BOSS_ENEMY_HP_MAX=500;
    private static int ENEMY_RATE_MAX=2;
    private static double ENEMY_RATE=1;
    private static double ENEMY_RATE_INCREASEMENT=0.02;

    private static boolean  gameOverFlag = false;
    private int score = 0;
    private int time = 0;
    private static boolean bossAliveFlag=false;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率,
     * 子弹发射的周期不随难度而改变
     * 敌机产生的周期随难度而改变，三种难度模式下生成的周期分别为600,500,400
     * 对于普通模式和困难模式，每隔半分钟提升一次难度。
     */
    private int cycleDurationOfShootAction = 600;
    private int cycleTimeOfShootAction = 0;
    private int cycleDurationOfEnemyGeneration=600;
    private int cycleTimeOfEnemyGeneration=0;
    private int HarderTime=30000;
    /**
     * 背景音乐的设置
     */
    private static boolean musicOpenFlag;
    private final int BACKGROUND_MUSIC=0;
    private final int BOSS_MUSIC=1;
    private final int OTHER_MUSIC=-1;
    /**
     * 在不同难度模式下，起始的精英敌机生成概率不同
     * 简单模式：起始为0.25
     * 普通模式：起始为0.33,最高到达0.5
     * 困难模式：起始为0.5，最高到达0.75
     * 每次概率增加0.01
     */
    private double eliteEnemyPossibility=0.25 ;
    private double eliteEnemyPossibilityMax=0.5;
    private double eliteEnemyPossibilityIncreasement=0.01;
    /**
     * 生成boss敌机的标志
     */
    private boolean bossGenerationFlag=true;
    /**
     * 不同模式下掉落道具的概率不同
     * 简单模式下生成道具的概率为0.75（3/4）
     * 普通模式下生成道具的概率为0.5（3/6）,最低为0.25
     * 困难模式下生成道具的概率为0.25（3/12），最低为0.10
     * 每隔半分钟，道具掉落概率降低0.01
     */
    private double supplyPossibility=0.75;
    private double supplyPossibilityMin=0.25;
    private double supplyPossibilityDecreasement=-0.01;

    /**
     *不同模式下生成boss机的得分阈值不同
     * 普通模式下，生成boss机阈值为300
     * 困难模式下，生成boss机阈值为150
     */
    private int miniumScoreOfBossGeneration=300;

    private SurfaceHolder mSurfaceHolder;
    private Canvas canvas;  //绘图的画布
    private Paint mPaint;
    private Object lock;

    public Game( Context context) {
        super(context);
        mPaint = new Paint();  //设置画笔
        mPaint.setAntiAlias(true);
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        loading_img();
        heroAircraft= HeroAircraft.getInstance();
        this.setFocusable(true);
//        this.grade=grade;
//        this.lock=lock;
//        Game.musicOpenFlag=musicOpenFlag;
        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService=Executors.newScheduledThreadPool(1);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
//        if(musicOpenFlag){
//            MusicThread musicThread=new MusicThread("src\\edu\\hitsz\\music\\bgm.wav",true,BACKGROUND_MUSIC);
//            musicThread.start();
//        }
        AbstractAircraftFactory.setEnemySpeedX(MOB_ENEMY_SPEEDX,ELITE_ENEMY_SPEEDX,BOSS_ENEMY_SPEEDX);
        AbstractAircraftFactory.setEnemySpeedY(MOB_ENEMY_SPEEDY,ELITE_ENEMY_SPEEDY,BOSS_ENEMY_SPEEDY);
        AbstractAircraftFactory.setEnemyHp(MOB_ENEMY_HP,ELITE_ENEMY_HP,BOSS_ENEMY_HP);
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;
            //如果时间过了半分钟，那么难度相应增加。
//            if(ifGameIsHarder()&&time%HarderTime==0&&time!=0){
//                System.out.print("难度增加！！！  ");
//                if(supplyPossibility>supplyPossibilityMin){
//                    supplyPossibility+=supplyPossibilityDecreasement;
//                    System.out.print("现在的道具掉落概率为"+supplyPossibility+"  ");
//                }
//                if(eliteEnemyPossibility<eliteEnemyPossibilityMax){
//                    eliteEnemyPossibility+=eliteEnemyPossibilityIncreasement;
//                    System.out.print("现在的精英敌机生成概率为"+eliteEnemyPossibility+"  ");
//                }
//                if(ENEMY_RATE<ENEMY_RATE_MAX){
//                    ENEMY_RATE+=ENEMY_RATE_INCREASEMENT;
//                    AbstractAircraftFactory.setEnemySpeedX((int)(MOB_ENEMY_SPEEDX*ENEMY_RATE),(int)(ELITE_ENEMY_SPEEDX*ENEMY_RATE),BOSS_ENEMY_SPEEDX);
//                    AbstractAircraftFactory.setEnemySpeedY((int)(MOB_ENEMY_SPEEDY*ENEMY_RATE),(int)(ELITE_ENEMY_SPEEDY*ENEMY_RATE),BOSS_ENEMY_SPEEDY);
//                    AbstractAircraftFactory.setEnemyHp((int)(MOB_ENEMY_HP*ENEMY_RATE),(int)(ELITE_ENEMY_HP*ENEMY_RATE),BOSS_ENEMY_HP);
//                    System.out.print("现在的敌机属性（速度和血量）倍率为"+ENEMY_RATE+"  ");
//                }
//                System.out.println();
//            }
            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudgeOfShootAction()) {
                System.out.println(time);
                // 飞机射出子弹
                shootAction();
            }
            if(timeCountAndNewCycleJudgeOfEnemyGeneration()){
                System.out.println(time);
                // 新敌机产生
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    enemyAircrafts.add(AbstractAircraftFactory.order(eliteEnemyPossibility,false));
                    if(score%miniumScoreOfBossGeneration==0&&score!=0&&!bossAliveFlag&&ifBossGenerationFlag()){
//                        //添加boss机
//                            if(ifBossHpIncrease()&&BOSS_ENEMY_HP<BOSS_ENEMY_HP_MAX&&score!=miniumScoreOfBossGeneration){
//                                BOSS_ENEMY_HP+=BOSS_ENEMY_HP_INCREASEMENT;
//                                AbstractAircraftFactory.setEnemyHp((int)(MOB_ENEMY_HP*ENEMY_RATE),(int)(ELITE_ENEMY_HP*ENEMY_RATE),BOSS_ENEMY_HP);
//                                System.out.println("现在的BOSS血量为"+BOSS_ENEMY_HP+"  ");
//                            }
                        enemyAircrafts.add(AbstractAircraftFactory.order(0,bossGenerationFlag));
                        bossAliveFlag=true;
//                        if(musicOpenFlag){
//                            MusicThread musicThread1=new MusicThread("src\\edu\\hitsz\\music\\bgm_boss.wav",true,BOSS_MUSIC);
//                            musicThread1.start();
//                        }
                    }
                }
            }
            // 子弹移动
            bulletsMoveAction();
            // 飞机移动
            aircraftsMoveAction();
            // 道具移动
            supplyMoveAction();
            //导弹移动
            missileMoveAction();
            // 撞击检测
            crashCheckAction();
            // 后处理
            postProcessAction();
            //每个时刻重绘界面
            draw();
            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
//                if(musicOpenFlag){
//                    MusicThread musicThread2=new MusicThread("src\\edu\\hitsz\\music\\game_over.wav",false,OTHER_MUSIC);
//                    musicThread2.start();
//                }
                gameOverFlag = true;
                System.out.println("Game Over!");
//                synchronized (lock){
//                    lock.notifyAll();
//                }
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);


    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudgeOfShootAction() {
        cycleTimeOfShootAction += timeInterval;
        if (cycleTimeOfShootAction >= cycleDurationOfShootAction && cycleTimeOfShootAction - timeInterval < cycleTimeOfShootAction) {
            // 跨越到新的周期
            cycleTimeOfShootAction %= cycleDurationOfShootAction;
            return true;
        } else {
            return false;
        }
    }

    private boolean timeCountAndNewCycleJudgeOfEnemyGeneration(){
        cycleTimeOfEnemyGeneration+=timeInterval;
        if (cycleTimeOfEnemyGeneration >= cycleDurationOfEnemyGeneration && cycleTimeOfEnemyGeneration - timeInterval < cycleTimeOfEnemyGeneration) {
            // 跨越到新的周期
            cycleTimeOfEnemyGeneration %= cycleDurationOfEnemyGeneration;
            return true;
        } else {
            return false;
        }
    }
    private void shootAction() {
        // TODO 敌机射击
        for(AbstractAircraft enemy:enemyAircrafts) {
            enemyBullets.addAll(enemy.shoot());
        }
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private void bulletsMoveAction() {
        for (Basebullet bullet : heroBullets) {
            bullet.forward();
        }
        for (Basebullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void supplyMoveAction() {
        for (AbstractSupply supply : supplies) {
            supply.forward();
        }
    }

    private void missileMoveAction(){
        for(Basebullet missile:missiles){
            missile.forward();
        }
    }
    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for (Basebullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 敌机撞击到英雄机子弹
                // 敌机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }
        // 英雄子弹攻击敌机
        for (Basebullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
//                    if(musicOpenFlag){
//                        MusicThread musicThread=new MusicThread("src\\edu\\hitsz\\music\\bullet_hit.wav",false,OTHER_MUSIC);
//                        musicThread.start();
//                    }
                    if (enemyAircraft.notValid()) {
                        if(enemyAircraft instanceof BossEnemy){
                            bossAliveFlag=false;
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }
        // 导弹攻击敌机
        for (Basebullet missile : missiles) {
            if (missile.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(missile)) {
                    // 敌机撞击到英雄机子弹
                    // 若敌机为Boss机，扣除100滴血，反之直接击毁
                    if(enemyAircraft instanceof BossEnemy){
                        enemyAircraft.decreaseHp(missile.getPower());
                    }else{
                        enemyAircraft.vanish();
                    }
//                    if(musicOpenFlag){
//                        MusicThread musicThread=new MusicThread("src\\edu\\hitsz\\music\\bullet_hit.wav",false,OTHER_MUSIC);
//                        musicThread.start();
//                    }
                    if (enemyAircraft.notValid()) {
                        if(enemyAircraft instanceof BossEnemy){
                            bossAliveFlag=false;
                        }
                    }
                }
            }
        }
        // Todo: 我方获得道具，道具生效
        for (AbstractSupply supply : supplies) {
            if(supply.notValid()){
                continue;
            }
            if (supply.crash(heroAircraft)||heroAircraft.crash(supply)) {
//                if(musicOpenFlag){
//                    MusicThread musicThread=new MusicThread("src\\edu\\hitsz\\music\\get_supply.wav",false,OTHER_MUSIC);
//                    musicThread.start();
//                }
                supply.benefits(heroAircraft,ifMissileGenerates());
                supply.vanish();
            }
        }
    }
    public int getScore(){
        return score;
    }

    public static boolean getGameOverFlag(){
        return gameOverFlag;
    }

    public static boolean getBossAliveFlag(){
        return bossAliveFlag;
    }

    public static void setBossAliveFlag(boolean flag){bossAliveFlag=flag;}

    public static boolean getmusicOpenFlag(){
        return musicOpenFlag;
    }

    public static List<AbstractAircraft> getEnemyAircrafts(){
        return enemyAircrafts;
    }

    public static List<Basebullet> getEnemyBullets(){
        return enemyBullets;
    }

    public static List<Basebullet> getMissiles(){
        return missiles;
    }

    /**
     * 设置不同难度等级下的不同最大敌机数量
     * @param enemyMaxNumber 敌机数量，分别为4,6,8
     */
    public void setEnemyMaxNumber(int enemyMaxNumber){
        this.enemyMaxNumber=enemyMaxNumber;
    }

    /**
     * 简单模式不产生boss机，而普通模式和困难模式产生boss机
     * @return 游戏里是否会生成boss机，true代表会生成
     */
    public boolean ifBossGenerationFlag(){
        return true;
    }

    /**
     * 难度是否会随时间增加
     */
    public boolean ifGameIsHarder(){
        return true;
    }

    /**
     * boss机是否会不断加血
     */
    public boolean ifBossHpIncrease(){
        return false;
    }

    /**
     * 是否会产生导弹
     */
     public  boolean ifMissileGenerates(){
         return true;
     }

    /**
     * 对于设置的三种机型的血量，速度进行不同模式难度下的更改
     */
    public void setEnemySpeedX(int mobEnemySpeedX,int eliteEnemySpeedX,int bossEnemySpeedX){
        MOB_ENEMY_SPEEDX=mobEnemySpeedX;
        ELITE_ENEMY_SPEEDX=eliteEnemySpeedX;
        BOSS_ENEMY_SPEEDX=bossEnemySpeedX;
    }

    public  void setEnemySpeedY(int mobEnemySpeedY,int eliteEnemySpeedY,int bossEnemySpeedY){
        MOB_ENEMY_SPEEDY=mobEnemySpeedY;
        ELITE_ENEMY_SPEEDY=eliteEnemySpeedY;
        BOSS_ENEMY_SPEEDY=bossEnemySpeedY;
    }

    public  void setEnemyHp(int mobEnemyHp,int eliteEnemyHp,int bossEnemyHp){
        MOB_ENEMY_HP=mobEnemyHp;
        ELITE_ENEMY_HP=eliteEnemyHp;
        BOSS_ENEMY_HP=bossEnemyHp;
    }

    /**
     *设置敌机的产生周期
     */
    public void setCycleDurationOfEnemyGeneration(int cycleDurationOfEnemyGeneration){
        this.cycleDurationOfEnemyGeneration=cycleDurationOfEnemyGeneration;
    }

    /**
     *设置精英敌机的产生概率
     */
    public void setEliteEnemyPossibility(double eliteEnemyPossibility){
        this.eliteEnemyPossibility=eliteEnemyPossibility;
    }

    /**
     *设置道具的掉落概率
     */
    public void setSupplyPossibility(double supplyPossibility){
        this.supplyPossibility=supplyPossibility;
    }

    /**
     *设置boss机产生的最小得分阈值
     */
    public void setMiniumScoreOfBossGeneration(int miniumScoreOfBossGeneration){
        this.miniumScoreOfBossGeneration=miniumScoreOfBossGeneration;
    }
    /**
     * 设置精英敌机产生概率的上限
     */

    public void setEliteEnemyPossibilityMax(double eliteEnemyPossibilityMax) {
        this.eliteEnemyPossibilityMax = eliteEnemyPossibilityMax;
    }

    /**
     * 设置道具掉落概率的下限
     */
    public void setSupplyPossibilityMin(double supplyPossibilityMin) {
        this.supplyPossibilityMin = supplyPossibilityMin;
    }

    /**
     * 对于失效的精英敌机和boss机掉落道具
     * @param enemyAircraft 失效的敌机
     */
    public void addSupply(AbstractAircraft enemyAircraft){
        if ((enemyAircraft instanceof EliteEnemy||enemyAircraft instanceof BossEnemy)&&enemyAircraft.notValid()) {
            supplies.addAll(AbstractSupplyFactory.order(enemyAircraft,supplyPossibility));
        }
    }
    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        missiles.removeIf(AbstractFlyingObject::notValid);
        /**
         * 实现道具的掉落和分数的相加
         */
        for(AbstractAircraft enemyAircraft:enemyAircrafts){
            if(enemyAircraft.notValid()){
                score=enemyAircraft.addScore(score);
            }
            addSupply(enemyAircraft);
        }
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        supplies.removeIf(AbstractFlyingObject::notValid);
    }

    public void draw(){
        canvas=mSurfaceHolder.lockCanvas();
        if(mSurfaceHolder==null || canvas == null){
            return;
        }

        canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE,0,this.backGroundTop- MainActivity.WINDOW_HEIGHT,mPaint);
        canvas.drawBitmap(ImageManager.BACKGROUND_IMAGE,0,this.backGroundTop,mPaint);
        backGroundTop +=1;
        if(backGroundTop == MainActivity.WINDOW_HEIGHT){
            this.backGroundTop =0;
        }
        paintImageWithPositionRevised(canvas, enemyBullets);
        paintImageWithPositionRevised(canvas, heroBullets);
        paintImageWithPositionRevised(canvas, supplies);
        paintImageWithPositionRevised(canvas, enemyAircrafts);
        paintImageWithPositionRevised(canvas, missiles);

        canvas.drawBitmap(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, mPaint);

        //绘制得分和生命值
        paintScoreAndLife(canvas);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }

    public void loading_img()  {
        ImageManager.BACKGROUND_IMAGE = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        ImageManager.BACKGROUND_EASY_IMAGE = BitmapFactory.decodeResource(getResources(),R.drawable.bg2);
        ImageManager.BACKGROUND_SIMPLE_IMAGE = BitmapFactory.decodeResource(getResources(),R.drawable.bg3);
        ImageManager.BACKGROUND_HARD_IMAGE = BitmapFactory.decodeResource(getResources(),R.drawable.bg);

        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(getResources(),R.drawable.hero);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(getResources(),R.drawable.mob);
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(),R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(getResources(),R.drawable.bullet_enemy);
        ImageManager.Elite_ENEMY_IMAGE=BitmapFactory.decodeResource(getResources(),R.drawable.elite);
        ImageManager.Boss_ENEMY_IMAGE=BitmapFactory.decodeResource(getResources(),R.drawable.boss);
        ImageManager.Blood_Supply_IMAGE=BitmapFactory.decodeResource(getResources(),R.drawable.prop_blood);
        ImageManager.Fire_Supply_IMAGE=BitmapFactory.decodeResource(getResources(),R.drawable.prop_fire);
        ImageManager.Bomb_Supply_IMAGE=BitmapFactory.decodeResource(getResources(),R.drawable.prop_bomb);
        ImageManager.MISSILE_IMAGE=BitmapFactory.decodeResource(getResources(),R.drawable.missile);

        ImageManager.CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), ImageManager.HERO_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), ImageManager.MOB_ENEMY_IMAGE);
        ImageManager. CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), ImageManager.Elite_ENEMY_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), ImageManager.Boss_ENEMY_IMAGE);
        ImageManager. CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), ImageManager.HERO_BULLET_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ImageManager.ENEMY_BULLET_IMAGE);
        ImageManager. CLASSNAME_IMAGE_MAP.put(BloodSupply.class.getName(), ImageManager.Blood_Supply_IMAGE);
        ImageManager. CLASSNAME_IMAGE_MAP.put(FireSupply.class.getName(), ImageManager.Fire_Supply_IMAGE);
        ImageManager.CLASSNAME_IMAGE_MAP.put(BombSupply.class.getName(), ImageManager.Bomb_Supply_IMAGE);
        ImageManager. CLASSNAME_IMAGE_MAP.put(Missile.class.getName(), ImageManager.MISSILE_IMAGE);
    }

    private void paintImageWithPositionRevised(Canvas canvas, List<? extends AbstractFlyingObject> objects){
        if(objects.size()==0){
            return;
        }else{
            for(int i=0;i<objects.size();i++){
                Bitmap image=objects.get(i).getImage();

                assert image !=null :objects.getClass().getName()+"has no image";
                canvas.drawBitmap(image,objects.get(i).getLocationX()-image.getWidth()/2,objects.get(i).getLocationY()-image.getHeight()/2,mPaint);
            }
        }
    }

    private void paintScoreAndLife(Canvas canvas) {
        Paint paint=new Paint();
        int x = 50;
        int y = 150;
        paint.setColor(Color.rgb(176,7,14));
        paint.setTextSize(100);
        paint.setTypeface(Typeface.SANS_SERIF);
        canvas.drawText("SCORE:" + this.score, x, y,paint);
        y = y + 100;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(),x, y,paint);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        new Thread(this).start();

    }
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        MainActivity.WINDOW_WIDTH = width;
        MainActivity.WINDOW_HEIGHT = height;
    }
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        gameOverFlag=true;
    }

    @Override
    public void run() {
        action();
    }
}



//***********************
    //      Draw 各部分
    //***********************


