package edu.hitsz.factory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossFactory implements EnemyFactory{
    @Override
    public AbstractEnemy createEnemy(){
        return new BossEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth())) * 1,
                ImageManager.BOSS_ENEMY_IMAGE.getHeight()/2,
                1,
                0,
                600);
    }
}
