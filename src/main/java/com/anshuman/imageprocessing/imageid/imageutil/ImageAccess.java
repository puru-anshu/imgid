package com.anshuman.imageprocessing.imageid.imageutil;

/**
 * User: Anshuman
 * Date: Oct 1, 2010
 * Time: 2:53:55 PM
 */

import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.process.ByteProcessor;
import ij.process.ColorProcessor;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

import java.awt.*;

public class ImageAccess {
    public static final int PATTERN_SQUARE_3x3 = 0;
    public static final int PATTERN_CROSS_3x3 = 1;
    private double[] pixels = null;
    private int nx = 0;
    private int ny = 0;
    private int size = 0;

    public ImageAccess(double[][] array) {
        if (array == null) {
            throw new ArrayStoreException("Constructor: array == null.");
        }
        this.ny = array[0].length;
        this.nx = array.length;
        this.size = (this.nx * this.ny);
        this.pixels = new double[this.size];
        int k = 0;
        for (int j = 0; j < this.ny; ++j)
            for (int i = 0; i < this.nx; ++i)
                this.pixels[(k++)] = array[i][j];
    }

    public ImageAccess(ImageProcessor ip) {
        if (ip == null) {
            throw new ArrayStoreException("Constructor: ImageProcessor == null.");
        }
        this.nx = ip.getWidth();
        this.ny = ip.getHeight();
        this.size = (this.nx * this.ny);
        this.pixels = new double[this.size];
        int k;
        if (ip.getPixels() instanceof byte[]) {
            byte[] bsrc = (byte[]) (byte[]) ip.getPixels();
            for (k = 0; k < this.size; ++k) {
                this.pixels[k] = (bsrc[k] & 0xFF);
            }
        } else if (ip.getPixels() instanceof short[]) {
            short[] ssrc = (short[]) (short[]) ip.getPixels();
            for (k = 0; k < this.size; ++k)
                this.pixels[k] = (ssrc[k] & 0xFFFF);
        } else if (ip.getPixels() instanceof float[]) {
            float[] fsrc = (float[]) (float[]) ip.getPixels();
            for (k = 0; k < this.size; ++k)
                this.pixels[k] = fsrc[k];
        } else {
            throw new ArrayStoreException("Constructor: Unexpected image type.");
        }
    }

    public ImageAccess(ColorProcessor cp, int colorPlane) {
        if (cp == null) {
            throw new ArrayStoreException("Constructor: ColorProcessor == null.");
        }
        if (colorPlane < 0) {
            throw new ArrayStoreException("Constructor: colorPlane < 0.");
        }
        if (colorPlane > 2) {
            throw new ArrayStoreException("Constructor: colorPlane > 2.");
        }
        this.nx = cp.getWidth();
        this.ny = cp.getHeight();
        this.size = (this.nx * this.ny);
        this.pixels = new double[this.size];
        byte[] r = new byte[this.size];
        byte[] g = new byte[this.size];
        byte[] b = new byte[this.size];
        cp.getRGB(r, g, b);
        if (colorPlane == 0)
            for (int k = 0; k < this.size; ++k)
                this.pixels[k] = (r[k] & 0xFF);
        else if (colorPlane == 1)
            for (int k = 0; k < this.size; ++k)
                this.pixels[k] = (g[k] & 0xFF);
        else if (colorPlane == 2)
            for (int k = 0; k < this.size; ++k)
                this.pixels[k] = (b[k] & 0xFF);
    }

    public ImageAccess(int nx, int ny) {
        if (nx < 1) {
            throw new ArrayStoreException("Constructor: nx < 1.");
        }
        if (ny < 1) {
            throw new ArrayStoreException("Constructor: ny < 1.");
        }
        this.nx = nx;
        this.ny = ny;
        this.size = (nx * ny);
        this.pixels = new double[this.size];
    }

    public int getWidth() {
        return this.nx;
    }

    public int getHeight() {
        return this.ny;
    }

