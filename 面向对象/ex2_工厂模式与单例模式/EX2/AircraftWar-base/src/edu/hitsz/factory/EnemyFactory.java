package edu.hitsz.factory;

import edu.hitsz.aircraft.FatherEnemy;

public interface EnemyFactory {
    public abstract FatherEnemy createEnemy();
}
