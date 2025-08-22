package com.krakenplugins.example.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

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
        if (context.getTargetRock() == null) {
            log.warn("No target rock selected");
            return BehaviorResult.FAILURE;
        }

        if (gameObjectService.interact(context.getTargetRock(), "Mine")) {
            context.setLastActionTime(System.currentTimeMillis());
            sleepService.sleep(800, 1200);
            return BehaviorResult.SUCCESS;
        } else {
            log.warn("Failed to click iron rock");
            context.setTargetRock(null);
            return BehaviorResult.FAILURE;
        }
    }
}
