package com.krakenplugins.example;

import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.eventbus.Subscribe;

@Singleton
@PluginDescriptor(
        name = "Alchemical Hydra",
        enabledByDefault = false,
        description = "Tracks Prayers and specials for the Alchemical Hydra.",
        tags = {"alchemical", "hydra", "alch", "boss", "pvm", "slayer"}
)
public class ExamplePlugin extends Plugin {
    private static final String MESSAGE_NEUTRALIZE = "The chemicals neutralise the Alchemical Hydra's defences!";
    private static final String MESSAGE_STUN = "The Alchemical Hydra temporarily stuns you.";
    private static final int[] HYDRA_REGIONS = {5279, 5280, 5535, 5536};

    @Inject
    private Client client;

    @Getter
    @Inject
    private ClientThread clientThread;


    @Inject
    private PrayerOverlay prayerOverlay;


    @Provides
    ExamplePluginConfig provideConfig(final ConfigManager configManager) {
        return configManager.getConfig(ExamplePluginConfig.class);
    }

    @Override
    protected void startUp() {
        if (client.getGameState() == GameState.LOGGED_IN) {
            init();
        }
    }

    private void init() {

    }

    @Override
    protected void shutDown() {

    }

    @Subscribe
    private void onGameStateChanged(final GameStateChanged event) {
        final GameState gameState = event.getGameState();

        switch (gameState) {
            case LOGGED_IN:
                init();
                break;
            case HOPPING:
            case LOGIN_SCREEN:
                shutDown();
            default:
                break;
        }
    }
}
