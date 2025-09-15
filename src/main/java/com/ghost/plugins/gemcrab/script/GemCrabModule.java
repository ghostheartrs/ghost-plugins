package com.ghost.plugins.gemcrab.script;

import com.google.inject.AbstractModule;
import com.ghost.plugins.gemcrab.script.factory.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GemCrabModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SequenceNodeFactory.class).to(SequenceNodeFactoryImpl.class);
        bind(RepeatNodeFactory.class).to(RepeatNodeFactoryImpl.class);
        bind(SelectorNodeFactory.class).to(SelectorNodeFactoryImpl.class);
        bind(GemCrabScript.CrawlThroughCaveAction.class);
    }
}