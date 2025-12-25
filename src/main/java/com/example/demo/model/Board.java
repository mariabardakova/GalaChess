package com.example.demo.model;

import com.example.demo.File;
import com.example.demo.PieceColor;
import com.example.demo.model.piece.*;
import java.util.*;


/**
 * Класс игровой доски 10×10.
 * @author Maria Bardakova
 * @version 1.0
 */
public class Board {
    /** Отображение координат → фигура */
    public HashMap<Coordinates, Piece> pieces = new HashMap<>();
    private Set<Coordinates> initialEmptySq;

    /**
     * Устанавливает фигуру на указанную клетку.
     * @param coordinates координаты клетки
     * @param piece фигура
     */
    public void setPiece(Coordinates coordinates, Piece piece){
        piece.coordinates = coordinates;
        pieces.put(coordinates, piece);
    }

    /**
     * Удаляет фигуру с указанной клетки.
     * @param coordinates координаты клетки
     */
    public void removePiece(Coordinates coordinates){
        pieces.remove(coordinates);
    }

    /**
     * Перемещает фигуру с одной клетки на другую.
     * @param from исходная позиция
     * @param to целевая позиция
     */
    public void movePiece(Coordinates from, Coordinates to) {
        Piece piece = getPiece(from);
        if (piece == null){
            return;
        }
        removePiece(from);
        setPiece(to, piece);
        if (piece instanceof Pawn pawn) {
            pawn.markMoved();
        }
    }

    /**
     * Проверяет, свободна ли клетка.
     * @param coordinates координаты клетки
     * @return {@code true}, если клетка пуста
     */
    public boolean isSquareEmpty(Coordinates coordinates){
        return !pieces.containsKey(coordinates);
    }

    /**
     * Возвращает фигуру на указанной клетке.
     * @param coordinates координаты клетки
     * @return фигура или {@code null}, если клетка пуста
     */
    public Piece getPiece(Coordinates coordinates){
        return pieces.get(coordinates);
    }

