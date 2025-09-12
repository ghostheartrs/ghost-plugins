package com.krakenplugins.ghost.woodcutting;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kraken.api.Context;
import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.BehaviorTreeScript;
import com.krakenplugins.ghost.woodcutting.actions.ChopTreeAction;
import com.krakenplugins.ghost.woodcutting.actions.DropLogsAction;
import com.krakenplugins.ghost.woodcutting.actions.WaitAction;
import com.krakenplugins.ghost.woodcutting.conditions.InventoryFullCondition;
import com.krakenplugins.ghost.woodcutting.conditions.IsChoppingCondition;
import com.krakenplugins.ghost.woodcutting.factory.SelectorNodeFactory;
import com.krakenplugins.ghost.woodcutting.factory.SequenceNodeFactory;
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

    private final InventoryFullCondition inventoryFullCondition;
    private final DropLogsAction dropLogsAction;
    private final IsChoppingCondition isChoppingCondition;
    private final WaitAction waitAction;
    private final ChopTreeAction chopTreeAction;
    private final SequenceNodeFactory sequenceFactory;
    private final SelectorNodeFactory selectorFactory;

    @Inject
    public WoodcuttingScript(Context context,
                             InventoryFullCondition inventoryFullCondition,
                             DropLogsAction dropLogsAction,
                             IsChoppingCondition isChoppingCondition,
                             WaitAction waitAction,
                             ChopTreeAction chopTreeAction,
                             SequenceNodeFactory sequenceFactory,
                             SelectorNodeFactory selectorFactory) {
        super(context);
        this.inventoryFullCondition = inventoryFullCondition;
        this.dropLogsAction = dropLogsAction;
        this.isChoppingCondition = isChoppingCondition;
        this.waitAction = waitAction;
        this.chopTreeAction = chopTreeAction;
        this.sequenceFactory = sequenceFactory;
        this.selectorFactory = selectorFactory;
    }

    @Override
    protected BehaviorNode buildBehaviorTree() {
        return selectorFactory.create(List.of(
                sequenceFactory.create(List.of(
                        inventoryFullCondition,
                        dropLogsAction
                )),
                sequenceFactory.create(List.of(
                        isChoppingCondition,
                        waitAction
                )),
                chopTreeAction
        ));
    }

    @Override
    protected void onBehaviorTreeStart() {
        log.info("Woodcutting Script started!");
    }
}
