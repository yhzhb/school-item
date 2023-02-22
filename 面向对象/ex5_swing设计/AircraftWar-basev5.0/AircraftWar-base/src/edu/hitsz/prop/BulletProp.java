package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.RandomShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

public class BulletProp extends AbstractProp {
        public  BulletProp (int locationX ,int locationY, int speedX, int speedY)
        {
            super(locationX, locationY, speedX, speedY);
        }

    /**
     * 线程休眠控制子弹发射状态
     * @param heroAircraft
     */
    @Override
    public void effect(HeroAircraft heroAircraft) {

            Runnable addfire =()->{
                  try{
                      heroAircraft.setShootStrategy(new RandomShootStrategy());
                      Thread.sleep(20000);
                      heroAircraft.setShootStrategy(new StraightShootStrategy());
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
            };

            new Thread(addfire).start();
            System.out.println("FireSupply active!");
            //heroAircraft.setShootStrategy(new RandomShootStrategy());
    }
}
