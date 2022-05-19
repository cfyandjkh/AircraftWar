package edu.hitsz.supply;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;

import edu.hitsz.bullet.Basebullet;
import edu.hitsz.subscribe.Subscriber;

import java.util.ArrayList;
import java.util.List;

public class BombSupply extends AbstractSupply {
    private static ArrayList<Subscriber> subscribers= new ArrayList<>();
    public BombSupply(int locationX,int locationY,int speedX,int speedY){
        super(locationX,locationY,speedX,speedY);
    }
    @Override
    public void  benefits(HeroAircraft heroAircraft,boolean ifMissileGenerates){
        //-1表示是其他类型音乐。
//        if(Game.getmusicOpenFlag()){
//            MusicThread musicThread=new MusicThread("src\\edu\\hitsz\\edu.hitsz.music\\bomb_explosion.wav",false,-1);
//            musicThread.start();
//        }
        System.out.println("BombSupply active");
        postProcessAction(Game.getEnemyAircrafts(),Game.getEnemyBullets());
        announceAll();
    }

    public void announceAll(){
        for(Subscriber subscriber:subscribers){
            subscriber.update();
        }
    }

    /**
     * 每次炸弹生效前对订阅列表更新处理
     * @param enemyAircrafts 敌机类
     * @param enemyBullets 子弹类
     */
    public static void postProcessAction(List<AbstractAircraft> enemyAircrafts, List<Basebullet> enemyBullets){
        subscribers.clear();
        subscribers.addAll(enemyAircrafts);
        subscribers.addAll(enemyBullets);
    }
}
