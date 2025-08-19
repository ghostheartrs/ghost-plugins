package com.krakenplugins.example.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import com.krakenplugins.example.script.Util;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import static com.krakenplugins.example.script.MiningScript.MINING_AREA;

@Slf4j
public class WalkToMiningAreaAction extends BaseScriptNode implements ActionNode {

    @Inject
    public WalkToMiningAreaAction(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public BehaviorResult performAction() {
        log.info("Walking to mining area at {}", MINING_AREA);

        if (Util.walkTo(MINING_AREA)) {
            return BehaviorResult.RUNNING;
        } else {
            log.warn("Failed to initiate walk to mining area");
            return BehaviorResult.FAILURE;
        }
    }
}