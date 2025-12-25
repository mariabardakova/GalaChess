package com.example.demo.model;

import java.util.Objects;

/**
 * Класс, представляющий ход фигуры на доске.
 * @author Maria Bardakova
 * @version 1.0
 */
public class Move {
    /** Начальные координаты хода */
    public final Coordinates from;
    /** Конечные координаты хода */
    public final Coordinates to;

    /**
     * Конструктор хода.
     * @param from начальная позиция
     * @param to конечная позиция
     */
    public Move(Coordinates from, Coordinates to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move m)) return false;
        return Objects.equals(from, m.from) && Objects.equals(to, m.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}