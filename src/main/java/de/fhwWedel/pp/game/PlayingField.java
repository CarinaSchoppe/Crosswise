package de.fhwWedel.pp.game;

import de.fhwWedel.pp.util.Position;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class PlayingField {

    private final int size;
    private final Position[][] fieldMap;

    public PlayingField(final int size) {
        if (size < 2) {
            throw new IllegalArgumentException("Size of playing field must be at least 2");
        }
        this.size = size;
        this.fieldMap = new Position[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.fieldMap[i][j] = new Position(i, j);
            }
        }
    }

    public @NotNull Position getCorrespondingPlayingField(@NotNull final Position position) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (fieldMap[i][j].equals(position)) {
                    return fieldMap[i][j];
                }
            }
        }
        return null;
    }
}
