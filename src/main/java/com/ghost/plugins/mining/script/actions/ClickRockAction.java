package com.ghost.plugins.mining.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.RandomService;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.ghost.plugins.mining.script.BaseScriptNode;
import com.ghost.plugins.mining.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;

@Slf4j
public class ClickRockAction extends BaseScriptNode implements ActionNode {

    private final ScriptContext context;
    private final SleepService sleepService;
    private final GameObjectService gameObjectService;

    @Inject
    public ClickRockAction(Client client, ScriptContext context, SleepService sleepService, GameObjectService gameObjectService) {
        super(client, context);
        this.context = context;
        this.sleepService = sleepService;
        this.gameObjectService = gameObjectService;
    }

    @Override
    public BehaviorResult performAction() {
        context.setStatus("Clicking Rock");
        GameObject nearestRock = gameObjectService.findReachableObject("Iron rocks", true, 5, client.getLocalPlayer().getWorldLocation(), true, "Mine");

        if (nearestRock == null) {
           log.info("No available iron rocks found, waiting for respawn");
           return BehaviorResult.FAILURE;
        }

        context.setTargetRock(nearestRock);

        // TODO This game object interaction is just messed up so many yellow clicks and it messes up banking as well.
        if (gameObjectService.interact(nearestRock, "Mine")) {
            sleepService.sleepUntil(() -> context.isPlayerMining(client.getLocalPlayer()), RandomService.between(1200, 2000));
            return BehaviorResult.SUCCESS;
        } else {
            context.setTargetRock(null);
            return BehaviorResult.FAILURE;
        }
    }
}
