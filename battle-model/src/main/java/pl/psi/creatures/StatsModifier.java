package pl.psi.creatures;

public class StatsModifier {
    private int attackBonus; // sprawdzic inne statystyki tez
    private int armorBonus;

    public StatsModifier(int attackBonus, int armorBonus) {
        this.attackBonus = attackBonus;
        this.armorBonus = armorBonus;
    }

    public StatsModifier() {
    }

    public int getAttackBonus() {
        return attackBonus;
    }

    public int getArmorBonus() {
        return armorBonus;
    }
}