package edu.hitsz.supply;

import edu.hitsz.aircraft.AbstractAircraft;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;

/**
 * 所有道具工厂的父类
 * @author hitsz
 */
public abstract class AbstractSupplyFactory {

    private static int rd;
    private static int limit;
    /**
     * 为避免返回空指针，采用同子弹生成的方式生成道具（采用表List）
     * @return 返回一个列表List<AbstractSupply>
     */
    public static List<AbstractSupply> order(AbstractAircraft enemyAircraft,double supplyPossibility){
        List<AbstractSupply> supply=new LinkedList<>();
        rd=new Random().nextInt(100);
        limit=(int)(supplyPossibility*100-1);
            if (rd <=limit/3) {
                supply .add(new BloodSupplyFactory().create(enemyAircraft.getLocationX(),enemyAircraft.getLocationY()));
            } else if (limit/3<rd&&rd<=(2*limit)/3) {
                supply .add(new FireSupplyFactory().create(enemyAircraft.getLocationX(),enemyAircraft.getLocationY()));
            } else if ((2*limit)/3<rd&&rd<=limit) {
                supply.add(new BombSupplyFactory().create(enemyAircraft.getLocationX(),enemyAircraft.getLocationY())) ;
            }
            return supply;
    }

    /**
     * 抽象方法create用以创建道具
     * @param locationX 传入的精英敌机或boss机的横坐标
     * @param locationY 传入的精英敌机或boss机的纵坐标
     * @return 抽象道具
     */
    public abstract AbstractSupply create(int locationX,int locationY);
}
