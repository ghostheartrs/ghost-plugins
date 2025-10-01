package com.ghost.plugins.fishing.script.factory;

import com.kraken.api.core.script.BehaviorNode;
import com.kraken.api.core.script.node.SequenceNode;
import java.util.List;

public interface SequenceNodeFactory {
    SequenceNode create(List<BehaviorNode> children);
}