    public double getMaximum() {
        double maxi = this.pixels[0];
        for (int i = 1; i < this.size; ++i)
            if (this.pixels[i] > maxi)
                maxi = this.pixels[i];
        return maxi;
    }

    public double getMinimum() {
        double mini = this.pixels[0];
        for (int i = 1; i < this.size; ++i)
            if (this.pixels[i] < mini)
                mini = this.pixels[i];
        return mini;
    }

    public double getMean() {
        double mean = 0.0D;
        for (int i = 0; i < this.size; ++i)
            mean += this.pixels[i];
        mean /= this.size;
        return mean;
    }

    public double[][] getArrayPixels() {
        double[][] array = new double[this.nx][this.ny];
        int k = 0;
        for (int j = 0; j < this.ny; ++j)
            for (int i = 0; i < this.nx; ++i)
                array[i][j] = this.pixels[(k++)];
        return array;
    }

    public double[] getPixels() {
        return this.pixels;
    }

    public FloatProcessor createFloatProcessor() {
        FloatProcessor fp = new FloatProcessor(this.nx, this.ny);
        float[] fsrc = new float[this.size];
        for (int k = 0; k < this.size; ++k)
            fsrc[k] = (float) this.pixels[k];
        fp.setPixels(fsrc);
        return fp;
    }

    public ByteProcessor createByteProcessor() {
        ByteProcessor bp = new ByteProcessor(this.nx, this.ny);
        byte[] bsrc = new byte[this.size];

        for (int k = 0; k < this.size; ++k) {
            double p = this.pixels[k];
            if (p < 0.0D)
                p = 0.0D;
            if (p > 255.0D)
                p = 255.0D;
            bsrc[k] = (byte) (int) p;
        }
        bp.setPixels(bsrc);
        return bp;
    }

    public ImageAccess duplicate() {
        ImageAccess ia = new ImageAccess(this.nx, this.ny);
        for (int i = 0; i < this.size; ++i)
            ia.pixels[i] = this.pixels[i];
        return ia;
    }

    public double getPixel(int x, int y) {
        int periodx = 2 * this.nx - 2;
        int periody = 2 * this.ny - 2;
        if (x < 0) {
            for (; x < 0; x += periodx) ;
            if (x >= this.nx) x = periodx - x;
        } else if (x >= this.nx) {
            for (; x >= this.nx; x -= periodx) ;
            if (x < 0) x = -x;
        }

        if (y < 0) {
            for (; y < 0; y += periody) ;
            if (y >= this.ny) y = periody - y;
        } else if (y >= this.ny) {
            for (; y >= this.ny; y -= periody) ;
            if (y < 0) y = -y;
        }
        return this.pixels[(x + y * this.nx)];
    }

    public double getInterpolatedPixel(double x, double y) {
        int periodx;
        int periody;
        int i;
        int j;
        int di;
        if (Double.isNaN(x))
            return 0.0D;
        if (Double.isNaN(y)) {
            return 0.0D;
        }
        if (x < 0.0D) {
            periodx = 2 * this.nx - 2;
            for (; x < 0.0D; x += periodx) ;
            if (x >= this.nx) x = periodx - x;
        } else if (x >= this.nx) {
            periodx = 2 * this.nx - 2;
            for (; x >= this.nx; x -= periodx) ;
            if (x < 0.0D) x = -x;
        }

        if (y < 0.0D) {
            periody = 2 * this.ny - 2;
            for (; y < 0.0D; y += periody) ;
            if (y >= this.ny) y = periody - y;
        } else if (y >= this.ny) {
            periody = 2 * this.ny - 2;
            for (; y >= this.ny; y -= periody) ;
            if (y < 0.0D) y = -y;
        }

        if (x >= 0.0D) {
            i = (int) x;
        } else {
            int iAdd = (int) x - 1;
            i = (int) (x - iAdd) + iAdd;
        }

        if (y >= 0.0D) {
            j = (int) y;
        } else {
            int iAdd = (int) y - 1;
            j = (int) (y - iAdd) + iAdd;
        }

        double dx = x - i;
        double dy = y - j;

        if (i >= this.nx - 1)
            di = -1;
        else
            di = 1;
        int index = i + j * this.nx;
        double v00 = this.pixels[index];
        double v10 = this.pixels[(index + di)];
        if (j >= this.ny - 1)
            index -= this.nx;
        else
            index += this.nx;
        double v01 = this.pixels[index];
        double v11 = this.pixels[(index + di)];
        return (dx * (v11 * dy - (v10 * (dy - 1.0D))) - ((dx - 1.0D) * (v01 * dy - (v00 * (dy - 1.0D)))));
    }

