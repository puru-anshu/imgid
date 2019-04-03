package com.anshuman.imageprocessing.imageid.imageutil;

/**
 * User: Anshuman
 * Date: Oct 1, 2010
 * Time: 2:53:01 PM
 */
public class Resize {
    private int interpDegree;
    private int analyDegree;
    private int syntheDegree;
    private double zoomY;
    private double zoomX;
    private boolean inversable;
    private int analyEven;
    private int corrDegree;
    private double halfSupport;
    private double[] splineArrayHeight;
    private double[] splineArrayWidth;
    private int[] indexMinHeight;
    private int[] indexMaxHeight;
    private int[] indexMinWidth;
    private int[] indexMaxWidth;
    private final double tolerance = 1.E-009D;

    public Resize() {
        this.analyEven = 0;


    }

    public void computeZoom(ImageAccess input, ImageAccess output, int analyDegree, int syntheDegree, int interpDegree, double zoomY, double zoomX, double shiftY, double shiftX, boolean inversable) {
        double affineIndex;
        this.interpDegree = interpDegree;
        this.analyDegree = analyDegree;
        this.syntheDegree = syntheDegree;
        this.zoomY = zoomY;
        this.zoomX = zoomX;
        this.inversable = inversable;

        int nx = input.getWidth();
        int ny = input.getHeight();

        int[] size = new int[4];

        int totalDegree = interpDegree + analyDegree + 1;

        size = calculatefinalsize(inversable, ny, nx, zoomY, zoomX);

        int workingSizeX = size[1];
        int workingSizeY = size[0];
        int finalSizeX = size[3];
        int finalSizeY = size[2];

        if ((analyDegree + 1) / 2 * 2 == analyDegree + 1) {
            this.analyEven = 1;
        }
        double cociente = (analyDegree + 1) / 2.0D;
        double go = analyDegree + 1;
        this.corrDegree = (analyDegree + syntheDegree + 1);
        this.halfSupport = ((totalDegree + 1.0D) / 2.0D);

        int addBorderHeight = border(finalSizeY, this.corrDegree);
        if (addBorderHeight < totalDegree) {
            addBorderHeight += totalDegree;
        }

        int finalTotalHeight = finalSizeY + addBorderHeight;
        int lengthTotalHeight = workingSizeY + (int) Math.ceil(addBorderHeight / zoomY);

        this.indexMinHeight = new int[finalTotalHeight];
        this.indexMaxHeight = new int[finalTotalHeight];

        int lengthArraySplnHeight = finalTotalHeight * (2 + totalDegree);
        int i = 0;

        double factHeight = Math.pow(zoomY, analyDegree + 1);

        shiftY += ((analyDegree + 1.0D) / 2.0D - Math.floor((analyDegree + 1.0D) / 2.0D)) * (1.0D / zoomY - 1.0D);
        this.splineArrayHeight = new double[lengthArraySplnHeight];

        for (int l = 0; l < finalTotalHeight; ++l) {
            affineIndex = l / zoomY + shiftY;
            this.indexMinHeight[l] = (int) Math.ceil(affineIndex - this.halfSupport);
            this.indexMaxHeight[l] = (int) Math.floor(affineIndex + this.halfSupport);
            for (int k = this.indexMinHeight[l]; k <= this.indexMaxHeight[l]; ++k) {
                this.splineArrayHeight[i] = (factHeight * beta(affineIndex - k, totalDegree));
                ++i;
            }
        }

        int addBorderWidth = border(finalSizeX, this.corrDegree);
        if (addBorderWidth < totalDegree) {
            addBorderWidth += totalDegree;
        }

        int finalTotalWidth = finalSizeX + addBorderWidth;
        int lengthTotalWidth = workingSizeX + (int) Math.ceil(addBorderWidth / zoomX);

        this.indexMinWidth = new int[finalTotalWidth];
        this.indexMaxWidth = new int[finalTotalWidth];

        int lengthArraySplnWidth = finalTotalWidth * (2 + totalDegree);
        i = 0;
        double factWidth = Math.pow(zoomX, analyDegree + 1);

        shiftX += ((analyDegree + 1.0D) / 2.0D - Math.floor((analyDegree + 1.0D) / 2.0D)) * (1.0D / zoomX - 1.0D);
        this.splineArrayWidth = new double[lengthArraySplnWidth];

        for (int l = 0; l < finalTotalWidth; ++l) {
            affineIndex = l / zoomX + shiftX;
            this.indexMinWidth[l] = (int) Math.ceil(affineIndex - this.halfSupport);
            this.indexMaxWidth[l] = (int) Math.floor(affineIndex + this.halfSupport);
            for (int k = this.indexMinWidth[l]; k <= this.indexMaxWidth[l]; ++k) {
                this.splineArrayWidth[i] = (factWidth * beta(affineIndex - k, totalDegree));
                ++i;
            }
        }
        double[] outputColumn = new double[finalSizeY];
        double[] outputRow = new double[finalSizeX];
        double[] workingRow = new double[workingSizeX];
        double[] workingColumn = new double[workingSizeY];

        double[] addVectorHeight = new double[lengthTotalHeight];
        double[] addOutputVectorHeight = new double[finalTotalHeight];
        double[] addVectorWidth = new double[lengthTotalWidth];
        double[] addOutputVectorWidth = new double[finalTotalWidth];

        int periodColumnSym = 2 * workingSizeY - 2;
        int periodRowSym = 2 * workingSizeX - 2;
        int periodColumnAsym = 2 * workingSizeY - 3;
        int periodRowAsym = 2 * workingSizeX - 3;

        ImageAccess image = new ImageAccess(finalSizeX, workingSizeY);

        if (inversable == true) {
            ImageAccess inverImage = new ImageAccess(workingSizeX, workingSizeY);

            for (int x = 0; x < nx; ++x) {
                for (int y = 0; y < ny; ++y) {
                    inverImage.putPixel(x, y, input.getPixel(x, y));
                }
            }

            if (workingSizeX > nx) {
                inverImage.getColumn(nx - 1, workingColumn);
                for (int y = nx; y < workingSizeX; ++y) {
                    inverImage.putColumn(y, workingColumn);
                }
            }

            if (workingSizeY > ny) {
                inverImage.getRow(ny - 1, workingRow);
                for (int y = ny; y < workingSizeY; ++y) {
                    inverImage.putRow(y, workingRow);
                }

            }

            for (int y = 0; y < workingSizeY; ++y) {
                inverImage.getRow(y, workingRow);
                getInterpolationCoefficients(workingRow, interpDegree);
                resamplingRow(workingRow, outputRow, addVectorWidth, addOutputVectorWidth, periodRowSym, periodRowAsym);

                image.putRow(y, outputRow);
            }

            for (int y = 0; y < finalSizeX; ++y) {
                image.getColumn(y, workingColumn);
                getInterpolationCoefficients(workingColumn, interpDegree);
                resamplingColumn(workingColumn, outputColumn, addVectorHeight, addOutputVectorHeight, periodColumnSym, periodColumnAsym);

                output.putColumn(y, outputColumn);
            }

        } else {
            for (int y = 0; y < workingSizeY; ++y) {
                input.getRow(y, workingRow);
                getInterpolationCoefficients(workingRow, interpDegree);
                resamplingRow(workingRow, outputRow, addVectorWidth, addOutputVectorWidth, periodRowSym, periodRowAsym);

                image.putRow(y, outputRow);
            }

            for (int y = 0; y < finalSizeX; ++y) {
                image.getColumn(y, workingColumn);
                getInterpolationCoefficients(workingColumn, interpDegree);
                resamplingColumn(workingColumn, outputColumn, addVectorHeight, addOutputVectorHeight, periodColumnSym, periodColumnAsym);

                output.putColumn(y, outputColumn);
            }
        }
    }

