package com.ghost.plugins.woodcutting.conditions;

import com.ghost.plugins.woodcutting.WoodcuttingConfig;
import com.ghost.plugins.woodcutting.types.BankLocation;
import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;

@Slf4j
public class AtBankCondition implements ConditionNode {

    private static final int BANK_RADIUS = 5;
    private final WoodcuttingConfig config;
    private final Client client;

    @Inject
    public AtBankCondition(WoodcuttingConfig config, Client client) {
        this.config = config;
        this.client = client;
    }

    @Override
    public boolean checkCondition() {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null || !config.bankingEnabled()) {
            return false;
        }

        BankLocation selectedBank = config.bankLocation();
        return localPlayer.getWorldLocation().distanceTo(selectedBank.getWorldPoint()) <= BANK_RADIUS;
    }
}