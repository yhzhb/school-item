package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;

public interface EnemyFactory {
      AbstractEnemy createEnemy(double Strong,int shoot_freq);
}
