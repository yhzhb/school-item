package edu.hitsz.application;

import edu.hitsz.factory.BossFactory;

import java.awt.*;

public class NormalGame extends AbstractGame{

    public NormalGame() {
        this.Bossflag = false;
        this.Bossvalue = 500;
        this.step = 500;
        this.timeStep = 9000;
    }
    @Override
    public void paintbg(Graphics g) {

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE_NORMAL, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE_NORMAL, 0, this.backGroundTop, null);

    }

    @Override
    public void Bossharder(){
        if(this.cycleDuration>400){
            this.cycleDuration = this.cycleDuration -20;
        }
        if(enemyMaxNumber<7){
            this.enemyMaxNumber =this.enemyMaxNumber+1;
        }
        this.gamecot = this.gamecot + 0.01;
    }


    @Override
    public void Enemyharder(){
        if(Eliteapp < 0.5) {
            this.Eliteapp = this.Eliteapp + 0.01;
        }
    }

}
