package edu.hitsz.aircraft;

import com.example.aircraftwar.MainActivity;

import edu.hitsz.application.ImageManager;
import edu.hitsz.shoot.InterfaceShoot;
import edu.hitsz.shoot.StraightShoot;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */

    /**
     * 子弹伤害
     */
    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    /**
     * 生成heroaircraft
     */
    static private HeroAircraft heroAircraft;

    /**
     * 构造方式private，符合单例准则
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp,InterfaceShoot shoot) {
        super(locationX, locationY, speedX, speedY, hp,shoot);
        setDirection(-1);
        setPower(30);
        setShootNum(1);
    }

    /**
     * 设置全局静态调用方法getInstance
     */
    public static HeroAircraft getInstance(){
        if(heroAircraft==null){
            synchronized (HeroAircraft.class){
                if(heroAircraft==null) {
                    heroAircraft=new HeroAircraft(
                            MainActivity.WINDOW_WIDTH / 2,
                            MainActivity.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight() ,
                            0, 0, 100,new StraightShoot());
                }
            }
        }
        return heroAircraft;
    }
    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

    public void setShoot(InterfaceShoot shoot){
        this.shoot=shoot;
    }

    @Override
    public int addScore(int score){return 0;}

}