    /**
     * Расставляет фигуры в начальную позицию по правилам «Шахмат Гала».
     */
    public void setupDefaultPiecesPositions(){
        setPiece(new Coordinates(File.A, 1), new King(PieceColor.WHITE, new Coordinates(File.A, 1)));
        setPiece(new Coordinates(File.J, 1), new King(PieceColor.WHITE, new Coordinates(File.J, 1)));
        setPiece(new Coordinates(File.A, 10), new King(PieceColor.BLACK, new Coordinates(File.A, 10)));
        setPiece(new Coordinates(File.J, 10), new King(PieceColor.BLACK, new Coordinates(File.J, 10)));

        setPiece(new Coordinates(File.A, 3), new Rook(PieceColor.WHITE, new Coordinates(File.A, 3)));
        setPiece(new Coordinates(File.B, 2), new Rook(PieceColor.WHITE, new Coordinates(File.B, 2)));
        setPiece(new Coordinates(File.C, 1), new Rook(PieceColor.WHITE, new Coordinates(File.C, 1)));
        setPiece(new Coordinates(File.I, 1), new Rook(PieceColor.WHITE, new Coordinates(File.I, 1)));
        setPiece(new Coordinates(File.J, 2), new Rook(PieceColor.WHITE, new Coordinates(File.J, 2)));

        setPiece(new Coordinates(File.A, 9), new Rook(PieceColor.BLACK, new Coordinates(File.A, 9)));
        setPiece(new Coordinates(File.B, 10), new Rook(PieceColor.BLACK, new Coordinates(File.B, 10)));
        setPiece(new Coordinates(File.H, 10), new Rook(PieceColor.BLACK, new Coordinates(File.H, 10)));
        setPiece(new Coordinates(File.I, 9), new Rook(PieceColor.BLACK, new Coordinates(File.I, 9)));
        setPiece(new Coordinates(File.J, 8), new Rook(PieceColor.BLACK, new Coordinates(File.J, 8)));

        setPiece(new Coordinates(File.A, 2), new Bishop(PieceColor.WHITE, new Coordinates(File.A, 2)));
        setPiece(new Coordinates(File.B, 1), new Bishop(PieceColor.WHITE, new Coordinates(File.B, 1)));
        setPiece(new Coordinates(File.H, 1), new Bishop(PieceColor.WHITE, new Coordinates(File.H, 1)));
        setPiece(new Coordinates(File.I, 2), new Bishop(PieceColor.WHITE, new Coordinates(File.I, 2)));
        setPiece(new Coordinates(File.J, 3), new Bishop(PieceColor.WHITE, new Coordinates(File.J, 3)));

        setPiece(new Coordinates(File.A, 8), new Bishop(PieceColor.BLACK, new Coordinates(File.A, 8)));
        setPiece(new Coordinates(File.B, 9), new Bishop(PieceColor.BLACK, new Coordinates(File.B, 9)));
        setPiece(new Coordinates(File.C, 10), new Bishop(PieceColor.BLACK, new Coordinates(File.C, 10)));
        setPiece(new Coordinates(File.I, 10), new Bishop(PieceColor.BLACK, new Coordinates(File.I, 10)));
        setPiece(new Coordinates(File.J, 9), new Bishop(PieceColor.BLACK, new Coordinates(File.J, 9)));

        setPiece(new Coordinates(File.A, 4), new Pawn(PieceColor.WHITE, new Coordinates(File.A, 4)));
        setPiece(new Coordinates(File.B, 3), new Pawn(PieceColor.WHITE, new Coordinates(File.B, 3)));
        setPiece(new Coordinates(File.C, 2), new Pawn(PieceColor.WHITE, new Coordinates(File.C, 2)));
        setPiece(new Coordinates(File.D, 1), new Pawn(PieceColor.WHITE, new Coordinates(File.D, 1)));
        setPiece(new Coordinates(File.G, 1), new Pawn(PieceColor.WHITE, new Coordinates(File.G, 1)));
        setPiece(new Coordinates(File.H, 2), new Pawn(PieceColor.WHITE, new Coordinates(File.H, 2)));
        setPiece(new Coordinates(File.I, 3), new Pawn(PieceColor.WHITE, new Coordinates(File.I, 3)));
        setPiece(new Coordinates(File.J, 4), new Pawn(PieceColor.WHITE, new Coordinates(File.J, 4)));

        setPiece(new Coordinates(File.A, 7), new Pawn(PieceColor.BLACK, new Coordinates(File.A, 7)));
        setPiece(new Coordinates(File.B, 8), new Pawn(PieceColor.BLACK, new Coordinates(File.B, 8)));
        setPiece(new Coordinates(File.C, 9), new Pawn(PieceColor.BLACK, new Coordinates(File.C, 9)));
        setPiece(new Coordinates(File.D, 10), new Pawn(PieceColor.BLACK, new Coordinates(File.D, 10)));
        setPiece(new Coordinates(File.G, 10), new Pawn(PieceColor.BLACK, new Coordinates(File.G, 10)));
        setPiece(new Coordinates(File.H, 9), new Pawn(PieceColor.BLACK, new Coordinates(File.H, 9)));
        setPiece(new Coordinates(File.I, 8), new Pawn(PieceColor.BLACK, new Coordinates(File.I, 8)));
        setPiece(new Coordinates(File.J, 7), new Pawn(PieceColor.BLACK, new Coordinates(File.J, 7)));

        initialEmptySq = new HashSet<>();

        for(File file : File.values()){
            for(int rank = 1; rank <= 10; rank ++){
                Coordinates coordinates = new Coordinates(file, rank);
                if (!pieces.containsKey(coordinates)){
                    initialEmptySq.add(coordinates);
                }
            }
        }
    }

    /**
     * Возвращает множество изначально пустых клеток.
     * @return множество координат
     */
    public Set<Coordinates> getInitialEmptySq(){
        return initialEmptySq;
    }

    /**
     * Получает все допустимые ходы для фигур заданного цвета.
     * @param color цвет фигур
     * @return список возможных ходов
     */
    public List<Move> getAllMoves(PieceColor color) {
        List<Move> moves = new ArrayList<>();
        for (var entry : pieces.entrySet()) {
            Piece piece = entry.getValue();
            if (piece.color == color) {
                Set<Coordinates> targets = piece.getAvailableMove(this);
                Coordinates from = entry.getKey();
                for (Coordinates to : targets) {
                    moves.add(new Move(from, to));
                }
            }
        }
        return moves;
    }
}