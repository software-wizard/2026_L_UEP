package pl.psi.hero.skills;

public class LearningSkill extends AbstractSkill {
    private float learningFactor;

    public LearningSkill()
    {
        super();
        this.learningFactor = 0.05f;
    }

    @Override
    public void upgrade()
    {
        if ( this.level.equals( SkillLevel.BASIC) )
        {
            this.level = SkillLevel.ADVANCED;
            this.learningFactor = 0.10f;
        }
        else if ( this.level.equals( SkillLevel.ADVANCED) )
        {
            this.level = SkillLevel.EXPERT;
            this.learningFactor = 0.15f;
        }
        else
        {
            throw new IllegalStateException( "Cannot upgrade from Expert level." );
        }
    }

    @Override
    public SkillName getName() {return SkillName.LEARNING;}

    @Override
    public float getFactor() { return learningFactor;}
}
