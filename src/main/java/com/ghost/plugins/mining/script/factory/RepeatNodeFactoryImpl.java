package com.ghost.plugins.mining.script.factory;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.node.RepeatNode;
import lombok.NoArgsConstructor;

@NoArgsConstructor(onConstructor_ =  {@Inject})
public class RepeatNodeFactoryImpl implements RepeatNodeFactory {

    @Override
    public RepeatNode create(BehaviorNode child, int times) {
        return new RepeatNode(child, times);
    }
}