    private void resamplingRow(double[] inputVector, double[] outputVector, double[] addVector, double[] addOutputVector, int maxSymBoundary, int maxAsymBoundary) {
        int lengthInput = inputVector.length;
        int lengthOutput = outputVector.length;
        int lengthtotal = addVector.length;
        int lengthOutputtotal = addOutputVector.length;

        double average = 0.0D;

        if (this.analyDegree != -1) {
            average = doInteg(inputVector, this.analyDegree + 1);
        }

        System.arraycopy(inputVector, 0, addVector, 0, lengthInput);

        for (int l = lengthInput; l < lengthtotal; ++l) {
            int l2;
            if (this.analyEven == 1) {
                l2 = l;
                if (l >= maxSymBoundary)
                    l2 = (int) Math.abs(Math.IEEEremainder(l, maxSymBoundary));
                if (l2 >= lengthInput)
                    l2 = maxSymBoundary - l2;
                addVector[l] = inputVector[l2];
            } else {
                l2 = l;
                if (l >= maxAsymBoundary)
                    l2 = (int) Math.abs(Math.IEEEremainder(l, maxAsymBoundary));
                if (l2 >= lengthInput)
                    l2 = maxAsymBoundary - l2;
                addVector[l] = (-inputVector[l2]);
            }
        }

        int i = 0;

        for (int l = 0; l < lengthOutputtotal; ++l) {
            addOutputVector[l] = 0.0D;
            for (int k = this.indexMinWidth[l]; k <= this.indexMaxWidth[l]; ++k) {
                int index = k;
                double sign = 1.0D;
                if (k < 0) {
                    index = -k;
                    if (this.analyEven == 0) {
                        --index;
                        sign = -1.0D;
                    }
                }
                if (k >= lengthtotal) {
                    index = lengthtotal - 1;
                }
                addOutputVector[l] += sign * addVector[index] * this.splineArrayWidth[i];
                ++i;
            }

        }

        if (this.analyDegree != -1) {
            doDiff(addOutputVector, this.analyDegree + 1);
            for (i = 0; i < lengthOutputtotal; ++i) {
                addOutputVector[i] += average;
            }
            getInterpolationCoefficients(addOutputVector, this.corrDegree);

            getSamples(addOutputVector, this.syntheDegree);
        }

        System.arraycopy(addOutputVector, 0, outputVector, 0, lengthOutput);
    }

