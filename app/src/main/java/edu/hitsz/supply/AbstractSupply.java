package edu.hitsz.supply;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.aircraft.HeroAircraft;
public abstract class AbstractSupply extends AbstractFlyingObject{
    //重写每种道具的效果,抽象方法没有方法体
    public abstract void benefits(HeroAircraft heroAircraft,boolean ifMissileGenerates);
    //构造方法同父类
    public AbstractSupply(int locationX,int locationY,int speedX,int speedY){
        super(locationX,locationY,speedX,speedY);
    }
}
