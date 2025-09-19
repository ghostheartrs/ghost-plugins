package com.ghost.plugins.woodcutting.overlay;

import com.ghost.plugins.woodcutting.WoodcuttingConfig;
import com.ghost.plugins.woodcutting.script.ScriptContext;
import com.ghost.plugins.woodcutting.WoodcuttingScript;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

@Singleton
public class ScriptOverlay extends OverlayPanel {

    private static final Color HEADER_COLOR = ColorScheme.BRAND_ORANGE;

    private final WoodcuttingScript woodcuttingScript;
    private final ScriptContext scriptContext;
    private final WoodcuttingConfig config;
    private final Client client;

    private long lastUpdateTime = 0;
    private long xpPerHour = 0;
    private long logsPerHour = 0;

    @Inject
    private ScriptOverlay(WoodcuttingScript woodcuttingScript, ScriptContext scriptContext, WoodcuttingConfig config, Client client) {
        this.woodcuttingScript = woodcuttingScript;
        this.scriptContext = scriptContext;
        this.config = config;
        this.client = client;
        setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (woodcuttingScript.isRunning()) {
            panelComponent.getChildren().clear();
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Ghost's Woodcutter")
                    .color(HEADER_COLOR)
                    .build());

            long now = System.currentTimeMillis();
            long runtime = now - scriptContext.getStartTime();
            int xpGained = client.getSkillExperience(Skill.WOODCUTTING) - scriptContext.getStartXp();

            if (now - lastUpdateTime > 2500) {
                xpPerHour = perHour(xpGained, runtime);
                logsPerHour = perHour(scriptContext.getLogsCut(), runtime);
                lastUpdateTime = now;
            }

            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Status:")
                    .right(scriptContext.getStatus())
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Runtime:")
                    .right(formatTime(runtime))
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("XP Gained:")
                    .right(String.format("%,d (%,d/hr)", xpGained, xpPerHour))
                    .build());
            panelComponent.getChildren().add(LineComponent.builder()
                    .left("Logs Cut:")
                    .right(String.format("%,d (%,d/hr)", scriptContext.getLogsCut(), logsPerHour))
                    .build());

            if (config.showDebugInfo()) {
                panelComponent.getChildren().add(TitleComponent.builder().text("Debug Info").color(Color.RED).build());
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Behavior Tree State:")
                        .right(woodcuttingScript.getBehaviorTree().getLastResult().toString())
                        .build());
                if (scriptContext.getTargetTree() != null) {
                    panelComponent.getChildren().add(LineComponent.builder()
                            .left("Target Tree:")
                            .right(scriptContext.getTargetTree().getId() + " at " + scriptContext.getTargetTree().getWorldLocation())
                            .build());
                }
            }
        }
        return super.render(graphics);
    }

    private String formatTime(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private long perHour(int count, long runtime) {
        if (runtime > 0) {
            return (long) (count * 3600000.0 / runtime);
        }
        return 0;
    }
}