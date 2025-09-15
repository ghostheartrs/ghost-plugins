package com.ghost.plugins.gemcrab.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.ghost.plugins.gemcrab.script.BaseScriptNode;
import com.ghost.plugins.gemcrab.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
public class WaitInTunnelAction extends BaseScriptNode implements ActionNode {
    private final SleepService sleepService;

    @Inject
    public WaitInTunnelAction(Client client, ScriptContext context, SleepService sleepService) {
        super(client, context);
        this.sleepService = sleepService;
    }

    @Override
    public BehaviorResult performAction() {
        context.setStatus("Waiting for respawn");
        sleepService.sleep(5000, 10000);
        return BehaviorResult.SUCCESS;
    }
}
