package com.krakenplugins.ghost.gemcrab.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.movement.MovementService;
import com.krakenplugins.ghost.gemcrab.script.BaseScriptNode;
import com.krakenplugins.ghost.gemcrab.script.GemCrabScript;
import com.krakenplugins.ghost.gemcrab.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
public class WalkToCrabAreaAction extends BaseScriptNode implements ActionNode {

    private final MovementService movementService;

    @Inject
    public WalkToCrabAreaAction(Client client, ScriptContext context, MovementService movementService) {
        super(client, context);
        this.movementService = movementService;
    }

    @Override
    public BehaviorResult performAction() {
        if (client.getLocalPlayer() == null) {
            return BehaviorResult.FAILURE;
        }

        if (client.getLocalPlayer().getWorldLocation().distanceTo(GemCrabScript.CRAB_AREA) < 5) {
            return BehaviorResult.SUCCESS;
        }

        context.setStatus("Walking to crab area");
        if (movementService.walkTo(GemCrabScript.CRAB_AREA)) {
            return BehaviorResult.SUCCESS;
        }

        return BehaviorResult.FAILURE;
    }
}
