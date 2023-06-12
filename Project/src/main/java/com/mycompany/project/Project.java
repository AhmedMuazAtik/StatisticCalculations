/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.project;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author ahmed
 */
public class Project {

    /*calculateQuartiles: Bir veri setinin çeyreklerini hesaplar.
    calculateInterquartileRange: Bir veri setinin interquartile aralığını hesaplar.
    calculateSkewness: Bir veri setinin çarpıklığını hesaplar.
    calculateKurtosis: Bir veri setinin basıklığını hesaplar.
    sort: Veri setini sıralar.
    isNormalDistribution: Veri setinin normal dağılıma sahip olup olmadığını kontrol eder.
    readDataFromCSV: CSV dosyasından veri okur.
    calculateSampleSize: Örneklemin boyutunu hesaplar.
    lookupZScore: Belirli bir güven seviyesine karşılık gelen z puanını bulur.
    calculatePopulationStdDev: Bir veri setinin popülasyon standart sapmasını hesaplar.
    calculateMedian: Bir veri setinin ortancasını hesaplar.
    calculateVariance: Bir veri setinin varyansını hesaplar.
    calculateStandardDeviation: Bir veri setinin standart sapmasını hesaplar.
    customSqrt: Bir sayının karekökünü hesaplar.
    calculateMean: Bir veri setinin ortalamasını hesaplar.
    customAbs: Bir sayının mutlak değerini hesaplar.*/
    
    public static void main(String[] args) throws IOException {

        String csvFile = "C:\\Users\\ahmed\\Desktop\\2121221002_AhmedMuazAtik_Project\\Salary_Data.csv";
        String delimiter = ","; // CSV delimiter
        int columnIndex = 4; // Index of the column you want to extract (0-based index)

        ArrayList<Double> doubles = new ArrayList<>();

        try ( BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(delimiter);
                if (fields.length > columnIndex) {
                    try {
                        double value = Double.parseDouble(fields[columnIndex]);
                        doubles.add(value);
                    } catch (NumberFormatException e) {
                        // Handle non-integer values in the column if needed
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        double mean = calculateMean(doubles);

        double median = calculateMedian(doubles);

        double variance = calculateVariance(doubles);

        double deviation = calculateStandardDeviation(doubles);

        double error = calculateStandardError(doubles);

        double criticalValue = 1.96;

        int sample = 5;

        double meanMarginOfError = criticalValue * Math.sqrt(variance / sample);
        double marginOfError = 0.1; // Desired margin of error
        double confidenceLevel = 0.90; // 90% confidence level

        String column = "Years of Experience";

        ArrayList<Double> data = readDataFromCSV(csvFile, column);

        int sampleSize = calculateSampleSize(data, marginOfError, confidenceLevel);

        // Determine skewness
        double skewness = calculateSkewness(data);

        // Determine kurtosis
        double kurtosis = calculateKurtosis(data);

        // Determine normality using Jarque-Bera test
        boolean isNormal = isNormallyDistributed(data);

        System.out.println("Mean: " + mean);

        System.out.println("Median: " + median);

        System.out.println("Variance: " + variance);

        System.out.println("Standart Deviation: " + deviation);

        System.out.println("Standart Error: " + error);

        System.out.println("Outliers: " + findOutliers(doubles));

        System.out.println("%95 Confidence Interval: " + meanMarginOfError);

        System.out.println("Sample Size: " + sampleSize);

        // Decide the shape of the distribution
        if (isNormal) {
            System.out.println("Normally Distributed");
        } else {
            System.out.println("Not Normally Distributed");
        }
        System.out.println("Skewness: " + skewness);

        System.out.println("Kurtosis: " + kurtosis);

        System.out.println("Histogram: ");

        // Define histogram parameters
        int numBins = 10;
        double minValue = getMinValue(data);
        double maxValue = getMaxValue(data);

        // Calculate bin width
        double binWidth = (maxValue - minValue) / numBins;

        // Initialize bin counts
        int[] binCounts = new int[numBins];

        // Populate bin counts
        for (double value : data) {
            int binIndex = (int) ((value - minValue) / binWidth);
            if (binIndex >= 0 && binIndex < numBins) {
                binCounts[binIndex]++;
            }
        }

        // Display histogram
        for (int i = 0; i < numBins; i++) {
            double binStart = minValue + i * binWidth;
            double binEnd = binStart + binWidth;
            System.out.printf("%.2f - %.2f: %d\n", binStart, binEnd, binCounts[i]);
        }

        // Create a JFrame to hold the boxplot
        JFrame frame = new JFrame("Box Plot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        // Create a JPanel to draw the boxplot
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Calculate boxplot dimensions
                int x = 50; // X-coordinate of the boxplot
                int y = 50; // Y-coordinate of the boxplot
                int width = 300; // Width of the boxplot
                int height = 300; // Height of the boxplot

                // Calculate quartiles and interquartile range
                double q1 = getQuartile(data, 1);
                double q3 = getQuartile(data, 3);
                double iqr = q3 - q1;

                // Calculate whisker positions
                double lowerWhisker = q1 - 1.5 * iqr;
                double upperWhisker = q3 + 1.5 * iqr;

                // Draw the box
                g.setColor(Color.BLACK);
                g.drawRect(x, y + height / 4, width, height / 2);
                g.drawLine(x, y + height / 2, x + width, y + height / 2);

                // Draw the whiskers
                g.drawLine(x, y + height / 2, x, y + height / 4);
                g.drawLine(x + width, y + height / 2, x + width, y + height / 4);

                // Draw the median line
                double median = getQuartile(data, 2);
                int medianX = x;
                int medianY = (int) (y + height / 2 - ((median - lowerWhisker) / (upperWhisker - lowerWhisker)) * height / 2);
                g.drawLine(medianX, medianY, medianX + width, medianY);

                // Draw the outliers
                for (double value : data) {
                    if (value < lowerWhisker || value > upperWhisker) {
                        int outlierX = (int) (x + width / 2);
                        int outlierY = (int) (y + height / 2 - ((value - lowerWhisker) / (upperWhisker - lowerWhisker)) * height / 2);
                        g.setColor(Color.RED);
                        g.drawOval(outlierX - 3, outlierY - 3, 6, 6);
                    }
                }
            }
        };

        // Set the layout manager to null for custom positioning
        panel.setLayout(null);

        // Set the bounds of the panel
        panel.setBounds(0, 0, frame.getWidth(), frame.getHeight());

        // Add the panel to the frame
        frame.add(panel);

        // Make the frame visible
        frame.setVisible(true);
    }

    private static double getLowerWhisker(ArrayList<Double> data) {
        double q1 = getQuartile(data, 1);
        double iqr = getInterquartileRange(data);
        return q1 - 1.5 * iqr;
    }

    private static double getUpperWhisker(ArrayList<Double> data) {
        double q3 = getQuartile(data, 3);
        double iqr = getInterquartileRange(data);
        return q3 + 1.5 * iqr;
    }

    private static double getInterquartileRange(ArrayList<Double> data) {
        double q1 = getQuartile(data, 1);
        double q3 = getQuartile(data, 3);
        return q3 - q1;
    }

    private static double getQuartile(ArrayList<Double> data, int quartile) {
        int n = data.size();
        ArrayList<Double> sortedData = new ArrayList<>(data);

        // Bubble sort to sort the data list
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sortedData.get(j) > sortedData.get(j + 1)) {
                    double temp = sortedData.get(j);
                    sortedData.set(j, sortedData.get(j + 1));
                    sortedData.set(j + 1, temp);
                }
            }
        }

