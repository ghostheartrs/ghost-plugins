package com.ghost.plugins.gemcrab.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.npc.NpcService;
import com.ghost.plugins.gemcrab.script.BaseScriptNode;
import com.ghost.plugins.gemcrab.script.GemCrabScript;
import com.ghost.plugins.gemcrab.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;

@Slf4j
public class FindCrabAction extends BaseScriptNode implements ActionNode {

    private final NpcService npcService;

    @Inject
    public FindCrabAction(Client client, ScriptContext context, NpcService npcService) {
        super(client, context);
        this.npcService = npcService;
    }

    @Override
    public BehaviorResult performAction() {
        context.setStatus("Finding crab");

        NPC target = npcService.getNpcs(npc -> GemCrabScript.GEMSTONE_CRAB_IDS.contains(npc.getId())).findFirst().orElse(null);

        if (target != null) {
            context.setTargetCrab(target);
            return BehaviorResult.SUCCESS;
        }

        return BehaviorResult.FAILURE;
    }
}
