package logic;

public enum AnimationTime {
    SLOW(4), MIDDLE(2), FAST(1);

    private final int time;

    AnimationTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}

