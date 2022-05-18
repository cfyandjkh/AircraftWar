package edu.hitsz.aircraft;

import edu.hitsz.application.Game;
import edu.hitsz.bullet.Basebullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.shoot.InterfaceShoot;
import edu.hitsz.subscribe.Subscriber;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *AbstractAircraft没有抽象方法但为抽象类，抽象类不一定有抽象方法，但是有抽象方法的一定是抽象类
 * @author hitsz
 */
public  abstract class AbstractAircraft extends AbstractFlyingObject implements Subscriber {
    /**
     * 生命值
     */
    protected int maxHp=100;
    protected int hp;
    protected InterfaceShoot shoot;
    protected int shootNum;
    protected  int direction;
    protected int power;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp, InterfaceShoot shoot) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.shoot=shoot;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0){
            hp=0;
            vanish();
        }
    }
    //加血道具来加血
    public void increaseHp(int increase){
        if((hp+increase)<=maxHp) {
            hp += increase;
        }else {
            hp=maxHp;
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp){
        this.hp=hp;
    }

    public void setShootNum(int shootNum){
        this.shootNum=shootNum;
    }

    public void setDirection(int direction){
        this.direction=direction;
    }

    public void setPower(int power){
        this.power=power;
    }

    public int getShootNum(){
        return shootNum;
    }

    public int getPower(){
        return power;
    }

    public int getDirection(){
        return direction;
    }
    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     *  可射击对象需实现，返回子弹,list<Basebullet>表示子弹类及其子类
     *  非可射击对象空实现，返回null
     */
    public List<Basebullet> shoot(){
        return shoot.execute(this);
    }
    //敌机阵亡后游戏加多少分数。
    public abstract int addScore(int score);
    @Override
    public void update(){
        if(isValid){
            if(this instanceof BossEnemy){
                decreaseHp(100);
                if(((BossEnemy) this).notValid()){
                    Game.setBossAliveFlag(false);
                }
            }else{
                vanish();
            }
        }
    }
}


