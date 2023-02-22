package edu.hitsz.aircraft;

import edu.hitsz.basic.Exploed;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.strategy.GetPropStrategy;

import java.util.List;

public abstract  class AbstractEnemy extends AbstractAircraft implements Exploed{


    private GetPropStrategy getPropStrategy;
    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp,int  shoot_freq) {
        super(locationX, locationY, speedX, speedY, hp,shoot_freq);
    }

    /**
     * 产生道具方法，策略模式
     */

    public void setGetPropStrategy(GetPropStrategy getPropStrategy) {
        this.getPropStrategy = getPropStrategy;
    }
    public  List<AbstractProp> getProp(){
        return getPropStrategy.doGetprop(this);
    };

    @Override
    public void update() {

    }
}
