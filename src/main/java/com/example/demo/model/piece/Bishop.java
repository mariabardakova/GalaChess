package com.example.demo.model.piece;

import com.example.demo.*;
import com.example.demo.logic.CoordinatesUtils;
import com.example.demo.model.Board;
import com.example.demo.model.Coordinates;
import com.example.demo.model.Zone;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс слона — фигура с особым поведением в углах доски.
 * В углах ходит диагонально с ортогональными ответвлениями, вне углов — как ладья.
 * @author Maria Bardakova
 * @version 1.0
 */
public class Bishop extends Piece {

    /**
     * Конструктор слона.
     * @param color цвет фигуры
     * @param coordinates начальная позиция
     */
    public Bishop(PieceColor color, Coordinates coordinates) {
        super(color, coordinates);
    }

    @Override
    public Set<Coordinates> getAvailableMove(Board board) {
        Zone zone = CoordinatesUtils.getZone(coordinates, color);

        if (zone == Zone.OWN_CORNER || zone == Zone.ENEMY_CORNER) {
            Set<Coordinates> moves = new HashSet<>();
            CoordinatesShift[] diagDirs = {
                    new CoordinatesShift(1, 1),
                    new CoordinatesShift(-1, 1),
                    new CoordinatesShift(1, -1),
                    new CoordinatesShift(-1, -1)
            };
            for (CoordinatesShift dir : diagDirs) {
                traceFromCorner(board, dir, moves);
            }
            return moves;
        } else {
            return addOrthogonalMoves(board, new HashSet<>());
        }
    }

    /**
     * Генерирует ходы слона из угловой зоны в заданном диагональном направлении.
     * @param board текущая доска
     * @param dir направление движения
     * @param result множество для добавления допустимых ходов
     */
    private void traceFromCorner(Board board, CoordinatesShift dir, Set<Coordinates> result) {
        Coordinates cur = coordinates;
        int stepsInCorner = 0;

        while (isInAnyCorner(cur) && cur.canShift(dir)) {
            cur = cur.shift(dir);
            stepsInCorner++;

            if (!isInAnyCorner(cur)) {
                addIfValid(board, cur, result);

                int maxExtra = (stepsInCorner == 1) ? 10 : 1;
                CoordinatesShift[] orthoDirs = getOrthoFromDiag(dir);

                for (CoordinatesShift ortho : orthoDirs) {
                    Coordinates pos = cur;
                    for (int i = 0; i < maxExtra; i++) {
                        if (!pos.canShift(ortho)) break;
                        pos = pos.shift(ortho);
                        if (isInAnyCorner(pos)) break;
                        addIfValid(board, pos, result);
                    }
                }
                break;
            }

            addIfValid(board, cur, result);
        }
    }

    /**
     * Проверяет, находится ли клетка в любой угловой зоне доски.
     * @param c координаты клетки
     * @return {@code true}, если клетка в углу
     */
    private boolean isInAnyCorner(Coordinates c) {
        int f = c.file.ordinal();
        int r = c.rank;
        return (f <= 3 && r <= 4) || (f >= 6 && r <= 4) ||
                (f <= 3 && r >= 7) || (f >= 6 && r >= 7);
    }

    /**
     * Добавляет ортогональные ходы (аналогично ладье).
     * @param board текущая доска
     * @param result множество для добавления ходов
     * @return обновлённое множество ходов
     */
    private Set<Coordinates> addOrthogonalMoves(Board board, Set<Coordinates> result) {
        CoordinatesShift[] dirs = {
                new CoordinatesShift(0, 1),
                new CoordinatesShift(1, 0),
                new CoordinatesShift(0, -1),
                new CoordinatesShift(-1, 0)
        };
        for (CoordinatesShift dir : dirs) {
            Coordinates cur = coordinates;
            while (cur.canShift(dir)) {
                cur = cur.shift(dir);
                Piece p = board.getPiece(cur);
                if (p == null) {
                    result.add(cur);
                } else {
                    if (p.color != color) result.add(cur);
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Возвращает ортогональные направления, соответствующие диагональному вектору.
     * @param dir диагональное направление
     * @return массив ортогональных смещений
     */
    private CoordinatesShift[] getOrthoFromDiag(CoordinatesShift dir) {
        if (dir.fileShist == 1 && dir.rankShift == 1) {
            return new CoordinatesShift[]{new CoordinatesShift(1,0), new CoordinatesShift(0,1)};
        } else if (dir.fileShist == -1 && dir.rankShift == 1) {
            return new CoordinatesShift[]{new CoordinatesShift(-1,0), new CoordinatesShift(0,1)};
        } else if (dir.fileShist == 1 && dir.rankShift == -1) {
            return new CoordinatesShift[]{new CoordinatesShift(1,0), new CoordinatesShift(0,-1)};
        } else {
            return new CoordinatesShift[]{new CoordinatesShift(-1,0), new CoordinatesShift(0,-1)};
        }
    }

    /**
     * Добавляет клетку в список ходов, если она пуста или занята вражеской фигурой.
     * @param board текущая доска
     * @param c координаты клетки
     * @param result множество ходов
     */
    private void addIfValid(Board board, Coordinates c, Set<Coordinates> result) {
        Piece p = board.getPiece(c);
        if (p == null || p.color != color) {
            result.add(c);
        }
    }

    @Override
    protected Set<CoordinatesShift> getPieceMoves() {
        return Set.of();
    }
}