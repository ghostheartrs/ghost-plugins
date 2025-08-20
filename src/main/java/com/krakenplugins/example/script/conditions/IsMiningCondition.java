package com.krakenplugins.example.script.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.Client;
import net.runelite.api.Player;

@Slf4j
public class IsMiningCondition extends BaseScriptNode implements ConditionNode {

    @Inject
    public IsMiningCondition(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public boolean checkCondition() {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) return false;

        // Check if player has mining animation
        boolean hasAnimation = localPlayer.getAnimation() == AnimationID.MINING_IRON_PICKAXE ||
                localPlayer.getAnimation() == AnimationID.MINING_STEEL_PICKAXE ||
                localPlayer.getAnimation() == AnimationID.MINING_MITHRIL_PICKAXE ||
                localPlayer.getAnimation() == AnimationID.MINING_ADAMANT_PICKAXE ||
                localPlayer.getAnimation() == AnimationID.MINING_RUNE_PICKAXE ||
                localPlayer.getAnimation() == AnimationID.MINING_DRAGON_PICKAXE;

        context.setMining(hasAnimation);
        log.info("User is mining: {}", hasAnimation);

        if (hasAnimation) {
            log.info("Player is currently mining");
        }

        return hasAnimation;
    }
}
