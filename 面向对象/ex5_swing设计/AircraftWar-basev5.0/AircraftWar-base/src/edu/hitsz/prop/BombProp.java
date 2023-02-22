package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.MusicThread;

public class BombProp extends AbstractProp {

    public  BombProp (int locationX ,int locationY, int speedX, int speedY)
    {
        super(locationX, locationY, speedX, speedY);

    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        new MusicThread("src/videos/bomb_explosion.wav").start();
        System.out.println("BombSupply active!");
    }


}
