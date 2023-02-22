package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.FatherEnemy;
import edu.hitsz.prop.GameProp;

public interface PropFactory {
    public abstract GameProp createProp(AbstractAircraft abstractAircraft);
}
