/**
 *  author: Samruddhi Kadam
 *  roll no: 2441
 *  Title: Hurdles
 *  Start Date: 24 September 2024
 *  Modified Date: 7 October 2024
 *  Description: this is Hurdle Game uses Javafx for its UI.
 */
package hurdles;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class HurdlesGame extends Application {

    // Game constants
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;
    private static final int PLAYER_SIZE = 40;
    private static final int HURDLE_WIDTH = 20;
    private static final int HURDLE_HEIGHT = 60;
    private static final int FLOOR_HEIGHT = 50;

    // Game elements
    private Pane gamePane;
    private Circle player;  // Updated to a circle shape for better character look
    private Rectangle[] hurdles;  // Multiple hurdles
    private Label scoreLabel;
    private Line floorLine;
    private Button startButton;

    // Game state variables
    private boolean jumping = false;
    private double velocityY = 0;
    private final double gravity = 0.5;
    private final double jumpPower = -10;
    private double hurdleSpeed = 5;
    private int score = 0;
    private boolean gameOver = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create main game pane and set background
        gamePane = new Pane();
        gamePane.setStyle("-fx-background-color: lightblue;");
        Scene scene = new Scene(gamePane, WIDTH, HEIGHT);

        // Create the player (circle with eyes)
        player = new Circle(PLAYER_SIZE / 2, Color.BLUE);
        player.setLayoutX(100);
        player.setLayoutY(HEIGHT - PLAYER_SIZE - FLOOR_HEIGHT);
        
        // Create hurdles with different shapes
        hurdles = new Rectangle[2];
        hurdles[0] = new Rectangle(HURDLE_WIDTH, HURDLE_HEIGHT, Color.RED);
        hurdles[1] = new Rectangle(HURDLE_WIDTH, HURDLE_HEIGHT * 1.5, Color.GREEN);  // Taller hurdle

        // Position the hurdles off-screen
        for (int i = 0; i < hurdles.length; i++) {
            hurdles[i].setLayoutX(WIDTH + i * 300);  // Spaced out hurdles
            hurdles[i].setLayoutY(HEIGHT - hurdles[i].getHeight() - FLOOR_HEIGHT);
        }

        // Create the score label
        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(new Font(20));
        scoreLabel.setTextFill(Color.BLACK);
        scoreLabel.setLayoutX(10);
        scoreLabel.setLayoutY(10);

        // Add a floor line
        floorLine = new Line(0, HEIGHT - FLOOR_HEIGHT, WIDTH, HEIGHT - FLOOR_HEIGHT);
        floorLine.setStroke(Color.BROWN);
        floorLine.setStrokeWidth(5);

        // Add a start button
        startButton = new Button("Start Game");
        startButton.setLayoutX(WIDTH / 2 - 50);
        startButton.setLayoutY(HEIGHT / 2 - 20);
        startButton.setOnAction(e -> startGame());

        // Add elements to the scene
        gamePane.getChildren().addAll(floorLine, player, scoreLabel, startButton);
        gamePane.getChildren().addAll(hurdles);

        // Handle key press events for jumping and restarting
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && !jumping && !gameOver) {
                jumping = true;
                velocityY = jumpPower;
            }
            if (event.getCode() == KeyCode.R && gameOver) {
                resetGame();  // Restart the game
            }
        });

        // Set the stage
        primaryStage.setTitle("Enhanced Hurdles Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Start the game and remove the start button
    private void startGame() {
        gamePane.getChildren().remove(startButton);
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver) {
                    updatePlayer();
                    updateHurdles();
                    checkCollision();
                    increaseDifficulty();
                }
            }
        };
        gameLoop.start();
    }

    // Update the player's position based on jumping state and gravity
    private void updatePlayer() {
        if (jumping) {
            velocityY += gravity;
            player.setLayoutY(player.getLayoutY() + velocityY);

            // Check if the player has landed on the floor
            if (player.getLayoutY() >= HEIGHT - PLAYER_SIZE - FLOOR_HEIGHT) {
                player.setLayoutY(HEIGHT - PLAYER_SIZE - FLOOR_HEIGHT);
                jumping = false;
            }
        }
    }

    // Move the hurdles and reset them once they go off-screen
    private void updateHurdles() {
        for (Rectangle hurdle : hurdles) {
            hurdle.setLayoutX(hurdle.getLayoutX() - hurdleSpeed);

            // Reset the hurdle when it goes off-screen and increase the score
            if (hurdle.getLayoutX() < -HURDLE_WIDTH) {
                hurdle.setLayoutX(WIDTH);
                score++;
                scoreLabel.setText("Score: " + score);
            }
        }
    }

    // Check for collision between the player and the hurdles
    private void checkCollision() {
        for (Rectangle hurdle : hurdles) {
            if (player.getBoundsInParent().intersects(hurdle.getBoundsInParent())) {
                System.out.println("Game Over!");
                gameOver = true;
                hurdleSpeed = 0;  // Stop the hurdles
                scoreLabel.setText("Game Over! Final Score: " + score + " (Press 'R' to Restart)");
            }
        }
    }

    // Increase the difficulty by speeding up the hurdles
    private void increaseDifficulty() {
        // Increase speed every 5 points
        if (score > 0 && score % 5 == 0) {
            hurdleSpeed += 0.01;
        }
    }

    // Reset the game to its initial state
    private void resetGame() {
        score = 0;
        hurdleSpeed = 5;
        for (int i = 0; i < hurdles.length; i++) {
            hurdles[i].setLayoutX(WIDTH + i * 300);  // Reset hurdle positions
        }
        scoreLabel.setText("Score: 0");
        gameOver = false;
        player.setLayoutY(HEIGHT - PLAYER_SIZE - FLOOR_HEIGHT);  // Reset player position
    }
}