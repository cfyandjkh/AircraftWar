package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.Basebullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.supply.BombSupply;

import java.util.LinkedList;
import java.util.List;

public class StraightShoot implements InterfaceShoot{
    @Override
    public List<Basebullet> execute(AbstractAircraft aircraft){
        List<Basebullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() +aircraft.getDirection()*2;
        int speedx = 0;
        int speedy = aircraft.getSpeedY() + aircraft.getDirection()*20;
        Basebullet abstractBullet;
        for(int i=0; i<aircraft.getShootNum(); i++){
            if(aircraft instanceof HeroAircraft) {
                abstractBullet = new HeroBullet(x + (i * 2 - aircraft.getShootNum() + 1) * 10, y, speedx, speedy, aircraft.getPower());
            }else{
                abstractBullet = new EnemyBullet(x + (i * 2 - aircraft.getShootNum() + 1) * 10, y, speedx, speedy, aircraft.getPower());
            }
            res.add(abstractBullet);
        }
        return res;
    }
}
