package pl.psi.hero.skills;

import org.junit.jupiter.api.Test;
import pl.psi.hero.HeroClass;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SkillProbabilityTableTest {

    @Test
    void readsKnownWeight() {
        SkillProbabilityTable table = SkillProbabilityTable.getDefault();

        assertEquals(8, table.getWeight(HeroClass.DEATH_KNIGHT, SkillName.OFFENCE));
    }

    @Test
    void preservesZeroWeight() {
        String csv = validCsv().replace("OFFENCE,1,1", "OFFENCE,0,1");

        SkillProbabilityTable table = new SkillProbabilityTable(new StringReader(csv));

        assertEquals(0, table.getWeight(HeroClass.KNIGHT, SkillName.OFFENCE));
    }

    @Test
    void failsWhenHeroClassColumnIsMissing() {
        String csv = validCsv().replace(",ELEMENTALIST", "");

        assertThrows(IllegalArgumentException.class, () -> new SkillProbabilityTable(new StringReader(csv)));
    }

    @Test
    void failsWhenSkillRowIsMissing() {
        String csv = validCsv().replace("OFFENCE,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1\n", "");

        assertThrows(IllegalArgumentException.class, () -> new SkillProbabilityTable(new StringReader(csv)));
    }

    @Test
    void failsWhenWeightValueIsMissing() {
        String csv = validCsv().replace("OFFENCE,1,1", "OFFENCE,,1");

        assertThrows(IllegalArgumentException.class, () -> new SkillProbabilityTable(new StringReader(csv)));
    }

    private String validCsv() {
        StringBuilder csv = new StringBuilder("skill");
        for (HeroClass heroClass : HeroClass.values()) {
            csv.append(",").append(heroClass.name());
        }
        csv.append("\n");

        for (SkillName skillName : SkillName.values()) {
            csv.append(skillName.name());
            for (HeroClass ignored : HeroClass.values()) {
                csv.append(",1");
            }
            csv.append("\n");
        }
        return csv.toString();
    }
}
