package com.krakenplugins.example.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.MiningScript;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;

import static com.krakenplugins.example.script.MiningScript.BANK_AREA;
import static com.krakenplugins.example.script.MiningScript.MINING_AREA;
import static com.krakenplugins.example.script.Util.getRandomDelay;
import static com.krakenplugins.example.script.Util.sleep;

@Slf4j
public class FallbackWaitAction extends BaseScriptNode implements ActionNode {

    private final SleepService sleepService;

    @Inject
    public FallbackWaitAction(Client client, ScriptContext context, SleepService sleepService) {
        super(client, context);
        this.sleepService = sleepService;
    }

    @Override
    public BehaviorResult performAction() {
        log.debug("Fallback action - waiting and assessing situation");

        // Log current state for debugging
        WorldPoint playerLoc = client.getLocalPlayer().getWorldLocation();
        log.debug("Player location: {}", playerLoc);
        log.debug("Distance to mining area: {}", playerLoc.distanceTo(MINING_AREA));
        log.debug("Distance to bank: {}", playerLoc.distanceTo(BANK_AREA));

        sleepService.sleep(1000, 2000);
        return BehaviorResult.SUCCESS;
    }
}
