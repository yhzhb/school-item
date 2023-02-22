package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.prop.BulletProp;
import edu.hitsz.prop.AbstractProp;

public class BupropFactory implements PropFactory{
    @Override
    public AbstractProp createProp(AbstractAircraft abstractAircraft){
        return new BulletProp(abstractAircraft.getLocationX(), abstractAircraft.getLocationY(), 0,8);
    }
}
