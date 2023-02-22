package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.factory.EliteFactory;
import edu.hitsz.factory.EnemyFactory;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;
import org.junit.jupiter.api.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EliteEnemyTest {
    private  AbstractEnemy enemy;
    @BeforeAll
    static void beforeAll() {
        System.out.println("**---Ready for test---**");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("**--- Finish ---**");
    }

    @BeforeEach
    void setUp() {
        System.out.println("**--- Executed before each test method in this class ---**");
       EliteFactory eliteFactory= new EliteFactory();
       enemy = eliteFactory.createEnemy();
    }

    @AfterEach
    void closeDown(){
        System.out.println("**--- Executed After each test method in this class ---**");
        enemy =null;
    }

    @RepeatedTest(10)
    @DisplayName("Test EliteEnemyshoot method")
    void shoot() {
        System.out.println("**---Begin to test  shoot method ---**");
        List<BaseBullet>  bullet = enemy.shoot();
        assertEquals(bullet.isEmpty(),false);
        for(int size = bullet.size(),i=0;i<size;i++) {
            assertEquals(bullet.get(i).getLocationX(), enemy.getLocationX());
            assertEquals(bullet.get(i).getLocationY(),enemy.getLocationY()+ 1*2);
            assertEquals(bullet.get(i).getSpeedY(),enemy.getSpeedY()+1*5);
            assertEquals(bullet.get(i).getSpeedX(),0);
        }
    }

    @RepeatedTest(20)
    @DisplayName("Test ElitegetProp method")
    void getProp() {
        System.out.println("**---Begin to test  getProp method ---**");
               AbstractProp prop = enemy.getProp();

                assertEquals(prop==null||prop.getClass().getSimpleName().equals("BloodProp")||
                prop.getClass().getSimpleName().equals("BombProp")||
                prop.getClass().getSimpleName().equals("BulletProp"),true);

               if(prop!=null) {
                   assertEquals(prop.getLocationX(), enemy.getLocationX());
                   assertEquals(prop.getLocationY(), enemy.getLocationY());
                   assertEquals(prop.getSpeedY(), 0);
                   assertEquals(prop.getSpeedX(), 0);
               }
           }

}