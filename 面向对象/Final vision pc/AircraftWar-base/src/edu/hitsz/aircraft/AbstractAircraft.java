package edu.hitsz.aircraft;

import edu.hitsz.basic.Exploed;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.strategy.ShootStrategy;

import java.util.List;

/**
 * 所有种类飞机的抽象父类：
 * 敌机（BOSS, ELITE, MOB），英雄飞机
 *
 * @author hitsz
 */
public abstract class AbstractAircraft extends AbstractFlyingObject  {
    /**
     * 生命值
     */
    protected int maxHp;
    protected int hp;
    protected int direction;
    protected int shoot_freq;
    private ShootStrategy shootStrategy;

    public AbstractAircraft(int locationX, int locationY, int speedX, int speedY, int hp,int shoot_freq) {
        super(locationX, locationY, speedX, speedY);
        this.hp = hp;
        this.maxHp = hp;
        this.shoot_freq = shoot_freq;
    }

    public void decreaseHp(int decrease){
        hp -= decrease;
        if(hp <= 0) {
            hp = 0;
            vanish();
        }
    }

    public int getHp() {
        return hp;
    }

    public int getdirection() {
        return direction;
    }


    /**
     *策略模式实现射击
     */


    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public  List<BaseBullet> shoot()
    {
        return shootStrategy.doShoot(this);
    }

}


