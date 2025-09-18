package com.ghost.plugins.mining.script.actions;

import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.bank.BankService;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.ghost.plugins.mining.script.BaseScriptNode;
import com.ghost.plugins.mining.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;

import javax.inject.Inject;

import static com.ghost.plugins.mining.script.MiningScript.IRON_ORE_ID;

@Slf4j
public class BankOreAction extends BaseScriptNode implements ActionNode {

    private final GameObjectService gameObjectService;
    private final SleepService sleepService;
    private final BankService bankService;

    @Inject
    public BankOreAction(Client client, ScriptContext context, GameObjectService gameObjectService, SleepService sleepService, BankService bankService) {
        super(client, context);
        this.gameObjectService = gameObjectService;
        this.sleepService = sleepService;
        this.bankService = bankService;
    }

    @Override
    public BehaviorResult performAction() {
        context.setStatus("Banking Ore");
        try {
            // Find bank booth or banker
            TileObject bankBooth = gameObjectService.all((o) -> o.getId() == 10583, 10).stream().findFirst().orElse(null);
            if (bankBooth != null) {
                log.info("Opening bank booth");
                gameObjectService.interact(bankBooth, "Bank");
                sleepService.sleep(600, 1800);

                // Deposit all iron ore
                if(bankService.isOpen()) {
                    bankService.depositAll(IRON_ORE_ID);
                    // TODO also deposit gems
                }

                sleepService.sleep(600, 1200);

                bankService.close();
                sleepService.sleepUntil(() -> !bankService.isOpen(), 2000);

                if (!bankService.isOpen()) {
                    log.info("Bank closed successfully");
                    return BehaviorResult.SUCCESS;
                } else {
                    log.warn("Failed to close bank.");
                    return BehaviorResult.FAILURE;
                }
            } else {
                log.warn("No bank booth or banker found at bank location");
                return BehaviorResult.FAILURE;
            }
        } catch (Exception e) {
            log.error("Error while banking", e);
            return BehaviorResult.FAILURE;
        }
    }
}
