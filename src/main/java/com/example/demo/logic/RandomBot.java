package com.example.demo.logic;

import com.example.demo.PieceColor;
import com.example.demo.model.Board;
import com.example.demo.model.Move;

import java.util.List;
import java.util.Random;

/**
 * Простой бот, выбирающий случайный допустимый ход.
 * @author Maria Bardakova
 * @version 1.0
 */
public class RandomBot {
    /** Цвет фигур, за которые играет бот */
    public final PieceColor color;
    private final Random random = new Random();

    /**
     * Конструктор бота.
     * @param color цвет фигур бота
     */
    public RandomBot(PieceColor color) {
        this.color = color;
    }

    /**
     * Выбирает случайный ход из доступных.
     * @param board текущая доска
     * @return ход или {@code null}, если ходов нет
     */
    public Move chooseMove(Board board) {
        List<Move> moves = board.getAllMoves(color);
        if (moves.isEmpty()) return null;
        return moves.get(random.nextInt(moves.size()));
    }
}