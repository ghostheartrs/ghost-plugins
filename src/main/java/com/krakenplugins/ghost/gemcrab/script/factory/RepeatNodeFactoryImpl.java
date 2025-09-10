package com.krakenplugins.ghost.gemcrab.script.factory;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.node.RepeatNode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(onConstructor_ =  {@Inject})
public class RepeatNodeFactoryImpl implements RepeatNodeFactory {

    @Override
    public RepeatNode create(BehaviorNode child) {
        return new RepeatNode(child, Integer.MAX_VALUE);
    }

    @Override
    public RepeatNode create(BehaviorNode child, int times) {
        return new RepeatNode(child, times);
    }
}
