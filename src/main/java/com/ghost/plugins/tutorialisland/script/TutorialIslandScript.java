package com.ghost.plugins.tutorialisland.script;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.kraken.api.Context;
import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.BehaviorTreeScript;
import com.ghost.plugins.tutorialisland.script.actions.CreateCharacterAction;
import com.ghost.plugins.tutorialisland.script.conditions.IsOnCharacterCreationScreen;
import com.ghost.plugins.woodcutting.factory.SelectorNodeFactory;
import com.ghost.plugins.woodcutting.factory.SequenceNodeFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Singleton
public class TutorialIslandScript extends BehaviorTreeScript {

    private final IsOnCharacterCreationScreen isOnCharacterCreationScreen;
    private final CreateCharacterAction createCharacterAction;

    private final SequenceNodeFactory sequenceFactory;
    private final SelectorNodeFactory selectorFactory;

    @Inject
    public TutorialIslandScript(Context context,
                                IsOnCharacterCreationScreen isOnCharacterCreationScreen,
                                CreateCharacterAction createCharacterAction,
                                SequenceNodeFactory sequenceFactory,
                                SelectorNodeFactory selectorFactory) {
        super(context);
        this.isOnCharacterCreationScreen = isOnCharacterCreationScreen;
        this.createCharacterAction = createCharacterAction;
        this.sequenceFactory = sequenceFactory;
        this.selectorFactory = selectorFactory;
    }

    @Override
    protected BehaviorNode buildBehaviorTree() {
        
        return selectorFactory.create(List.of(
                // Character Creation
                sequenceFactory.create(List.of(
                        isOnCharacterCreationScreen,
                        createCharacterAction
                ))
                // Add more steps as needed.
        ));
    }

    @Override
    protected void onBehaviorTreeStart() {
        log.info("Tutorial Island Script Started!");
    }
}