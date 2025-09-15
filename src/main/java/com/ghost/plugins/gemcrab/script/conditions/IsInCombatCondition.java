package com.ghost.plugins.gemcrab.script.conditions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kraken.api.core.script.node.ConditionNode;
import com.ghost.plugins.gemcrab.script.BaseScriptNode;
import com.ghost.plugins.gemcrab.script.ScriptContext;
import net.runelite.api.Client;
import net.runelite.api.Player;

@Singleton
public class IsInCombatCondition extends BaseScriptNode implements ConditionNode {

    @Inject
    public IsInCombatCondition(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public boolean checkCondition() {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null || context.getTargetCrab() == null) {
            return false;
        }

        boolean inCombat = localPlayer.getInteracting() == context.getTargetCrab();

        if (inCombat) {
            context.setStatus("In combat");
        } else {
            context.setStatus("Not in combat");
        }

        return inCombat;
    }
}
