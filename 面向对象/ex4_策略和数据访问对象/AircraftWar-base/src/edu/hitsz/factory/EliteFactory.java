package edu.hitsz.factory;

import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class EliteFactory implements EnemyFactory{
    private double seed;
    @Override
    public AbstractEnemy createEnemy(){
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
        return new EliteEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())) * 1,
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2) * 1,
                2*speedXdirection,
                10,
                60);
    }
}
