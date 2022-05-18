package edu.hitsz.supply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;

public class BloodSupply extends AbstractSupply{
    public BloodSupply(int locationX,int locationY,int speedX,int speedY){
        super(locationX,locationY,speedX,speedY);
    }
    @Override
    public void benefits(HeroAircraft heroAircraft,boolean ifMissileGenerates){
        heroAircraft.increaseHp(10);
    }
}
