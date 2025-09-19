package com.ghost.plugins.tutorialisland.script.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.kraken.api.interaction.widget.WidgetService;

public class IsOnCharacterCreationScreen implements ConditionNode {

    private final WidgetService widgetService;

    @Inject
    public IsOnCharacterCreationScreen(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    @Override
    public boolean checkCondition() {
        return widgetService.findWidget("Confirm") != null;
    }
}