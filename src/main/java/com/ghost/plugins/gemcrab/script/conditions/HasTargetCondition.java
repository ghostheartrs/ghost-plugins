package com.ghost.plugins.gemcrab.script.conditions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kraken.api.core.script.node.ConditionNode;
import com.ghost.plugins.gemcrab.script.BaseScriptNode;
import com.ghost.plugins.gemcrab.script.ScriptContext;
import net.runelite.api.Client;

@Singleton
public class HasTargetCondition extends BaseScriptNode implements ConditionNode {

    @Inject
    public HasTargetCondition(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public boolean checkCondition() {
        context.setStatus("Checking for target");
        return context.getTargetCrab() != null;
    }
}
