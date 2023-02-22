package edu.hitsz.aircraft;

import edu.hitsz.strategy.BossPropStrategy;
import edu.hitsz.strategy.RandomShootStrategy;



public class BossEnemy extends AbstractEnemy {

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp,int shoot_freq) {
        super(locationX, locationY, speedX, speedY, hp,shoot_freq);
        this.setGetPropStrategy(new BossPropStrategy());
        this.setShootStrategy(new RandomShootStrategy(20,(600/shoot_freq)-1));
        this.direction=1;

    }




    @Override
    public void update(){
        if(hp>100) {
            this.decreaseHp(100);
        }
        else
        {
            hp =1;
        }
    }
}
