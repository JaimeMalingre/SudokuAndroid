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

    private EditText[][] celdasSudoku = new EditText[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gridLayout = findViewById(R.id.grid);
        Button botonReset = findViewById(R.id.resetButton);
        Button resolverBoton = findViewById(R.id.solveButton);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                celdasSudoku[i][j] = new EditText(this);
                gridLayout.addView(celdasSudoku[i][j]);
            }
        }

        botonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetarTablero();
            }
        });

        resolverBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolverTablero();
            }
        });

        resetarTablero();
    }

    private void resolverTablero() {
        // Obtén el tablero actual desde las celdas de la interfaz de usuario
        int[][] sudokuBoard = tablero();

        if (resolverSudoku(sudokuBoard)) {
            // Si se pudo resolver, actualiza la interfaz de usuario con la solución
            actuaslizarInterfaz(sudokuBoard);
        }
    }

    private boolean resolverSudoku(int[][] tabla) {
        // Encuentra una celda vacía
        int[] celdaVacia = encontrarCeldaVacia(tabla);
        int fila = celdaVacia[0];
        int columna = celdaVacia[1];

        // Si no hay celdas vacías, el Sudoku está resuelto
        if (fila == -1 && columna == -1) {
            return true;
        }

        // Intenta colocar un número del 1 al 9 en la celda vacía
        for (int num = 1; num <= 9; num++) {
            if (esNumeroValido(tabla, fila, columna, num)) {
                // Si el número es válido, colócalo en la celda
                tabla[fila][columna] = num;

                // Intenta resolver el resto del tablero recursivamente
                if (resolverSudoku(tabla)) {
                    return true; // La solución se encontró
                }

                // Si no se puede resolver con este número, retrocede y prueba otro
                tabla[fila][columna] = 0;
            }
        }

        // No se encontró una solución con ninguno de los números, retrocede
        return false;
    }

    private int[] encontrarCeldaVacia(int[][] tableroS) {
        int[] resultado = {-1, -1};
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tableroS[i][j] == 0) {
                    resultado[0] = i;
                    resultado[1] = j;
                    return resultado;
                }
            }
        }
        return resultado; // Devuelve si no hay celdas vacías
    }

    private boolean esNumeroValido(int[][] board, int fila, int columna, int num) {
        // Verifica si el número ya está en la fila o columna
        for (int i = 0; i < 9; i++) {
            if (board[fila][i] == num || board[i][columna] == num) {
                return false;
            }
        }

        // Verifica si el número ya está en el bloque 3x3
        int empezarFila = fila - fila % 3;
        int empezarColumna = columna - columna % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[empezarFila + i][empezarColumna + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }


    private int[][] tablero() {
        int[][] sudokuBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String celda = celdasSudoku[i][j].getText().toString();
                if (!celda.isEmpty()) {
                    sudokuBoard[i][j] = Integer.parseInt(celda);
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
        actuaslizarInterfaz(sudokuBoard);
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

    private void actuaslizarInterfaz(int[][] tableroSudoku) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tableroSudoku[i][j] != 0) {
                    celdasSudoku[i][j].setText(String.valueOf(tableroSudoku[i][j]));
                    celdasSudoku[i][j].setEnabled(false);
                    // Cambia el color de los números generados aleatoriamente a gris
                    celdasSudoku[i][j].setTextColor(Color.BLACK);

                } else {
                    celdasSudoku[i][j].setText("");
                    celdasSudoku[i][j].setEnabled(true);
                    // Deja el color de los números introducidos por el usuario en negro
                    celdasSudoku[i][j].setTextColor(Color.GRAY);
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