    private void resamplingColumn(double[] inputVector, double[] outputVector, double[] addVector, double[] addOutputVector, int maxSymBoundary, int maxAsymBoundary) {
        int lengthInput = inputVector.length;
        int lengthOutput = outputVector.length;
        int lengthtotal = addVector.length;
        int lengthOutputtotal = addOutputVector.length;

        double average = 0.0D;

        if (this.analyDegree != -1) {
            average = doInteg(inputVector, this.analyDegree + 1);
        }

        System.arraycopy(inputVector, 0, addVector, 0, lengthInput);

        for (int l = lengthInput; l < lengthtotal; ++l) {
            int l2;
            if (this.analyEven == 1) {
                l2 = l;
                if (l >= maxSymBoundary)
                    l2 = (int) Math.abs(Math.IEEEremainder(l, maxSymBoundary));
                if (l2 >= lengthInput)
                    l2 = maxSymBoundary - l2;
                addVector[l] = inputVector[l2];
            } else {
                l2 = l;
                if (l >= maxAsymBoundary)
                    l2 = (int) Math.abs(Math.IEEEremainder(l, maxAsymBoundary));
                if (l2 >= lengthInput)
                    l2 = maxAsymBoundary - l2;
                addVector[l] = (-inputVector[l2]);
            }
        }

        int i = 0;

        for (int l = 0; l < lengthOutputtotal; ++l) {
            addOutputVector[l] = 0.0D;
            for (int k = this.indexMinHeight[l]; k <= this.indexMaxHeight[l]; ++k) {
                int index = k;
                double sign = 1.0D;
                if (k < 0) {
                    index = -k;
                    if (this.analyEven == 0) {
                        --index;
                        sign = -1.0D;
                    }
                }
                if (k >= lengthtotal) {
                    index = lengthtotal - 1;
                }
                addOutputVector[l] += sign * addVector[index] * this.splineArrayHeight[i];
                ++i;
            }
        }

        if (this.analyDegree != -1) {
            doDiff(addOutputVector, this.analyDegree + 1);
            for (i = 0; i < lengthOutputtotal; ++i) {
                addOutputVector[i] += average;
            }
            getInterpolationCoefficients(addOutputVector, this.corrDegree);

            getSamples(addOutputVector, this.syntheDegree);
        }

        System.arraycopy(addOutputVector, 0, outputVector, 0, lengthOutput);
    }

