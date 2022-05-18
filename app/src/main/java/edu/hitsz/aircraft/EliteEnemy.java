package edu.hitsz.aircraft;


import com.example.aircraftwar.MainActivity;

import edu.hitsz.shoot.InterfaceShoot;

/**
 * @author hitsz
 */
public class EliteEnemy extends AbstractAircraft {
    /**
     * direction表示子弹发射方向
     */
    /**
     * shootNum为子弹数量
     */
    /**
     * power为子弹伤害
     */

    /**
     * speed为敌机移动速度
     * @param locationX 精英机横坐标
     * @param locationY 精英机纵坐标
     * @param speedX 精英机横向速度
     * @param speedY 精英机纵向速度
     * @param hp 精英机初始生命值
     */
    public EliteEnemy(int  locationX, int locationY, int speedX, int speedY, int hp,InterfaceShoot shoot){
        super(  locationX,  locationY,  speedX,  speedY,  hp,shoot);
        setDirection(1);
        setPower(10);
        setShootNum(1);
    }
    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.WINDOW_HEIGHT ) {
            vanish();
        }
    }
    @Override
    public int addScore(int score){
        return (score+10);
    }
}
