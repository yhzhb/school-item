package edu.hitsz.aircraft;

import edu.hitsz.strategy.BossPropStrategy;
import edu.hitsz.strategy.RandomShootStrategy;


public class BossEnemy extends AbstractEnemy {
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
        this.setGetPropStrategy(new BossPropStrategy());
        this.setShootStrategy(new RandomShootStrategy());
        this.direction=1;
    }
}
