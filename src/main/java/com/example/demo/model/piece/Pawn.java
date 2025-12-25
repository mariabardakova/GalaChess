package com.example.demo.model.piece;

import com.example.demo.*;
import com.example.demo.logic.CoordinatesUtils;
import com.example.demo.model.Board;
import com.example.demo.model.Coordinates;
import com.example.demo.model.Zone;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс пешки — фигура с особым поведением в собственном углу.
 * В своём углу ходит только по диагонали вглубь угла (без взятия), вне угла — как король.
 * @author Maria Bardakova
 * @version 1.0
 */
public class Pawn extends Piece {

    /** Флаг первого хода (для двойного шага) */
    private boolean isFirstMove = true;

    /**
     * Конструктор пешки.
     * @param color цвет фигуры
     * @param coordinates начальная позиция
     */
    public Pawn(PieceColor color, Coordinates coordinates) {
        super(color, coordinates);
    }

    @Override
    protected Set<CoordinatesShift> getPieceMoves() {
        return Set.of();
    }

    @Override
    public Set<Coordinates> getAvailableMove(Board board) {
        Set<Coordinates> moves = new HashSet<>();
        Zone zone = CoordinatesUtils.getZone(coordinates, color);

        if (zone == Zone.OWN_CORNER) {
            CoordinatesShift diag = getDiagonalShiftForOwnCorner();
            if (diag.fileShist == 0 && diag.rankShift == 0) {
                return moves;
            }

            if (coordinates.canShift(diag)) {
                Coordinates target = coordinates.shift(diag);
                if (board.isSquareEmpty(target)) {
                    moves.add(target);
                }
            }

            if (isFirstMove && canDoubleStep(board, diag)) {
                Coordinates doubleTarget = coordinates.shift(diag).shift(diag);
                moves.add(doubleTarget);
            }

        } else {
            CoordinatesShift[] kingMoves = {
                    new CoordinatesShift( 0,  1), new CoordinatesShift( 1,  1),
                    new CoordinatesShift( 1,  0), new CoordinatesShift( 1, -1),
                    new CoordinatesShift( 0, -1), new CoordinatesShift(-1, -1),
                    new CoordinatesShift(-1,  0), new CoordinatesShift(-1,  1)
            };

            for (CoordinatesShift shift : kingMoves) {
                if (coordinates.canShift(shift)) {
                    Coordinates target = coordinates.shift(shift);
                    Piece piece = board.getPiece(target);
                    if (piece == null) {
                        moves.add(target);
                    } else if (piece.color != this.color) {
                        moves.add(target);
                    }
                }
            }
        }

        return moves;
    }

    /**
     * Определяет диагональное направление движения пешки в своём углу.
     * @return вектор смещения
     */
    private CoordinatesShift getDiagonalShiftForOwnCorner() {
        int f = coordinates.file.ordinal();
        int r = coordinates.rank;

        if (color == PieceColor.WHITE) {
            if (f <= 3 && r <= 4) return new CoordinatesShift( 1,  1);
            if (f >= 6 && r <= 4) return new CoordinatesShift(-1,  1);
        } else {
            if (f <= 3 && r >= 7) return new CoordinatesShift( 1, -1);
            if (f >= 6 && r >= 7) return new CoordinatesShift(-1, -1);
        }
        return new CoordinatesShift(0, 0);
    }

    /**
     * Проверяет возможность двойного шага из угла.
     * @param board текущая доска
     * @param diag направление движения
     * @return {@code true}, если двойной шаг возможен
     */
    private boolean canDoubleStep(Board board, CoordinatesShift diag) {
        if (!coordinates.canShift(diag)) return false;
        Coordinates step1 = coordinates.shift(diag);
        if (!step1.canShift(diag)) return false;
        Coordinates step2 = step1.shift(diag);

        if (!board.isSquareEmpty(step1) || !board.isSquareEmpty(step2)) {
            return false;
        }

        Zone zoneAfterStep1 = CoordinatesUtils.getZone(step1, color);
        return zoneAfterStep1 == Zone.OWN_CORNER;
    }

    /**
     * Помечает пешку как сделавшую ход (запрещает двойной шаг в будущем).
     */
    public void markMoved() {
        isFirstMove = false;
    }
}