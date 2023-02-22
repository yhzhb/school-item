package edu.hitsz.application;

import edu.hitsz.factory.BossFactory;

import java.awt.*;

public class HardGame extends AbstractGame{
    public HardGame(){
         this.Bossflag = false;
         this.Bossvalue = 300;
         this.step =300;
         this.timeStep =1800;
    }

    @Override
    public void paintbg(Graphics g) {
        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE_HARD, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE_HARD, 0, this.backGroundTop, null);
    }

    @Override
    public void Bossharder(){
        this.Bosshpcoe = this.Bosshpcoe + Bosscnt* 0.1;
        if(this.cycleDuration>100) {
            this.cycleDuration = this.cycleDuration - 20;
        }
        if(enemyMaxNumber<10) {
            enemyMaxNumber = enemyMaxNumber + 1;
        }
    }


    @Override
    public void Enemyharder(){
        this.gamecot = this.gamecot + 0.01;
        if (Eliteapp<1) {
            this.Eliteapp = this.Eliteapp + 0.01;
        }
    }

}
