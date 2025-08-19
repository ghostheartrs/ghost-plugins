package com.krakenplugins.example.script;

import com.google.inject.Inject;
import com.kraken.api.Context;
import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.BehaviorTreeScript;
import com.krakenplugins.example.script.actions.*;
import com.krakenplugins.example.script.conditions.AtBankCondition;
import com.krakenplugins.example.script.conditions.InMiningAreaCondition;
import com.krakenplugins.example.script.conditions.InventoryFullCondition;
import com.krakenplugins.example.script.conditions.IsMiningCondition;
import com.krakenplugins.example.script.factory.RepeatNodeFactory;
import com.krakenplugins.example.script.factory.SelectorNodeFactory;
import com.krakenplugins.example.script.factory.SequenceNodeFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.runelite.api.coords.WorldArea;

import java.util.List;

import static com.krakenplugins.example.script.Util.getRandomDelay;

@Slf4j
public class MiningScript extends BehaviorTreeScript {

    public static final int IRON_ORE_ID = 440;
    public static final List<Integer> IRON_ROCK_ID = List.of(11364, 11365);
    public static final int IRON_ROCK_DEPLETED_ID = 11391;
    public static final WorldArea MINING_AREA = new WorldArea(3279, 3371, 16, 16, 0);
    public static final WorldArea BANK_AREA = new WorldArea(3250, 3423, 7, 7, 0); // Varrock East bank
    public static final int MINING_AREA_RADIUS = 10;
    public static final int BANK_RADIUS = 3;

    private final InventoryFullCondition inventoryFullCondition;
    private final AtBankCondition atBankCondition;
    private final BankOreAction bankOreAction;
    private final WalkToBankAction walkToBankAction;
    private final InMiningAreaCondition inMiningAreaCondition;
    private final IsMiningCondition isMiningCondition;
    private final WaitAction waitAction;
    private final FindIronRockAction findIronRockAction;
    private final ClickRockAction clickRockAction;
    private final WalkToMiningAreaAction walkToMiningAreaAction;
    private final FallbackWaitAction fallbackWaitAction;
    private final SequenceNodeFactory sequenceFactory;
    private final SelectorNodeFactory selectorFactory;
    private final RepeatNodeFactory repeatFactory;

    @Inject
    public MiningScript(Context context, InventoryFullCondition inventoryFullCondition, AtBankCondition atBankCondition, BankOreAction bankOreAction, WalkToBankAction walkToBankAction, InMiningAreaCondition inMiningAreaCondition, IsMiningCondition isMiningCondition, WaitAction waitAction, FindIronRockAction findIronRockAction, ClickRockAction clickRockAction, WalkToMiningAreaAction walkToMiningAreaAction, FallbackWaitAction fallbackWaitAction, SequenceNodeFactory sequenceFactory, SelectorNodeFactory selectorFactory, RepeatNodeFactory repeatFactory) {
        super(context);
        this.inventoryFullCondition = inventoryFullCondition;
        this.atBankCondition = atBankCondition;
        this.bankOreAction = bankOreAction;
        this.walkToBankAction = walkToBankAction;
        this.inMiningAreaCondition = inMiningAreaCondition;
        this.isMiningCondition = isMiningCondition;
        this.waitAction = waitAction;
        this.findIronRockAction = findIronRockAction;
        this.clickRockAction = clickRockAction;
        this.walkToMiningAreaAction = walkToMiningAreaAction;
        this.fallbackWaitAction = fallbackWaitAction;
        this.sequenceFactory = sequenceFactory;
        this.selectorFactory = selectorFactory;
        this.repeatFactory = repeatFactory;
    }

    @Override
    protected BehaviorNode buildBehaviorTree() {
        return selectorFactory.create(List.of(
                // Priority 1: Handle full inventory
                sequenceFactory.create(List.of(
                        inventoryFullCondition,
                        selectorFactory.create(List.of(
                                sequenceFactory.create(List.of(
                                        atBankCondition,
                                        repeatFactory.create(bankOreAction, 1)
                                )),
                                walkToBankAction
                        ))
                )),

                // Priority 2: Mine iron ore
                sequenceFactory.create(List.of(
                        inMiningAreaCondition,
                        selectorFactory.create(List.of(
                                sequenceFactory.create(List.of(
                                        isMiningCondition,
                                        waitAction
                                )),
                                sequenceFactory.create(List.of(
                                        findIronRockAction,
                                        clickRockAction
                                ))
                        ))
                )),

                // Priority 3: Walk to mining area
                walkToMiningAreaAction,

                // Fallback
                fallbackWaitAction
        ));
    }

    @Override
    protected void onBehaviorTreeStart() {
        log.info("Iron Mining Script started!");
    }

    @Override
    protected long onBehaviorTreeSuccess() {
        return getRandomDelay(100, 300);
    }

    @Override
    protected long onBehaviorTreeFailure() {
        log.debug("Behavior tree iteration failed, retrying...");
        return getRandomDelay(500, 1000);
    }

    @Override
    protected long onBehaviorTreeRunning() {
        return getRandomDelay(50, 150);
    }

    @Override
    protected long getDefaultLoopDelay() {
        return getRandomDelay(100, 300);
    }
}
