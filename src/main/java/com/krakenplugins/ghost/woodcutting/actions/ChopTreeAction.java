package com.krakenplugins.ghost.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.krakenplugins.ghost.woodcutting.WoodcuttingConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

import java.util.List;
import java.util.function.Predicate;

import static com.krakenplugins.ghost.woodcutting.WoodcuttingScript.WC_ANIMATIONS;

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
        if (localPlayer == null) {
            return BehaviorResult.FAILURE;
        }

        if (localPlayer.getAnimation() != -1) {
            log.info("Player is not idle, waiting before attempting to chop.");
            return BehaviorResult.FAILURE;
        }

        WorldPoint playerLocation = localPlayer.getWorldLocation();

        List<Integer> targetTreeIds = config.treeType().getTreeIds();

        Predicate<GameObject> treePredicate = o -> {
            if (!targetTreeIds.contains(o.getId())) {
                return false;
            }
            ObjectComposition comp = gameObjectService.convertToObjectComposition(o);
            return comp != null && gameObjectService.hasAction(comp, "Chop down");
        };

        GameObject nearestTree = gameObjectService.getGameObjects(treePredicate, playerLocation, 15)
                .stream()
                .findFirst()
                .orElse(null);

        if (nearestTree == null) {
            log.info("No trees found nearby.");
            return BehaviorResult.FAILURE;
        }

        WorldPoint locationBeforeClick = localPlayer.getWorldLocation();

        if (gameObjectService.interact(nearestTree, "Chop down")) {
            log.info("Found tree, attempting to chop.");

            boolean conditionMet = sleepService.sleepUntil(() -> {
                Player p = client.getLocalPlayer();
                if (p == null) {
                    return false;
                }
                return WC_ANIMATIONS.contains(p.getAnimation()) || !locationBeforeClick.equals(p.getWorldLocation());
            }, 5000);

            if (conditionMet) {
                Player p = client.getLocalPlayer();
                if (p != null && WC_ANIMATIONS.contains(p.getAnimation())) {
                    return BehaviorResult.SUCCESS;
                }
                if (p != null && !locationBeforeClick.equals(p.getWorldLocation())) {
                    log.warn("Player moved after clicking tree, assuming misclick. Waiting for player to stop moving.");
                    sleepService.sleepUntil(() -> {
                        Player p2 = client.getLocalPlayer();
                        return p2 == null || p2.getAnimation() == -1;
                    }, 5000);
                    return BehaviorResult.FAILURE;
                }
            }
        }

        log.warn("Failed to interact with tree.");
        return BehaviorResult.FAILURE;
    }
}
