package com.example.aproksymacja;

// Klasa AproksymacjaWielomianowa implementuje aproksymację wielomianową
public class AproksymacjaWielomianowa {
    private double[] xValues; // Tablica wartości x
    private double[] yValues; // Tablica wartości y
    private int degree; // Stopień wielomianu
    private double[] coefficients; // Współczynniki wielomianu

    // Konstruktor klasy AproksymacjaWielomianowa
    public AproksymacjaWielomianowa(double[] xValues, double[] yValues, int degree) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.degree = degree;
        perturbValuesIfNecessary(); // Perturbacja wartości x, jeśli konieczne
        this.coefficients = computeCoefficients(); // Obliczanie współczynników wielomianu
    }

    // Metoda perturbująca wartości x, aby uniknąć problemów numerycznych
    private void perturbValuesIfNecessary() {
        for (int i = 1; i < xValues.length; i++) {
            if (Math.abs(xValues[i] - xValues[i - 1]) < 1e-10) {
                xValues[i] += 1e-9; // Perturbacja wartości x
            }
        }
    }

    // Metoda obliczająca współczynniki wielomianu
    private double[] computeCoefficients() {
        int n = Math.min(xValues.length, degree + 1); // Liczba współczynników
        double[][] matrix = new double[n][n]; // Macierz układu równań
        double[] vector = new double[n]; // Wektor wyrazów wolnych

        // Tworzenie macierzy układu równań
        for (int i = 0; i < n; i++) {
            vector[i] = yValues[i];
            double xPower = 1;
            for (int j = 0; j < n; j++) {
                matrix[i][j] = xPower;
                xPower *= xValues[i];
            }
        }

        return gaussElimination(matrix, vector); // Rozwiązanie układu równań metodą eliminacji Gaussa
    }

    // Metoda rozwiązująca układ równań metodą eliminacji Gaussa
    private double[] gaussElimination(double[][] matrix, double[] vector) {
        int n = vector.length;
        for (int i = 0; i < n; i++) {
            // Znalezienie wiersza o maksymalnej wartości w kolumnie i
            int max = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(matrix[j][i]) > Math.abs(matrix[max][i])) {
                    max = j;
                }
            }

            // Zamiana wierszy
            double[] temp = matrix[i];
            matrix[i] = matrix[max];
            matrix[max] = temp;

            double t = vector[i];
            vector[i] = vector[max];
            vector[max] = t;

            // Eliminacja zmiennych
            for (int j = i + 1; j < n; j++) {
                double factor = matrix[j][i] / matrix[i][i];
                vector[j] -= factor * vector[i];
                for (int k = i; k < n; k++) {
                    matrix[j][k] -= factor * matrix[i][k];
                }
            }
        }

        // Rozwiązanie układu równań
        double[] result = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += matrix[i][j] * result[j];
            }
            result[i] = (vector[i] - sum) / matrix[i][i];
        }
        return result;
    }

    // Metoda obliczająca wartości aproksymacji dla podanych wartości x
    public double[] oblicz(double[] x) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            double y = 0;
            double xPower = 1;
            for (int j = 0; j < coefficients.length; j++) {
                y += coefficients[j] * xPower;
                xPower *= x[i];
            }
            result[i] = y;
        }
        return result;
    }
}