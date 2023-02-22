package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HeroAircraftTest {

    HeroAircraft heroAircraft;

    @BeforeAll
    static void beforeAll() {
        System.out.println("**---Ready for test---**");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("**--- Finish ---**");
    }

    @BeforeEach
    void Setup() {
        System.out.println("**--- Executed before each test method in this class ---**");
        heroAircraft = HeroAircraft.getHeroAircraft();
    }

    @AfterEach
    void CloseDown(){
        System.out.println("**--- Executed after each test method in this class ---**");
        heroAircraft = null;
    }

    @Test
    @DisplayName("Test decreaseHp method")
    void decreaseHp() {
        System.out.println("**---Begin to test  decreaseHp method ---**");

        Random rand = new Random();
        int power = rand.nextInt(100);
        heroAircraft.decreaseHp(power);
        assertEquals(heroAircraft.getHp(),100-power);

        heroAircraft.decreaseHp(100);
        assertTrue(heroAircraft.getHp()==0);
    }

    @Test
    @DisplayName("Test heroAircraftshoot method")
    void shoot() {
        System.out.println("**---Begin to test  shoot method ---**");
        List<BaseBullet> bullet = heroAircraft.shoot();
        assertEquals(bullet.isEmpty(),false);

            assertEquals(bullet.get(0).getLocationX(), heroAircraft.getLocationX());
            assertEquals(bullet.get(0).getLocationY(),heroAircraft.getLocationY()+(-1)*2);
            assertEquals(bullet.get(0).getSpeedY(),heroAircraft.getSpeedY()+(-1)*5);
            assertEquals(bullet.get(0).getSpeedX(),0);

    }
}