package com.example.aproksymacja;

// Klasa AproksymacjaSredniaRuchoma implementuje aproksymację średnią ruchomą
public class AproksymacjaSredniaRuchoma {
    private double[] xValues; // Tablica wartości x
    private double[] yValues; // Tablica wartości y
    private int windowSize; // Rozmiar okna średniej ruchomej

    // Konstruktor klasy AproksymacjaSredniaRuchoma
    public AproksymacjaSredniaRuchoma(double[] xValues, double[] yValues, int windowSize) {
        this.xValues = xValues;
        this.yValues = yValues;
        this.windowSize = windowSize;
    }

    // Metoda obliczająca aproksymację średnią ruchomą
    public double[] oblicz() {
        double[] wyniki = new double[yValues.length]; // Tablica na wyniki aproksymacji

        // Pętla iterująca przez wszystkie wartości y
        for (int i = 0; i < yValues.length; i++) {
            // Określenie zakresu okna
            int start = Math.max(0, i - windowSize / 2);
            int end = Math.min(yValues.length - 1, i + windowSize / 2);

            double sum = 0; // Suma wartości y w oknie
            for (int j = start; j <= end; j++) {
                sum += yValues[j];
            }

            // Obliczenie średniej z wartości y w oknie
            wyniki[i] = sum / (end - start + 1);
        }

        return wyniki; // Zwrócenie tablicy wyników
    }
}