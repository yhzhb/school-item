package edu.hitsz.prop;


import edu.hitsz.aircraft.HeroAircraft;

public class BloodProp extends GameProp {


    public BloodProp(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);

    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
            heroAircraft.decreaseHp(-40);
            System.out.println("BloodSupply active!");
    }

}
