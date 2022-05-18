package edu.hitsz.bullet;

import com.example.aircraftwar.MainActivity;

import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.subscribe.Subscriber;

/**
 * 子弹类。
 * 也可以考虑不同类型的子弹
 *
 * @author hitsz
 */
public class Basebullet extends AbstractFlyingObject implements Subscriber {

    private int power = 10;

    public Basebullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= MainActivity.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= MainActivity.WINDOW_HEIGHT ) {
            // 向下飞行出界
            vanish();
        }else if (locationY <= 0){
            // 向上飞行出界
            vanish();
        }
    }

    public int getPower() {
        return power;
    }

    @Override
    public void update(){
        if(isValid&&this instanceof EnemyBullet){
            vanish();
        }
    }
}
