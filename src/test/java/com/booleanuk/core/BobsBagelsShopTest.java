package com.booleanuk.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class BobsBagelsShopTest {
    @Test
    public void testBobsBagelsShop() {
        BobsBagelsShop shop = new BobsBagelsShop(new HashMap<>());
        Assertions.assertEquals(new HashMap<>(), shop.getInventory());
    }

    @Test
    public void testShowInventory() {
        BobsBagelsShop shop = new BobsBagelsShop(new HashMap<>());
        Assertions.assertEquals("No items in stock.", shop.showInventory());

        Bagel bglo = new Bagel("BGLO", "Onion", 0.49);
        Bagel bglp = new Bagel("BGLP", "Plain", 0.39);
        HashMap<Item, Integer> testInventory = new HashMap<>();
        testInventory.put(bglo, 100);
        testInventory.put(bglp, 100);
        BobsBagelsShop shop2 = new BobsBagelsShop(testInventory);
        Assertions.assertTrue(shop2.showInventory().contains("100\t\tBGLO\t0.49\tBagel\tOnion"));
        Assertions.assertTrue(shop2.showInventory().contains("100\t\tBGLP\t0.39\tBagel\tPlain"));
    }
}
