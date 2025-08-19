package com.krakenplugins.example.script.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.kraken.api.interaction.inventory.InventoryService;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.MiningScript;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemContainer;

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
        boolean isFull = (long) inventoryService.all().size() == 28;
        boolean hasIron = inventoryService.all().stream().anyMatch(item -> item.getId() == MiningScript.IRON_ORE_ID);

        if (isFull && hasIron) {
            log.info("Inventory is full, and we have iron need to bank");
        }

        return isFull && hasIron;
    }
}