package com.ghost.plugins.gemcrab;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.kraken.api.overlay.MouseTrackerOverlay;
import com.kraken.api.overlay.MovementOverlay;
import com.ghost.plugins.gemcrab.overlay.ScriptOverlay;
import com.ghost.plugins.gemcrab.overlay.TargetCrabOverlay;
import com.ghost.plugins.gemcrab.script.GemCrabModule;
import com.ghost.plugins.gemcrab.script.GemCrabScript;
import com.ghost.plugins.gemcrab.script.actions.AttackCrabAction;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@Singleton
@PluginDescriptor(
        name = "<html><font color=\"#FF0000\">[\uD83D\uDC7B] </font>GemCrab</html>",
        enabledByDefault = false,
        description = "Automatically fights The Gemstone Crab.",
        tags = {"combat", "automation", "kraken"}
)
public class GemCrabPlugin extends Plugin {

    @Inject
    private Client client;

    @Getter
    @Inject
    private ClientThread clientThread;

    @Inject
    private EventBus eventBus;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private MovementOverlay movementOverlay;

    @Inject
    private ScriptOverlay scriptOverlay;

    @Inject
    private MouseTrackerOverlay mouseTrackerOverlay;

    @Inject
    private TargetCrabOverlay targetCrabOverlay;

    @Inject
    private GemCrabScript gemCrabScript;

    @Provides
    GemCrabConfig provideConfig(final ConfigManager configManager) {
        return configManager.getConfig(GemCrabConfig.class);
    }

    @Override
    public void configure(final Binder binder) {
        binder.install(new GemCrabModule());
    }

    @Override
    protected void startUp() {
        eventBus.register(AttackCrabAction.class);

        overlayManager.add(movementOverlay);
        overlayManager.add(scriptOverlay);
        overlayManager.add(mouseTrackerOverlay);
        overlayManager.add(targetCrabOverlay);

        if (client.getGameState() == GameState.LOGGED_IN) {
            log.info("Starting GemCrab Plugin...");
            gemCrabScript.start();
        }
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(movementOverlay);
        overlayManager.remove(scriptOverlay);
        overlayManager.remove(mouseTrackerOverlay);
        overlayManager.remove(targetCrabOverlay);
        if(gemCrabScript.isRunning()) {
            log.info("Shutting down GemCrab Plugin...");
            gemCrabScript.stop();
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
                if (!gemCrabScript.isRunning()) {
                    startUp();
                }
                break;
            case HOPPING:
            case LOGIN_SCREEN:
                if (gemCrabScript.isRunning()) {
                    shutDown();
                }
                break;
            default:
                break;
        }
    }
}
