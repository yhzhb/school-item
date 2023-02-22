package edu.hitsz.aircraft;

import edu.hitsz.application.Main;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.RandomPropStrategy;
import edu.hitsz.strategy.StraightShootStrategy;


import java.util.LinkedList;
import java.util.List;


public class EliteEnemy extends AbstractEnemy {

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp,int shoot_freq) {
        super(locationX, locationY, speedX, speedY, hp,shoot_freq);
        this.setShootStrategy(new StraightShootStrategy(15,(int)(600/shoot_freq)-1));
        this.direction= 1;
        this.setGetPropStrategy(new RandomPropStrategy());
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }


    @Override
    public void update(){
       this.vanish();
    }

 /**
  *精英敌机产生道具
 *
    @Override
    public List<AbstractProp> getProp() {
        List<AbstractProp> res = new LinkedList<>();
        if (getpropFactory() != null) {
            res.add(getpropFactory().createProp(this));
        }
        return res;
    }
    */
}
