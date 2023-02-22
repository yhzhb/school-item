package edu.hitsz.factory;


import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.AbstractProp;

public class BopropFactory implements  PropFactory{
    @Override
    public AbstractProp createProp(AbstractAircraft abstractAircraft) {
        return  new BombProp(abstractAircraft.getLocationX(), abstractAircraft.getLocationY(), 0,8);
    }
}