    private double beta(double x, int degree) {
        double a;
        double betan = 0.0D;

        switch (degree) {
            case 0:
                if (Math.abs(x) < 0.5D) {
                    betan = 1.0D;
                    break;
                }

                if (x != -0.5D) break;
                betan = 1.0D;
                break;
            case 1:
                x = Math.abs(x);
                if (x >= 1.0D) break;
                betan = 1.0D - x;
                break;
            case 2:
                x = Math.abs(x);
                if (x < 0.5D) {
                    betan = 0.75D - (x * x);
                    break;
                }

                if (x >= 1.5D) break;
                x -= 1.5D;
                betan = x * x * 0.5D;
                break;
            case 3:
                x = Math.abs(x);
                if (x < 1.0D) {
                    betan = x * x * (x - 2.0D) * 0.5D + 0.6666666666666666D;
                    break;
                }
                if (x >= 2.0D) break;
                x -= 2.0D;
                betan = x * x * x * -0.1666666666666667D;
                break;
            case 4:
                x = Math.abs(x);
                if (x < 0.5D) {
                    x *= x;
                    betan = x * (x * 0.25D - 0.625D) + 0.5989583333333334D;
                    break;
                }
                if (x < 1.5D) {
                    betan = x * (x * (x * (0.8333333333333334D - (x * 0.1666666666666667D)) - 1.25D) + 0.2083333333333333D) + 0.5729166666666666D;
                    break;
                }

                if (x >= 2.5D) break;
                x -= 2.5D;
                x *= x;
                betan = x * x * 0.04166666666666666D;
                break;
            case 5:
                x = Math.abs(x);
                if (x < 1.0D) {
                    a = x * x;
                    betan = a * (a * (0.25D - (x * 0.08333333333333333D)) - 0.5D) + 0.55D;
                    break;
                }

                if (x < 2.0D) {
                    betan = x * (x * (x * (x * (x * 0.04166666666666666D - 0.375D) + 1.25D) - 1.75D) + 0.625D) + 0.425D;
                    break;
                }

                if (x >= 3.0D) break;
                a = 3.0D - x;
                x = a * a;
                betan = a * x * x * 0.008333333333333333D;
                break;
            case 6:
                x = Math.abs(x);
                if (x < 0.5D) {
                    x *= x;
                    betan = x * (x * (0.1458333333333333D - (x * 0.02777777777777778D)) - 0.4010416666666667D) + 0.5110243055555556D;
                    break;
                }

                if (x < 1.5D) {
                    betan = x * (x * (x * (x * (x * (x * 0.02083333333333333D - 0.1458333333333333D) + 0.328125D) - 0.1215277777777778D) - 0.35546875D) - 0.009114583333333334D) + 0.5117838541666667D;
                    break;
                }

                if (x < 2.5D) {
                    betan = x * (x * (x * (x * (x * (0.1166666666666667D - (x * 0.008333333333333333D)) - 0.65625D) + 1.847222222222222D) - 2.5703125D) + 1.319791666666667D) + 0.1795572916666667D;
                    break;
                }

                if (x >= 3.5D) break;
                x -= 3.5D;
                x *= x * x;
                betan = x * x * 0.001388888888888889D;
                break;
            case 7:

                x = Math.abs(x);
                if (x < 1.0D) {
                    a = x * x;
                    betan = a * (a * (a * (x * 0.006944444444444444D - 0.02777777777777778D) + 0.111111111111111D) - 0.3333333333333333D) + 0.4793650793650794D;
                    break;
                }

                if (x < 2.0D) {
                    betan = x * (x * (x * (x * (x * (x * (0.05D - (x * 0.004166666666666667D)) - 0.2333333333333333D) + 0.5D) - 0.388888888888889D) - 0.1D) - 0.07777777777777778D) + 0.4904761904761905D;
                    break;
                }

                if (x < 3.0D) {
                    betan = x * (x * (x * (x * (x * (x * (x * 0.001388888888888889D - 0.02777777777777778D) + 0.2333333333333333D) - 1.055555555555556D) + 2.722222222222222D) - 3.833333333333334D) + 2.411111111111111D) - 0.2206349206349206D;
                    break;
                }

                if (x >= 4.0D) break;
                a = 4.0D - x;
                x = a * a * a;
                betan = x * x * a * 0.0001984126984126984D;
        }

        ;
        return betan;
    }

