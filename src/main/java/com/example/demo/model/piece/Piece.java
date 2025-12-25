package com.example.demo.model.piece;

import com.example.demo.model.Board;
import com.example.demo.model.Coordinates;
import com.example.demo.PieceColor;

import java.util.HashSet;
import java.util.Set;

/**
 * Абстрактный базовый класс шахматной фигуры.
 * @author Maria Bardakova
 * @version 1.0
 */
public abstract class Piece {
    /** Цвет фигуры */
    public final PieceColor color;
    /** Текущие координаты фигуры */
    public Coordinates coordinates;

    /**
     * Конструктор фигуры.
     * @param color цвет фигуры
     * @param coordinates начальная позиция
     */
    public Piece(PieceColor color, Coordinates coordinates) {
        this.color = color;
        this.coordinates = coordinates;
    }

    /**
     * Возвращает множество допустимых ходов фигуры на текущей доске.
     * @param board текущая доска
     * @return множество координат для ходов
     */
    public Set<Coordinates> getAvailableMove(Board board){
        Set<Coordinates> result = new HashSet<>();
        for(CoordinatesShift shift : getPieceMoves()){
            if (coordinates.canShift(shift)){
                Coordinates newCoordinates = coordinates.shift(shift);
                if (isSquareAvailable(newCoordinates, board)){
                    result.add(newCoordinates);
                }
            }
        }
        return result;
    }

    /**
     * Проверяет, доступна ли клетка для хода: пуста или занята фигурой противника.
     * @param coordinates координаты клетки
     * @param board текущая доска
     * @return {@code true}, если ход возможен
     */
    private boolean isSquareAvailable(Coordinates coordinates, Board board){
        return board.isSquareEmpty(coordinates) || board.getPiece(coordinates).color != color;
    }

    /**
     * Возвращает базовые смещения фигуры (без учёта доски и блокировки).
     * @return множество векторов смещения
     */
    protected abstract Set<CoordinatesShift> getPieceMoves();
}