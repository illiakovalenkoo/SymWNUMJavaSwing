package com.example;
 
// Abstrakcyjna klasa Skaner implementuje interfejs Numeryczny
public abstract class Skaner implements Numeryczny { 
    protected double start; // Początkowa wartość zakresu
    protected double end; // Końcowa wartość zakresu
    protected int points; // Liczba punktów do obliczenia
    protected double step; // Krok między punktami
   
    // Konstruktor klasy, inicjalizujący pola start, end, points i step
    public Skaner(double start, double end, int points, double step) {
        this.start = start;
        this.end = end;
        this.points = points;
        this.step = step;
    }
}