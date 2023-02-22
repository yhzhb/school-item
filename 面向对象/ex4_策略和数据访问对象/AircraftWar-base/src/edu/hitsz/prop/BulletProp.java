package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.RandomShootStrategy;

public class BulletProp extends AbstractProp {
        public  BulletProp (int locationX ,int locationY, int speedX, int speedY)
        {
            super(locationX, locationY, speedX, speedY);
        }

    @Override
    public void effect(HeroAircraft heroAircraft) {
            heroAircraft.setShootStrategy(new RandomShootStrategy());
            System.out.println("FireSupply active!");
    }
}
