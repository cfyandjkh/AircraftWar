package edu.hitsz.aircraft;

import com.example.aircraftwar.MainActivity;

import edu.hitsz.application.ImageManager;
import edu.hitsz.shoot.DisperseShoot;

import java.util.Random;

public class BossEnemyFactory extends AbstractAircraftFactory{
    @Override
    public AbstractAircraft create(int speedX,int speedY,int hp){
        return new BossEnemy((int) (Math.random() * (MainActivity.WINDOW_WIDTH - ImageManager.Boss_ENEMY_IMAGE.getWidth())) * 1,
                20,
                new Random().nextInt(2)==1?speedX:-speedX,
                speedY,
                hp,
                new DisperseShoot());
    }
}
