package edu.hitsz.supply;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;
import edu.hitsz.bullet.MissileFactory;
import edu.hitsz.shoot.DisperseShoot;
import edu.hitsz.shoot.InterfaceShoot;
import edu.hitsz.shoot.StraightShoot;

public class FireSupply extends AbstractSupply {
    static private InterfaceShoot disperseShoot =new DisperseShoot();
    static private InterfaceShoot straightShoot =new StraightShoot();
    public FireSupply(int locationX,int locationY,int speedX,int speedY){
        super(locationX,locationY,speedX,speedY);
    }
    @Override
    public void  benefits(HeroAircraft heroAircraft,boolean ifMissileGenerates){
        System.out.println("FireSupply active");
        if(ifMissileGenerates){
            Game.getMissiles().addAll(new MissileFactory().missileCreate());
        }
        heroAircraft.setShoot(disperseShoot);
        heroAircraft.setShootNum(3);
        Runnable task=()->{
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            heroAircraft.setShoot(straightShoot);
            heroAircraft.setShootNum(1);
        };
        Thread thread=new Thread(task);
        thread.start();
    }

}
