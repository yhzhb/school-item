package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class StraightShootStrategy implements  ShootStrategy{
    /**
     * 子弹数量
     */
    private int shootNum = 1;
    private int power = 30;

    /**
     * 直射子弹，通过direction子弹发射方向判断敌机或英雄机
     * @param abstractAircraft
     * @return
     */
    @Override
    public List<BaseBullet> doShoot(AbstractAircraft abstractAircraft){
        List<BaseBullet> res = new LinkedList<>();
        int x = abstractAircraft.getLocationX();
        int y = abstractAircraft.getLocationY() + abstractAircraft.getdirection()*2;
        int speedY = abstractAircraft.getSpeedY() + abstractAircraft.getdirection()*5;
        BaseBullet abstractBullet;
        for(int i=0; i<shootNum; i++){
            if(abstractAircraft.getdirection()<0) {
                abstractBullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, 0, speedY, power);
            }
            else
            {
                abstractBullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, abstractAircraft.getSpeedX(), speedY, power-15);
            }
            res.add(abstractBullet);
        }
        return res;
    }
}
