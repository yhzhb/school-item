package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.strategy.RandomShootStrategy;
import edu.hitsz.strategy.StraightShootStrategy;

public class BulletProp extends AbstractProp {
        private int cnt=0;
        private Thread thread;
        private Thread lathread;//上一个启动的线程
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
         Runnable addfire = () -> {
            try {
                heroAircraft.setShootStrategy(new RandomShootStrategy((int) (30 * (cnt*0.2+1)),0));
                Thread.sleep(10000);
                heroAircraft.setShootStrategy(new StraightShootStrategy((int) (30 * (cnt*0.2+1)),0));
                System.out.println("Bullet num supply disappear!");
            } catch (Exception e) {
                e.printStackTrace();
            }
             };

             if(cnt ==0) {
                   thread = new Thread(addfire, "Supply");
                   thread.start();
                    System.out.println("Firesupply active! Both power and bulletnum up! 子弹数量强化持续10秒。");
               }
               else {
                   if(lathread.isAlive()){
                       System.out.println("子弹数目已强化，只强化伤害！"+"当前子弹伤害："+(int)(30*(cnt*0.2+1)));
                   }else {
                       thread = new Thread(addfire, "Supply");
                       thread.start();
                       System.out.println("Firesupply active! Both power and num up!子弹数量强化持续10秒。");
                   }
               }

           // new Thread(addfire).start();
            //heroAircraft.setShootStrategy(new RandomShootStrategy());

    }

    public void  setCnt(int cnt){
        this.cnt = cnt;
    }

    public void setThread(Thread thread){
        lathread = thread;
    }

    public Thread getThread(){
        return  thread;
    }
}
