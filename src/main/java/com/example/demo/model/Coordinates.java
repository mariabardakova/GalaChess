package com.example.demo.model;

import com.example.demo.File;
import com.example.demo.model.piece.CoordinatesShift;

import java.util.Objects;

/**
 * Класс координат клетки доски.
 * @author Maria Bardakova
 * @version 1.0
 */
public class Coordinates {
    /** Вертикаль (столбец) */
    public final File file;
    /** Горизонталь (строка), от 1 до 10 */
    public final Integer rank;

    /**
     * Конструктор координат.
     * @param file вертикаль
     * @param rank горизонталь (1–10)
     */
    public Coordinates(File file, Integer rank) {
        this.file = file;
        this.rank = rank;
    }

    /**
     * Возвращает новые координаты, смещённые на заданный вектор.
     * @param shift вектор смещения
     * @return новые координаты
     * @throws ArrayIndexOutOfBoundsException если смещение выходит за пределы доски
     */
    public Coordinates shift(CoordinatesShift shift) {
        return new Coordinates(File.values()[this.file.ordinal() + shift.fileShist], this.rank + shift.rankShift);
    }

    /**
     * Проверяет, возможно ли смещение без выхода за границы доски.
     * @param shift вектор смещения
     * @return {@code true}, если смещение допустимо
     */
    public boolean canShift(CoordinatesShift shift) {
        int f = file.ordinal() + shift.fileShist;
        int r = rank + shift.rankShift;

        if (f < 0 || f >= File.values().length) return false;
        if (r < 1 || r > 10) return false;

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates that)) return false;
        return file == that.file && Objects.equals(rank, that.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, rank);
    }

    @Override
    public String toString() {
        return file.name() + rank;
    }
}