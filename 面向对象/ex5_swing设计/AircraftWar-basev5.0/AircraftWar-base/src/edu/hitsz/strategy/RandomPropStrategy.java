package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.factory.BlpropFactory;
import edu.hitsz.factory.BopropFactory;
import edu.hitsz.factory.BupropFactory;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;


import java.util.LinkedList;
import java.util.List;

public class RandomPropStrategy implements GetPropStrategy{

    private double seed;

    /**
     * 随机产生单个道具方式
     * @param abstractEnemy
     * @return
     */
    @Override
    public List<AbstractProp> doGetprop(AbstractEnemy abstractEnemy){
        List<AbstractProp> props = new LinkedList<>();
        PropFactory propFactory=null;
        seed = Math.random();
        if (seed < 0.2) {
            propFactory=new BopropFactory();
        } else if (seed < 0.4&&seed>0.2) {
            propFactory=new BupropFactory();
        } else if (seed < 0.6&&seed>0.4) {
            propFactory =new BlpropFactory();
        }
        if (propFactory!=null) {
            props.add(propFactory.createProp(abstractEnemy));
        }
         return props;
    }
}
