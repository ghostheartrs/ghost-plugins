package com.ghost.plugins.tutorialisland;

import com.google.inject.AbstractModule;
import com.ghost.plugins.woodcutting.factory.SelectorNodeFactory;
import com.ghost.plugins.woodcutting.factory.SelectorNodeFactoryImpl;
import com.ghost.plugins.woodcutting.factory.SequenceNodeFactory;
import com.ghost.plugins.woodcutting.factory.SequenceNodeFactoryImpl;

public class TutorialIslandModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(SequenceNodeFactory.class).to(SequenceNodeFactoryImpl.class);
        bind(SelectorNodeFactory.class).to(SelectorNodeFactoryImpl.class);
    }
}