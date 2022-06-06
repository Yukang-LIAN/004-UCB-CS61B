package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.ArrayList;

public class Percolation {

    private final int N;
    private int openNum = 0;
    private int topSite;
    private int bottomSite;
    private boolean openFlag[][];
    private WeightedQuickUnionUF model;
    private WeightedQuickUnionUF modelWithoutBottom;

    /**
     * * create N-by-N grid, with all sites initially blocked
     */
    public Percolation(int N) {
        if (N < 0) {
            throw new IllegalArgumentException();
        }

        this.N = N;
        this.topSite = N * N;
        this.bottomSite = N * N + 1;
        openFlag = new boolean[N][N];
        model = new WeightedQuickUnionUF(N * N + 2);
        modelWithoutBottom = new WeightedQuickUnionUF(N * N + 1);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                openFlag[i][j] = false;
            }
        }


        for (int i = 0; i < N; i++) {
            model.union(topSite, xyTo1D(0, i));
        }
        for (int i = 0; i < N; i++) {
            modelWithoutBottom.union(topSite, xyTo1D(0, i));
        }

        for (int i = 0; i < N; i++) {
            model.union(bottomSite, xyTo1D(N - 1, i));
        }
    }

    /**
     * * open the site (row, col) if it is not open already
     */
    public void open(int row, int col) {
        validateRange(row, col);
        if (isOpen(row, col)) {
            return;
        }
        openFlag[row][col] = true;
        openNum++;
        connectWithNeighbor(row, col);
    }

    /**
     * * is the site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        validateRange(row, col);
        return openFlag[row][col];
    }

    /**
     * * is the site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        validateRange(row, col);
        if (!isOpen(row, col)) {
            return false;
        }
        return modelWithoutBottom.connected(xyTo1D(row, col), topSite);
    }

    /**
     * * number of open sites
     */
    public int numberOfOpenSites() {
        return openNum;
    }

    /**
     * * does the system percolate?
     */
    public boolean percolates() {
        if (numberOfOpenSites() == 0) {
            return false;
        }
        return model.connected(topSite, bottomSite);
    }

    /**
     * * return x y to an array
     */
    private int xyTo1D(int r, int c) {
        return r * this.N + c;
    }

    /**
     * * connect with four neighbors if they are not open
     */
    private void connectWithNeighbor(int row, int col) {
        connectBlock(row, col, row - 1, col);
        connectBlock(row, col, row + 1, col);
        connectBlock(row, col, row, col + 1);
        connectBlock(row, col, row, col - 1);
    }

    /**
     * * connect with block if it's not open
     */
    private void connectBlock(int ax, int ay, int bx, int by) {
        if (bx < 0 || bx >= N || by < 0 || by >= N) {
            return;
        }
        if (isOpen(bx, by)) {
            model.union(xyTo1D(ax, ay), xyTo1D(bx, by));
            modelWithoutBottom.union(xyTo1D(ax, ay), xyTo1D(bx, by));
        }
    }

    /**
     * * check if input is validate
     */
    private void validateRange(int row, int col) {
        if (row < 0 || row >= N || col < 0 || col >= N) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * * use for unit testing (not required)
     */
    public static void main(String[] args) {

    }

}
