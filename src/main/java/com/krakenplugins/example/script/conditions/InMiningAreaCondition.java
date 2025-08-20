package com.krakenplugins.example.script.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.kraken.api.interaction.player.PlayerService;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import static com.krakenplugins.example.script.MiningScript.MINING_AREA;

@Slf4j
public class InMiningAreaCondition extends BaseScriptNode implements ConditionNode {

    private final PlayerService playerService;

    @Inject
    public InMiningAreaCondition(Client client, ScriptContext context, PlayerService playerService) {
        super(client, context);
        this.playerService = playerService;
    }

    @Override
    public boolean checkCondition() {
        log.info("User is mining area: {}", playerService.isInArea(MINING_AREA));
        return playerService.isInArea(MINING_AREA);
    }
}