package pl.psi.hero.artifacts;

import pl.psi.hero.EconomyHero;

import java.util.Set;
import java.util.stream.Collectors;

public class DragonSetService {

    public static boolean hasFullDragonSet(EconomyHero hero) {

        Set<ArtifactType> equipped =
                hero.getEquipment()
                        .getEquippedArtifacts()
                        .stream()
                        .map(Artifact::getType)
                        .collect(Collectors.toSet());

        return equipped.contains(
                ArtifactType.CROWN_OF_DRAGONTOOTH)
                &&
                equipped.contains(
                        ArtifactType.DRAGON_SCALE_ARMOR)
                &&
                equipped.contains(
                        ArtifactType.DRAGON_SCALE_SHIELD)
                &&
                equipped.contains(
                        ArtifactType.RED_DRAGON_FLAME_TONGUE)
                &&
                equipped.contains(
                        ArtifactType.DRAGON_WING_TABARD);
    }
}