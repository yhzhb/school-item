package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.LinkedList;
import java.util.List;

public class RandomShootStrategy implements ShootStrategy {
    private int shootNum = 3;
    private int power = 30;

    /**
     * 散射子弹
     * @param abstractAircraft
     * @return
     */
    @Override
    public List<BaseBullet> doShoot(AbstractAircraft abstractAircraft)
    {
        List<BaseBullet> res = new LinkedList<>();
        int x = abstractAircraft.getLocationX();
        int y = abstractAircraft.getLocationY() + abstractAircraft.getdirection()*2;
        int speedY =abstractAircraft.getSpeedY() + abstractAircraft.getdirection()*8;
        BaseBullet[] abstractBullet=new BaseBullet[shootNum];
        if(abstractAircraft.getdirection()>0) {
            abstractBullet[0] = new EnemyBullet(x, y + 2, abstractAircraft.getSpeedX(), speedY, power);
            abstractBullet[1] = new EnemyBullet(x, y + 2, abstractAircraft.getSpeedX() - 2, speedY, power);
            abstractBullet[2] = new EnemyBullet(x, y + 2, abstractAircraft.getSpeedX() + 2, speedY, power);
        }
        else
        {
            abstractBullet[0] = new HeroBullet(x, y + 2, abstractAircraft.getSpeedX(), speedY, power);
            abstractBullet[1] = new HeroBullet(x, y + 2, abstractAircraft.getSpeedX() - 2, speedY, power);
            abstractBullet[2] = new HeroBullet(x, y + 2, abstractAircraft.getSpeedX() + 2, speedY, power);
        }
        res.add(abstractBullet[0]);
        res.add(abstractBullet[1]);
        res.add(abstractBullet[2]);
        return res;
    }
}
