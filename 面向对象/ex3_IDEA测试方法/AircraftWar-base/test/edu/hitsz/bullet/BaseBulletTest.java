package edu.hitsz.bullet;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseBulletTest {

    private  BaseBullet bullet = new BaseBullet(0,0,0,20,30);
    private AbstractAircraft abstractAircraft;
    @BeforeAll
    static void beforeAll() {
        System.out.println("**---Ready for test---**");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("**--- Finish ---**");
    }

    @Test
    void forward() {
        System.out.println("**--- Begin to test  forward ---**");
        bullet.forward();
        assertEquals(bullet.getLocationY(),20);
        bullet=null;
    }


}