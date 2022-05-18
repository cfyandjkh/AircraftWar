package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.Basebullet;

import java.util.List;

public interface InterfaceShoot {
    List<Basebullet> execute(AbstractAircraft aircraft);
}
