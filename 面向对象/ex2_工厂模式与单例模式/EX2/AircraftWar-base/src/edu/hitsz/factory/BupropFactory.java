package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.FatherEnemy;
import edu.hitsz.prop.BulletProp;
import edu.hitsz.prop.GameProp;

public class BupropFactory implements PropFactory{
    public GameProp createProp(AbstractAircraft abstractAircraft){
        return new BulletProp(abstractAircraft.getLocationX(), abstractAircraft.getLocationY(), 0,0);
    }
}
