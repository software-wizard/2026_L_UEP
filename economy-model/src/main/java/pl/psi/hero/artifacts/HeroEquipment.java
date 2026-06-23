package pl.psi.hero.artifacts;

import lombok.Getter;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class HeroEquipment {

    private final Map<ArtifactSlot, Artifact> equippedArtifacts =
            new EnumMap<>(ArtifactSlot.class);

    public Artifact equip(ArtifactSlot slot, Artifact artifact) {
        return equippedArtifacts.put(slot, artifact);
    }

    public Artifact unequip(ArtifactSlot slot) {
        return equippedArtifacts.remove(slot);
    }

    public Collection<Artifact> getEquippedArtifacts() {
        return equippedArtifacts.values();
    }
}