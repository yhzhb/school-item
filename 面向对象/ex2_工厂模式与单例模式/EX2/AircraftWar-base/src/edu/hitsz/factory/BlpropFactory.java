package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.FatherEnemy;
import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.GameProp;

public class BlpropFactory implements PropFactory{
    @Override
    public GameProp createProp(AbstractAircraft abstractAircraft) {
        return new BloodProp(abstractAircraft.getLocationX(), abstractAircraft.getLocationY(), 0,0);
    }
}
