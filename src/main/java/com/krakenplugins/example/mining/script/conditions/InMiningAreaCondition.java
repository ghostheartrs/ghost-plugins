package com.krakenplugins.example.mining.script.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.kraken.api.interaction.inventory.InventoryService;
import com.kraken.api.interaction.player.PlayerService;
import com.krakenplugins.example.mining.script.BaseScriptNode;
import com.krakenplugins.example.mining.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import static com.krakenplugins.example.mining.script.MiningScript.MINING_AREA;

@Slf4j
public class InMiningAreaCondition extends BaseScriptNode implements ConditionNode {

    private final PlayerService playerService;
    private final InventoryService inventoryService;

    @Inject
    public InMiningAreaCondition(Client client, ScriptContext context, PlayerService playerService, InventoryService inventoryService) {
        super(client, context);
        this.playerService = playerService;
        this.inventoryService = inventoryService;
    }

    @Override
    public boolean checkCondition() {
        // Before we even check for target rocks we need to both:
        // - be in the mining area
        // - Not have a full inventory
        return playerService.isInArea(MINING_AREA) && inventoryService.all().size() < 28;
    }
}