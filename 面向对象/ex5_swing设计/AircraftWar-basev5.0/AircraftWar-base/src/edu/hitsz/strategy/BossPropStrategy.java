package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractEnemy;
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

    @Override
    public List<AbstractProp> doGetprop(AbstractEnemy abstractEnemy) {
        List<AbstractProp> res = new LinkedList<>();
        AbstractProp[] abstractprops=new AbstractProp[propnum];
        abstractprops[0] = new BloodProp(abstractEnemy.getLocationX(), abstractEnemy.getLocationY(), 0,8);
        abstractprops[1] = new BombProp(abstractEnemy.getLocationX(),abstractEnemy.getLocationY(),abstractEnemy.getSpeedX(),8);
        abstractprops[2] = new BulletProp(abstractEnemy.getLocationX(),abstractEnemy.getLocationY(),abstractEnemy.getSpeedX()*(-1),8);

        res.add(abstractprops[0]);
        res.add(abstractprops[1]);
        res.add(abstractprops[2]);

        return res;
    }
}
