package com.krakenplugins.example.mining.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.krakenplugins.example.mining.script.BaseScriptNode;
import com.krakenplugins.example.mining.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

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
        context.setStatus("Waiting...");
        sleepService.sleep(100, 200);
        return BehaviorResult.SUCCESS;
    }
}
