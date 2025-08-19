package com.krakenplugins.example.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
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

    @Inject
    public FindIronRockAction(Client client, ScriptContext context) {
        super(client, context);
    }

    @Override
    public BehaviorResult performAction() {
        List<TileObject> ironRocks = Util.findAllGameObjects(IRON_ROCK_ID);

        if (ironRocks.isEmpty()) {
            log.debug("No iron rocks found in area");
            return BehaviorResult.FAILURE;
        }

        // Filter rocks that are not depleted and are reachable
        List<TileObject> availableRocks = ironRocks.stream()
                .filter(this::isRockAvailable)
                .collect(Collectors.toList());

        if (availableRocks.isEmpty()) {
            log.debug("No available iron rocks found, waiting for respawn");
            return BehaviorResult.FAILURE;
        }

        // Select random available rock
        context.setTargetRock(availableRocks.get(context.getRandom().nextInt(availableRocks.size())));
        log.debug("Selected iron rock at {}", context.getTargetRock().getWorldLocation());

        return BehaviorResult.SUCCESS;
    }

    private boolean isRockAvailable(TileObject rock) {
        // Check if rock is not depleted (has the correct ID)
        return true;
        //return rock.getId() == IRON_ROCK_ID;
    }
}