package org.example;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Scanner;

public class TreeVisualizer extends Application {

    static class Node {
        char letter;
        String morseCode;
        Node left;
        Node right;

        public Node(char letter, String morseCode) {
            this.letter = letter;
            this.morseCode = morseCode;
        }
        public Node(char letter) {
            this.letter = letter;
            this.morseCode = "";
        }

    }


    static class MorseBST {
        private Node root;

        public MorseBST() {
            root = new Node(' ');
        }

        public void insert(char letter, String morseCode) {
            Node current = root;
            for (char c : morseCode.toCharArray()) {
                if (c == '.') {
                    if (current.right == null) current.right = new Node(' ');
                    current = current.right;
                } else if (c == '-') {
                    if (current.left == null) current.left = new Node(' ');
                    current = current.left;
                }
            }
            current.letter = letter;
            current.morseCode = morseCode;
        }

        public int getHeight() {
            return getHeight(root);
        }

        private int getHeight(Node node) {
            if (node == null) {
                return 0;
            }
            return 1 + Math.max(getHeight(node.left), getHeight(node.right));
        }

        public char decodeCharacter(String morseCode) {
            Node current = root;
            for (char c : morseCode.toCharArray()) {
                if (current == null) return '?';
                current = (c == '.') ? current.right : current.left;
            }
            return (current != null) ? current.letter : '?';
        }

        public String decodeMessage(String morseMessage) {
            StringBuilder sb = new StringBuilder();
            String[] morseChars = morseMessage.trim().split(" ");
            for (String mc : morseChars) {
                if (mc.equals("/")) {
                    sb.append(" ");
                } else {
                    sb.append(decodeCharacter(mc));
                }
            }
            return sb.toString();
        }

        public void drawTree(Canvas canvas) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);

            drawNode(gc, root, canvas.getWidth() / 2, 40, canvas.getWidth() / 4, 1);
        }

        private void drawNode(GraphicsContext gc, Node node, double x, double y, double xOffset, int level) {
            if (node == null) return;

            gc.setStroke(Color.BLACK);
            gc.strokeOval(x - 15, y - 15, 30, 30);
            gc.strokeText(String.valueOf(node.letter == ' ' ? ' ' : node.letter), x - 5, y + 5);

            if (node.left != null) {
                double newX = x - xOffset;
                double newY = y + 120;
                gc.strokeLine(x, y + 15, newX, newY - 15);
                drawNode(gc, node.left, newX, newY, xOffset / 2, level + 1);
            }

            if (node.right != null) {
                double newX = x + xOffset;
                double newY = y + 120;
                gc.strokeLine(x, y + 15, newX, newY - 15);
                drawNode(gc, node.right, newX, newY, xOffset / 2, level + 1);
            }
        }
        public void printTree() {
            printTreeRecursive(root, "", true);
        }

        private void printTreeRecursive(Node node, String prefix, boolean isTail) {
            if (node == null) return;

            System.out.println(prefix + (isTail ? "└── " : "├── ") + (node.letter == ' ' ? "*" : node.letter));

            boolean hasLeft = node.left != null;
            boolean hasRight = node.right != null;

            if (hasLeft || hasRight) {
                if (hasRight)
                    printTreeRecursive(node.right, prefix + (isTail ? "    " : "│   "), false);
                if (hasLeft)
                    printTreeRecursive(node.left, prefix + (isTail ? "    " : "│   "), true);
            }
        }

    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Visualizador de Árvore Morse");

        MorseBST bst = new MorseBST();

        // Letras principais em código Morse (pode expandir mais depois)
        bst.insert('W', ".--");
        bst.insert('D', "-..");
        bst.insert('X', "-..-");
        bst.insert('E', ".");
        bst.insert('M', "--");
        bst.insert('C', "-.-.");
        bst.insert('N', "-.");
        bst.insert('A', ".-");
        bst.insert('S', "...");
        bst.insert('F', "..-.");
        bst.insert('Y', "-.--");
        bst.insert('B', "-...");
        bst.insert('K', "-.-");
        bst.insert('Q', "--.-");
        bst.insert('Z', "--..");
        bst.insert('T', "-");
        bst.insert('L', ".-..");
        bst.insert('O', "---");
        bst.insert('H', "....");
        bst.insert('V', "...-");
        bst.insert('I', "..");
        bst.insert('J', ".---");
        bst.insert('P', ".--.");
        bst.insert('G', "--.");
        bst.insert('R', ".-.");
        bst.insert('U', "..-");

        int height = bst.getHeight();
        int canvasHeight = 100 + height * 100;
        int canvasWidth = (int) Math.pow(2, height) * 40;

        Canvas canvas = new Canvas(canvasWidth, canvasHeight);
        bst.drawTree(canvas);

        Group root = new Group();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, canvasWidth, canvasHeight);
        primaryStage.setScene(scene);
        primaryStage.show();

        System.out.println("Árvore Binária de Busca (Visualização Hierárquica):");
        bst.printTree();
        System.out.println();

        // Input do usuário para decodificação
        System.out.println("Digite a mensagem em código Morse (use espaço entre letras e '/' entre palavras):");
        Scanner scanner = new Scanner(System.in);
        String morseInput = scanner.nextLine();

        String decoded = bst.decodeMessage(morseInput);
        System.out.println("Mensagem decodificada: " + decoded);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
