package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.prop.AbstractProp;

import java.util.List;

public interface GetPropStrategy {
    List<AbstractProp> doGetprop(AbstractEnemy abstractEnemy);
}
