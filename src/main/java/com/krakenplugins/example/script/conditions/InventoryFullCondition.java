package com.krakenplugins.example.script.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.kraken.api.interaction.inventory.InventoryService;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.MiningScript;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

@Slf4j
public class InventoryFullCondition extends BaseScriptNode implements ConditionNode {

    private final InventoryService inventoryService;

    @Inject
    public InventoryFullCondition(Client client, ScriptContext context, InventoryService inventoryService) {
        super(client, context);
        this.inventoryService = inventoryService;
    }

    @Override
    public boolean checkCondition() {
        context.setStatus("Checking inventory");
        boolean isFull = (long) inventoryService.all().size() == 28;
        boolean hasIron = inventoryService.all().stream().anyMatch(item -> item.getId() == MiningScript.IRON_ORE_ID);
        return isFull && hasIron;
    }
}