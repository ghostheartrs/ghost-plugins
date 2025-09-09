package com.krakenplugins.ghost.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Player;

import java.util.Optional;

import static com.krakenplugins.ghost.woodcutting.WoodcuttingScript.TREE_IDS;
import static com.krakenplugins.ghost.woodcutting.WoodcuttingScript.WC_ANIMATIONS;

@Slf4j
public class ChopTreeAction implements ActionNode {

    private final GameObjectService gameObjectService;
    private final SleepService sleepService;
    private final Client client;

    @Inject
    public ChopTreeAction(GameObjectService gameObjectService, SleepService sleepService, Client client) {
        this.gameObjectService = gameObjectService;
        this.sleepService = sleepService;
        this.client = client;
    }

    @Override
    public BehaviorResult performAction() {
        Optional<GameObject> nearestTree = gameObjectService.closest(obj -> TREE_IDS.contains(obj.getId()));

        if (nearestTree.isEmpty()) {
            log.info("No trees found nearby.");
            return BehaviorResult.FAILURE;
        }

        if (gameObjectService.interact(nearestTree.get(), "Chop down")) {
            log.info("Found tree, attempting to chop.");
            sleepService.sleepUntil(() -> {
                Player localPlayer = client.getLocalPlayer();
                return localPlayer != null && WC_ANIMATIONS.contains(localPlayer.getAnimation());
            }, 5000);
            return BehaviorResult.SUCCESS;
        } else {
            log.warn("Failed to interact with tree.");
            return BehaviorResult.FAILURE;
        }
    }
}
