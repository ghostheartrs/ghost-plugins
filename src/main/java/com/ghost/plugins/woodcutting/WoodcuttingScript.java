package com.ghost.plugins.woodcutting;

import com.ghost.plugins.woodcutting.actions.BankAction;
import com.ghost.plugins.woodcutting.actions.ChopTreeAction;
import com.ghost.plugins.woodcutting.actions.DropLogsAction;
import com.ghost.plugins.woodcutting.actions.WaitAction;
import com.ghost.plugins.woodcutting.actions.WalkToBankAction;
import com.ghost.plugins.woodcutting.actions.WalkToStartAreaAction;
import com.ghost.plugins.woodcutting.actions.WalkToTreesAction;
import com.ghost.plugins.woodcutting.conditions.AtBankCondition;
import com.ghost.plugins.woodcutting.conditions.AtTreesCondition;
import com.ghost.plugins.woodcutting.conditions.InventoryFullCondition;
import com.ghost.plugins.woodcutting.conditions.IsChoppingCondition;
import com.ghost.plugins.woodcutting.conditions.IsOutOfAreaCondition;
import com.ghost.plugins.woodcutting.factory.SelectorNodeFactory;
import com.ghost.plugins.woodcutting.factory.SequenceNodeFactory;
import com.ghost.plugins.woodcutting.script.ScriptContext;
import com.ghost.plugins.woodcutting.types.ChoppingMode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kraken.api.Context;
import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.BehaviorTreeScript;
import com.kraken.api.core.script.node.ConditionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.gameval.InventoryID;
import net.runelite.client.eventbus.Subscribe;

@Slf4j
@Singleton
public class WoodcuttingScript extends BehaviorTreeScript {

    public static final List<Integer> WC_ANIMATIONS = List.of(
            AnimationID.WOODCUTTING_BRONZE,
            AnimationID.WOODCUTTING_IRON,
            AnimationID.WOODCUTTING_STEEL,
            AnimationID.WOODCUTTING_BLACK,
            AnimationID.WOODCUTTING_MITHRIL,
            AnimationID.WOODCUTTING_ADAMANT,
            AnimationID.WOODCUTTING_RUNE,
            AnimationID.WOODCUTTING_DRAGON
    );

    private final WoodcuttingConfig config;
    private final InventoryFullCondition inventoryFullCondition;
    private final DropLogsAction dropLogsAction;
    private final IsChoppingCondition isChoppingCondition;
    private final WaitAction waitAction;
    private final ChopTreeAction chopTreeAction;
    private final WalkToBankAction walkToBankAction;
    private final BankAction bankAction;
    private final WalkToTreesAction walkToTreesAction;
    private final AtBankCondition atBankCondition;
    private final AtTreesCondition atTreesCondition;
    private final SequenceNodeFactory sequenceFactory;
    private final SelectorNodeFactory selectorFactory;
    private final IsOutOfAreaCondition isOutOfAreaCondition;
    private final WalkToStartAreaAction walkToStartAreaAction;
    private final GameObjectService gameObjectService;

    @Getter
    private final ScriptContext scriptContext;
    private final Context context;

    private int lastKnownLogCount = 0;

    @Inject
    public WoodcuttingScript(Context context,
                             WoodcuttingConfig config,
                             InventoryFullCondition inventoryFullCondition,
                             DropLogsAction dropLogsAction,
                             IsChoppingCondition isChoppingCondition,
                             WaitAction waitAction,
                             ChopTreeAction chopTreeAction,
                             WalkToBankAction walkToBankAction,
                             BankAction bankAction,
                             WalkToTreesAction walkToTreesAction,
                             AtBankCondition atBankCondition,
                             AtTreesCondition atTreesCondition,
                             SequenceNodeFactory sequenceFactory,
                             SelectorNodeFactory selectorFactory,
                             IsOutOfAreaCondition isOutOfAreaCondition,
                             WalkToStartAreaAction walkToStartAreaAction,
                             GameObjectService gameObjectService,
                             ScriptContext scriptContext) {
        super(context);
        this.context = context;
        this.config = config;
        this.inventoryFullCondition = inventoryFullCondition;
        this.dropLogsAction = dropLogsAction;
        this.isChoppingCondition = isChoppingCondition;
        this.waitAction = waitAction;
        this.chopTreeAction = chopTreeAction;
        this.walkToBankAction = walkToBankAction;
        this.bankAction = bankAction;
        this.walkToTreesAction = walkToTreesAction;
        this.atBankCondition = atBankCondition;
        this.atTreesCondition = atTreesCondition;
        this.sequenceFactory = sequenceFactory;
        this.selectorFactory = selectorFactory;
        this.isOutOfAreaCondition = isOutOfAreaCondition;
        this.walkToStartAreaAction = walkToStartAreaAction;
        this.gameObjectService = gameObjectService;
        this.scriptContext = scriptContext;
    }

