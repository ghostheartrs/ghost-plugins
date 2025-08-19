package com.krakenplugins.example.script.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

import static com.krakenplugins.example.script.MiningScript.BANK_AREA;
import static com.krakenplugins.example.script.MiningScript.BANK_RADIUS;

@Slf4j
public class AtBankCondition extends BaseScriptNode implements ConditionNode {

    @Inject
    public AtBankCondition(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public boolean checkCondition() {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        boolean atBank = playerLocation.distanceTo(BANK_AREA) <= BANK_RADIUS;

        if (atBank) {
            log.info("Player is at bank location");
        }

        return atBank;
    }
}
