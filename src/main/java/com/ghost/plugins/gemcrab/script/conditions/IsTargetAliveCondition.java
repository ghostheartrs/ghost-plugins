package com.ghost.plugins.gemcrab.script.conditions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kraken.api.core.script.node.ConditionNode;
import com.ghost.plugins.gemcrab.script.BaseScriptNode;
import com.ghost.plugins.gemcrab.script.ScriptContext;
import net.runelite.api.Client;
import net.runelite.api.NPC;

@Singleton
public class IsTargetAliveCondition extends BaseScriptNode implements ConditionNode {

    @Inject
    public IsTargetAliveCondition(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public boolean checkCondition() {
        NPC target = context.getTargetCrab();
        if (target == null) {
            return false;
        }

        // When an NPC dies, its health ratio becomes 0.
        // A health ratio of -1 indicates that the health is not tracked.
        // So, we consider the NPC alive if the health ratio is not 0.
        boolean isAlive = target.getHealthRatio() != 0;

        if (!isAlive) {
            context.setStatus("Target is dead");
            context.setTargetCrab(null); // Clear the dead target
        } else {
            context.setStatus("Checking if target is alive");
        }

        return isAlive;
    }
}
