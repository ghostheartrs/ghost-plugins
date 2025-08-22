package com.krakenplugins.example.script.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.Client;
import net.runelite.api.Player;

@Slf4j
public class IsMiningCondition extends BaseScriptNode implements ConditionNode {

    @Inject
    public IsMiningCondition(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public boolean checkCondition() {
        context.setStatus("Checking if mining...");
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) return false;
        return context.isPlayerMining(localPlayer);
    }
}
