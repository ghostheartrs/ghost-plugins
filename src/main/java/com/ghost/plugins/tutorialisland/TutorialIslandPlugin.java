package com.ghost.plugins.tutorialisland;

import com.ghost.plugins.tutorialisland.script.TutorialIslandScript;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.kraken.api.Context;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
        name = "<html><font color=\"#FF0000\">[\uD83D\uDC7B] </font>Tutorial Island</html>",
        description = "Automates Tutorial Island.",
        tags = {"tutorial", "automation", "kraken", "ghost"},
        enabledByDefault = false
)
public class TutorialIslandPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private Context context;

    @Inject
    private TutorialIslandScript tutorialIslandScript;

    @Provides
    TutorialIslandConfig provideConfig(final ConfigManager configManager) {
        return configManager.getConfig(TutorialIslandConfig.class);
    }

    @Override
    public void configure(final Binder binder) {
        binder.install(new TutorialIslandModule());
    }

    @Override
    protected void startUp() {
        if (client.getGameState() == GameState.LOGGED_IN) {
            log.info("Starting Tutorial Island Plugin...");
            context.loadHooks();
            context.loadPacketUtils();
            tutorialIslandScript.start();
        }
    }

    @Override
    protected void shutDown() {
        if (tutorialIslandScript.isRunning()) {
            log.info("Shutting down Tutorial Island Plugin...");
            tutorialIslandScript.stop();
        }
    }

    @Subscribe
    private void onGameStateChanged(final GameStateChanged event) {
        if (event.getGameState() == GameState.LOGGED_IN && !tutorialIslandScript.isRunning()) {
            startUp();
        }
    }
}