package edu.hitsz.bullet;

import com.example.aircraftwar.MainActivity;

import edu.hitsz.application.ImageManager;

import java.util.LinkedList;
import java.util.List;

public class MissileFactory {
    public List<Basebullet> missileCreate(){
        List<Basebullet> res=new LinkedList<>();
        res.add(new Missile(ImageManager.MISSILE_IMAGE.getWidth()/2, MainActivity.WINDOW_HEIGHT-ImageManager.MISSILE_IMAGE.getHeight()/2,0,-20,100));
        res.add(new Missile(MainActivity.WINDOW_WIDTH/2, MainActivity.WINDOW_HEIGHT-ImageManager.MISSILE_IMAGE.getHeight()/2,0,-20,100));
        res.add(new Missile(MainActivity.WINDOW_WIDTH-ImageManager.MISSILE_IMAGE.getWidth()/2, MainActivity.WINDOW_HEIGHT-ImageManager.MISSILE_IMAGE.getHeight()/2,0,-20,100));
        return res;
    }
}
