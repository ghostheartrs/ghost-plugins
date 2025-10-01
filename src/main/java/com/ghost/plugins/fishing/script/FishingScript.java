package com.ghost.plugins.fishing.script;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kraken.api.Context;
import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.BehaviorTreeScript;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.AnimationID;
import net.runelite.api.Skill;
import com.ghost.plugins.fishing.FishingConfig;
import com.ghost.plugins.fishing.script.ScriptContext;

import java.util.List;

@Slf4j
@Singleton
public class FishingScript extends BehaviorTreeScript {

    public static final List<Integer> FISHING_ANIMATIONS = List.of(
            AnimationID.FISHING_NET,
            AnimationID.FISHING_POLE_CAST
    );

    private final FishingConfig config;

    @Getter
    private final ScriptContext scriptContext;
    private final Context context;

    @Inject
    public FishingScript(Context context, FishingConfig config, ScriptContext scriptContext) {
        super(context);
        this.context = context;
        this.config = config;
        this.scriptContext = scriptContext;
    }

    @Override
    protected BehaviorNode buildBehaviorTree() {
        // We will build the behavior tree logic here in the next steps.
        // For now, it returns a simple node that does nothing.
        return () -> {
            scriptContext.setStatus("Behavior Tree not implemented.");
            return com.kraken.api.core.script.BehaviorResult.SUCCESS;
        };
    }

    @Override
    protected void onBehaviorTreeStart() {
        log.info("Fishing Script started!");
        scriptContext.setStartTime(System.currentTimeMillis());
        scriptContext.setStartXp(context.getClient().getSkillExperience(Skill.FISHING));
        scriptContext.setFishCaught(0);
        scriptContext.setTargetFishingSpot(null);
    }

    @Override
    protected long onBehaviorTreeSuccess() {
        return 600; // Default tick delay
    }

    // Other lifecycle methods...
    @Override
    protected long onBehaviorTreeFailure() { return 600; }

    @Override
    protected long onBehaviorTreeRunning() { return 600; }
}