    private double doInteg(double[] c, int nb) {
        int f;
        int size = c.length;
        double m = 0.0D;
        double average = 0.0D;

        switch (nb) {
            case 1:
                for (f = 0; f < size; ++f)
                    average += c[f];
                average = (2.0D * average - c[(size - 1)] - c[0]) / (2 * size - 2);
                integSA(c, average);
                break;
            case 2:
                for (f = 0; f < size; ++f)
                    average += c[f];
                average = (2.0D * average - c[(size - 1)] - c[0]) / (2 * size - 2);
                integSA(c, average);
                integAS(c, c);
                break;
            case 3:
                for (f = 0; f < size; ++f)
                    average += c[f];
                average = (2.0D * average - c[(size - 1)] - c[0]) / (2 * size - 2);
                integSA(c, average);
                integAS(c, c);
                for (f = 0; f < size; ++f)
                    m += c[f];
                m = (2.0D * m - c[(size - 1)] - c[0]) / (2 * size - 2);
                integSA(c, m);
                break;
            case 4:
                for (f = 0; f < size; ++f)
                    average += c[f];
                average = (2.0D * average - c[(size - 1)] - c[0]) / (2 * size - 2);
                integSA(c, average);
                integAS(c, c);
                for (f = 0; f < size; ++f)
                    m += c[f];
                m = (2.0D * m - c[(size - 1)] - c[0]) / (2 * size - 2);
                integSA(c, m);
                integAS(c, c);
        }

        return average;
    }

    private void integSA(double[] c, double m) {
        int size = c.length;
        c[0] = ((c[0] - m) * 0.5D);
        for (int i = 1; i < size; ++i)
            c[i] = (c[i] - m + c[(i - 1)]);
    }

    private void integAS(double[] c, double[] y) {
        int size = c.length;
        double[] z = new double[size];
        System.arraycopy(c, 0, z, 0, size);
        y[0] = z[0];
        y[1] = 0.0D;
        for (int i = 2; i < size; ++i)
            y[i] = (y[(i - 1)] - z[(i - 1)]);
    }

    private void doDiff(double[] c, int nb) {
        int size = c.length;
        switch (nb) {
            case 1:
                diffAS(c);
                break;
            case 2:
                diffSA(c);
                diffAS(c);
                break;
            case 3:
                diffAS(c);
                diffSA(c);
                diffAS(c);
                break;
            case 4:
                diffSA(c);
                diffAS(c);
                diffSA(c);
                diffAS(c);
        }
    }

    private void diffSA(double[] c) {
        int size = c.length;
        double old = c[(size - 2)];
        for (int i = 0; i <= size - 2; ++i)
            c[i] -= c[(i + 1)];
        c[(size - 1)] -= old;
    }

    private void diffAS(double[] c) {
        int size = c.length;
        for (int i = size - 1; i > 0; --i)
            c[i] -= c[(i - 1)];
        c[0] = (2.0D * c[0]);
    }

    private int border(int size, int degree) {
        double z;
        int horizon = size;

        switch (degree) {
            case 0:
            case 1:
                return 0;
            case 2:
                z = Math.sqrt(8.0D) - 3.0D;
                break;
            case 3:
                z = Math.sqrt(3.0D) - 2.0D;
                break;
            case 4:
                z = Math.sqrt(664.0D - Math.sqrt(438976.0D)) + Math.sqrt(304.0D) - 19.0D;
                break;
            case 5:
                z = Math.sqrt(67.5D - Math.sqrt(4436.25D)) + Math.sqrt(26.25D) - 6.5D;

                break;
            case 6:
                z = -0.4882945893030448D;
                break;
            case 7:
                z = -0.5352804307964382D;
                break;
            default:
                throw new IllegalArgumentException("Invalid interpDegree degree (should be [0..7])");
        }

        horizon = 2 + (int) (Math.log(1.E-009D) / Math.log(Math.abs(z)));
        horizon = (horizon < size) ? horizon : size;
        return horizon;
    }

