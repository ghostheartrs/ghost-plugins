package com.ghost.plugins.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.inventory.InventoryItem;
import com.kraken.api.interaction.inventory.InventoryService;
import lombok.extern.slf4j.Slf4j;
import java.util.Comparator;
import java.util.Optional;

@Slf4j
public class DropLogsAction implements ActionNode {

    private final InventoryService inventoryService;

    @Inject
    public DropLogsAction(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    public BehaviorResult performAction() {
        
        Optional<InventoryItem> logToDrop = inventoryService.all().stream()
                .filter(item -> {
                    String name = item.getName();
                    return name != null && name.toLowerCase().contains("logs");
                })
                .min(Comparator.comparingInt(InventoryItem::getSlot));

        
        if (logToDrop.isPresent()) {
            log.info("Dropping log: {}", logToDrop.get().getName());
            inventoryService.interact(logToDrop.get(), "Drop");
            return BehaviorResult.RUNNING; 
        }

        
        log.info("Finished dropping logs.");
        return BehaviorResult.SUCCESS;
    }
}