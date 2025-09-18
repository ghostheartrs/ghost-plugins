package com.ghost.plugins.tutorialisland.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.widget.WidgetService;
import com.ghost.plugins.tutorialisland.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.widgets.Widget;

@Slf4j
public class CreateCharacterAction implements ActionNode {

    private final WidgetService widgetService;
    private final SleepService sleepService;
    private final ScriptContext context;

    @Inject
    public CreateCharacterAction(WidgetService widgetService, SleepService sleepService, ScriptContext context) {
        this.widgetService = widgetService;
        this.sleepService = sleepService;
        this.context = context;
    }

    @Override
    public BehaviorResult performAction() {
        context.setStatus("Creating character...");

        Widget confirmButton = widgetService.findWidget("Confirm");

        if (confirmButton != null) {
            log.info("Found confirm button, clicking to create character.");
            widgetService.interact(confirmButton, "Confirm");
            sleepService.sleepUntil(() -> widgetService.findWidget("Confirm") == null, 5000);
            return BehaviorResult.SUCCESS;
        }

        return BehaviorResult.FAILURE;
    }
}