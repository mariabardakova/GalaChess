package com.example.demo.ui;

import com.example.demo.PieceColor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Главный класс приложения «Шахматы Гала».
 * @author Maria Bardakova
 * @version 1.0
 */
public class HelloApplication extends Application {

    /**
     * Точка входа в приложение. Организует выбор режима: игрок (с цветом) или наблюдатель.
     */
    public static void main(String[] args) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Шахматы Гала");
            System.out.println("Введите:");
            System.out.println("  Хочу играть! [белые|чёрные]");
            System.out.println("  Я наблюдатель");

            String line = in.readLine().trim();
            String lower = line.toLowerCase();

            if (lower.equals("я наблюдатель")) {
                new GameController().runObserverMode();
            } else if (lower.startsWith("хочу играть!")) {
                PieceColor color = PieceColor.WHITE;
                if (lower.contains("чёр") || lower.contains("чер")) {
                    color = PieceColor.BLACK;
                }

                System.setProperty("player.color", color.name());
                Application.launch(HelloApplication.class);
            } else {
                System.out.println("Неизвестная команда.");
                main(new String[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Запускает JavaFX-интерфейс и инициализирует контроллер.
     * @param stage основное окно приложения
     * @throws Exception при ошибке загрузки FXML
     * @see GameController#initInteractiveMode(PieceColor)
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/com/example/demo/game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 600);
        stage.setTitle("Шахматы Гала");
        stage.setScene(scene);
        stage.show();

        GameController ctrl = fxmlLoader.getController();
        String colorStr = System.getProperty("player.color", "WHITE");
        ctrl.initInteractiveMode(PieceColor.valueOf(colorStr));
    }
}