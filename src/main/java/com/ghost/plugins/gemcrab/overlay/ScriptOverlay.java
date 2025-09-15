package com.ghost.plugins.gemcrab.overlay;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ghost.plugins.gemcrab.script.GemCrabScript;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

import java.awt.*;

@Singleton
public class ScriptOverlay extends OverlayPanel {

    private static final Color HEADER_COLOR = ColorScheme.BRAND_ORANGE;

    private final GemCrabScript gemCrabScript;

    @Inject
    private ScriptOverlay(GemCrabScript gemCrabScript) {
        this.gemCrabScript = gemCrabScript;
        setPosition(OverlayPosition.TOP_LEFT);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
            panelComponent.getChildren().add(TitleComponent.builder()
                    .text("Auto GemCrab")
                    .color(HEADER_COLOR)
                    .build());
            panelComponent.getChildren().add(TitleComponent.builder().text("").build());
            addTextLine("Status: " + gemCrabScript.getScriptContext().getStatus());
            addTextLine("Runtime: " + gemCrabScript.getRuntimeString());
            addTextLine("Crabs Killed: " + "0"); // TODO Add this
        return super.render(graphics);
    }

    private void addTextLine(String text) {
        panelComponent.getChildren().add(LineComponent.builder()
                .left(text)
                .leftColor(Color.WHITE)
                .build());
    }
}

