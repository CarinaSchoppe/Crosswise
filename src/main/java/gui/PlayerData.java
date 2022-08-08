package gui;

import java.util.Arrays;
import java.util.Objects;

public record PlayerData(String name, boolean isActive, boolean isAI, int[] hand) {


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerData that)) return false;

        if (isActive() != that.isActive()) return false;
        if (isAI() != that.isAI()) return false;
        if (!Objects.equals(name, that.name)) return false;
        return Arrays.equals(hand, that.hand);
    }


    @Override
    public String toString() {
        return "PlayerData{" +
                "name='" + name + '\'' +
                ", isActive=" + isActive +
                ", isAI=" + isAI +
                ", hand=" + Arrays.toString(hand) +
                '}';
    }

    @Override
    public String name() {
        return "" + name;
    }


    @Override
    public int[] hand() {
        return hand.clone();
    }

}