    public static int[] calculatefinalsize(boolean inversable, int height, int width, double zoomY, double zoomX) {
        int[] size = new int[4];

        size[0] = height;
        size[1] = width;

        if (inversable == true) {
            int w2 = (int) Math.round(Math.round((size[0] - 1) * zoomY) / zoomY);
            while (size[0] - 1 - w2 != 0) {
                size[0] += 1;
                w2 = (int) Math.round(Math.round((size[0] - 1) * zoomY) / zoomY);
            }

            int h2 = (int) Math.round(Math.round((size[1] - 1) * zoomX) / zoomX);
            while (size[1] - 1 - h2 != 0) {
                size[1] += 1;
                h2 = (int) Math.round(Math.round((size[1] - 1) * zoomX) / zoomX);
            }
            size[2] = ((int) Math.round((size[0] - 1) * zoomY) + 1);
            size[3] = ((int) Math.round((size[1] - 1) * zoomX) + 1);
        } else {
            size[2] = (int) Math.round(size[0] * zoomY);
            size[3] = (int) Math.round(size[1] * zoomX);
        }
        return size;
    }

    private void getInterpolationCoefficients(double[] c, int degree) {
        double[] z = new double[0];
        double lambda = 1.0D;

        switch (degree) {
            case 0:
            case 1:
                return;
            case 2:
                z = new double[1];
                z[0] = (Math.sqrt(8.0D) - 3.0D);
                break;
            case 3:
                z = new double[1];
                z[0] = (Math.sqrt(3.0D) - 2.0D);
                break;
            case 4:
                z = new double[2];
                z[0] = (Math.sqrt(664.0D - Math.sqrt(438976.0D)) + Math.sqrt(304.0D) - 19.0D);
                z[1] = (Math.sqrt(664.0D + Math.sqrt(438976.0D)) - Math.sqrt(304.0D) - 19.0D);
                break;
            case 5:
                z = new double[2];
                z[0] = (Math.sqrt(67.5D - Math.sqrt(4436.25D)) + Math.sqrt(26.25D) - 6.5D);

                z[1] = (Math.sqrt(67.5D + Math.sqrt(4436.25D)) - Math.sqrt(26.25D) - 6.5D);

                break;
            case 6:
                z = new double[3];
                z[0] = -0.4882945893030448D;
                z[1] = -0.08167927107623751D;
                z[2] = -0.001414151808325818D;
                break;
            case 7:
                z = new double[3];
                z[0] = -0.5352804307964382D;
                z[1] = -0.1225546151923267D;
                z[2] = -0.009148694809608277D;
                break;
            default:
                throw new IllegalArgumentException("Invalid spline degree (should be [0..7])");
        }

        if (c.length == 1) {
            return;
        }

        for (int k = 0; k < z.length; ++k) {
            lambda = lambda * (1.0D - z[k]) * (1.0D - (1.0D / z[k]));
        }

        for (int n = 0; n < c.length; ++n) {
            c[n] *= lambda;
        }

        for (int k = 0; k < z.length; ++k) {
            c[0] = getInitialCausalCoefficient(c, z[k], 1.E-009D);
            for (int n = 1; n < c.length; ++n) {
                c[n] += z[k] * c[(n - 1)];
            }
            c[(c.length - 1)] = getInitialAntiCausalCoefficient(c, z[k], 1.E-009D);
            for (int n = c.length - 2; 0 <= n; --n)
                c[n] = (z[k] * (c[(n + 1)] - c[n]));
        }
    }

    private void getSamples(double[] c, int degree) {
        double[] h = new double[0];
        double[] s = new double[c.length];

        switch (degree) {
            case 0:
            case 1:
                return;
            case 2:
                h = new double[2];
                h[0] = 0.75D;
                h[1] = 0.125D;
                break;
            case 3:
                h = new double[2];
                h[0] = 0.6666666666666666D;
                h[1] = 0.1666666666666667D;
                break;
            case 4:
                h = new double[3];
                h[0] = 0.5989583333333334D;
                h[1] = 0.1979166666666667D;
                h[2] = 0.002604166666666667D;
                break;
            case 5:
                h = new double[3];
                h[0] = 0.55D;
                h[1] = 0.2166666666666667D;
                h[2] = 0.008333333333333333D;
                break;
            case 6:
                h = new double[4];
                h[0] = 0.5110243055555556D;
                h[1] = 0.2287977430555556D;
                h[2] = 0.01566840277777778D;
                h[3] = 2.170138888888889E-005D;
                break;
            case 7:
                h = new double[4];
                h[0] = 0.4793650793650794D;
                h[1] = 0.236309523809524D;
                h[2] = 0.02380952380952381D;
                h[3] = 0.0001984126984126984D;
                break;
            default:
                throw new IllegalArgumentException("Invalid spline degree (should be [0..7])");
        }

        symmetricFir(h, c, s);
        System.arraycopy(s, 0, c, 0, s.length);
    }

