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
        context.setStatus("Waiting... (fallback)");
        sleepService.sleep(100, 200);
        return BehaviorResult.SUCCESS;
    }
}
