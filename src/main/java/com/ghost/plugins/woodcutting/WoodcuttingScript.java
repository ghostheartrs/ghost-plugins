package com.ghost.plugins.woodcutting;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kraken.api.Context;
import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.BehaviorTreeScript;
import com.kraken.api.core.script.node.ConditionNode;
import com.ghost.plugins.woodcutting.actions.*;
import com.ghost.plugins.woodcutting.conditions.AtBankCondition;
import com.ghost.plugins.woodcutting.conditions.AtTreesCondition;
import com.ghost.plugins.woodcutting.conditions.InventoryFullCondition;
import com.ghost.plugins.woodcutting.conditions.IsChoppingCondition;
import com.ghost.plugins.woodcutting.factory.SelectorNodeFactory;
import com.ghost.plugins.woodcutting.factory.SequenceNodeFactory;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;

import java.util.List;

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
                             SelectorNodeFactory selectorFactory) {
        super(context);
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
    }

    @Override
    protected BehaviorNode buildBehaviorTree() {
        ConditionNode bankingEnabled = () -> config.bankingEnabled();

        // --- Behavior for when inventory IS FULL ---
        // This sequence first checks if banking is enabled. If so, it walks to the bank and banks.
        // If banking is not enabled, the sequence fails and the selector moves to the dropLogsAction.
        BehaviorNode handleBanking = sequenceFactory.create(List.of(
                bankingEnabled,
                selectorFactory.create(List.of(
                        sequenceFactory.create(List.of(atBankCondition, bankAction)),
                        walkToBankAction
                ))
        ));

        BehaviorNode handleFullInventory = sequenceFactory.create(List.of(
                inventoryFullCondition,
                selectorFactory.create(List.of(
                        handleBanking,
                        dropLogsAction // Fallback action if banking is disabled
                ))
        ));

        // --- Behavior for when inventory IS NOT FULL ---
// This selector handles the core woodcutting loop.
        BehaviorNode choppingLogic = selectorFactory.create(List.of(
                sequenceFactory.create(List.of(isChoppingCondition, waitAction)), // If already chopping, just wait.
                chopTreeAction // If not chopping, find a tree to chop.
        ));

// This sequence ensures the player is at the trees before attempting to chop.
// The redundant inventory check has been removed.
        BehaviorNode performWoodcutting = selectorFactory.create(List.of(
                sequenceFactory.create(List.of(atTreesCondition, choppingLogic)), // If at trees, start chopping logic.
                walkToTreesAction // If not at trees, walk to them. This is now the fallback.
        ));

// --- ROOT NODE ---
// The root selector prioritizes handling a full inventory. If the inventory is not full, it proceeds to woodcutting.
        return selectorFactory.create(List.of(
                handleFullInventory,
                performWoodcutting
        ));
    }

    @Override
    protected void onBehaviorTreeStart() {
        log.info("Woodcutting Script started!");
    }
}