package pl.psi.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EcoMapObjectDto {
    private String type; // e.g. TOWN, RESOURCE_GENERATOR, GOLD, BANK, SPELL, ARTIFACT
    private String param; // parameter or sub-type, e.g. PLAYER1, GEM, SWORD_OF_HELLFIRE, etc.
}
