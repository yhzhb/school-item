package edu.hitsz.factory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossFactory implements EnemyFactory{
    private double seed;
    @Override
    public AbstractEnemy createEnemy(double Strong,int shoot_freq){
        seed = Math.random();
        int speedXdirection;
        if (seed < 0.5)
        {
            speedXdirection =1;
        }
        else
        {
            speedXdirection =-1;
        }
        return new BossEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth())) * 1,
                ImageManager.BOSS_ENEMY_IMAGE.getHeight()/2,
                1*speedXdirection,
                0,
                (int) (600*Strong),
                shoot_freq
        );
    }
}