    public void getColumn(int x, double[] column) {
        if (x < 0)
            throw new IndexOutOfBoundsException("getColumn: x < 0.");
        if (x >= this.nx)
            throw new IndexOutOfBoundsException("getColumn: x >= nx.");
        if (column == null) {
            throw new ArrayStoreException("getColumn: column == null.");
        }
        if (column.length != this.ny) {
            throw new ArrayStoreException("getColumn: column.length != ny.");
        }
        for (int i = 0; i < this.ny; ++i) {
            column[i] = this.pixels[x];
            x += this.nx;
        }
    }

    public void getColumn(int x, int y, double[] column) {
        if (x < 0)
            throw new IndexOutOfBoundsException("getColumn: x < 0.");
        if (x >= this.nx)
            throw new IndexOutOfBoundsException("getColumn: x >= nx.");
        if (column == null)
            throw new ArrayStoreException("getColumn: column == null.");
        int by = column.length;
        if ((y >= 0) &&
                (y < this.ny - by - 1)) {
            int index = y * this.nx + x;
            for (int i = 0; i < by; ++i) {
                column[i] = this.pixels[index];
                index += this.nx;
            }
            return;
        }

        int[] yt = new int[by];
        for (int k = 0; k < by; ++k) {
            int ya = y + k;
            int periody = 2 * this.ny - 2;
            for (; ya < 0; ya += periody) ;
            while (ya >= this.ny) {
                ya = periody - ya;
                if (ya >= 0) continue;
                ya = -ya;
            }
            yt[k] = ya;
        }
        int index = 0;
        for (int i = 0; i < by; ++i) {
            index = yt[i] * this.nx + x;
            column[i] = this.pixels[index];
        }
    }

    public void getRow(int y, double[] row) {
        if (y < 0)
            throw new IndexOutOfBoundsException("getRow: y < 0.");
        if (y >= this.ny)
            throw new IndexOutOfBoundsException("getRow: y >= ny.");
        if (row == null) {
            throw new ArrayStoreException("getColumn: row == null.");
        }
        if (row.length != this.nx) {
            throw new ArrayStoreException("getColumn: row.length != nx.");
        }
        y *= this.nx;
        for (int i = 0; i < this.nx; ++i)
            row[i] = this.pixels[(y++)];
    }

    public void getRow(int x, int y, double[] row) {
        if (y < 0)
            throw new IndexOutOfBoundsException("getRow: y < 0.");
        if (y >= this.ny)
            throw new IndexOutOfBoundsException("getRow: y >= ny.");
        if (row == null)
            throw new ArrayStoreException("getRow: row == null.");
        int bx = row.length;
        if ((x >= 0) &&
                (x < this.nx - bx - 1)) {
            int index = y * this.nx + x;
            for (int i = 0; i < bx; ++i) {
                row[i] = this.pixels[(index++)];
            }
            return;
        }
        int periodx = 2 * this.nx - 2;
        int[] xt = new int[bx];
        for (int k = 0; k < bx; ++k) {
            int xa = x + k;
            for (; xa < 0; xa += periodx) ;
            while (xa >= this.nx) {
                xa = periodx - xa;
                if (xa >= 0) continue;
                xa = -xa;
            }
            xt[k] = xa;
        }
        int somme = 0;
        int index = y * this.nx;
        for (int i = 0; i < bx; ++i) {
            somme = index + xt[i];
            row[i] = this.pixels[somme];
        }
    }

