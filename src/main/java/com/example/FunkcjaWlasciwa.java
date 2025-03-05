package com.example;
// Klasa FunkcjaWlasciwa dziedziczy po klasie Skaner
public class FunkcjaWlasciwa extends Skaner {
    // Konstruktor klasy, inicjalizujący pola start, end, points, step
    public FunkcjaWlasciwa(double start, double end, int points, double step) {
        super(start, end, points, step);  // Wywołanie konstruktora klasy nadrzędnej
    }

    @Override
    public double[] oblicz() {
        double[] wyniki = new double[points]; // Tablica wyników
        double x = start; // Początkowa wartość x
        for (int i = 0; i < points; i++) {
            wyniki[i] = funkcja(x); // Obliczenie wartości funkcji
            x += step; // Zwiększenie x o krok
        }
        return wyniki; // Zwrócenie tablicy wyników
    }

    private double funkcja(double x) {
        // Implementacja funkcji podanej przez użytkownika
        return Math.sin(x); // Przykład: funkcja sinus
    }

    @Override
    public void wyswietl() {
        // Implementacja wyświetlania wykresu funkcji
    }
}