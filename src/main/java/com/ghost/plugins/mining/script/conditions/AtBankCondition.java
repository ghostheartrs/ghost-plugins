package com.ghost.plugins.mining.script.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.ghost.plugins.mining.script.BaseScriptNode;
import com.ghost.plugins.mining.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

import static com.ghost.plugins.mining.script.MiningScript.BANK_AREA;

@Slf4j
public class AtBankCondition extends BaseScriptNode implements ConditionNode {

    public static final int BANK_RADIUS = 5;

    @Inject
    public AtBankCondition(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public boolean checkCondition() {
        context.setStatus("Checking if at Bank");
        // TODO Problem here where we are almost at the bank, we do our final walk to the bank and pathing stop but we haven't made it inside the bank yet
        // so then it goes back and re-calculates a new path as the player is approaching the bank. This could potentially upset the next path that needs calculated.
        // Best solution is ensuring that once we have the condition to path to an area we don't go back to that condition for a while so make it stricter.
        WorldPoint playerLocation = client.getLocalPlayer().getWorldLocation();
        boolean atBank = playerLocation.distanceTo(BANK_AREA) <= BANK_RADIUS;

        if (atBank) {
            log.info("Player is at bank location");
        } else {
            log.info("Player is not at bank location distance away: {}", playerLocation.distanceTo(BANK_AREA));
        }

        return atBank;
    }
}
