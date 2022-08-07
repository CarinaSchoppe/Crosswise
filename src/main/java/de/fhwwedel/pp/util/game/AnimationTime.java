package de.fhwwedel.pp.util.game;

public enum AnimationTime {
    SLOW(2.0f), MIDDLE(1.0f), FAST(0.5f);

    private final float time;

    AnimationTime(float time) {
        this.time = time;
    }

    public float getTime() {
        return time;
    }
}

