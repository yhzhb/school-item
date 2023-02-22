package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.Exploed;
import edu.hitsz.basic.MusicThread;

import java.util.ArrayList;
import java.util.List;

public class BombProp extends AbstractProp {

    private List<Exploed>  exploedthings = new ArrayList<>();
    public  BombProp (int locationX ,int locationY, int speedX, int speedY)
    {
        super(locationX, locationY, speedX, speedY);

    }

    @Override
    public void effect(HeroAircraft heroAircraft) {
        new MusicThread("src/videos/bomb_explosion.wav").start();
        for(Exploed exploed:exploedthings){
            exploed.update();
        }
        System.out.println("BombSupply active!");
    }

    public void addMember(Exploed exploed){
        exploedthings.add(exploed);
    }


}
