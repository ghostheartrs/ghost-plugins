package com.ghost.plugins.woodcutting;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.kraken.api.Context; // <-- Make sure this is imported
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
@PluginDescriptor(
        name = "<html><font color=\"#FF0000\">[GH] </font>Woodcutting</html>",
        enabledByDefault = false,
        description = "Ghost's AIO woodcutting plugin.",
        tags = {"woodcutting", "automation", "kraken", "ghost"}
)
public class WoodcuttingPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private WoodcuttingConfig config;

    @Inject
    private EventBus eventBus;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private WoodcuttingScript woodcuttingScript;

    @Inject
    private Context context; // <-- Inject the Context

    @Provides
    WoodcuttingConfig provideConfig(final ConfigManager configManager) {
        return configManager.getConfig(WoodcuttingConfig.class);
    }

    @Override
    public void configure(final Binder binder) {
        binder.install(new WoodcuttingModule());
    }

    @Override
    protected void startUp() {
        if (client.getGameState() == GameState.LOGGED_IN) {
            log.info("Starting Woodcutting Plugin...");

            // --- LOAD BOTH ---
            context.loadHooks();
            context.loadPacketUtils();
            // -----------------

            woodcuttingScript.start();
        }
    }

    @Override
    protected void shutDown() {
        if(woodcuttingScript.isRunning()) {
            log.info("Shutting down Woodcutting Plugin...");
            woodcuttingScript.stop();
        }
    }

    @Subscribe
    private void onGameStateChanged(final GameStateChanged event) {
        final GameState gameState = event.getGameState();

        switch (gameState) {
            case LOGGED_IN:
                if (!woodcuttingScript.isRunning()) {
                    startUp();
                }
                break;
            case HOPPING:
            case LOGIN_SCREEN:
                if (woodcuttingScript.isRunning()) {
                    shutDown();
                }
                break;
            default:
                break;
        }
    }
}
