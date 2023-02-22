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
    private int power ;
    private int freq_control;
    private int cnt =0;

    public StraightShootStrategy(int power,int freq_control){
        this.power = power;
        this.freq_control = freq_control;
    }

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
        if(cnt == 0) {
            for (int i = 0; i < shootNum; i++) {
                if (abstractAircraft.getdirection() < 0) {
                    abstractBullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, 0, speedY, power);
                } else {
                    abstractBullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, abstractAircraft.getSpeedX(), speedY, power);
                }
                res.add(abstractBullet);
            }
            if(freq_control!=0) {
                cnt++;
            }
        }
        else if(cnt == freq_control){
             cnt=0;
        }
        else {
            cnt ++;
        }
        return res;
    }
}
