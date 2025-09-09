package com.krakenplugins.ghost.woodcutting;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.krakenplugins.ghostloader.IManagedPlugin;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.swing.JPanel;

@Slf4j
@Singleton
@PluginDescriptor(
        name = "Woodcutting Plugin",
        enabledByDefault = false,
        description = "A woodcutting script.",
        tags = {"woodcutting", "automation", "kraken"},
        hidden = true
)
public class WoodcuttingPlugin extends Plugin implements IManagedPlugin {

    @Inject
    private Client client;

    @Inject
    private EventBus eventBus;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private WoodcuttingScript woodcuttingScript;

    @Override
    public void configure(final Binder binder) {
        binder.install(new WoodcuttingModule());
    }

    @Override
    protected void startUp() {
        if (client.getGameState() == GameState.LOGGED_IN) {
            log.info("Starting Woodcutting Plugin...");
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
                    woodcuttingScript.start();
                }
                break;
            case HOPPING:
            case LOGIN_SCREEN:
                if (woodcuttingScript.isRunning()) {
                    woodcuttingScript.stop();
                }
                break;
            default:
                break;
        }
    }

    // --- IManagedPlugin Implementation ---

    @Override
    public String getName() {
        return "Woodcutting Plugin";
    }

    @Override
    public String getDescription() {
        return "A woodcutting script.";
    }

    @Override
    public void onEnable() {
        startUp();
    }

    @Override
    public void onDisable() {
        shutDown();
    }

    @Override
    public JPanel getConfigurationPanel() {
        return null;
    }
}
