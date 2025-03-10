package com.example.interpolacja;

// Klasa InterpolacjaLiniowa implementuje interpolację liniową
public class InterpolacjaLiniowa {

    private double start; // Początkowa wartość zakresu
    private double end; // Końcowa wartość zakresu
    private int points; // Liczba punktów do interpolacji
    private double step; // Krok między punktami
    private double[] xValues; // Tablica wartości x
    private double[] yValues; // Tablica wartości y

    // Konstruktor klasy InterpolacjaLiniowa
    public InterpolacjaLiniowa(double start, double end, int points, double step, double[] xValues, double[] yValues) {
        this.start = start;
        this.end = end;
        this.points = points;
        this.step = step;
        this.xValues = xValues;
        this.yValues = yValues;
    }

    // Metoda obliczająca wartości interpolacji dla podanych wartości x
    public double[] oblicz() {
        double[] wyniki = new double[points]; // Tablica na wyniki interpolacji
        for (int i = 0; i < points; i++) {
            wyniki[i] = interpolate(xValues[i], xValues, yValues); // Interpolacja liniowa dla każdego punktu
        }
        return wyniki; // Zwrócenie tablicy wyników
    }

    // Statyczna metoda wykonująca interpolację liniową
    public static double interpolate(double x, double[] xs, double[] ys) {
        for (int i = 1; i < xs.length; i++) {
            if (x < xs[i]) {
                double x0 = xs[i - 1];
                double x1 = xs[i];
                double y0 = ys[i - 1];
                double y1 = ys[i];
                // Obliczanie wartości y na podstawie interpolacji liniowej
                return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
            }
        }
        return ys[ys.length - 1]; // Zwrócenie ostatniej wartości y, jeśli x jest poza zakresem
    }
}