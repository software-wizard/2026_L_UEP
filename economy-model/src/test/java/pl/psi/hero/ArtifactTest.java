package pl.psi.hero;

import org.junit.jupiter.api.Test;
import pl.psi.hero.artifacts.Artifact;
import pl.psi.hero.artifacts.ArtifactSlot;
import pl.psi.hero.artifacts.ArtifactType;
import pl.psi.hero.artifacts.DragonSetService;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ArtifactTest {

    @Test
    void shouldEquipOwnedArtifact() {

        EconomyHero hero = new EconomyHero();

        Artifact artifact =
                new Artifact(
                        ArtifactType.ARMOR_OF_WONDER);

        hero.addArtifact(artifact);

        hero.equipArtifact(
                artifact,
                ArtifactSlot.PRIMARY_1);

        assertTrue(
                hero.getEquipment()
                        .getEquippedArtifacts()
                        .contains(artifact));
    }

    @Test
    void shouldThrowWhenArtifactNotOwned() {

        EconomyHero hero = new EconomyHero();

        Artifact artifact =
                new Artifact(
                        ArtifactType.ARMOR_OF_WONDER);

        assertThrows(
                IllegalArgumentException.class,
                () -> hero.equipArtifact(
                        artifact,
                        ArtifactSlot.PRIMARY_1));
    }
    @Test
    void shouldRejectWrongSlot() {

        EconomyHero hero = new EconomyHero();

        Artifact artifact =
                new Artifact(
                        ArtifactType.CROWN_OF_DRAGONTOOTH);

        hero.addArtifact(artifact);

        assertThrows(
                IllegalArgumentException.class,
                () -> hero.equipArtifact(
                        artifact,
                        ArtifactSlot.PRIMARY_1));
    }

    @Test
    void shouldUnequipArtifact() {

        EconomyHero hero = new EconomyHero();

        Artifact artifact =
                new Artifact(
                        ArtifactType.ARMOR_OF_WONDER);

        hero.addArtifact(artifact);

        hero.equipArtifact(
                artifact,
                ArtifactSlot.PRIMARY_1);

        hero.unequipArtifact(
                ArtifactSlot.PRIMARY_1);

        assertFalse(
                hero.getEquipment()
                        .getEquippedArtifacts()
                        .contains(artifact));
    }

    @Test
    void shouldDetectFullDragonSet() {

        EconomyHero hero = new EconomyHero();

        Artifact helm =
                new Artifact(
                        ArtifactType.CROWN_OF_DRAGONTOOTH);

        Artifact armor =
                new Artifact(
                        ArtifactType.DRAGON_SCALE_ARMOR);

        Artifact shield =
                new Artifact(
                        ArtifactType.DRAGON_SCALE_SHIELD);

        Artifact weapon =
                new Artifact(
                        ArtifactType.RED_DRAGON_FLAME_TONGUE);

        Artifact tabard =
                new Artifact(
                        ArtifactType.DRAGON_WING_TABARD);

        hero.addArtifact(helm);
        hero.addArtifact(armor);
        hero.addArtifact(shield);
        hero.addArtifact(weapon);
        hero.addArtifact(tabard);

        hero.equipArtifact(
                helm,
                ArtifactSlot.DRAGON_HELM);

        hero.equipArtifact(
                armor,
                ArtifactSlot.DRAGON_ARMOR);

        hero.equipArtifact(
                shield,
                ArtifactSlot.DRAGON_SHIELD);

        hero.equipArtifact(
                weapon,
                ArtifactSlot.DRAGON_WEAPON);

        hero.equipArtifact(
                tabard,
                ArtifactSlot.PRIMARY_1);

        assertTrue(
                DragonSetService.hasFullDragonSet(hero));
    }


}