    public void getNeighborhood(int x, int y, double[][] neigh) {
        int bx = neigh.length;
        int by = neigh[0].length;
        int bx2 = (bx - 1) / 2;
        int by2 = (by - 1) / 2;
        if ((x >= bx2) &&
                (y >= by2) &&
                (x < this.nx - bx2 - 1) &&
                (y < this.ny - by2 - 1)) {
            int index = (y - by2) * this.nx + x - bx2;
            for (int j = 0; j < by; ++j) {
                for (int i = 0; i < bx; ++i) {
                    neigh[i][j] = this.pixels[(index++)];
                }
                index += this.nx - bx;
            }
            return;
        }
        int[] xt = new int[bx];
        for (int k = 0; k < bx; ++k) {
            int xa = x + k - bx2;
            int periodx = 2 * this.nx - 2;
            while (xa < 0)
                xa += periodx;
            while (xa >= this.nx) {
                xa = periodx - xa;
                if (xa >= 0) continue;
                xa = -xa;
            }
            xt[k] = xa;
        }
        int[] yt = new int[by];
        for (int k = 0; k < by; ++k) {
            int ya = y + k - by2;
            int periody = 2 * this.ny - 2;
            for (; ya < 0; ya += periody) ;
            while (ya >= this.ny) {
                ya = periody - ya;
                if (ya >= 0) continue;
                ya = -ya;
            }
            yt[k] = ya;
        }
        int sum = 0;
        for (int j = 0; j < by; ++j) {
            int index = yt[j] * this.nx;
            for (int i = 0; i < bx; ++i) {
                sum = index + xt[i];
                neigh[i][j] = this.pixels[sum];
            }
        }
    }

    public void getPattern(int x, int y, double[] neigh, int pattern) {
        if (neigh == null)
            throw new ArrayStoreException("getPattern: neigh == null.");
        switch (pattern) {
            case 0:
                if (neigh.length != 9) {
                    throw new ArrayStoreException("getPattern: neigh.length != 9.");
                }
                getPatternSquare3x3(x, y, neigh);
                break;
            case 1:
                if (neigh.length != 5) {
                    throw new ArrayStoreException("getPattern: neigh.length != 5");
                }
                getPatternCross3x3(x, y, neigh);
                break;
            default:
                throw new ArrayStoreException("getPattern: unexpected pattern.");
        }
    }

    private void getPatternSquare3x3(int x, int y, double[] neigh) {
        if ((x >= 1) &&
                (y >= 1) &&
                (x < this.nx - 1) &&
                (y < this.ny - 1)) {
            int index = (y - 1) * this.nx + x - 1;
            neigh[0] = this.pixels[(index++)];
            neigh[1] = this.pixels[(index++)];
            neigh[2] = this.pixels[index];
            index += this.nx - 2;
            neigh[3] = this.pixels[(index++)];
            neigh[4] = this.pixels[(index++)];
            neigh[5] = this.pixels[index];
            index += this.nx - 2;
            neigh[6] = this.pixels[(index++)];
            neigh[7] = this.pixels[(index++)];
            neigh[8] = this.pixels[index];
            return;
        }
        int x1 = x - 1;
        int x2 = x;
        int x3 = x + 1;
        int y1 = y - 1;
        int y2 = y;
        int y3 = y + 1;
        if (x == 0)
            x1 = x3;
        if (y == 0)
            y1 = y3;
        if (x == this.nx - 1)
            x3 = x1;
        if (y == this.ny - 1)
            y3 = y1;
        int offset = y1 * this.nx;
        neigh[0] = this.pixels[(offset + x1)];
        neigh[1] = this.pixels[(offset + x2)];
        neigh[2] = this.pixels[(offset + x3)];
        offset = y2 * this.nx;
        neigh[3] = this.pixels[(offset + x1)];
        neigh[4] = this.pixels[(offset + x2)];
        neigh[5] = this.pixels[(offset + x3)];
        offset = y3 * this.nx;
        neigh[6] = this.pixels[(offset + x1)];
        neigh[7] = this.pixels[(offset + x2)];
        neigh[8] = this.pixels[(offset + x3)];
    }

