package com.krakenplugins.ghost.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitAction implements ActionNode {
    private final SleepService sleepService;

    @Inject
    public WaitAction(SleepService sleepService) {
        this.sleepService = sleepService;
    }

    @Override
    public BehaviorResult performAction() {
        sleepService.sleep(500, 1000);
        return BehaviorResult.SUCCESS;
    }
}
