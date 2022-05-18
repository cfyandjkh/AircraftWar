package edu.hitsz.application;


import android.graphics.Bitmap;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.bullet.Missile;
import edu.hitsz.supply.BloodSupply;
import edu.hitsz.supply.BombSupply;
import edu.hitsz.supply.FireSupply;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author hitsz
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Bitmap BACKGROUND_IMAGE;
    public static Bitmap BACKGROUND_EASY_IMAGE;
    public static Bitmap BACKGROUND_SIMPLE_IMAGE;
    public static Bitmap BACKGROUND_HARD_IMAGE;
    public static Bitmap HERO_IMAGE;
    public static Bitmap HERO_BULLET_IMAGE;
    public static Bitmap ENEMY_BULLET_IMAGE;
    public static Bitmap MOB_ENEMY_IMAGE;
    public static Bitmap Elite_ENEMY_IMAGE;
    public static Bitmap Boss_ENEMY_IMAGE;
    public static Bitmap Blood_Supply_IMAGE;
    public static Bitmap Fire_Supply_IMAGE;
    public static Bitmap Bomb_Supply_IMAGE;
    public static Bitmap MISSILE_IMAGE;

    static {

        CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
        CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EliteEnemy.class.getName(), Elite_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(), Boss_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BloodSupply.class.getName(), Blood_Supply_IMAGE);
        CLASSNAME_IMAGE_MAP.put(FireSupply.class.getName(), Fire_Supply_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BombSupply.class.getName(), Bomb_Supply_IMAGE);
        CLASSNAME_IMAGE_MAP.put(Missile.class.getName(), MISSILE_IMAGE);

    }

    public static Bitmap get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Bitmap get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }

}
