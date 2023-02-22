package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.factory.BlpropFactory;
import edu.hitsz.factory.BopropFactory;
import edu.hitsz.factory.BupropFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;

import java.util.LinkedList;
import java.util.List;

/**
 * boss机生成道具策略
 */
public class BossPropStrategy implements GetPropStrategy{
    /**
     * 一次坠毁产生道具数量
     */
    private int propnum = 3;
    private double seed;
    @Override
    public List<AbstractProp> doGetprop(AbstractEnemy abstractEnemy) {
        List<AbstractProp> res = new LinkedList<>();
        AbstractProp[] abstractprops=new AbstractProp[propnum];

        for(int i=0;i<3;i++) {
            seed = Math.random();
            if (seed < 0.1) {
                abstractprops[i]=new BombProp(abstractEnemy.getLocationX(),abstractEnemy.getLocationY(),(abstractEnemy.getSpeedX()-i*abstractEnemy.getSpeedX()),8);
            } else if (seed < 0.55 && seed > 0.1) {
                abstractprops[i] = new BloodProp(abstractEnemy.getLocationX(),abstractEnemy.getLocationY(),(abstractEnemy.getSpeedX()-i*abstractEnemy.getSpeedX()),8);
            } else {
                abstractprops[i] = new BulletProp(abstractEnemy.getLocationX(),abstractEnemy.getLocationY(),(abstractEnemy.getSpeedX()-i*abstractEnemy.getSpeedX()),8);

            }
            res.add(abstractprops[i]);
        }
        return res;
    }
}
