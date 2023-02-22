package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.strategy.RandomShootStrategy;
import edu.hitsz.strategy.ShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {
    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY 英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp    初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.setShootStrategy(new StraightShootStrategy());
        this.direction=-1;
    }

    /**
     * 英雄机的实例及返回英雄机类的唯一实例的方法
     */
    private volatile static  HeroAircraft heroAircraft;


    public static HeroAircraft getHeroAircraft(){
             if (heroAircraft==null){
                 synchronized (HeroAircraft.class) {
                     if (heroAircraft == null) {
                         heroAircraft = new HeroAircraft(Main.WINDOW_WIDTH / 2,
                                 Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                                 0, 0, 700);
                     }
                 }
             }
        return  heroAircraft;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }

}