        int index = (int) Math.ceil(n * quartile / 4.0) - 1;
        return sortedData.get(index);
    }

    public static double getMinValue(List<Double> data) {
        double minValue = Double.MAX_VALUE;
        for (double value : data) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    public static double getMaxValue(List<Double> data) {
        double maxValue = Double.MIN_VALUE;
        for (double value : data) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    public static double calculateSkewness(ArrayList<Double> data) {
        double mean = calculateMean(data);
        double variance = calculateVariance(data);
        double stdDev = Math.sqrt(variance);

        double skewness = 0.0;
        for (double value : data) {
            skewness += Math.pow((value - mean) / stdDev, 3);
        }
        skewness *= (1.0 / data.size());
        return skewness;
    }

    public static double calculateKurtosis(ArrayList<Double> data) {
        double mean = calculateMean(data);
        double variance = calculateVariance(data);
        double stdDev = Math.sqrt(variance);

        double kurtosis = 0.0;
        for (double value : data) {
            kurtosis += Math.pow((value - mean) / stdDev, 4);
        }
        kurtosis *= (1.0 / data.size());
        return kurtosis - 3.0;
    }

    public static void sortList(List<Double> data) {
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = 0; j < data.size() - 1 - i; j++) {
                if (data.get(j) > data.get(j + 1)) {
                    double temp = data.get(j);
                    data.set(j, data.get(j + 1));
                    data.set(j + 1, temp);
                }
            }
        }
    }

    public static boolean isNormallyDistributed(ArrayList<Double> data) {
        int n = data.size();

        ArrayList<Double> sortedData = new ArrayList<>(data);
        sortList(sortedData);

        List<Double> deviations = new ArrayList<>();
        double mean = calculateMean(sortedData);
        double stdDev = calculateStandardDeviation(sortedData);
        for (int i = 0; i < sortedData.size(); i++) {
            double value = sortedData.get(i);
            deviations.add((value - mean) / stdDev);
        }

        double jarqueBera = (n / 6.0) * (Math.pow(calculateSkewness(sortedData), 2)
                + (1.0 / 4.0) * Math.pow(calculateKurtosis(sortedData), 2));
        boolean isNormal = jarqueBera <= 5.99;
        return isNormal;
    }

    public static ArrayList<Double> readDataFromCSV(String csvFile, String column) {
        ArrayList<Double> data = new ArrayList<>();

        try ( BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            String[] headers = br.readLine().split(",");
            int columnIndex = -1;

            for (int i = 0; i < headers.length; i++) {
                if (headers[i].equalsIgnoreCase(column)) {
                    columnIndex = i;
                    break;
                }
            }

            if (columnIndex == -1) {
                throw new IllegalArgumentException("Column not found in the CSV file.");
            }

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                double value = Double.parseDouble(values[columnIndex]);
                data.add(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static double customCeil(double value) {
        int intValue = (int) value;
        return intValue < value ? intValue + 1 : intValue;
    }

    public static int calculateSampleSize(List<Double> data, double marginOfError, double confidenceLevel) {
        double zScore = getZScore(confidenceLevel);
        double estimatedPopulationStdDev = calculatePopulationStdDev(data);
        double sampleSize = customPow((zScore * estimatedPopulationStdDev) / marginOfError, 2);
        return (int) customCeil(sampleSize);
    }

    public static double getZScore(double confidenceLevel) {
        double[] confidenceLevels = {0.8, 0.85, 0.9, 0.95, 0.975, 0.99, 0.995};
        double[] zScores = {1.2816, 1.4401, 1.645, 1.9599, 2.2414, 2.5758, 2.807};

        // Find the closest confidence level in the lookup table
        int closestIndex = 0;
        double minDifference = customAbs(confidenceLevels[0] - confidenceLevel);
        for (int i = 1; i < confidenceLevels.length; i++) {
            double difference = customAbs(confidenceLevels[i] - confidenceLevel);
            if (difference < minDifference) {
                minDifference = difference;
                closestIndex = i;
            }
        }

        return zScores[closestIndex];
    }

    public static double customPow(double base, double exponent) {
        double result = 1.0;
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }
        return result;
    }

    public static double calculatePopulationStdDev(List<Double> data) {
        double sum = 0.0;
        double mean = calculateMean(data);

        for (double value : data) {
            double difference = value - mean;
            sum += customPow(difference, 2);
        }

        double variance = sum / (data.size() - 1);
        return customSqrt(variance);
    }

    public static double calculateMedian(ArrayList<Double> dataset) {
        int length = dataset.size();
        int middleIndex = length / 2;

        // Custom sorting algorithm (e.g., bubble sort)
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - i - 1; j++) {
                if (dataset.get(j) > dataset.get(j + 1)) {
                    // Swap the elements
                    double temp = dataset.get(j);
                    dataset.set(j, dataset.get(j + 1));
                    dataset.set(j + 1, temp);
                }
            }
        }

        if (length % 2 == 0) {
            // Even number of values, average the two middle values
            double middleValue1 = dataset.get(middleIndex - 1);
            double middleValue2 = dataset.get(middleIndex);
            return (middleValue1 + middleValue2) / 2.0;
        } else {
            // Odd number of values, return the middle value
            return dataset.get(middleIndex);
        }
    }

    public static double calculateVariance(ArrayList<Double> dataset) {
        double mean = calculateMean(dataset);
        double sumOfSquaredDifferences = 0.0;

        for (double value : dataset) {
            double difference = value - mean;
            double squaredDifference = difference * difference;
            sumOfSquaredDifferences += squaredDifference;
        }

        double variance = sumOfSquaredDifferences / dataset.size();
        return variance;
    }

    public static double calculateStandardDeviation(ArrayList<Double> dataset) {
        double variance = calculateVariance(dataset);

        // Calculate the square root of the variance
        double standardDeviation = customSqrt(variance);

        return standardDeviation;
    }

    public static double customSqrt(double value) {
        if (value == 0 || value == 1) {
            return value;
        }

        double start = 0;
        double end = value;
        double precision = 0.000001; // Tolerance for convergence

        // Binary search to approximate the square root
        while (end - start > precision) {
            double mid = (start + end) / 2;
            double square = mid * mid;

            if (square == value) {
                return mid;
            } else if (square < value) {
                start = mid;
            } else {
                end = mid;
            }
        }

        return (start + end) / 2; // Approximated square root
    }

    public static double calculateStandardError(ArrayList<Double> dataset) {
        double standardDeviation = calculateStandardDeviation(dataset);
        int sampleSize = dataset.size();

        // Calculate the square root of the sample size
        double sqrtSampleSize = customSqrt(sampleSize);

        // Calculate the standard error using division
        double standardError = standardDeviation / sqrtSampleSize;

        return standardError;
    }

    public static double calculateMean(List<Double> dataset) {
        double sum = 0.0;
        for (double value : dataset) {
            sum += value;
        }
        return sum / dataset.size();
    }

    public static List<Double> findOutliers(ArrayList<Double> dataset) {
        List<Double> outliers = new ArrayList<>();
        double mean = calculateMean(dataset);
        double standardDeviation = calculateStandardDeviation(dataset);
        double threshold = 2.0; // Threshold for outliers (can be adjusted)
        double deviationThreshold = threshold * standardDeviation;

        for (double value : dataset) {
            double deviation = customAbs(value - mean);
            if (deviation > deviationThreshold && !outliers.contains(value)) {
                outliers.add(value);
            }
        }

        return outliers;
    }

    public static double customAbs(double value) {
        return value < 0 ? -value : value;
    }

}
