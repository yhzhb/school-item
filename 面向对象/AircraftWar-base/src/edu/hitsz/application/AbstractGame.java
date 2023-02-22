package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.MusicThread;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.basic.AbstractFlyingObject;

import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.BossFactory;
import edu.hitsz.factory.EliteFactory;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.factory.MobFactory;
import edu.hitsz.prop.AbstractProp;

import edu.hitsz.prop.BombProp;
import edu.hitsz.prop.BulletProp;
import edu.hitsz.scorerand.UserDao;
import edu.hitsz.scorerand.UserDaoImpl;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.Random;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class AbstractGame extends JPanel {

    private Thread thread;
    protected int backGroundTop = 0;
    protected double gamecot=1;
    protected boolean Bossflag ;
    protected double  Bosshpcoe =1;
    protected int   Bossvalue;
    protected int  Bosscnt=0;
    protected int step;//boss机出现阈值提升
    protected int timeStep;//普通敌机能力提升时间间隔
    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    protected final HeroAircraft heroAircraft;
    protected final List<AbstractEnemy> enemyAircrafts;

    protected final List<BaseBullet> heroBullets;
    protected final List<BaseBullet> enemyBullets;

    protected final List<AbstractProp>  Items;
    protected EnemyFactory enemyFactory;
    private UserDao userDao =new UserDaoImpl();
    protected MusicThread bg= new MusicThread("src/videos/bgm.wav");
    protected MusicThread bossbgm= new MusicThread("src/videos/bgm_boss.wav");




    protected double Eliteapp =0.2;
    protected int enemyMaxNumber = 5;

    private boolean gameOverFlag = false;

    private boolean BossupdatFlag = false;
    private String addtime;
    private int time = 0;
    private int Blcnt=0;
    private int score = 0;

    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    protected int cycleDuration = 600;
    private int cycleTime = 0;


    public AbstractGame() {

        heroAircraft=HeroAircraft.getHeroAircraft();
        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();

        Items = new LinkedList<>();




        /**
         * Scheduled 线程池，用于定时任务调度
         * 关于alibaba code guide：可命名的 ThreadFactory 一般需要第三方包
         * apache 第三方库： org.apache.commons.lang3.concurrent.BasicThreadFactory
         */
        this.executorService = new ScheduledThreadPoolExecutor(1,
                new BasicThreadFactory.Builder().namingPattern("game-action-%d").daemon(true).build());

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {



        musicAction();
        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                //System.out.println(time);//time
                // 新敌机产生
                double seed=Math.random();
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    if (seed>Eliteapp) {
                       enemyFactory = new MobFactory();
                    }else {
                        enemyFactory = new EliteFactory();
                    }
                    enemyAircrafts.add(enemyFactory.createEnemy(gamecot,cycleDuration));
                }

                // 飞机射出子弹
                shootAction();
            }
            createBoss();

            // 子弹移动
            bulletsMoveAction();
            // 飞机移动
            aircraftsMoveAction();
            //道具移动
            propsMoveAction();
            // 撞击检测
            crashCheckAction();
            // 后处理
            postProcessAction();
            //每个时刻重绘界面
            repaint();
            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                setUserDaoAction();
                bg.setIsplaying(false);
                bossbgm.setIsplaying(false);
                new MusicThread("src/videos/game_over.wav").start();
                System.out.println("Game Over!");
                this.setVisible(false);
                synchronized (Main.obj){
                    Main.obj.notify();
                }
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void musicAction(){
        bg.setIsloop(true);
        bossbgm.setIsloop(true);
        bossbgm.setIsplaying(false);
        bg.start();
        bossbgm.start();
    }



    private void shootAction() {
        // TODO 敌机射击
         for(AbstractEnemy enemyAircraft :enemyAircrafts){
             enemyBullets.addAll(enemyAircraft.shoot());
         }
        // 英雄射击
       heroBullets.addAll(heroAircraft.shoot());
    }


    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();

        }

    }




    private void aircraftsMoveAction() {
        for (AbstractEnemy enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }


    private void propsMoveAction(){
        for(AbstractProp abstractProp : Items){
            abstractProp.forward();
        }
    }

    /**
     * Boss机创造
     */
   private void createBoss(){
       enemyFactory = new BossFactory();
       makeharder();
       if(score>=Bossvalue &&!Bossflag) {
           System.out.println("Boss机出现！\n 当前Boss机血量倍率："+Bosshpcoe+",当前同屏敌人数目："+enemyMaxNumber);
           bg.setIsplaying(false);
           bossbgm.setIsplaying(true);
           enemyAircrafts.add(enemyFactory.createEnemy(Bosshpcoe,cycleDuration));
           Bossflag = true;
           Bosscnt = Bosscnt + 1;
           Bossvalue = Bossvalue + step;
       }
       else if(score>= Bossvalue){//boss存在到达下一个阈值，难度继续提升
           Bossvalue = Bossvalue + step;
       }
   }

   private void setUserDaoAction(){
       try {
           userDao.doload();
       } catch (IOException e) {
           e.printStackTrace();
       }
       SimpleDateFormat dft = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
       addtime = dft.format(new Date());
   }
    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // TODO 敌机子弹攻击英雄
        for(BaseBullet bullet :enemyBullets){
            if (bullet.notValid()) {
                continue;
            }

            if(heroAircraft.crash(bullet)){
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }

        }

        // 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    new MusicThread("src/videos/bullet_hit.wav").start();
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        if(enemyAircraft.getClass().getSimpleName().equals("BossEnemy")){
                            Bossflag=false;
                            BossupdatFlag = true;//boss能力更新
                            bossbgm.setIsplaying(false);
                            bg.setIsplaying(true);
                            score+= 80;
                        }
                        else if(enemyAircraft instanceof EliteEnemy){
                            score+= 20;
                        }
                        // TODO 获得分数，产生道具补给
                        else {
                            score += 10;
                        }
                        if(!enemyAircraft.getProp().isEmpty()) {
                            Items.addAll(enemyAircraft.getProp());
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // Todo: 我方获得道具，道具生效
        for(AbstractProp items : Items) {
            if (heroAircraft.crash(items)|| items.crash(heroAircraft)){
                new MusicThread("src/videos/get_supply.wav").start();
                int cnt=0;
               // AbstractEnemy checkpoint = null;
                if(items.getClass().getSimpleName().equals("BombProp")){
                    for(int size=enemyBullets.size(),i=0;i<size;i++){
                        if(!enemyBullets.get(i).notValid()){
                            items.addMember(enemyBullets.get(i));
                        }
                    }
                    for (int size = enemyAircrafts.size(),i=0;i<size;i++){
                        if(!enemyAircrafts.get(i).notValid()){
                            items.addMember(enemyAircrafts.get(i));
                            if(!enemyAircrafts.get(i).getClass().getSimpleName().equals("BossEnemy")) {
                                cnt++;
                            }
                            /*else if(enemyAircrafts.get(i).getClass().getSimpleName().equals("BossEnemy")){
                                checkpoint = enemyAircrafts.get(i);
                            }*/
                        }
                    }
                }
                if(items instanceof BulletProp){
                    Blcnt =Blcnt +1;
                    ((BulletProp) items).setCnt(Blcnt-1);
                    ((BulletProp) items).setThread(thread);
                }
                items.effect(heroAircraft);
                if (items instanceof BulletProp ){
                    if(((BulletProp) items).getThread()!=null){
                        thread = ((BulletProp) items).getThread();
                    }
                }
                /*if (checkpoint!=null&&checkpoint.notValid()){
                    Bossflag = false;
                }*/
                score =score+10*cnt;
                items.vanish();
            }
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * 4.suan'chu
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        Items.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        this.paintbg(g);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);
        //绘制道具
        paintImageWithPositionRevised(g,Items);
        //绘制敌机
        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    public abstract void paintbg(Graphics g);


    protected void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    protected void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }

    /**
     * 得到游戏内部数据
     */
    public int getScore() {
        return score;
    }

    public String getAddtime(){
        return addtime;
    }

    public UserDao  getUserDao(){
        return userDao;
    }

    public double getBosshpcoe(){
        return Bosshpcoe;
    }


    /**
     * 模板方法
     */
    private final void  makeharder(){
           if(BossupdatFlag){
               this.Bossharder();
               BossupdatFlag = false;
           }

           if(time%timeStep==0&&time>0) {
               this.Enemyharder();
               if(Bossvalue>0) {
                   System.out.println("提高难度！精英机概率:" + String.format("%.2f", Eliteapp) + ",敌机出现及子弹射击周期：" + cycleDuration + ",敌机属性倍率：" + String.format("%.2f", gamecot));
               }else {
                   System.out.println("简单模式！精英机概率:" + String.format("%.2f", Eliteapp) + ",敌机出现及子弹射击周期：" + cycleDuration + ",敌机属性倍率：" + String.format("%.2f", gamecot));
               }
           }
    }

    public abstract void Bossharder();

    public abstract void Enemyharder();
}
