package com.krakenplugins.example.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import com.krakenplugins.example.script.Util;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import static com.krakenplugins.example.script.MiningScript.BANK_AREA;

@Slf4j
public class WalkToBankAction extends BaseScriptNode implements ActionNode {

    @Inject
    public WalkToBankAction(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public BehaviorResult performAction() {
        log.info("Walking to bank at {}", BANK_AREA);

        if (Util.walkTo(BANK_AREA)) {
            return BehaviorResult.RUNNING; // Walking in progress
        } else {
            log.warn("Failed to initiate walk to bank");
            return BehaviorResult.FAILURE;
        }
    }
}