package com.krakenplugins.ghost.gemcrab.script.factory;

import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.node.RepeatNode;

public interface RepeatNodeFactory {
    RepeatNode create(BehaviorNode child);
    RepeatNode create(BehaviorNode child, int times);
}
