package com.example;

import com.example.aproksymacja.AproksymacjaSredniaRuchoma;
import com.example.aproksymacja.AproksymacjaWielomianowa;
import com.example.interpolacja.InterpolacjaLiniowa;
import com.example.interpolacja.InterpolacjaWielomianowa;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.nfunk.jep.JEP;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Główna klasa symulatora WNUMów
public class SymulatorWNUMow {
    public static void main(String[] args) {
        // Tworzenie okna aplikacji
        JFrame frame = new JFrame("Symulator WNUMów");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 550); // Ustawianie rozmiaru okna

        JPanel panel = new JPanel();
        frame.add(panel); // Dodawanie panelu do ramki
        placeComponents(panel); // Dodawanie komponentów do panelu

        frame.setVisible(true); // Wyświetlanie okna
    }

    // Metoda dodająca komponenty do panelu
    private static void placeComponents(JPanel panel) {
        panel.setLayout(null); // Wyłączanie domyślnego menedżera układu

        // Etykieta i pole tekstowe dla początku przedziału
        JLabel startLabel = new JLabel("Początek przedziału:");
        startLabel.setBounds(10, 20, 150, 25);
        panel.add(startLabel);

        JTextField startText = new JTextField(20);
        startText.setBounds(160, 20, 165, 25);
        panel.add(startText);

        // Etykieta i pole tekstowe dla końca przedziału
        JLabel endLabel = new JLabel("Koniec przedziału:");
        endLabel.setBounds(10, 50, 150, 25);
        panel.add(endLabel);

        JTextField endText = new JTextField(20);
        endText.setBounds(160, 50, 165, 25);
        panel.add(endText);

        // Etykieta i pole tekstowe dla liczby punktów
        JLabel pointsLabel = new JLabel("Liczba punktów:");
        pointsLabel.setBounds(10, 80, 150, 25);
        panel.add(pointsLabel);

        JTextField pointsText = new JTextField(20);
        pointsText.setBounds(160, 80, 165, 25);
        panel.add(pointsText);

        // Etykieta i pole tekstowe dla funkcji
        JLabel functionLabel = new JLabel("Funkcja:");
        functionLabel.setBounds(10, 110, 150, 25);
        panel.add(functionLabel);

        JTextField functionText = new JTextField(20);
        functionText.setBounds(160, 110, 165, 25);
        panel.add(functionText);

        // Etykieta i pole tekstowe dla stopnia wielomianu
        JLabel degreeLabel = new JLabel("Stopień wielomianu:");
        degreeLabel.setBounds(10, 140, 150, 25);
        panel.add(degreeLabel);

        JTextField degreeText = new JTextField(20);
        degreeText.setBounds(160, 140, 165, 25);
        panel.add(degreeText);

        // Checkboxy dla różnych metod interpolacji i aproksymacji
        JCheckBox functionCheckBox = new JCheckBox("Funkcja Właściwa");
        functionCheckBox.setBounds(10, 170, 150, 25);
        panel.add(functionCheckBox);

        JCheckBox linearCheckBox = new JCheckBox("Interpolacja Liniowa");
        linearCheckBox.setBounds(10, 200, 150, 25);
        panel.add(linearCheckBox);

        JCheckBox polynomialCheckBox = new JCheckBox("Interpolacja Wielomianowa");
        polynomialCheckBox.setBounds(10, 230, 200, 25);
        panel.add(polynomialCheckBox);

        JCheckBox polynomialApproximationCheckBox = new JCheckBox("Aproksymacja Wielomianowa");
        polynomialApproximationCheckBox.setBounds(10, 260, 250, 25);
        panel.add(polynomialApproximationCheckBox);

        JCheckBox movingAverageCheckBox = new JCheckBox("Aproksymacja Średnią Ruchomą");
        movingAverageCheckBox.setBounds(10, 290, 250, 25);
        panel.add(movingAverageCheckBox);

        // Przycisk do wyświetlania wykresu
        JButton showButton = new JButton("Pokaż Wykres");
        showButton.setBounds(10, 320, 150, 25);
        panel.add(showButton);

        // Przycisk do wyświetlania wykresu błędu
        JButton errorButton = new JButton("Pokaż Wykres Błędu");
        errorButton.setBounds(200, 320, 150, 25);
        panel.add(errorButton);

        // Dodawanie akcji do przycisku "Pokaż Wykres"
        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    showChart(panel, startText, endText, pointsText, functionText, degreeText, functionCheckBox, linearCheckBox, polynomialCheckBox, polynomialApproximationCheckBox, movingAverageCheckBox);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Problem parsowania funkcji: " + exception.getMessage());
                }
            }
        });

        // Dodawanie akcji do przycisku "Pokaż Wykres Błędu"
        errorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    showErrorChart(panel, startText, endText, pointsText, functionText, degreeText, linearCheckBox, polynomialCheckBox, polynomialApproximationCheckBox, movingAverageCheckBox);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Problem przy obliczaniu błędu: " + exception.getMessage());
                }
            }
        });
    }

    // Metoda pokazująca wykres na podstawie wprowadzonych danych
    private static void showChart(JPanel panel, JTextField startText, JTextField endText, JTextField pointsText, JTextField functionText, JTextField degreeText, JCheckBox functionCheckBox, JCheckBox linearCheckBox, JCheckBox polynomialCheckBox, JCheckBox polynomialApproximationCheckBox, JCheckBox movingAverageCheckBox) throws Exception {
        try {
            // Parsowanie danych wejściowych
            double start = Double.parseDouble(startText.getText());
            double end = Double.parseDouble(endText.getText());
            int points = Integer.parseInt(pointsText.getText());
            String function = functionText.getText();
            int degree = Integer.parseInt(degreeText.getText());

            // Walidacja danych wejściowych
            if (start >= end) {
                JOptionPane.showMessageDialog(panel, "Początek przedziału musi być mniejszy od końca przedziału");
                return;
            }

            if (points <= 1) {
                JOptionPane.showMessageDialog(panel, "Liczba punktów powinna być większa od 1");
                return;
            }

            // Inicjalizacja parsera JEP do obliczania wartości funkcji
            JEP jep = new JEP();
            jep.addVariable("x", 0);
            jep.parseExpression(function);
            if (jep.hasError()) {
                JOptionPane.showMessageDialog(panel, "Ta funkcja jest niepoprawna");
                return;
            }

            double step = (end - start) / (points - 1);

            // Tablice na wartości x i y
            double[] xValues = new double[points];
            double[] yValues = new double[points];

            // Obliczanie wartości y dla danych wartości x
            for (int i = 0; i < points; i++) {
                xValues[i] = start + i * step;
                jep.addVariable("x", xValues[i]);
                yValues[i] = jep.getValue();
            }

            XYSeriesCollection dataset = new XYSeriesCollection();

            // Dodawanie serii danych dla funkcji właściwej
            if (functionCheckBox.isSelected()) {
                // Ideal function with high resolution
                int idealPoints = (int) ((end - start) * 1000); // 1000 points per unit
                double idealStep = (end - start) / (idealPoints - 1);
                double[] idealXValues = new double[idealPoints];
                double[] idealYValues = new double[idealPoints];

                XYSeries seriesFunction = new XYSeries("Funkcja Właściwa");

                for (int i = 0; i < idealPoints; i++) {
                    idealXValues[i] = start + i * idealStep;
                    jep.addVariable("x", idealXValues[i]);
                    idealYValues[i] = jep.getValue();
                    seriesFunction.add(idealXValues[i], idealYValues[i]);
                }

                dataset.addSeries(seriesFunction);
            }

            // Dodawanie serii danych dla interpolacji liniowej
            if (linearCheckBox.isSelected()) {
                InterpolacjaLiniowa interpolacjaLiniowa = new InterpolacjaLiniowa(start, end, points, step, xValues, yValues);
                double[] wynikiInterpolacjiLiniowej = interpolacjaLiniowa.oblicz();

                XYSeries seriesInterpolationLiniowa = new XYSeries("Interpolacja Liniowa");

                for (int i = 0; i < wynikiInterpolacjiLiniowej.length; i++) {
                    seriesInterpolationLiniowa.add(xValues[i], wynikiInterpolacjiLiniowej[i]);
                }

                dataset.addSeries(seriesInterpolationLiniowa);
            }

            // Dodawanie serii danych dla interpolacji wielomianowej
            if (polynomialCheckBox.isSelected()) {
                InterpolacjaWielomianowa interpolacjaWielomianowa = new InterpolacjaWielomianowa(xValues, yValues, degree);
                double[] wynikiInterpolacjiWielomianowej = interpolacjaWielomianowa.oblicz(xValues.length * 10);

                XYSeries seriesInterpolationWielomianowa = new XYSeries("Interpolacja Wielomianowa");

                double stepPoly = (end - start) / (wynikiInterpolacjiWielomianowej.length - 1);
                for (int i = 0; i < wynikiInterpolacjiWielomianowej.length; i++) {
                    double x = start + i * stepPoly;
                    seriesInterpolationWielomianowa.add(x, wynikiInterpolacjiWielomianowej[i]);
                }

                dataset.addSeries(seriesInterpolationWielomianowa);
            }

            // Dodawanie serii danych dla aproksymacji wielomianowej
            if (polynomialApproximationCheckBox.isSelected()) {
                AproksymacjaWielomianowa aproksymacja = new AproksymacjaWielomianowa(xValues, yValues, degree);
                double[] wynikiAproksymacji = aproksymacja.oblicz(xValues);

                XYSeries seriesPolynomialApproximation = new XYSeries("Aproksymacja Wielomianowa");

                double stepApprox = (end - start) / (wynikiAproksymacji.length - 1);
                for (int i = 0; i < wynikiAproksymacji.length; i++) {
                    double x = start + i * stepApprox;
                    seriesPolynomialApproximation.add(x, wynikiAproksymacji[i]);
                }

                dataset.addSeries(seriesPolynomialApproximation);
            }

            // Dodawanie serii danych dla aproksymacji średnią ruchomą
            if (movingAverageCheckBox.isSelected()) {
                AproksymacjaSredniaRuchoma aproksymacja = new AproksymacjaSredniaRuchoma(xValues, yValues, 3);
                double[] wynikiAproksymacji = aproksymacja.oblicz();

                XYSeries seriesMovingAverageApproximation = new XYSeries("Aproksymacja Średnią Ruchomą");

                for (int i = 0; i < wynikiAproksymacji.length; i++) {
                    seriesMovingAverageApproximation.add(xValues[i], wynikiAproksymacji[i]);
                }

                dataset.addSeries(seriesMovingAverageApproximation);
            }

            // Tworzenie wykresu
            JFreeChart chart = ChartFactory.createXYLineChart(
                    "Wykresy Funkcji",
                    "X",
                    "Y",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            // Tworzenie panelu z wykresem i wyświetlanie go w nowym oknie
            ChartPanel chartPanel = new ChartPanel(chart);
            JFrame chartFrame = new JFrame("Wykres");
            chartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            chartFrame.add(chartPanel);
            chartFrame.pack();
            chartFrame.setVisible(true);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(panel, "Proszę podać właściwe dane numeryczne");
        }
    }

    // Metoda pokazująca wykres błędu na podstawie wprowadzonych danych
    private static void showErrorChart(JPanel panel, JTextField startText, JTextField endText, JTextField pointsText, JTextField functionText, JTextField degreeText, JCheckBox linearCheckBox, JCheckBox polynomialCheckBox, JCheckBox polynomialApproximationCheckBox, JCheckBox movingAverageCheckBox) throws Exception {
        double start = Double.parseDouble(startText.getText());
        double end = Double.parseDouble(endText.getText());
        int points = Integer.parseInt(pointsText.getText());
        String function = functionText.getText();
        int degree = Integer.parseInt(degreeText.getText());

        // Walidacja danych wejściowych
        if (start >= end) {
            JOptionPane.showMessageDialog(panel, "Wartość początkowa musi mniejsza od końcowej");
            return;
        }

        // Określenie metody interpolacji/aproksymacji
        String method = null;
        if (linearCheckBox.isSelected()) {
            method = "linear";
        } else if (polynomialCheckBox.isSelected()) {
            method = "polynomial";
        } else if (polynomialApproximationCheckBox.isSelected()) {
            method = "aproksymacja_wielomianowa";
        } else if (movingAverageCheckBox.isSelected()) {
            method = "srednia_ruchoma";
        } else {
            JOptionPane.showMessageDialog(panel, "Proszę wybrać metodę");
            return;
        }

        double[] errors;
        String title;
        String xLabel;

        if (method.equals("srednia_ruchoma")) {
            errors = ErrorChart.calculatePercentageErrorForDifferentWindowSizes(start, end, function, points);
            title = "Wykres błędu dla Aproksymacji Średnią Ruchomą";
            xLabel = "Rozmiar okna";
        } else {
            errors = ErrorChart.calculatePercentageErrorForDifferentPoints(start, end, function, method, degree);
            title = "Wykres błędu dla " + method.substring(0, 1).toUpperCase() + method.substring(1) + " interpolacji";
            xLabel = "Liczba punktów";
        }

        ErrorChart chart = new ErrorChart(title, errors, xLabel);
        chart.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chart.pack();
        chart.setVisible(true);
    }
}