package pl.psi.hero.skills;

public enum SkillName {
    OFFENCE(false),
    ARMORER(false),
    FIRE_MAGIC(true),
    WATER_MAGIC(true),
    EARTH_MAGIC(true),
    AIR_MAGIC(true);

    private final boolean isMagic;

    SkillName(boolean isMagic) {
        this.isMagic = isMagic;
    }

    public boolean isMagic() {
        return isMagic;
    }
}
