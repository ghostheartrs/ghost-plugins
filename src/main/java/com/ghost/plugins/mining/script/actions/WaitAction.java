package com.ghost.plugins.mining.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.ghost.plugins.mining.script.BaseScriptNode;
import com.ghost.plugins.mining.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
public class WaitAction extends BaseScriptNode implements ActionNode {
    private final SleepService sleepService;

    @Inject
    public WaitAction(Client client, ScriptContext context, SleepService sleepService) {
        super(client, context);
        this.sleepService = sleepService;
    }

    @Override
    public BehaviorResult performAction() {
        context.setStatus("Waiting...");
        sleepService.sleep(100, 500);
        return BehaviorResult.SUCCESS;
    }
}