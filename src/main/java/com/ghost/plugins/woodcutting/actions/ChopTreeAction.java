package com.ghost.plugins.woodcutting.actions;

import com.ghost.plugins.woodcutting.WoodcuttingConfig;
import com.ghost.plugins.woodcutting.script.ScriptContext;
import com.ghost.plugins.woodcutting.types.ChoppingMode;
import com.google.inject.Inject;
import com.kraken.api.Context;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

        GameObject treeToChop = findNextTree(localPlayer);

        if (treeToChop == null) {
            scriptContext.setStatus("Waiting for respawn...");
            scriptContext.setTargetTree(null);
            if (config.choppingMode() != ChoppingMode.DYNAMIC) {
                sleepService.sleep(1500, 2500);
                return BehaviorResult.RUNNING;
            }
            return BehaviorResult.FAILURE;
        }

        scriptContext.setTargetTree(treeToChop);
        scriptContext.setStatus("Chopping " + config.treeType().getName());

        WorldPoint locationBeforeClick = localPlayer.getWorldLocation();

        if (gameObjectService.interactReflect(treeToChop, "Chop down")) {
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

    private GameObject findNextTree(Player localPlayer) {
        ChoppingMode mode = config.choppingMode();
        List<Integer> targetTreeIds = config.treeType().getTreeIds();
        WorldPoint playerLocation = localPlayer.getWorldLocation();

        Predicate<TileObject> isAValidTree = o -> {
            if (o == null || !(o instanceof GameObject)) return false;
            if (!targetTreeIds.contains(o.getId())) return false;

            return context.runOnClientThread(() -> {
                ObjectComposition comp = client.getObjectDefinition(o.getId());
                if (comp != null && comp.getImpostorIds() != null) {
                    comp = comp.getImpostor();
                }
                return comp != null && gameObjectService.hasAction(comp, "Chop down");
            });
        };

        switch (mode) {
            case CHOP_AND_WAIT:
                WorldPoint targetLocation = scriptContext.getChopAndWaitLocation();

                if (targetLocation == null) {
                    GameObject newTarget = gameObjectService.getGameObjects(isAValidTree::test, playerLocation, 15)
                            .stream()
                            .findFirst()
                            .orElse(null);
                    if (newTarget != null) {
                        scriptContext.setChopAndWaitLocation(newTarget.getWorldLocation());
                    }
                    return newTarget;
                }

                return gameObjectService.all(o -> o.getWorldLocation().equals(targetLocation))
                        .stream()
                        .filter(isAValidTree)
                        .map(o -> (GameObject) o)
                        .findFirst()
                        .orElse(null);

            case CLUSTER:
                if (scriptContext.getTreeClusterLocations().isEmpty()) {
                    populateTreeCluster();
                }

                List<WorldPoint> clusterLocations = scriptContext.getTreeClusterLocations();
                if (clusterLocations.isEmpty()) {
                    log.warn("Cluster mode is active, but no trees could be found to form a cluster.");
                    return null;
                }

                for (WorldPoint location : clusterLocations) {
                    GameObject treeAtLocation = gameObjectService.all(o -> o.getWorldLocation().equals(location))
                            .stream()
                            .filter(isAValidTree)
                            .map(o -> (GameObject) o)
                            .findFirst()
                            .orElse(null);

                    if (treeAtLocation != null) {
                        return treeAtLocation;
                    }
                }
                return null;

            case DYNAMIC:
            default:
                return gameObjectService.getGameObjects(isAValidTree::test, playerLocation, 15)
                        .stream()
                        .findFirst()
                        .orElse(null);
        }
    }

    private void populateTreeCluster() {
        Player localPlayer = context.getClient().getLocalPlayer();
        if (localPlayer == null) return;

        scriptContext.setStatus("Finding tree cluster...");
        List<Integer> targetTreeIds = config.treeType().getTreeIds();
        Predicate<GameObject> isAValidTree = o -> {
            if (o == null || !targetTreeIds.contains(o.getId())) {
                return false;
            }
            return context.runOnClientThread(() -> {
                ObjectComposition comp = context.getClient().getObjectDefinition(o.getId());
                if (comp != null && comp.getImpostorIds() != null) {
                    comp = comp.getImpostor();
                }
                return comp != null && gameObjectService.hasAction(comp, "Chop down");
            });
        };

        List<WorldPoint> nearbyTreeLocations = gameObjectService.getGameObjects(
                        isAValidTree,
                        localPlayer.getWorldLocation(),
                        15
                ).stream()
                .sorted(Comparator.comparingInt(tree ->
                        tree.getWorldLocation().distanceTo(localPlayer.getWorldLocation())))
                .limit(3)
                .map(GameObject::getWorldLocation)
                .collect(Collectors.toList());

        scriptContext.setTreeClusterLocations(nearbyTreeLocations);
        log.info("Found {} tree locations for the cluster.", nearbyTreeLocations.size());
    }
}