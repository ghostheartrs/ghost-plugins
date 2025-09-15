package com.ghost.plugins.agility.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AgilityCourse {
    GNOME_STRONGHOLD("Gnome Stronghold"),
    DRAYNOR_VILLAGE("Draynor Village"),
    VARROCK("Varrock");

    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
