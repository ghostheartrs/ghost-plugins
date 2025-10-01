package com.ghost.plugins.fishing;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.kraken.api.Context;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import com.ghost.plugins.fishing.script.FishingScript;
import com.ghost.plugins.fishing.script.ScriptContext;
import com.ghost.plugins.fishing.script.FishingScript;
import com.ghost.plugins.fishing.script.FishingModule;


@Slf4j
@Singleton
@PluginDescriptor(
        name = "<html><font color=\"#FF0000\">[\uD83D\uDC7B] </font>Fishing</html>",
        enabledByDefault = false,
        description = "Ghost's AIO fishing plugin.",
        tags = {"fishing", "automation", "kraken", "ghost"}
)
public class FishingPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private EventBus eventBus;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private Context context;

    @Inject
    private FishingScript fishingScript;

    @Provides
    FishingConfig provideConfig(final ConfigManager configManager) {
        return configManager.getConfig(FishingConfig.class);
    }

    @Override
    public void configure(final Binder binder) {
        binder.install(new FishingModule());
    }

    @Override
    protected void startUp() {
        if (client.getGameState() == GameState.LOGGED_IN) {
            log.info("Starting Fishing Plugin...");
            context.loadHooks();
            context.loadPacketUtils();
            fishingScript.start();
        }
    }

    @Override
    protected void shutDown() {
        if(fishingScript.isRunning()) {
            log.info("Shutting down Fishing Plugin...");
            fishingScript.stop();
        }
    }

    @Subscribe
    private void onGameStateChanged(final GameStateChanged event) {
        final GameState gameState = event.getGameState();
        switch (gameState) {
            case LOGGED_IN:
                if (!fishingScript.isRunning()) {
                    startUp();
                }
                break;
            case HOPPING:
            case LOGIN_SCREEN:
                if (fishingScript.isRunning()) {
                    shutDown();
                }
                break;
            default:
                break;
        }
    }
}