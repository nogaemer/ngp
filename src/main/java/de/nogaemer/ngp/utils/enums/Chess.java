package de.nogaemer.ngp.utils.enums;

import org.bukkit.entity.EntityType;

public enum Chess {
    WHITE_BISHOP(1),
    WHITE_ROOK(2),
    WHITE_QUEEN(3),
    WHITE_PAWN(4),
    WHITE_KNIGHT(5),
    WHITE_KING(6),
    BLACK_BISHOP(7),
    BLACK_ROOK(8),
    BLACK_QUEEN(9),
    BLACK_PAWN(10),
    BLACK_KNIGHT(11),
    BLACK_KING(12);

    private final int modelData;

    Chess(int modelData) {
        this.modelData = modelData;
    }

    public int getID() {
        return this.modelData;
    }
}
