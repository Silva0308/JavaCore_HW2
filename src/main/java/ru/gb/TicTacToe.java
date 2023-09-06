package ru.gb;

import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    //задаем необходимые константы
    private static final char HUMAN = 'X'; // Человек играет за крестик
    private static final char COMPUTER = '0'; //Компьютер играет за нолик
    private static final char EMPTY_FIELD = '*'; // Обозначаем пустое поле

    private static final Scanner scanner = new Scanner(System.in); //объект для ввода ходов
    private static final Random random = new Random(); //объект для случайного хода компьютера

    private static char[][] field; // Двумерный массив хранит текущее состояние игрового поля

    private static int fieldSizeX; // Размерность игрового поля по оси Х
    private static int fieldSizeY; // Размерность игрового поля по оси Y

    private static int winCount; //Выигрышная комбинация


    public static void main(String[] args) {

        while (true){
             fieldSizeX = enterNum("Введите размер поля по горизонтали: ");
            fieldSizeY = enterNum("Введите размер поля по вертикали: ");
            winCount = enterNum("Введите количество клеток для победы: ");
            initialize(fieldSizeX, fieldSizeY);
            printField();
            while (true){
                humanTurn();
                printField();
                if (checkGameState(HUMAN,"Вы победили!"))
                    break;
                computerTurn();
                printField();
                if (checkGameState(COMPUTER,"Победил компьютер!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Ввод параметров игры
     *
     * @param helloLine строка, приглашающая ко вводу
     * @return введённый параметр
     */
    private static int enterNum(String helloLine) {
        int num;
        while (true) {
            System.out.print(helloLine);
            if (scanner.hasNextInt()) {
                num = scanner.nextInt();
                scanner.nextLine();
                break;
            } else {
                System.out.println("Некорректное число, повторите попытку ввода.");
                scanner.nextLine();
            }
        }
        return num;
    }
    /**
     * Метод создает пустое игровое поле
     *
     * @param fieldSizeX Размерность игрового поля по оси Х
     * @param fieldSizeY Размерность игрового поля по оси Y
     */
    private static void initialize(int fieldSizeX, int fieldSizeY) {

        field = new char[fieldSizeX][fieldSizeY];
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                field[i][j] = EMPTY_FIELD;
            }
        }
    }

    /**
     * Метод рисует игровое поле вида
     * +-1-2-3-
     * 1|*|X|0|
     * 2|*|*|0|
     * 3|*|*|0|
     * --------
     */
    private static void printField() {
        System.out.print("+");
        for (int x = 0; x < fieldSizeX * 2 + 1; x++) {
            System.out.print((x % 2 == 0) ? "-" : x / 2 + 1);
        }
        System.out.println();
        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }
        for (int x = 0; x < fieldSizeX * 2 + 2; x++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Обработка хода игрока (человек)
     */
    private static void humanTurn() {
        int x, y;

        do {

            while (true) {
                System.out.print("Введите координату хода X в пределах игрового поля: ");
                if (scanner.hasNextInt()) {
                    x = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Некорректное число, повторите попытку ввода.");
                    scanner.nextLine();
                }
            }

            while (true) {
                System.out.print("Введите координату хода Y в пределах игрового поля: ");
                if (scanner.hasNextInt()) {
                    y = scanner.nextInt() - 1;
                    scanner.nextLine();
                    break;
                } else {
                    System.out.println("Некорректное число, повторите попытку ввода.");
                    scanner.nextLine();
                }
            }
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[y][x] = HUMAN;
    }

    /**
     * Проверка, ячейка является пустой (EMPTY_FIELD)
     *
     * @param x координата хода по X
     * @param y координата хода по Y
     * @return true, если клетка пустая
     */
    private static boolean isCellEmpty(int x, int y) {
        return field[y][x] == EMPTY_FIELD;
    }

    /**
     * Проверка корректности ввода
     * (координаты хода не должны превышать размерность игрового поля)
     *
     * @param x координата хода по X
     * @param y координата хода по Y
     * @return true, если координаты в пределах поля
     */
    private static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Обработка хода компьютера
     * метод выдает случайную координату в пределах игрового поля, проверяя пустая ли клетка
     */
    private static void computerTurn() {
        int x, y;

        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = COMPUTER;
    }

    /**
     * Проверка состояния игры
     *
     * @param c фишка игрока
     * @param s победный слоган
     * @return
     */
    private static boolean checkGameState(char c, String s) {
        if (checkWin(c)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }

        return false; // Игра продолжается
    }
    /**
     * Проверка игрового поля на состояние победы.
     * Проверка идет в цикле по вертикалям, по горизонталям и по правым и левым диагоналям
     * @param c проверяемые символы в цепочке (X или О)
     * @return true если на поле победная комбинация
     */


    public static boolean checkWin(char c) {
        // Проверка по вертикалям
        for (int y = 0; y < fieldSizeY; y++) {
            int count = 0;
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[x][y] == c) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == winCount) {
                    return true;
                }
            }
        }

        // Проверка по горизонталям
        for (int x = 0; x < fieldSizeX; x++) {
            int count = 0;
            for (int y = 0; y < fieldSizeY; y++) {
                if (field[x][y] == c) {
                    count++;
                } else {
                    count = 0;
                }
                if (count == winCount) {
                    return true;
                }
            }
        }

        // Проверка по диагонали слева сверху вниз направо
        for (int x = 0; x < fieldSizeX - winCount + 1; x++) {
            for (int y = 0; y < fieldSizeY - winCount + 1; y++) {
                int count = 0;
                for (int i = 0; i < winCount; i++) {
                    if (field[x + i][y + i] == c) {
                        count++;
                    } else {
                        count = 0;
                    }
                    if (count == winCount) {
                        return true;
                    }
                }
            }
        }

        // Проверка по диагонали справа сверху вниз налево
        for (int x = winCount - 1; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY - winCount + 1; y++) {
                int count = 0;
                for (int i = 0; i < winCount; i++) {
                    if (field[x - i][y + i] == c) {
                        count++;
                    } else {
                        count = 0;
                    }
                    if (count == winCount) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * Проверка на заполнение поля
     *
     * @return true, если поле заполнено
     */
    private static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }
}


