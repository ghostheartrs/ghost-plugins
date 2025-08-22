package com.krakenplugins.example.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import com.krakenplugins.example.script.Util;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.TileObject;

import java.util.List;
import java.util.stream.Collectors;

import static com.krakenplugins.example.script.MiningScript.IRON_ROCK_ID;

@Slf4j
public class FindIronRockAction extends BaseScriptNode implements ActionNode {

    private final GameObjectService gameObjectService;

    @Inject
    public FindIronRockAction(Client client, ScriptContext context, GameObjectService gameObjectService) {
        super(client, context);
        this.gameObjectService = gameObjectService;
    }

    @Override
    public BehaviorResult performAction() {
        context.setStatus("Locating iron ore rocks");
        GameObject nearestRock = gameObjectService.findReachableObject("Iron rocks", true, 5, client.getLocalPlayer().getWorldLocation(), true, "Mine");

        if (nearestRock == null) {
            log.info("No available iron rocks found, waiting for respawn");
            return BehaviorResult.FAILURE;
        }

        ObjectComposition comp = gameObjectService.convertToObjectComposition(nearestRock);
        log.info("Found rock actions: {}, name = {}", comp.getActions(), comp.getName());

        // Select a rock that is closest to the player
        context.setTargetRock(nearestRock);
        return BehaviorResult.SUCCESS;
    }
}