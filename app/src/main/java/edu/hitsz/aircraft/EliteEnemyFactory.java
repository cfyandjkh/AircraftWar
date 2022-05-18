package edu.hitsz.aircraft;

import com.example.aircraftwar.MainActivity;

import edu.hitsz.application.ImageManager;
import edu.hitsz.shoot.StraightShoot;

import java.util.Random;

public class EliteEnemyFactory extends AbstractAircraftFactory{
    @Override
    public AbstractAircraft create(int speedX,int speedY,int hp){
        return new EliteEnemy((int) (Math.random() * (MainActivity.WINDOW_WIDTH - ImageManager.Elite_ENEMY_IMAGE.getWidth())) * 1,
                (int) (Math.random() * MainActivity.WINDOW_HEIGHT * 0.2) * 1,
                new Random().nextInt(2)==1?speedX:-speedX,
                speedY,
                hp,
                new StraightShoot());
    }
}
