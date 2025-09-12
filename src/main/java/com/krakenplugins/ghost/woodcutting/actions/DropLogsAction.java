package com.krakenplugins.ghost.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.inventory.InventoryService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DropLogsAction implements ActionNode {

    private final InventoryService inventoryService;

    @Inject
    public DropLogsAction(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override 
    public BehaviorResult performAction() {
        log.info("Inventory is full, attempting to drop logs.");

        boolean dropped = inventoryService.dropAll(item -> {
            String name = item.getName();
            return name != null && name.toLowerCase().contains("logs");
        });

        if (dropped) {
            log.info("Successfully dropped logs.");
            return BehaviorResult.SUCCESS;
        }

        log.warn("Inventory is full, but no logs were found to drop.");
        return BehaviorResult.FAILURE;
    }
}
