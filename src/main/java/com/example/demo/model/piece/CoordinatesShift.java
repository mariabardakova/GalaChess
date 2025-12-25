package com.example.demo.model.piece;

/**
 * Класс вектора смещения на доске.
 * @author Maria Bardakova
 * @version 1.0
 */
public class CoordinatesShift {
    /** Смещение по вертикали */
    public final int fileShist;
    /** Смещение по горизонтали */
    public final int rankShift;

    /**
     * Конструктор смещения.
     * @param fileShist смещение по столбцам
     * @param rankShift смещение по строкам
     */
    public CoordinatesShift(int fileShist, int rankShift) {
        this.fileShist = fileShist;
        this.rankShift = rankShift;
    }
}