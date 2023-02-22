package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.BlpropFactory;
import edu.hitsz.factory.BopropFactory;
import edu.hitsz.factory.BupropFactory;
import edu.hitsz.factory.PropFactory;
import edu.hitsz.prop.AbstractProp;

import java.util.List;
import java.util.Random;

public abstract  class AbstractEnemy extends AbstractAircraft {


    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    /**
     * 飞机射击方法，可射击对象必须实现
     * @return
     *  可射击对象需实现，返回子弹
     *  非可射击对象空实现，返回null
     */
    @Override
    public abstract List<BaseBullet> shoot();

    /**
     * 产生道具方法，可实现对象必须实现
     *  可产生对象需实现，返回道具
     *  非可产生对象空实现，返回null
     */
    public abstract AbstractProp getProp();

    /**
     * 随机构建道具工厂，允许子类调用
      */
    Random rand =new Random();
    private int randtype =rand.nextInt(10);
    protected  PropFactory propFactory=null;
    protected  PropFactory getpropFactory(){
        if(randtype<4){
            switch (randtype){
                case 1:
                    propFactory =new BlpropFactory();
                    break;
                case 2:
                    propFactory=new BupropFactory();
                    break;
                case 3:
                    propFactory =new BopropFactory();
                    break;
                default:propFactory=null;
            }
        }
      return propFactory;
    }
}
