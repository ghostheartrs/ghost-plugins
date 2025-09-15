package com.ghost.plugins.gemcrab.script;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kraken.api.Context;
import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.BehaviorTreeScript;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.ghost.plugins.gemcrab.script.actions.*;
import com.ghost.plugins.gemcrab.script.conditions.*;
import com.ghost.plugins.gemcrab.script.factory.RepeatNodeFactory;
import com.ghost.plugins.gemcrab.script.factory.SelectorNodeFactory;
import com.ghost.plugins.gemcrab.script.factory.SequenceNodeFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

import java.util.Arrays;
import java.util.List;

import static com.ghost.plugins.gemcrab.script.Util.getRandomDelay;

@Slf4j
@Singleton
public class GemCrabScript extends BehaviorTreeScript {

    public static final List<Integer> GEMSTONE_CRAB_IDS = List.of(14779);
    public static final WorldPoint CRAB_AREA = new WorldPoint(1655, 3090, 0);
    public static final WorldPoint WAIT_AREA = new WorldPoint(1680, 3090, 0);

    private final HasTargetCondition hasTargetCondition;
    private final IsTargetAliveCondition isTargetAliveCondition;
    private final IsInCombatCondition isInCombatCondition;
    private final WaitAction waitAction;
    private final AttackCrabAction attackCrabAction;
    private final WalkToWaitAreaAction walkToWaitAreaAction;
    private final WaitInTunnelAction waitInTunnelAction;
    private final WalkToCrabAreaAction walkToCrabAreaAction;
    private final FindCrabAction findCrabAction;
    private final CrawlThroughCaveAction crawlThroughCaveAction;
    private final FallbackWaitAction fallbackWaitAction;
    private final SequenceNodeFactory sequenceFactory;
    private final SelectorNodeFactory selectorFactory;
    private final RepeatNodeFactory repeatFactory;

    @Getter
    private final ScriptContext scriptContext;

    @Inject
    public GemCrabScript(Context context, ScriptContext scriptContext, HasTargetCondition hasTargetCondition, IsTargetAliveCondition isTargetAliveCondition, IsInCombatCondition isInCombatCondition, WaitAction waitAction, AttackCrabAction attackCrabAction, WalkToWaitAreaAction walkToWaitAreaAction, WaitInTunnelAction waitInTunnelAction, WalkToCrabAreaAction walkToCrabAreaAction, FindCrabAction findCrabAction, CrawlThroughCaveAction crawlThroughCaveAction, FallbackWaitAction fallbackWaitAction, SequenceNodeFactory sequenceFactory, SelectorNodeFactory selectorFactory, RepeatNodeFactory repeatFactory) {
        super(context);
        this.scriptContext = scriptContext;
        this.hasTargetCondition = hasTargetCondition;
        this.isTargetAliveCondition = isTargetAliveCondition;
        this.isInCombatCondition = isInCombatCondition;
        this.waitAction = waitAction;
        this.attackCrabAction = attackCrabAction;
        this.walkToWaitAreaAction = walkToWaitAreaAction;
        this.waitInTunnelAction = waitInTunnelAction;
        this.walkToCrabAreaAction = walkToCrabAreaAction;
        this.findCrabAction = findCrabAction;
        this.crawlThroughCaveAction = crawlThroughCaveAction;
        this.fallbackWaitAction = fallbackWaitAction;
        this.sequenceFactory = sequenceFactory;
        this.selectorFactory = selectorFactory;
        this.repeatFactory = repeatFactory;
    }

    @Override
    protected BehaviorNode buildBehaviorTree() {
        BehaviorNode fightSequence = sequenceFactory.create(Arrays.asList(
                isInCombatCondition,
                waitAction
        ));

        BehaviorNode attackSelector = selectorFactory.create(Arrays.asList(
                fightSequence,
                attackCrabAction
        ));

        BehaviorNode targetAliveSequence = sequenceFactory.create(Arrays.asList(
                isTargetAliveCondition,
                attackSelector
        ));

        BehaviorNode targetDeadSequence = sequenceFactory.create(Arrays.asList(
                crawlThroughCaveAction,
                findCrabAction
        ));

        BehaviorNode handleTargetSelector = selectorFactory.create(Arrays.asList(
                targetAliveSequence,
                targetDeadSequence
        ));

        BehaviorNode handleExistingTargetSequence = sequenceFactory.create(Arrays.asList(
                hasTargetCondition,
                handleTargetSelector
        ));

        BehaviorNode fallbackRespawnSequence = sequenceFactory.create(Arrays.asList(
                walkToWaitAreaAction,
                waitInTunnelAction, // Wait for respawn
                walkToCrabAreaAction,
                findCrabAction
        ));

        BehaviorNode rootSelector = selectorFactory.create(Arrays.asList(
                handleExistingTargetSequence,
                findCrabAction, // Find a crab if we don't have one
                fallbackRespawnSequence, // If finding fails, do the full respawn walk
                fallbackWaitAction
        ));

        return repeatFactory.create(rootSelector);
    }

    @Override
    protected void onBehaviorTreeStart() {
        log.info("GemCrab Script started!");
    }

    @Override
    protected long onBehaviorTreeSuccess() {
        return getRandomDelay(1000, 3000);
    }

    @Override
    protected long onBehaviorTreeFailure() {
        log.debug("Behavior tree iteration failed, retrying...");
        return getRandomDelay(500, 1000);
    }

    @Override
    protected long onBehaviorTreeRunning() {
        return getRandomDelay(2000, 3000);
    }

    @Override
    protected long getDefaultLoopDelay() {
        return getRandomDelay(100, 300);
    }

    @Slf4j
    public static class CrawlThroughCaveAction extends BaseScriptNode implements ActionNode {

        private final GameObjectService gameObjectService;

        @Inject
        public CrawlThroughCaveAction(Client client, ScriptContext context, GameObjectService gameObjectService) {
            super(client, context);
            this.gameObjectService = gameObjectService;
        }

        @Override
        public BehaviorResult performAction() {
            context.setStatus("Using cave shortcut");

            Player player = client.getLocalPlayer();
            if (player == null) {
                return BehaviorResult.FAILURE;
            }

            GameObject cave = gameObjectService.getGameObjects(o -> o.getId() == 57631, player.getWorldLocation(), 15)
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (cave != null) {
                if (gameObjectService.interact(cave, "Crawl-through")) {
                    return BehaviorResult.SUCCESS;
                }
            }

            return BehaviorResult.FAILURE;
        }
    }
}