    private void getPatternCross3x3(int x, int y, double[] neigh) {
        if ((x >= 1) &&
                (y >= 1) &&
                (x < this.nx - 1) &&
                (y < this.ny - 1)) {
            int index = (y - 1) * this.nx + x;
            neigh[0] = this.pixels[index];
            index += this.nx - 1;
            neigh[1] = this.pixels[(index++)];
            neigh[2] = this.pixels[(index++)];
            neigh[3] = this.pixels[index];
            index += this.nx - 1;
            neigh[4] = this.pixels[index];
            return;
        }
        int x1 = x - 1;
        int x2 = x;
        int x3 = x + 1;
        int y1 = y - 1;
        int y2 = y;
        int y3 = y + 1;
        if (x == 0)
            x1 = x3;
        if (y == 0)
            y1 = y3;
        if (x == this.nx - 1)
            x3 = x1;
        if (y == this.ny - 1)
            y3 = y1;
        int offset = y1 * this.nx;
        neigh[0] = this.pixels[(offset + x2)];
        offset = y2 * this.nx;
        neigh[1] = this.pixels[(offset + x1)];
        neigh[2] = this.pixels[(offset + x2)];
        neigh[3] = this.pixels[(offset + x3)];
        offset = y3 * this.nx;
        neigh[4] = this.pixels[(offset + x2)];
    }

    public void getSubImage(int x, int y, ImageAccess output) {
        if (output == null)
            throw new ArrayStoreException("getSubImage: output == null.");
        if (x < 0)
            throw new ArrayStoreException("getSubImage: Incompatible image size");
        if (y < 0)
            throw new ArrayStoreException("getSubImage: Incompatible image size");
        if (x >= this.nx)
            throw new ArrayStoreException("getSubImage: Incompatible image size");
        if (y >= this.ny)
            throw new ArrayStoreException("getSubImage: Incompatible image size");
        int nxcopy = output.getWidth();
        int nycopy = output.getHeight();
        double[][] neigh = new double[nxcopy][nycopy];
        int nx2 = (nxcopy - 1) / 2;
        int ny2 = (nycopy - 1) / 2;
        getNeighborhood(x + nx2, y + ny2, neigh);
        output.putArrayPixels(neigh);
    }

    public void putPixel(int x, int y, double value) {
        if (x < 0)
            return;
        if (x >= this.nx)
            return;
        if (y < 0)
            return;
        if (y >= this.ny)
            return;
        this.pixels[(x + y * this.nx)] = value;
    }

    public void putColumn(int x, double[] column) {
        if (x < 0)
            throw new IndexOutOfBoundsException("putColumn: x < 0.");
        if (x >= this.nx)
            throw new IndexOutOfBoundsException("putColumn: x >= nx.");
        if (column == null)
            throw new ArrayStoreException("putColumn: column == null.");
        if (column.length != this.ny)
            throw new ArrayStoreException("putColumn: column.length != ny.");
        for (int i = 0; i < this.ny; ++i) {
            this.pixels[x] = column[i];
            x += this.nx;
        }
    }

    public void putColumn(int x, int y, double[] column) {
        if (x < 0)
            throw new IndexOutOfBoundsException("putColumn: x < 0.");
        if (x >= this.nx)
            throw new IndexOutOfBoundsException("putColumn: x >= nx.");
        if (column == null)
            throw new ArrayStoreException("putColumn: column == null.");
        int by = column.length;
        int index = y * this.nx + x;
        int top = 0;
        int bottom = 0;
        if (y >= 0) {
            if (y < this.ny - by)
                bottom = by;
            else
                bottom = -y + this.ny;
            for (int i = top; i < bottom; ++i) {
                this.pixels[index] = column[i];
                index += this.nx;
            }
            return;
        }

        index = x;
        top = -y;
        if (y < this.ny - by)
            bottom = by;
        else
            bottom = -y + this.ny;
        for (int i = top; i < bottom; ++i) {
            this.pixels[index] = column[i];
            index += this.nx;
        }
    }

