package com.example.demo.logic;

import com.example.demo.File;
import com.example.demo.PieceColor;
import com.example.demo.model.Coordinates;
import com.example.demo.model.Zone;

import java.util.Set;

/**
 * Утилитарный класс для работы с зонами доски.
 * @author Maria Bardakova
 * @version 1.0
 */
public class CoordinatesUtils {

    /** Центральные клетки доски: E5, E6, F5, F6 */
    public static final Set<Coordinates> CENTER_SQUARES = Set.of(
            new Coordinates(File.E, 5),
            new Coordinates(File.E, 6),
            new Coordinates(File.F, 5),
            new Coordinates(File.F, 6)
    );

    /**
     * Определяет зону, в которой находится клетка, относительно заданного цвета.
     * Это необходимо, так как от этого зависят возможности фигуры.
     * @param c координаты клетки
     * @param color цвет игрока
     * @return зона клетки
     */
    public static Zone getZone(Coordinates c, PieceColor color) {
        int f = c.file.ordinal();
        int r = c.rank;

        boolean inBottomLeft = (f <= 3 && r <= 4);
        boolean inBottomRight = (f >= 6 && r <= 4);
        boolean inTopLeft = (f <= 3 && r >= 7);
        boolean inTopRight = (f >= 6 && r >= 7);

        if (color == PieceColor.WHITE) {
            if (inBottomLeft || inBottomRight) return Zone.OWN_CORNER;
            if (inTopLeft || inTopRight) return Zone.ENEMY_CORNER;
        } else {
            if (inTopLeft || inTopRight) return Zone.OWN_CORNER;
            if (inBottomLeft || inBottomRight) return Zone.ENEMY_CORNER;
        }

        if (CENTER_SQUARES.contains(c)) return Zone.CENTER;
        return Zone.MIDDLE;
    }
}