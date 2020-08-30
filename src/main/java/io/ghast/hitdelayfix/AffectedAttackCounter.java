package io.ghast.hitdelayfix;

public class AffectedAttackCounter {

    private int blockedAttacks;
    private int unblockedAttacks;

    private boolean enabled = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void reset() {
        this.blockedAttacks = 0;
        this.unblockedAttacks = 0;
    }

    public int getBlockedAttacks() {
        return blockedAttacks;
    }

    public void incrementBlockedAttacks() {
        this.blockedAttacks++;
    }

    public void incrementUnblockedAttacks() {
        this.unblockedAttacks++;
    }

    public int getUnblockedAttacks() {
        return unblockedAttacks;
    }
}
