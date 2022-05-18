package edu.hitsz.aircraft;
import com.example.aircraftwar.MainActivity;

import edu.hitsz.application.ImageManager;
import edu.hitsz.shoot.StraightShoot;

public class MobEnemyFactory extends AbstractAircraftFactory {
    @Override
    public AbstractAircraft create(int speedX,int speedY,int hp){
        return new MobEnemy((int) (Math.random() * (MainActivity.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
                (int) (Math.random() * MainActivity.WINDOW_HEIGHT * 0.2) * 1,
                speedX,
                speedY,
                hp,
                new StraightShoot());
    }
}
