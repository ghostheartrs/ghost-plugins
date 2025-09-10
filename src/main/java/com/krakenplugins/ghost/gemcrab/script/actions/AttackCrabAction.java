package com.krakenplugins.ghost.gemcrab.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.npc.NpcService;
import com.krakenplugins.ghost.gemcrab.script.BaseScriptNode;
import com.krakenplugins.ghost.gemcrab.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
public class AttackCrabAction extends BaseScriptNode implements ActionNode {
    private final NpcService npcService;

    @Inject
    public AttackCrabAction(Client client, ScriptContext context, NpcService npcService) {
        super(client, context);
        this.npcService = npcService;
    }

    @Override
    public BehaviorResult performAction() {
        if (context.getTargetCrab() == null) {
            return BehaviorResult.FAILURE;
        }

        context.setStatus("Attacking crab");
        if (npcService.interact(context.getTargetCrab(), "Attack")) {
            return BehaviorResult.SUCCESS;
        }

        return BehaviorResult.FAILURE;
    }
}