    private double getInitialAntiCausalCoefficient(double[] c, double z, double tolerance) {
        return ((z * c[(c.length - 2)] + c[(c.length - 1)]) * z / (z * z - 1.0D));
    }

    private double getInitialCausalCoefficient(double[] c, double z, double tolerance) {
        double z1 = z;
        double zn = Math.pow(z, c.length - 1);
        double sum = c[0] + zn * c[(c.length - 1)];
        int horizon = c.length;

        if (tolerance > 0.0D) {
            horizon = 2 + (int) (Math.log(tolerance) / Math.log(Math.abs(z)));
            horizon = (horizon < c.length) ? horizon : c.length;
        }
        zn *= zn;
        for (int n = 1; n < horizon - 1; ++n) {
            zn /= z;
            sum += (z1 + zn) * c[n];
            z1 *= z;
        }
        return (sum / (1.0D - Math.pow(z, 2 * c.length - 2)));
    }

    private void symmetricFir(double[] h, double[] c, double[] s) {
        int i;
        if (c.length != s.length) {
            throw new IndexOutOfBoundsException("Incompatible size");
        }
        switch (h.length) {
            case 2:
                if (2 <= c.length) {
                    s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1]);
                    for (i = 1; i < c.length - 1; ++i) {
                        s[i] = (h[0] * c[i] + h[1] * (c[(i - 1)] + c[(i + 1)]));
                    }
                    s[(s.length - 1)] = (h[0] * c[(c.length - 1)] + 2.0D * h[1] * c[(c.length - 2)]);
                } else {
                    switch (c.length) {
                        case 1:
                            s[0] = ((h[0] + 2.0D * h[1]) * c[0]);
                            break;
                        default:
                            throw new NegativeArraySizeException("Invalid length of data");
                    }
                }
            case 3:
                if (4 <= c.length) {
                    s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[2]);
                    s[1] = (h[0] * c[1] + h[1] * (c[0] + c[2]) + h[2] * (c[1] + c[3]));
                    for (i = 2; i < c.length - 2; ++i) {
                        s[i] = (h[0] * c[i] + h[1] * (c[(i - 1)] + c[(i + 1)]) + h[2] * (c[(i - 2)] + c[(i + 2)]));
                    }

                    s[(s.length - 2)] = (h[0] * c[(c.length - 2)] + h[1] * (c[(c.length - 3)] + c[(c.length - 1)]) + h[2] * (c[(c.length - 4)] + c[(c.length - 2)]));