    @Override
    protected BehaviorNode buildBehaviorTree() {
        ConditionNode bankingEnabled = () -> config.bankingEnabled();
        ConditionNode bankingDisabled = () -> !config.bankingEnabled();
        ConditionNode notAtBank = () -> !atBankCondition.checkCondition();
        ConditionNode notAtTrees = () -> !atTreesCondition.checkCondition();
        ConditionNode notInventoryFull = () -> !inventoryFullCondition.checkCondition();
        ConditionNode isInAreaCondition = () -> !isOutOfAreaCondition.checkCondition();

        BehaviorNode bankingRoutine = selectorFactory.create(List.of(
                sequenceFactory.create(List.of(notAtBank, walkToBankAction)),
                bankAction
        ));

        BehaviorNode handleFullInventory = sequenceFactory.create(List.of(
                inventoryFullCondition,
                selectorFactory.create(List.of(
                        sequenceFactory.create(List.of(
                                bankingEnabled,
                                bankingRoutine
                        )),
                        sequenceFactory.create(List.of(
                                bankingDisabled,
                                dropLogsAction
                        ))
                ))
        ));

        BehaviorNode returnToTrees = sequenceFactory.create(List.of(
                bankingEnabled,
                notInventoryFull,
                atBankCondition,
                walkToTreesAction
        ));

        BehaviorNode walkToTreesIfNeeded = sequenceFactory.create(List.of(
                bankingEnabled,
                notInventoryFull,
                notAtTrees,
                notAtBank,
                walkToTreesAction
        ));

        BehaviorNode chopOrWaitSelector = selectorFactory.create(List.of(
                sequenceFactory.create(List.of(isChoppingCondition, waitAction)),
                chopTreeAction
        ));

        BehaviorNode choppingLogic = sequenceFactory.create(List.of(
                isInAreaCondition,
                chopOrWaitSelector
        ));

        BehaviorNode handleWandering = sequenceFactory.create(List.of(
                isOutOfAreaCondition,
                walkToStartAreaAction
        ));

        return selectorFactory.create(List.of(
                handleFullInventory,
                handleWandering,
                returnToTrees,
                walkToTreesIfNeeded,
                choppingLogic
        ));
    }

    @Override
    protected void onBehaviorTreeStart() {
        log.info("Woodcutting Script started!");

        scriptContext.setStartTime(System.currentTimeMillis());
        scriptContext.setStartXp(context.getClient().getSkillExperience(Skill.WOODCUTTING));
        scriptContext.setLogsCut(0);
        scriptContext.setTargetTree(null);
        scriptContext.setChopAndWaitLocation(null);
        scriptContext.getTreeClusterLocations().clear();
        lastKnownLogCount = 0;

        if (context.getClient().getLocalPlayer() != null) {
            scriptContext.setStartLocation(context.getClient().getLocalPlayer().getWorldLocation());
        }

        if (config.choppingMode() == ChoppingMode.CLUSTER) {
            populateTreeCluster();
        }
    }

    private void populateTreeCluster() {
        scriptContext.setStatus("Finding tree cluster...");
        Player localPlayer = context.getClient().getLocalPlayer();
        if (localPlayer != null) {
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

    @Subscribe
    public void onItemContainerChanged(ItemContainerChanged event) {
        if (event.getContainerId() != InventoryID.INV || !isRunning()) {
            return;
        }

        ItemContainer container = event.getItemContainer();
        if (container == null) {
            return;
        }

        int logId = config.treeType().getLogId();
        int currentLogCount = container.count(logId);

        if (currentLogCount > lastKnownLogCount) {
            int logsGained = currentLogCount - lastKnownLogCount;
            scriptContext.setLogsCut(scriptContext.getLogsCut() + logsGained);
        }

        lastKnownLogCount = currentLogCount;
    }

    @Override
    protected long onBehaviorTreeSuccess() {
        return getRandomDelay(150, 250);
    }

    @Override
    protected long onBehaviorTreeFailure() {
        log.debug("Behavior tree iteration failed, retrying...");
        return getRandomDelay(150, 250);
    }

    @Override
    protected long onBehaviorTreeRunning() {
        return getRandomDelay(150, 250);
    }

    private static long getRandomDelay(int min, int max) {
        return min + (long) (Math.random() * (max - min + 1));
    }
}