package com.ghost.plugins.woodcutting.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.kraken.api.interaction.inventory.InventoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InventoryFullCondition implements ConditionNode {

    private final InventoryService inventoryService;

    @Inject
    public InventoryFullCondition(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    public boolean checkCondition() {
        return inventoryService.all().size() >= 28;
    }
}
