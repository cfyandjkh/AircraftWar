package edu.hitsz.aircraft;

import com.example.aircraftwar.MainActivity;

import edu.hitsz.bullet.Basebullet;
import edu.hitsz.shoot.InterfaceShoot;

import java.util.LinkedList;
import java.util.List;

/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractAircraft {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp,InterfaceShoot shoot) {
        super(locationX, locationY, speedX, speedY, hp,shoot);
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
    public List<Basebullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public int addScore(int score){
        return (score+5);
    }
}
