package com.example.demo.ui;

import com.example.demo.File;
import com.example.demo.PieceColor;
import com.example.demo.logic.CoordinatesUtils;
import com.example.demo.logic.RandomBot;
import com.example.demo.model.Board;
import com.example.demo.model.Coordinates;
import com.example.demo.model.Move;
import com.example.demo.model.piece.King;
import com.example.demo.model.piece.Piece;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер игровой доски.
 * @author Maria Bardakova
 * @version 1.0
 */
public class GameController {

    @FXML
    private GridPane boardGrid;

    private Board gameBoard;
    private Map<Coordinates, StackPane> cellMap = new HashMap<>();
    private Coordinates selectedFrom = null;
    private PieceColor currentPlayer = PieceColor.WHITE;

    private RandomBot blackBot = new RandomBot(PieceColor.BLACK);
    private boolean isBotTurn = false;
    private boolean gameEnded = false;

    /**
     * Инициализация доски: создаёт клетки без фигур.
     */
    public void initialize() {
        cellMap = new HashMap<>();
        boardGrid.getChildren().clear();
        cellMap.clear();
        for (int rank = 10; rank >= 1; rank--) {
            for (File file : File.values()) {
                Coordinates c = new Coordinates(file, rank);
                StackPane cell = createCell(c);
                cellMap.put(c, cell);
                boardGrid.add(cell, file.ordinal(), 10 - rank);
            }
        }
    }

    /**
     * Создаёт клетку доски с фоном и текстом для фигур.
     * @param c координаты клетки
     * @return контейнер клетки
     */
    private StackPane createCell(Coordinates c) {
        StackPane cell = new StackPane();
        cell.setPrefSize(50, 50);

        Rectangle bg = new Rectangle(50, 50);
        Color bgColor = getCellColor(c);
        bg.setFill(bgColor);
        cell.getChildren().add(bg);

        Text pieceText = new Text();
        pieceText.setFont(javafx.scene.text.Font.font("Segoe UI Symbol", 24));
        pieceText.setTextAlignment(TextAlignment.CENTER);
        pieceText.setWrappingWidth(40);
        cell.getChildren().add(pieceText);

        cell.setOnMouseClicked(event -> handleCellClick(c));

        return cell;
    }

    /**
     * Определяет цвет клетки в зависимости от её положения.
     * @param c координаты клетки
     * @return цвет клетки
     */
    private Color getCellColor(Coordinates c) {
        int f = c.file.ordinal();
        int r = c.rank;

        boolean inCorner = (f <= 3 && r <= 4) || (f >= 6 && r <= 4) ||
                (f <= 3 && r >= 7) || (f >= 6 && r >= 7);

        if (inCorner) {
            return (f + r) % 2 == 0 ? Color.ORANGE : Color.WHITE;
        } else if (CoordinatesUtils.CENTER_SQUARES.contains(c)) {
            return (f + r) % 2 == 0 ? Color.YELLOW : Color.WHITE;
        } else {
            return (f + r) % 2 == 0 ? Color.LIGHTGRAY : Color.WHITE;
        }
    }

