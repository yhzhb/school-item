package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.MobFactory;
import edu.hitsz.prop.AbstractProp;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MobEnemyTest {
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
        MobFactory mobFactory= new MobFactory();
        enemy = mobFactory.createEnemy();
    }
    @AfterEach
    void closeDown(){
        System.out.println("**--- Executed after each test method in this class ---**");
        enemy=null;
    }

    @RepeatedTest(10)
    void shoot() {
        System.out.println("**---Begin to test  shoot method ---**");
        List<BaseBullet> bullet = enemy.shoot();
        assertEquals(bullet.isEmpty(),true);
    }

    @RepeatedTest(10)
    void getProp() {
        System.out.println("**---Begin to test  getProp method ---**");
            AbstractProp prop = enemy.getProp();
            assertNull(prop);
    }

}
