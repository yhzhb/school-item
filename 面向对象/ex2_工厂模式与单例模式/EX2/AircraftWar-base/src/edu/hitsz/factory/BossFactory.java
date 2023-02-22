package edu.hitsz.factory;

import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.FatherEnemy;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossFactory implements EnemyFactory{
    public FatherEnemy createEnemy(){
        return new BossEnemy((int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
                (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2) * 1,
                0,
                10,
                30);
    }
}
