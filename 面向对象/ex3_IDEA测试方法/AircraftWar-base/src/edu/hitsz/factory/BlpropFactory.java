package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.AbstractProp;

public class BlpropFactory implements PropFactory{
    @Override
    public AbstractProp createProp(AbstractAircraft abstractAircraft) {
        return new BloodProp(abstractAircraft.getLocationX(), abstractAircraft.getLocationY(), 0,0);
    }
}
