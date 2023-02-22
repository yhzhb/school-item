package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.prop.AbstractProp;

public interface PropFactory {
    public abstract AbstractProp createProp(AbstractAircraft abstractAircraft);
}