    public void putRow(int y, double[] row) {
        if (y < 0)
            throw new IndexOutOfBoundsException("putRow: y < 0.");
        if (y >= this.ny)
            throw new IndexOutOfBoundsException("putRow: y >= ny.");
        if (row == null)
            throw new ArrayStoreException("putRow: row == null.");
        if (row.length != this.nx)
            throw new ArrayStoreException("putRow: row.length != nx.");
        y *= this.nx;
        for (int i = 0; i < this.nx; ++i)
            this.pixels[(y++)] = row[i];
    }

    public void putRow(int x, int y, double[] row) {
        if (y < 0)
            throw new IndexOutOfBoundsException("putRow: y < 0.");
        if (y >= this.ny)
            throw new IndexOutOfBoundsException("putRow: y >= ny.");
        if (row == null)
            throw new ArrayStoreException("putRow: row == null.");
        int bx = row.length;
        int index = y * this.nx + x;
        int left = 0;
        int right = 0;
        if (x >= 0) {
            if (x < this.nx - bx)
                right = bx;
            else {
                right = -x + this.nx;
            }
            for (int i = left; i < right; ++i) {
                this.pixels[(index++)] = row[i];
            }
            return;
        }

        index = y * this.nx;
        left = -x;

        if (x < this.nx - bx)
            right = bx;
        else {
            right = -x + this.nx;
        }
        for (int i = left; i < right; ++i)
            this.pixels[(index++)] = row[i];
    }

    public void putArrayPixels(double[][] array) {
        if (array == null)
            throw new IndexOutOfBoundsException("putArrayPixels: array == null.");
        int bx = array.length;
        int by = array[0].length;
        if (bx * by != this.size)
            throw new IndexOutOfBoundsException("putArrayPixels: imcompatible size.");
        int k = 0;
        for (int j = 0; j < by; ++j)
            for (int i = 0; i < bx; ++i)
                this.pixels[(k++)] = array[i][j];
    }

    public void putSubImage(int x, int y, ImageAccess input) {
        if (input == null)
            throw new ArrayStoreException("putSubImage: input == null.");
        if (x < 0)
            throw new IndexOutOfBoundsException("putSubImage: x < 0.");
        if (y < 0)
            throw new IndexOutOfBoundsException("putSubImage: y < 0.");
        if (x >= this.nx)
            throw new IndexOutOfBoundsException("putSubImage: x >= nx.");
        if (y >= this.ny)
            throw new IndexOutOfBoundsException("putSubImage: y >= ny.");
        int nxcopy = input.getWidth();
        int nycopy = input.getHeight();

        if (x + nxcopy > this.nx)
            nxcopy = this.nx - x;
        if (y + nycopy > this.ny) {
            nycopy = this.ny - y;
        }
        double[] dsrc = input.getPixels();
        for (int j = 0; j < nycopy; ++j)
            System.arraycopy(dsrc, j * nxcopy, this.pixels, (j + y) * this.nx + x, nxcopy);
    }

