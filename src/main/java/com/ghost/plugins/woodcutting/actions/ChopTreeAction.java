package com.ghost.plugins.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.Context;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.ghost.plugins.woodcutting.WoodcuttingConfig;
import com.ghost.plugins.woodcutting.script.ScriptContext;
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
    private final ScriptContext scriptContext;
    private final Context context;

    @Inject
    public ChopTreeAction(WoodcuttingConfig config, GameObjectService gameObjectService, SleepService sleepService, Client client, ScriptContext scriptContext, Context context) {
        this.config = config;
        this.gameObjectService = gameObjectService;
        this.sleepService = sleepService;
        this.client = client;
        this.scriptContext = scriptContext;
        this.context = context;
    }

    @Override
    public BehaviorResult performAction() {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return BehaviorResult.FAILURE;
        }

        if (localPlayer.getAnimation() != -1) {
            log.info("Player is not idle, waiting before attempting to chop.");
            scriptContext.setStatus("Waiting for idle...");
            return BehaviorResult.FAILURE;
        }

        WorldPoint playerLocation = localPlayer.getWorldLocation();
        List<Integer> targetTreeIds = config.treeType().getTreeIds();

        Predicate<GameObject> treePredicate = o -> {
            if (!targetTreeIds.contains(o.getId())) {
                return false;
            }
            
            return context.runOnClientThread(() -> {
                ObjectComposition comp = client.getObjectDefinition(o.getId());
                if (comp != null && comp.getImpostorIds() != null) {
                    comp = comp.getImpostor();
                }
                return comp != null && gameObjectService.hasAction(comp, "Chop down");
            });
        };

        GameObject nearestTree = gameObjectService.getGameObjects(treePredicate, playerLocation, 15)
                .stream()
                .findFirst()
                .orElse(null);

        if (nearestTree == null) {
            log.info("No trees found nearby.");
            scriptContext.setStatus("No trees found");
            scriptContext.setTargetTree(null);
            return BehaviorResult.FAILURE;
        }

        scriptContext.setTargetTree(nearestTree);
        scriptContext.setStatus("Chopping " + config.treeType().getName());

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
        scriptContext.setTargetTree(null);
        return BehaviorResult.FAILURE;
    }
}