package com.example;

import com.example.aproksymacja.AproksymacjaSredniaRuchoma;
import com.example.aproksymacja.AproksymacjaWielomianowa;
import com.example.interpolacja.InterpolacjaLiniowa;
import com.example.interpolacja.InterpolacjaWielomianowa;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.nfunk.jep.JEP;

import javax.swing.*;

// Klasa ErrorChart dziedziczy po JFrame i tworzy okno z wykresem błędów
public class ErrorChart extends JFrame {

    // Konstruktor klasy ErrorChart
    public ErrorChart(String title, double[] errors, String xLabel) {
        super(title);
        // Tworzenie wykresu liniowego
        JFreeChart lineChart = ChartFactory.createLineChart(
                title, // Tytuł wykresu
                xLabel, // Etykieta osi X
                "Błąd [%]", // Etykieta osi Y
                createDataset(errors), // Dane do wykresu
                PlotOrientation.VERTICAL, // Orientacja wykresu
                true, true, false); // Legendy, tooltips, URL

        // Tworzenie panelu z wykresem
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    // Metoda tworząca dane do wykresu
    private DefaultCategoryDataset createDataset(double[] errors) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < errors.length; i++) {
            dataset.addValue(errors[i], "Błąd", Integer.toString(i + 1)); // Dodanie wartości błędu do danych
        }
        return dataset;
    }

    // Metoda obliczająca procentowy błąd dla różnych liczby punktów
    public static double[] calculatePercentageErrorForDifferentPoints(double start, double end, String function, String method, int degree) throws Exception {
        int maxPoints = 100; // Maksymalna liczba punktów
        double[] errors = new double[maxPoints]; // Tablica na błędy

        // Inicjalizacja parsera JEP do obliczania wartości funkcji
        JEP jep = new JEP();
        jep.addVariable("x", 0);
        jep.parseExpression(function);

        if (jep.hasError()) {
            throw new Exception("Nieprawidłowa funkcja");
        }

        // Obliczanie idealnych wartości funkcji
        int idealPoints = 1000; // Liczba punktów dla idealnej funkcji
        double[] idealXValues = new double[idealPoints];
        double[] idealYValues = new double[idealPoints];
        double idealStep = (end - start) / (idealPoints - 1);

        for (int i = 0; i < idealPoints; i++) {
            idealXValues[i] = start + i * idealStep;
            jep.addVariable("x", idealXValues[i]);
            idealYValues[i] = jep.getValue(); // Obliczanie wartości idealnej funkcji
        }

        // Obliczanie błędów dla różnych liczby punktów
        for (int points = 1; points <= maxPoints; points++) {
            double step = (end - start) / (points - 1);
            double[] xValues = new double[points];
            double[] yValues = new double[points];

            for (int i = 0; i < points; i++) {
                xValues[i] = start + i * step;
                jep.addVariable("x", xValues[i]);
                yValues[i] = jep.getValue(); // Obliczanie wartości funkcji dla interpolacji
            }

            double[] interpolatedValues = new double[idealXValues.length];
            if (method.equals("linear")) {
                InterpolacjaLiniowa interpolacjaLiniowa = new InterpolacjaLiniowa(start, end, points, step, xValues, yValues);
                for (int i = 0; i < idealXValues.length; i++) {
                    interpolatedValues[i] = interpolacjaLiniowa.interpolate(idealXValues[i], xValues, yValues); // Interpolacja liniowa
                }
            } else if (method.equals("polynomial")) {
                InterpolacjaWielomianowa interpolacjaWielomianowa = new InterpolacjaWielomianowa(xValues, yValues, degree);
                for (int i = 0; i < idealXValues.length; i++) {
                    interpolatedValues[i] = interpolacjaWielomianowa.interpolate(idealXValues[i]); // Interpolacja wielomianowa
                }
            } else if (method.equals("aproksymacja_wielomianowa")) {
                AproksymacjaWielomianowa aproksymacjaWielomianowa = new AproksymacjaWielomianowa(xValues, yValues, degree);
                for (int i = 0; i < idealXValues.length; i++) {
                    interpolatedValues[i] = aproksymacjaWielomianowa.oblicz(new double[]{idealXValues[i]})[0]; // Aproksymacja wielomianowa
                }
            }

            // Obliczanie całkowitego błędu
            double totalError = 0;
            for (int i = 0; i < idealXValues.length; i++) {
                totalError += Math.abs(idealYValues[i] - interpolatedValues[i % interpolatedValues.length]);
            }
            errors[points - 1] = (totalError / idealXValues.length) * 100; // Obliczanie średniego błędu procentowego
        }

        return errors;
    }

    // Metoda obliczająca procentowy błąd dla różnych rozmiarów okna średniej ruchomej
    public static double[] calculatePercentageErrorForDifferentWindowSizes(double start, double end, String function, int points) throws Exception {
        int maxWindowSize = 100; // Maksymalny rozmiar okna
        double[] errors = new double[maxWindowSize - 2]; // Tablica na błędy

        // Inicjalizacja parsera JEP do obliczania wartości funkcji
        JEP jep = new JEP();
        jep.addVariable("x", 0);
        jep.parseExpression(function);

        if (jep.hasError()) {
            throw new Exception("Nieprawidłowa funkcja");
        }

        // Obliczanie idealnych wartości funkcji
        int idealPoints = 1000; // Liczba punktów dla idealnej funkcji
        double[] idealXValues = new double[idealPoints];
        double[] idealYValues = new double[idealPoints];
        double idealStep = (end - start) / (idealPoints - 1);

        for (int i = 0; i < idealPoints; i++) {
            idealXValues[i] = start + i * idealStep;
            jep.addVariable("x", idealXValues[i]);
            idealYValues[i] = jep.getValue(); // Obliczanie wartości idealnej funkcji
        }

        // Obliczanie wartości funkcji dla aproksymacji
        double step = (end - start) / (points - 1);
        double[] xValues = new double[points];
        double[] yValues = new double[points];

        for (int i = 0; i < points; i++) {
            xValues[i] = start + i * step;
            jep.addVariable("x", xValues[i]);
            yValues[i] = jep.getValue(); // Obliczanie wartości funkcji
        }

        // Obliczanie błędów dla różnych rozmiarów okna średniej ruchomej
        for (int windowSize = 3; windowSize <= maxWindowSize; windowSize++) {
            AproksymacjaSredniaRuchoma movingAverage = new AproksymacjaSredniaRuchoma(xValues, yValues, windowSize);
            double[] wynikiSredniejRuchomej = movingAverage.oblicz();

            // Obliczanie całkowitego błędu
            double totalError = 0;
            for (int i = 0; i < idealXValues.length; i++) {
                totalError += Math.abs(idealYValues[i] - wynikiSredniejRuchomej[i % wynikiSredniejRuchomej.length]);
            }
            errors[windowSize - 3] = (totalError / idealXValues.length) * 100; // Obliczanie średniego błędu procentowego
        }

        return errors;
    }
}