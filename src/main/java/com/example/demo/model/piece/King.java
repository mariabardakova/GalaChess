package com.example.demo.model.piece;

import com.example.demo.model.Board;
import com.example.demo.model.Coordinates;
import com.example.demo.logic.CoordinatesUtils;
import com.example.demo.PieceColor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Класс короля — может ходить на соседние клетки и прыгать из центра на изначально пустые клетки.
 * @author Maria Bardakova
 * @version 1.0
 */
public class King extends Piece {

    private static final List<CoordinatesShift> KING_SHIFTS = Arrays.asList(
            new CoordinatesShift( 0,  1),
            new CoordinatesShift( 1,  1),
            new CoordinatesShift( 1,  0),
            new CoordinatesShift( 1, -1),
            new CoordinatesShift( 0, -1),
            new CoordinatesShift(-1, -1),
            new CoordinatesShift(-1,  0),
            new CoordinatesShift(-1,  1)
    );

    /**
     * Конструктор короля.
     * @param color цвет фигуры
     * @param coordinates начальная позиция
     */
    public King(PieceColor color, Coordinates coordinates) {
        super(color, coordinates);
    }

    @Override
    public Set<Coordinates> getAvailableMove(Board board) {
        Set<Coordinates> moves = new HashSet<>();

        for (CoordinatesShift shift : KING_SHIFTS) {
            if (coordinates.canShift(shift)) {
                Coordinates target = coordinates.shift(shift);
                Piece piece = board.getPiece(target);
                if (piece == null || piece.color != this.color) {
                    moves.add(target);
                }
            }
        }

        if (CoordinatesUtils.CENTER_SQUARES.contains(coordinates)) {
            Set<Coordinates> initialEmpty = board.getInitialEmptySq();
            for (Coordinates target : initialEmpty) {
                if (!target.equals(coordinates) && board.isSquareEmpty(target)) {
                    moves.add(target);
                }
            }
        }

        return moves;
    }

    @Override
    protected Set<CoordinatesShift> getPieceMoves() {
        return Set.of();
    }
}