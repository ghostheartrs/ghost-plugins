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
        // Filter rocks that are not depleted and are reachable
        List<TileObject> availableRocks = gameObjectService.getAll((o) -> IRON_ROCK_ID.contains(o.getId()), 5)
                .stream()
                .sorted((a, b) -> a.getLocalLocation().distanceTo(client.getLocalPlayer().getLocalLocation()) - b.getLocalLocation().distanceTo(client.getLocalPlayer().getLocalLocation()))
                .collect(Collectors.toList());

        if (availableRocks.isEmpty()) {
            log.debug("No available iron rocks found, waiting for respawn");
            return BehaviorResult.FAILURE;
        }

        // Select a rock that is closest to the player
        context.setTargetRock(availableRocks.get(0));
        return BehaviorResult.SUCCESS;
    }
}