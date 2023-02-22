package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Game;
import edu.hitsz.basic.AbstractFlyingObject;

public class BulletProp extends GameProp{
        public  BulletProp (int locationX ,int locationY, int speedX, int speedY)
        {
            super(locationX, locationY, speedX, speedY);
        }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        System.out.println("FireSupply active!");
    }
}
