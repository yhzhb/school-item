package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.GameProp;

import java.util.List;

public class BossEnemy extends FatherEnemy{

    private BossEnemy bossEnemy;
    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }



    @Override
    public List<BaseBullet> shoot() {
        return null;
    }

    @Override
    public GameProp getProp() {
        return null;
    }


}
