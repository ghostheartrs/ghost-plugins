package com.ghost.plugins.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.ghost.plugins.woodcutting.WoodcuttingConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

import java.util.List;
import java.util.function.Predicate;

import static com.ghost.plugins.woodcutting.WoodcuttingScript.WC_ANIMATIONS;

@Slf4j
public class ChopTreeAction implements ActionNode {

    private final WoodcuttingConfig config;
    private final GameObjectService gameObjectService;
    private final SleepService sleepService;
    private final Client client;

    @Inject
    public ChopTreeAction(WoodcuttingConfig config, GameObjectService gameObjectService, SleepService sleepService, Client client) {
        this.config = config;
        this.gameObjectService = gameObjectService;
        this.sleepService = sleepService;
        this.client = client;
    }
    @Override
    public BehaviorResult performAction() {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null || localPlayer.getAnimation() != -1) {
            return BehaviorResult.FAILURE;
        }

        // Use the high-level finder method. It's thread-safe and handles all the logic.
        GameObject nearestTree = gameObjectService.findReachableObject(
                config.treeType().getName(),
                true, // Exact name match
                15,   // Distance
                localPlayer.getWorldLocation(),
                true, // Check for an action
                "Chop down"
        );

        if (nearestTree == null) {
            log.info("No trees found nearby.");
            return BehaviorResult.FAILURE;
        }

        WorldPoint locationBeforeClick = localPlayer.getWorldLocation();

        if (gameObjectService.interact(nearestTree, "Chop down")) {
            log.info("Found tree, attempting to chop.");
            // The rest of your logic for waiting after the click is good.
            boolean conditionMet = sleepService.sleepUntil(() -> {
                Player p = client.getLocalPlayer();
                if (p == null) return false;
                return WC_ANIMATIONS.contains(p.getAnimation()) || !locationBeforeClick.equals(p.getWorldLocation());
            }, 5000);

            if (conditionMet && client.getLocalPlayer() != null && WC_ANIMATIONS.contains(client.getLocalPlayer().getAnimation())) {
                return BehaviorResult.SUCCESS;
            }
        }

        log.warn("Failed to interact with tree or confirm chopping animation.");
        return BehaviorResult.FAILURE;
    }
}
