package com.krakenplugins.example.script.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

import static com.krakenplugins.example.script.MiningScript.MINING_AREA;
import static com.krakenplugins.example.script.MiningScript.MINING_AREA_RADIUS;

@Slf4j
public class InMiningAreaCondition extends BaseScriptNode implements ConditionNode {

    @Inject
    public InMiningAreaCondition(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public boolean checkCondition() {
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        boolean inArea = playerLocation.distanceTo(MINING_AREA) <= MINING_AREA_RADIUS;

        if (!inArea) {
            log.info("Player not in mining area, current location: X={}, Y={}, Distance: {}", playerLocation.getX(), playerLocation.getY(), playerLocation.distanceTo(MINING_AREA));
        }
        return inArea;
    }
}