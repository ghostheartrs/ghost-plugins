package com.krakenplugins.ghost.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.bank.BankService;
import com.kraken.api.interaction.gameobject.GameObjectService;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;

@Slf4j
public class BankAction implements ActionNode {

    private final GameObjectService gameObjectService;
    private final SleepService sleepService;
    private final BankService bankService;
    private final Client client;

    @Inject
    public BankAction(GameObjectService gameObjectService, SleepService sleepService, BankService bankService, Client client) {
        this.gameObjectService = gameObjectService;
        this.sleepService = sleepService;
        this.bankService = bankService;
        this.client = client;
    }

    @Override
    public BehaviorResult performAction() {
        if (bankService.isOpen()) {
            log.info("Depositing logs.");
            bankService.depositAll(item -> {
                String name = item.getName();
                return name != null && name.toLowerCase().contains("logs");
            });
            sleepService.sleep(300, 600);
            bankService.close();
            sleepService.sleepUntil(() -> !bankService.isOpen(), 2000);
            return !bankService.isOpen() ? BehaviorResult.SUCCESS : BehaviorResult.FAILURE;
        }

        GameObject bankBooth = gameObjectService.findReachableObject("", false, 20, client.getLocalPlayer().getWorldLocation(), true, "Bank");
        if (bankBooth == null) {
            log.warn("No bank booth found.");
            return BehaviorResult.FAILURE;
        }

        log.info("Opening bank.");
        if (gameObjectService.interact(bankBooth, "Bank")) {
            sleepService.sleepUntil(bankService::isOpen, 5000);
            if (bankService.isOpen()) {
                return BehaviorResult.RUNNING; // Re-run to deposit
            }
        }

        log.warn("Failed to open the bank.");
        return BehaviorResult.FAILURE;
    }
}
