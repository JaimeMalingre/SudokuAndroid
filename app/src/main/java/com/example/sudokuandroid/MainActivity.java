package com.example.sudokuandroid;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText[][] sudokuCells = new EditText[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gridLayout = findViewById(R.id.grid);
        Button resetButton = findViewById(R.id.resetButton);
        Button solveButton = findViewById(R.id.solveButton);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuCells[i][j] = new EditText(this);
                gridLayout.addView(sudokuCells[i][j]);
            }
        }

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetarTablero();
            }
        });

        solveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolverTablero();
            }
        });

        resetarTablero();
    }

    private void resolverTablero() {
        // Limpia todas las celdas antes de resolver el Sudoku
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuCells[i][j].setText("");
            }
        }

        // Obtén el tablero actual desde las celdas de la interfaz de usuario
        int[][] sudokuBoard = getTableroDesdeInterfaz();

        if (resolverSudoku(sudokuBoard)) {
            // Si se pudo resolver, actualiza la interfaz de usuario con la solución
            updateUI(sudokuBoard);
        }
    }

    private boolean resolverSudoku(int[][] board) {
        // Encuentra una celda vacía
        int[] emptyCell = encontrarCeldaVacia(board);
        int row = emptyCell[0];
        int col = emptyCell[1];

        // Si no hay celdas vacías, el Sudoku está resuelto
        if (row == -1 && col == -1) {
            return true;
        }

        // Intenta colocar un número del 1 al 9 en la celda vacía
        for (int num = 1; num <= 9; num++) {
            if (esNumeroValido(board, row, col, num)) {
                // Si el número es válido, colócalo en la celda
                board[row][col] = num;

                // Intenta resolver el resto del tablero recursivamente
                if (resolverSudoku(board)) {
                    return true; // La solución se encontró
                }

                // Si no se puede resolver con este número, retrocede y prueba otro
                board[row][col] = 0;
            }
        }

        // No se encontró una solución con ninguno de los números, retrocede
        return false;
    }

    private int[] encontrarCeldaVacia(int[][] board) {
        int[] result = {-1, -1};
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    result[0] = i;
                    result[1] = j;
                    return result;
                }
            }
        }
        return result; // Retorna {-1, -1} si no hay celdas vacías
    }

    private boolean esNumeroValido(int[][] board, int row, int col, int num) {
        // Verifica si el número ya está en la fila o columna
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        // Verifica si el número ya está en el bloque 3x3
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }


    private int[][] getTableroDesdeInterfaz() {
        int[][] sudokuBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String cellValue = sudokuCells[i][j].getText().toString();
                if (!cellValue.isEmpty()) {
                    sudokuBoard[i][j] = Integer.parseInt(cellValue);
                } else {
                    sudokuBoard[i][j] = 0;
                }
            }
        }
        return sudokuBoard;
    }

    private void resetarTablero() {
        int[][] sudokuBoard = new int[9][9];

        // Genera un nuevo tablero de Sudoku
        nuevoTableroSudoku(sudokuBoard);

        // Elimina algunos números para crear el rompecabezas
        eliminarNumerosTablero(sudokuBoard);

        // Actualiza la interfaz de usuario con el rompecabezas generado
        updateUI(sudokuBoard);
    }

    private void eliminarNumerosTablero(int[][] board) {
        Random random = new Random();
        // Ajusta el nivel de dificultad cambiando el número de celdas a eliminar
        int numeros_eliminar = 40;

        for (int i = 0; i < numeros_eliminar; i++) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            board[row][col] = 0;
        }
    }

    private void updateUI(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != 0) {
                    sudokuCells[i][j].setText(String.valueOf(board[i][j]));
                    sudokuCells[i][j].setEnabled(false);
                    // Cambia el color de los números generados aleatoriamente a gris
                    sudokuCells[i][j].setTextColor(Color.BLACK);

                } else {
                    sudokuCells[i][j].setText("");
                    sudokuCells[i][j].setEnabled(true);
                    // Deja el color de los números introducidos por el usuario en negro
                    sudokuCells[i][j].setTextColor(Color.GRAY);
                }
            }
        }
    }

    private void nuevoTableroSudoku(int[][] board) {
        // Empieza con un tablero completamente resuelto
        //Tablero de Sudoku completamente resuelto y luego intercambiar aleatoriamente filas y columnas dentro del mismo bloque 3x3.
        //Esto garantizará que el tablero resultante sea también un Sudoku válido. Aquí hay un ejemplo de cómo podrías hacer esto:
        int[][] solvedBoard = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9},
                {4, 5, 6, 7, 8, 9, 1, 2, 3},
                {7, 8, 9, 1, 2, 3, 4, 5, 6},
                {2, 3, 4, 5, 6, 7, 8, 9, 1},
                {5, 6, 7, 8, 9, 1, 2, 3, 4},
                {8, 9, 1, 2, 3, 4, 5, 6, 7},
                {3, 4, 5, 6, 7, 8, 9, 1, 2},
                {6, 7, 8, 9, 1, 2, 3, 4, 5},
                {9, 1, 2, 3, 4, 5, 6, 7, 8}
        };

        Random random = new Random();

        // Mismas filas y columnas dentro del mismo bloque 3x3
        for (int i = 0; i < 9; i++) {
            int swapWith = i / 3 * 3 + random.nextInt(3);

            // Intercambiar filas
            int[] temp = solvedBoard[i];
            solvedBoard[i] = solvedBoard[swapWith];
            solvedBoard[swapWith] = temp;

            // Intercambiar columnas
            for (int j = 0; j < 9; j++) {
                int tempNum = solvedBoard[j][i];
                solvedBoard[j][i] = solvedBoard[j][swapWith];
                solvedBoard[j][swapWith] = tempNum;
            }
        }

        // Copia el tablero mezclado en el tablero de entrada
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = solvedBoard[i][j];
            }
        }
    }
}
