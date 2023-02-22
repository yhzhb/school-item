package edu.hitsz.application;

import edu.hitsz.factory.BossFactory;

import java.awt.*;

public class EasyGame extends AbstractGame{

    public EasyGame(){
        this.Bossflag = true;
        this.timeStep= 3000;
        this.Bossvalue =0;
    }

    @Override
    public void paintbg(Graphics g) {
        g.drawImage(ImageManager.BACKGROUND_IMAGE_EASY, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE_EASY, 0, this.backGroundTop, null);
    }

    @Override
    public void Bossharder(){

    }

    @Override
    public  void Enemyharder(){

    }
}
