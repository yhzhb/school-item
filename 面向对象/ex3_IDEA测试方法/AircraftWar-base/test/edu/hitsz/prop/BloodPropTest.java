package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.factory.BlpropFactory;
import edu.hitsz.factory.EliteFactory;
import edu.hitsz.factory.PropFactory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class BloodPropTest {
    private HeroAircraft heroAircraft = HeroAircraft.getHeroAircraft();
    private AbstractProp bloodProp;
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
        bloodProp = new BloodProp(Main.WINDOW_WIDTH / 2,
                Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight()+50,0,-20);
    }

    @AfterEach
    void closeDown(){
        System.out.println("**--- Executed after each test method in this class ---**");
        bloodProp=null;
    }

    @Test
    void crash() {
        System.out.println("**--- Begin to test  crash ---**");
        assertEquals(bloodProp.crash(heroAircraft),false);
        bloodProp.forward();
        assertEquals(bloodProp.getLocationY(),Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight()+30);
        assertEquals(bloodProp.crash(heroAircraft),true);
    }

    @Test
    void effect() {
            System.out.println("**--- Begin to test  effect ---**");
            bloodProp.effect(heroAircraft);
            assertEquals(heroAircraft.getHp(),140);
    }
}