    /**
     * Обрабатывает клик по клетке: выбор фигуры или ход.
     * @param clicked координаты нажатой клетки
     */
    private void handleCellClick(Coordinates clicked) {
        if (isBotTurn) return;

        Piece piece = gameBoard.getPiece(clicked);
        if (selectedFrom == null) {
            if (piece != null && piece.color == currentPlayer) {
                selectedFrom = clicked;
            }
            return;
        }

        if (piece != null && piece.color == currentPlayer && selectedFrom.equals(clicked)) {
            selectedFrom = null;
            return;
        }

        Piece fromPiece = gameBoard.getPiece(selectedFrom);
        if (fromPiece != null && fromPiece.getAvailableMove(gameBoard).contains(clicked)) {
            gameBoard.movePiece(selectedFrom, clicked);
            selectedFrom = null;
            currentPlayer = (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            updateBoardDisplay();
            checkGameEnd();

            if (currentPlayer == blackBot.color) {
                isBotTurn = true;
                PauseTransition pause = new PauseTransition(Duration.millis(1000));
                pause.setOnFinished(e -> {
                    makeBotMove();
                    isBotTurn = false;
                });
                pause.play();
            }
        } else {
            selectedFrom = null;
        }
    }

    /**
     * Выполняет ход бота.
     */
    private void makeBotMove() {
        if (currentPlayer != blackBot.color) return;

        Move move = blackBot.chooseMove(gameBoard);
        if (move != null) {
            gameBoard.movePiece(move.from, move.to);
            currentPlayer = (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
            updateBoardDisplay();
            checkGameEnd();
        }
    }

    /**
     * Обновляет отображение фигур на доске.
     */
    private void updateBoardDisplay() {
        for (var entry : cellMap.entrySet()) {
            Coordinates c = entry.getKey();
            StackPane cell = entry.getValue();
            Text text = (Text) cell.getChildren().get(1);

            Piece piece = gameBoard.getPiece(c);
            if (piece != null) {
                String sym = switch (piece.getClass().getSimpleName()) {
                    //Изображение фигур взято из Юникода
                    case "King" -> (piece.color == PieceColor.WHITE) ? "\u2654" : "\u265A";
                    case "Rook" -> (piece.color == PieceColor.WHITE) ? "\u2656" : "\u265C";
                    case "Bishop" -> (piece.color == PieceColor.WHITE) ? "\u2657" : "\u265D";
                    case "Pawn" -> (piece.color == PieceColor.WHITE) ? "\u2659" : "\u265F";
                    default -> "?";
                };
                text.setText(sym);
                text.setFill(Color.BLACK);
            } else {
                text.setText("");
            }
        }
    }

    /**
     * Проверяет условия окончания игры и показывает результат.
     */
    private void checkGameEnd() {
        int whiteKings = countKings(PieceColor.WHITE);
        int blackKings = countKings(PieceColor.BLACK);
        boolean whiteWin = isWin(PieceColor.WHITE);
        boolean blackWin = isWin(PieceColor.BLACK);

        String message = null;

        if (gameEnded) return;

        if (whiteWin) {
            message = "Победили белые";
        } else if (blackWin) {
            message = "Победили чёрные";
        } else if (whiteKings == 0) {
            message = "Победили чёрные";
        } else if (blackKings == 0) {
            message = "Победили белые";
        } else if (whiteKings == 1 && blackKings == 1) {
            message = "Ничья";
        }

        if (message != null) {
            gameEnded = true;
            showAlert(message);
            boardGrid.setDisable(true);
        }
    }

    /**
     * Отображает финальное окно с результатом игры.
     * @param message текст сообщения
     */
    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Игра окончена");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        });
    }

    /**
     * Проверяет победу цвета по правилам (два короля в центре).
     * @param color цвет фигур
     * @return {@code true}, если цвет победил
     */
    private boolean isWin(PieceColor color) {
        return isWin(gameBoard, color);
    }

    /**
     * Считает количество королей заданного цвета на доске.
     * @param color цвет фигур
     * @return количество королей
     */
    private int countKings(PieceColor color) {
        return countKings(gameBoard, color);
    }

    /**
     * Запускает неинтерактивный режим (игра ботов в консоли).
     */
    public void runObserverMode() {
        System.out.println("Неинтерактивный режим: игра в консоли");
        Board board = new Board();
        board.setupDefaultPiecesPositions();

        RandomBot whiteBot = new RandomBot(PieceColor.WHITE);
        RandomBot blackBot = new RandomBot(PieceColor.BLACK);
        PieceColor currentPlayer = PieceColor.WHITE;

        long startTime = System.currentTimeMillis();
        final long TIMEOUT_MS = 5 * 60 * 1000;

        while (true) {
            if (System.currentTimeMillis() - startTime > TIMEOUT_MS) {
                System.out.println("Время вышло — ничья");
                break;
            }

            Move move = (currentPlayer == PieceColor.WHITE)
                    ? whiteBot.chooseMove(board)
                    : blackBot.chooseMove(board);

            if (move == null) {
                System.out.println(currentPlayer + "не могут ходить — игра окончена");
                break;
            }

            board.movePiece(move.from, move.to);
            System.out.println(currentPlayer + ": " + move.from + " → " + move.to);

            if (isWin(board, PieceColor.WHITE)) { System.out.println("Победили белые"); break; }
            if (isWin(board, PieceColor.BLACK)) { System.out.println("Победили чёрные"); break; }
            int w = countKings(board, PieceColor.WHITE);
            int b = countKings(board, PieceColor.BLACK);
            if (w == 0) { System.out.println("Победили чёрные"); break; }
            if (b == 0) { System.out.println("Победили белые"); break; }
            if (w == 1 && b == 1) { System.out.println("Ничья"); break; }

            currentPlayer = (currentPlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

            try { Thread.sleep(500); } catch (InterruptedException e) { break; }
        }

        System.out.println("Игра завершена.");
    }

    /**
     * Проверяет победу цвета по правилам (два короля в центре).
     * @param board игровая доска
     * @param color цвет фигур
     * @return {@code true}, если цвет победил
     */
    private boolean isWin(Board board, PieceColor color) {
        int kingsInCenter = 0;
        for (Piece piece : board.pieces.values()) {
            if (piece instanceof King && piece.color == color) {
                if (CoordinatesUtils.CENTER_SQUARES.contains(piece.coordinates)) {
                    kingsInCenter++;
                }
            }
        }
        return kingsInCenter == 2;
    }

    /**
     * Считает количество королей заданного цвета на доске.
     * @param board игровая доска
     * @param color цвет фигур
     * @return количество королей
     */
    private int countKings(Board board, PieceColor color) {
        int count = 0;
        for (Piece piece : board.pieces.values()) {
            if (piece instanceof King && piece.color == color) {
                count++;
            }
        }
        return count;
    }

    /**
     * Инициализирует интерактивный режим с заданным цветом игрока.
     * @param playerColor цвет фигур игрока
     */
    public void initInteractiveMode(PieceColor playerColor) {
        gameBoard = new Board();
        gameBoard.setupDefaultPiecesPositions();
        currentPlayer = PieceColor.WHITE;

        PieceColor botColor = (playerColor == PieceColor.WHITE)
                ? PieceColor.BLACK
                : PieceColor.WHITE;
        blackBot = new RandomBot(botColor);

        if (playerColor == PieceColor.BLACK) {
            Platform.runLater(() -> {
                makeBotMove();
            });
        }

        PauseTransition timer = new PauseTransition(Duration.minutes(5));
        timer.setOnFinished(e -> {
            if (!boardGrid.isDisabled()) {
                showAlert("Время вышло — ничья");
                boardGrid.setDisable(true);
            }
        });
        timer.play();

        updateBoardDisplay();
    }
}