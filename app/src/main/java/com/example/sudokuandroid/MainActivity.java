package com.example.sudokuandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sudokuandroid.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText[][] sudokuCells = new EditText[9][9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridLayout gridLayout = findViewById(R.id.grid);
        Button resetButton = findViewById(R.id.resetButton);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sudokuCells[i][j] = new EditText(this);
                gridLayout.addView(sudokuCells[i][j]);
            }
        }

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });
        resetBoard();
    }

    private void resetBoard() {
        int[][] sudokuBoard = new int[9][9];

        // Generate a new Sudoku board
        generateSudokuBoard(sudokuBoard);

        // Remove some numbers to create the puzzle
        removeNumbersFromBoard(sudokuBoard);

        // Update the UI with the generated puzzle
        updateUI(sudokuBoard);
    }

    private void removeNumbersFromBoard(int[][] board) {
        Random random = new Random();
        // Ajusta el nivel de dificultad cambiando el número de celdas a eliminar
        int numeros_eliminar = 30;

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
                    sudokuCells[i][j].setEnabled(false); // Disable user input for given numbers
                } else {
                    sudokuCells[i][j].setText(""); // Clear text for empty cells
                    sudokuCells[i][j].setEnabled(true); // Enable user input for empty cells
                }
            }
        }
    }

    private void generateSudokuBoard(int[][] board) {
        // Start with a fully solved board
        //tablero de Sudoku completamente resuelto y luego intercambiar aleatoriamente filas y columnas dentro del mismo bloque 3x3.
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

        // Swap rows and columns within the same 3x3 block
        for (int i = 0; i < 9; i++) {
            int swapWith = i / 3 * 3 + random.nextInt(3);

            // Swap rows
            int[] temp = solvedBoard[i];
            solvedBoard[i] = solvedBoard[swapWith];
            solvedBoard[swapWith] = temp;

            // Swap columns
            for (int j = 0; j < 9; j++) {
                int tempNum = solvedBoard[j][i];
                solvedBoard[j][i] = solvedBoard[j][swapWith];
                solvedBoard[j][swapWith] = tempNum;
            }
        }

        // Copy the shuffled board to the input board
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = solvedBoard[i][j];
            }
        }
    }

    private boolean isValid(int[][] board, int row, int col, int number) {
        // Check the column
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == number) {
                return false;
            }
        }

        // Check the row
        for (int j = 0; j < 9; j++) {
            if (board[row][j] == number) {
                return false;
            }
        }

        // Check the box
        int boxRow = row - row % 3;
        int boxCol = col - col % 3;

        for (int i = boxRow; i < boxRow + 3; i++) {
            for (int j = boxCol; j < boxCol + 3; j++) {
                if (board[i][j] == number) {
                    return false;
                }
            }
        }
        return true;
    }
}
