package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class BombProp extends GameProp{

    public  BombProp (int locationX ,int locationY, int speedX, int speedY)
    {
        super(locationX, locationY, speedX, speedY);

    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active!");
    }


}
