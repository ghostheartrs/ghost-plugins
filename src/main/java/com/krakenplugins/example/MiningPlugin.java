package com.krakenplugins.example;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.krakenplugins.example.script.MiningModule;
import com.krakenplugins.example.script.MiningScript;
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
        if (client.getGameState() == GameState.LOGGED_IN) {
            miningScript.start();
        }
    }

    @Override
    protected void shutDown() {
        if(miningScript.isRunning()) {
            miningScript.stop();
        }
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
