package com.krakenplugins.example;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.kraken.api.overlay.MouseTrackerOverlay;
import com.kraken.api.overlay.MovementOverlay;
import com.krakenplugins.example.overlay.ScriptOverlay;
import com.krakenplugins.example.overlay.TargetRockOverlay;
import com.krakenplugins.example.script.MiningModule;
import com.krakenplugins.example.script.MiningScript;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
@PluginDescriptor(
        name = "Mining Plugin",
        enabledByDefault = false,
        description = "Demonstrates an example of building a Mining automation plugin using the Kraken API.",
        tags = {"example", "automation", "kraken"}
)
public class MiningPlugin extends Plugin {

    @Inject
    private Client client;

    @Getter
    @Inject
    private ClientThread clientThread;

    @Inject
    private OverlayManager overlayManager;

    // Helper Overlay for displaying movement paths from the MovementService within the API.
    @Inject
    private MovementOverlay movementOverlay;

    @Inject
    private ScriptOverlay scriptOverlay;

    @Inject
    private MouseTrackerOverlay mouseTrackerOverlay;

    @Inject
    private TargetRockOverlay targetRockOverlay;

    @Inject
    private MiningScript miningScript;

    @Provides
    MiningConfig provideConfig(final ConfigManager configManager) {
        return configManager.getConfig(MiningConfig.class);
    }

    @Override
    public void configure(final Binder binder) {
        binder.install(new MiningModule());
    }

    @Override
    protected void startUp() {
        overlayManager.add(movementOverlay);
        overlayManager.add(scriptOverlay);
        overlayManager.add(mouseTrackerOverlay);
        overlayManager.add(targetRockOverlay);

        if (client.getGameState() == GameState.LOGGED_IN) {
            log.info("Starting Mining Plugin...");
            miningScript.start();
        }
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(movementOverlay);
        overlayManager.remove(scriptOverlay);
        overlayManager.remove(mouseTrackerOverlay);
        overlayManager.remove(targetRockOverlay);
        if(miningScript.isRunning()) {
            log.info("Shutting down Mining Plugin...");
            miningScript.stop();
        }
    }

    @Subscribe
    private void onMenuOptionClicked(MenuOptionClicked event) {
        log.info("Option={}, Target={}, Param0={}, Param1={}, MenuAction={}, ItemId={}, id={}, itemOp={}, str={}",
                event.getMenuOption(), event.getMenuTarget(), event.getParam0(), event.getParam1(), event.getMenuAction().name(), event.getItemId(),
                event.getId(), event.getItemOp(), event);
    }

    @Subscribe
    private void onGameStateChanged(final GameStateChanged event) {
        final GameState gameState = event.getGameState();

        switch (gameState) {
            case LOGGED_IN:
                startUp();
                break;
            case HOPPING:
            case LOGIN_SCREEN:
                shutDown();
            default:
                break;
        }
    }
}
