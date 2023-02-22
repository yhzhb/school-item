package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import edu.hitsz.prop.BloodProp;
import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;
import edu.hitsz.prop.GameProp;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class EliteEnemy extends AbstractAircraft{

    /**攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

    /**
     * 子弹伤害
     */
    private int power = 30;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction = 1;

    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }


    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT ) {
            vanish();
        }
    }


    @Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction*2;
        int speedX = 0;
        int speedY =this.getSpeedY() + direction*5;
        BaseBullet abstractBullet;
        for(int i=0; i<shootNum; i++){
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            abstractBullet = new EnemyBullet(x + (i*2 - shootNum + 1)*10, y, speedX, speedY, power);

           res.add(abstractBullet);
        }
        return res;
    }



    @Override
    public List<GameProp> birth() {
        Random rand =new Random();
        int randtype =rand.nextInt(5);
        List<GameProp> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() ;
        int speedX = 0;
        int speedY = 0;
        GameProp abstractProp;
        if(randtype<4) {
            switch (randtype) {
                case 1:
                    abstractProp = new BloodProp(x, y, speedX, speedY);
                    res.add(abstractProp);
                    break;
                case 2:
                    abstractProp = new BulletProp(x, y, speedX, speedY);
                    res.add(abstractProp);
                    break;
                case 3:
                    abstractProp=new BombProp(x,y,speedX,speedY);
                    res.add(abstractProp);
                    break;
            }
        }
        return res;
    }

}
