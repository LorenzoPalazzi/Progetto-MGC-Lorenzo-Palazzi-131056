
package it.unicam.cs.mpgc.rpg131056;

import it.unicam.cs.mpgc.rpg131056.actions.BuildOars;
import it.unicam.cs.mpgc.rpg131056.actions.Eat;
import it.unicam.cs.mpgc.rpg131056.controller.GameState;
import it.unicam.cs.mpgc.rpg131056.model.*;
import it.unicam.cs.mpgc.rpg131056.persistence.JsonSaveManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SurvivalGameTest {

    @Test
    void testPlayerInitialStateAndResourceConsumption() {
        Player player = new Player();
        assertEquals(100, player.getHealth());
        assertEquals(100, player.getEnergy());
        assertTrue(player.isAlive());

        // Con meteo SUNNY la sete aumenta con moltiplicatore 1.5 (20 * 1.5 = 30)
        player.consumeResorces(20, 20, Weather.SUNNY);
        assertEquals(20, player.getHunger());
        assertEquals(30, player.getThirst());
        assertEquals(95, player.getEnergy());
        assertTrue(player.isAlive());
    }

    @Test
    void testPlayerDeathWhenHungerReachesMax() {
        Player player = new Player();
        player.increaseHunger(100);
        assertTrue(player.isDead());
        assertFalse(player.isAlive());
    }

    @Test
    void testInventoryAddAndRemove() {
        Inventory inventory = new Inventory();
        Material wood = new Material("Legna", 1.5);

        inventory.addItem(wood);
        assertTrue(inventory.hasItem("Legna"));
        assertEquals(1, inventory.getItems().size());

        inventory.removeItem(wood);
        assertFalse(inventory.hasItem("Legna"));
        assertTrue(inventory.getItems().isEmpty());
    }

    @Test
    void testBuildOarsSuccess() {
        Player player = new Player();
        Inventory inventory = new Inventory();
        for (int i = 0; i < 4; i++) {
            inventory.addItem(new Material("Legna", 1.5));
        }

        BuildOars buildOars = new BuildOars();
        buildOars.execute(player, inventory);

        assertTrue(inventory.hasItem("Remi"));
        assertFalse(inventory.hasItem("Legna"));
    }

    @Test
    void testBuildOarsFailsWithoutEnoughWood() {
        Player player = new Player();
        Inventory inventory = new Inventory();
        inventory.addItem(new Material("Legna", 1.5));
        inventory.addItem(new Material("Legna", 1.5));

        BuildOars buildOars = new BuildOars();
        assertThrows(IllegalStateException.class, () -> buildOars.execute(player, inventory));
    }

    @Test
    void testEatConsumesBestFoodAndReducesHunger() {
        Player player = new Player();
        player.increaseHunger(50);

        Inventory inventory = new Inventory();
        inventory.addItem(new Food("Bacca", 0.2, 10));
        inventory.addItem(new Food("Pesce cotto", 0.8, 35));

        Eat eatAction = new Eat();
        eatAction.execute(player, inventory);

        // Deve aver scelto il cibo più nutriente (50 - 35 = 15) lasciando la bacca
        assertEquals(15, player.getHunger());
        assertFalse(inventory.hasItem("Pesce cotto"));
        assertTrue(inventory.hasItem("Bacca"));
    }

    @Test
    void testEatFailsWithoutFood() {
        Player player = new Player();
        Inventory inventory = new Inventory();
        inventory.addItem(new Material("Legna", 1.5));

        Eat eatAction = new Eat();
        assertThrows(IllegalStateException.class, () -> eatAction.execute(player, inventory));
    }

    @Test
    void testJsonSaveAndLoadPreservesPolymorphicItems(@TempDir Path tempDir) {
        Player player = new Player();
        player.increaseHunger(40);

        Inventory inventory = new Inventory();
        inventory.addItem(new Food("Pesce", 0.5, 25));
        inventory.addItem(new Material("Legna", 1.5));

        GameState originalState = new GameState(player, inventory, 4, Weather.RAINY);
        String savePath = tempDir.resolve("test_save.json").toString();

        JsonSaveManager saveManager = new JsonSaveManager();
        saveManager.save(originalState, savePath);
        GameState loadedState = saveManager.load(savePath);

        assertNotNull(loadedState);
        assertEquals(4, loadedState.getCurrentDay());
        assertEquals(Weather.RAINY, loadedState.getCurrentWeather());
        assertEquals(40, loadedState.getPlayer().getHunger());
        assertTrue(loadedState.getInventory().hasItem("Pesce"));
        assertTrue(loadedState.getInventory().hasItem("Legna"));

        // Verifica che ItemAdapter abbia ricostruito correttamente la sottoclasse Food
        Food loadedFood = loadedState.getInventory().getItems().stream()
                .filter(item -> item instanceof Food)
                .map(item -> (Food) item)
                .findFirst()
                .orElseThrow();
        assertEquals(25, loadedFood.getHungerRestore());
    }
}
