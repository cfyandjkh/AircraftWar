package edu.hitsz.aircraft;



import com.example.aircraftwar.MainActivity;

import edu.hitsz.shoot.InterfaceShoot;

public class BossEnemy extends AbstractAircraft {
    public BossEnemy(int  locationX, int locationY, int speedX, int speedY, int hp,InterfaceShoot shoot){
        super(  locationX,  locationY,  speedX,  speedY,  hp,shoot);
        setDirection(1);
        setPower(10);
        setShootNum(3);
    }
    @Override
    public void forward() {
        super.forward();
        if (locationY >= MainActivity.WINDOW_HEIGHT ) {
            vanish();
        }
    }

    @Override
    public int addScore(int score){
        return (score+20);
    }

}
