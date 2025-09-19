package com.ghost.plugins.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.Context;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.bank.BankService;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.kraken.api.interaction.inventory.InventoryService;
import com.ghost.plugins.woodcutting.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Player;

@Slf4j
public class BankAction implements ActionNode {

    private final GameObjectService gameObjectService;
    private final SleepService sleepService;
    private final BankService bankService;
    private final InventoryService inventoryService;
    private final Client client;
    private final ScriptContext scriptContext;
    private final Context context;

    @Inject
    public BankAction(GameObjectService gameObjectService, SleepService sleepService, BankService bankService, InventoryService inventoryService, Client client, ScriptContext scriptContext, Context context) {
        this.gameObjectService = gameObjectService;
        this.sleepService = sleepService;
        this.bankService = bankService;
        this.inventoryService = inventoryService;
        this.client = client;
        this.scriptContext = scriptContext;
        this.context = context;
    }

    @Override
    public BehaviorResult performAction() {
        
        if (bankService.isOpen()) {
            scriptContext.setStatus("Depositing inventory...");
            log.info("Bank is open. Depositing all items.");

            bankService.depositAll();
            sleepService.sleepUntil(inventoryService::isEmpty, 3000);
            
            context.runOnClientThread(bankService::close);
            sleepService.sleepUntil(() -> !bankService.isOpen(), 2000);
            
            if (inventoryService.isEmpty()) {
                log.info("Banking successful.");
                return BehaviorResult.SUCCESS;
            } else {
                log.error("Failed to deposit items.");
                return BehaviorResult.FAILURE;
            }
        }
        
        scriptContext.setStatus("Opening bank...");
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return BehaviorResult.FAILURE;
        }

        GameObject bankBooth = gameObjectService.findReachableObject("", false, 15, localPlayer.getWorldLocation(), true, "Bank");
        if (bankBooth == null) {
            log.warn("No bank booth found to interact with.");
            return BehaviorResult.FAILURE;
        }

        log.info("Interacting with bank booth.");
        if (gameObjectService.interact(bankBooth, "Bank")) {
            sleepService.sleepUntil(bankService::isOpen, 5000);
            return BehaviorResult.RUNNING;
        }

        log.warn("Failed to interact with the bank booth.");
        return BehaviorResult.FAILURE;
    }
}