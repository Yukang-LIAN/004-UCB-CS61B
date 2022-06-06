package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;

public class PercolationStats {
    private final int T;
    private double[] thresholdArr;

    /**
     * * perform T independent experiments on an N-by-N grid
     */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        this.T = T;
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        thresholdArr = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation p = pf.make(N);
            int row;
            int col;
            while (!p.percolates()) {
                row = StdRandom.uniform(N);
                col = StdRandom.uniform(N);
                if (!p.isOpen(row, col)) {
                    p.open(row, col);
                }
            }
            thresholdArr[i] = (double) p.numberOfOpenSites() / (N * N);
        }
    }

    /**
     * * sample mean of percolation threshold
     */
    public double mean() {
        return StdStats.mean(thresholdArr);
    }

    /**
     * * sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(thresholdArr);
    }

    /**
     * * low endpoint of 95% confidence interval
     */
    public double confidenceLow() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    /**
     * * high endpoint of 95% confidence interval
     */
    public double confidenceHigh() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

//    public static void main(String[] args) {
//        PercolationStats stats = new PercolationStats(20, 30, new PercolationFactory());
//        System.out.println(stats.mean());
//        System.out.println(stats.stddev());
//    }
}
