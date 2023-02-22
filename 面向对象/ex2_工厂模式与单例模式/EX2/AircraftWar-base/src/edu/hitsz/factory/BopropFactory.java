package edu.hitsz.factory;


import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.FatherEnemy;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.GameProp;

public class BopropFactory implements  PropFactory{
    public GameProp createProp(AbstractAircraft abstractAircraft) {
        return  new BombProp(abstractAircraft.getLocationX(), abstractAircraft.getLocationY(), 0,0);
    }
}