    public void setConstant(double constant) {
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] = constant;
    }

    public void normalizeContrast() {
        double a;
        double minGoal = 0.0D;
        double maxGoal = 255.0D;

        double minImage = getMinimum();
        double maxImage = getMaximum();

        if (minImage - maxImage == 0.0D) {
            a = 1.0D;
            minImage = (maxGoal - minGoal) / 2.0D;
        } else {
            a = (maxGoal - minGoal) / (maxImage - minImage);
        }
        for (int i = 0; i < this.size; ++i)
            this.pixels[i] = (float) (a * (this.pixels[i] - minImage) + minGoal);
    }

    public void show(String title, Point loc) {
        FloatProcessor fp = createFloatProcessor();
        fp.resetMinAndMax();
        ImagePlus impResult = new ImagePlus(title, fp);
        impResult.show();
        ImageWindow window = impResult.getWindow();
        window.setLocation(loc.x, loc.y);
        impResult.show();
    }

    public void show(String title) {
        FloatProcessor fp = createFloatProcessor();
        fp.resetMinAndMax();
        ImagePlus impResult = new ImagePlus(title, fp);
        impResult.show();
    }

    public void abs() {
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] = Math.abs(this.pixels[k]);
    }

    public void sqrt() {
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] = Math.sqrt(this.pixels[k]);
    }

    public void pow(double a) {
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] = Math.pow(this.pixels[k], a);
    }

    public void add(double constant) {
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] += constant;
    }

    public void multiply(double constant) {
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] *= constant;
    }

    public void subtract(double constant) {
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] -= constant;
    }

    public void divide(double constant) {
        if (constant == 0.0D)
            throw new ArrayStoreException("divide: Divide by 0");
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] /= constant;
    }

    public void add(ImageAccess im1, ImageAccess im2) {
        if (im1.getWidth() != this.nx)
            throw new ArrayStoreException("add: incompatible size.");
        if (im1.getHeight() != this.ny)
            throw new ArrayStoreException("add: incompatible size.");
        if (im2.getWidth() != this.nx)
            throw new ArrayStoreException("add: incompatible size.");
        if (im2.getHeight() != this.ny)
            throw new ArrayStoreException("add: incompatible size.");
        double[] doubleOperand1 = im1.getPixels();
        double[] doubleOperand2 = im2.getPixels();
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] = (doubleOperand1[k] + doubleOperand2[k]);
    }

    public void multiply(ImageAccess im1, ImageAccess im2) {
        if (im1.getWidth() != this.nx)
            throw new ArrayStoreException("multiply: incompatible size.");
        if (im1.getHeight() != this.ny)
            throw new ArrayStoreException("multiply: incompatible size.");
        if (im2.getWidth() != this.nx)
            throw new ArrayStoreException("multiply: incompatible size.");
        if (im2.getHeight() != this.ny)
            throw new ArrayStoreException("multiply: incompatible size.");
        double[] doubleOperand1 = im1.getPixels();
        double[] doubleOperand2 = im2.getPixels();
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] = (doubleOperand1[k] * doubleOperand2[k]);
    }

    public void subtract(ImageAccess im1, ImageAccess im2) {
        if (im1.getWidth() != this.nx)
            throw new ArrayStoreException("subtract: incompatible size.");
        if (im1.getHeight() != this.ny)
            throw new ArrayStoreException("subtract: incompatible size.");
        if (im2.getWidth() != this.nx)
            throw new ArrayStoreException("subtract: incompatible size.");
        if (im2.getHeight() != this.ny)
            throw new ArrayStoreException("subtract: incompatible size.");
        double[] doubleOperand1 = im1.getPixels();
        double[] doubleOperand2 = im2.getPixels();
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] = (doubleOperand1[k] - doubleOperand2[k]);
    }

    public void divide(ImageAccess im1, ImageAccess im2) {
        if (im1.getWidth() != this.nx)
            throw new ArrayStoreException("divide: incompatible size.");
        if (im1.getHeight() != this.ny)
            throw new ArrayStoreException("divide: incompatible size.");
        if (im2.getWidth() != this.nx)
            throw new ArrayStoreException("divide: incompatible size.");
        if (im2.getHeight() != this.ny)
            throw new ArrayStoreException("divide: incompatible size.");
        double[] doubleOperand1 = im1.getPixels();
        double[] doubleOperand2 = im2.getPixels();
        for (int k = 0; k < this.size; ++k)
            this.pixels[k] = (doubleOperand1[k] / doubleOperand2[k]);
    }
}
