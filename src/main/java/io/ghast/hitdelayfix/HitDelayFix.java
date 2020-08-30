package io.ghast.hitdelayfix;

public enum HitDelayFix {
    INSTANCE;

    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AffectedAttackCounter affectedAttackCounter = new AffectedAttackCounter();
}