                    s[(s.length - 1)] = (h[0] * c[(c.length - 1)] + 2.0D * h[1] * c[(c.length - 2)] + 2.0D * h[2] * c[(c.length - 3)]);
                } else {
                    switch (c.length) {
                        case 3:
                            s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[2]);
                            s[1] = (h[0] * c[1] + h[1] * (c[0] + c[2]) + 2.0D * h[2] * c[1]);
                            s[2] = (h[0] * c[2] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[0]);
                            break;
                        case 2:
                            s[0] = ((h[0] + 2.0D * h[2]) * c[0] + 2.0D * h[1] * c[1]);
                            s[1] = ((h[0] + 2.0D * h[2]) * c[1] + 2.0D * h[1] * c[0]);
                            break;
                        case 1:
                            s[0] = ((h[0] + 2.0D * (h[1] + h[2])) * c[0]);
                            break;
                        default:
                            throw new NegativeArraySizeException("Invalid length of data");
                    }
                }
            case 4:
                if (6 <= c.length) {
                    s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[2] + 2.0D * h[3] * c[3]);

                    s[1] = (h[0] * c[1] + h[1] * (c[0] + c[2]) + h[2] * (c[1] + c[3]) + h[3] * (c[2] + c[4]));

                    s[2] = (h[0] * c[2] + h[1] * (c[1] + c[3]) + h[2] * (c[0] + c[4]) + h[3] * (c[1] + c[5]));

                    for (i = 3; i < c.length - 3; ++i) {
                        s[i] = (h[0] * c[i] + h[1] * (c[(i - 1)] + c[(i + 1)]) + h[2] * (c[(i - 2)] + c[(i + 2)]) + h[3] * (c[(i - 3)] + c[(i + 3)]));
                    }

                    s[(s.length - 3)] = (h[0] * c[(c.length - 3)] + h[1] * (c[(c.length - 4)] + c[(c.length - 2)]) + h[2] * (c[(c.length - 5)] + c[(c.length - 1)]) + h[3] * (c[(c.length - 6)] + c[(c.length - 2)]));

                    s[(s.length - 2)] = (h[0] * c[(c.length - 2)] + h[1] * (c[(c.length - 3)] + c[(c.length - 1)]) + h[2] * (c[(c.length - 4)] + c[(c.length - 2)]) + h[3] * (c[(c.length - 5)] + c[(c.length - 3)]));

                    s[(s.length - 1)] = (h[0] * c[(c.length - 1)] + 2.0D * h[1] * c[(c.length - 2)] + 2.0D * h[2] * c[(c.length - 3)] + 2.0D * h[3] * c[(c.length - 4)]);
                } else {
                    switch (c.length) {
                        case 5:
                            s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[2] + 2.0D * h[3] * c[3]);

                            s[1] = (h[0] * c[1] + h[1] * (c[0] + c[2]) + h[2] * (c[1] + c[3]) + h[3] * (c[2] + c[4]));

                            s[2] = (h[0] * c[2] + (h[1] + h[3]) * (c[1] + c[3]) + h[2] * (c[0] + c[4]));

                            s[3] = (h[0] * c[3] + h[1] * (c[2] + c[4]) + h[2] * (c[1] + c[3]) + h[3] * (c[0] + c[2]));

                            s[4] = (h[0] * c[4] + 2.0D * h[1] * c[3] + 2.0D * h[2] * c[2] + 2.0D * h[3] * c[1]);

                            break;
                        case 4:
                            s[0] = (h[0] * c[0] + 2.0D * h[1] * c[1] + 2.0D * h[2] * c[2] + 2.0D * h[3] * c[3]);

                            s[1] = (h[0] * c[1] + h[1] * (c[0] + c[2]) + h[2] * (c[1] + c[3]) + 2.0D * h[3] * c[2]);

                            s[2] = (h[0] * c[2] + h[1] * (c[1] + c[3]) + h[2] * (c[0] + c[2]) + 2.0D * h[3] * c[1]);

                            s[3] = (h[0] * c[3] + 2.0D * h[1] * c[2] + 2.0D * h[2] * c[1] + 2.0D * h[3] * c[0]);

                            break;
                        case 3:
                            s[0] = (h[0] * c[0] + 2.0D * (h[1] + h[3]) * c[1] + 2.0D * h[2] * c[2]);
                            s[1] = (h[0] * c[1] + (h[1] + h[3]) * (c[0] + c[2]) + 2.0D * h[2] * c[1]);
                            s[2] = (h[0] * c[2] + 2.0D * (h[1] + h[3]) * c[1] + 2.0D * h[2] * c[0]);
                            break;
                        case 2:
                            s[0] = ((h[0] + 2.0D * h[2]) * c[0] + 2.0D * (h[1] + h[3]) * c[1]);
                            s[1] = ((h[0] + 2.0D * h[2]) * c[1] + 2.0D * (h[1] + h[3]) * c[0]);
                            break;
                        case 1:
                            s[0] = ((h[0] + 2.0D * (h[1] + h[2] + h[3])) * c[0]);
                            break;
                        default:
                            throw new NegativeArraySizeException("Invalid length of data");
                    }
                }
        }

        throw new IllegalArgumentException("Invalid filter half-length (should be [2..4])");
    }
}
