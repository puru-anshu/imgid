package com.anshuman.imageprocessing.imageid.mathutil;



/**
 * .
 * User: anshu
 * Date: Nov 23, 2010
 * Time: 3:26:36 PM
 */
public class Matrix {

    // Instance fields

    /**
     * The double array val contains the <B>Matrix</B> elements.
     * Should never be addressed directly;
     * all access through the methods set and get.
     *
     * @see Matrix#set
     * @see Matrix#get
     */
    private float[][] val;
    /**
     * The number of rows in the <B>Matrix</B>.
     */
    private int row;
    /**
     * The number of columns in the <B>Matrix</B>.
     */
    private int col;

    // Constructors


    public Matrix(float[][] a) {
        this.row = a.length;
        this.col = a[0].length;
        this.val = new float[row][col];
        for (int i = 1; i <= row; i++)
            for (int j = 1; j <= col; j++)
                set(i, j, a[i - 1][j - 1]);
    }


    public Matrix(Matrix a) {
        this.row = a.row;
        this.col = a.col;
        this.val = new float[row][col];
        for (int i = 1; i <= a.row; i++)
            for (int j = 1; j <= a.col; j++)
                set(i, j, a.get(i, j));
    }


    public Matrix(int r, int c, float val) {
        row = r;
        col = c;
        this.val = new float[row][col];
        for (int i = 1; i <= row; i++)
            for (int j = 1; j <= col; j++)
                set(i, j, val);

    }

    public Matrix(int r, int c) {
        this(r, c, 0);
    }

    // Class Methods

    /**
     * Checks whether the matrices a and b have identical dimensions.
     * If checkEqualSize returns true addition and subtraction of
     * matrices are valid operations.
     * If false is returned most methods invoking checkEqualSize
     * will issue a runtime exception.
     */
    public static boolean checkEqualSize(Matrix a, Matrix b) {
        return (a.getRowNum() == b.getRowNum() &&
                a.getColNum() == b.getColNum());
    }

    /**
     * Adds the matrices a and b and returns the sum.
     * The dimensions of the matrices must be identical.
     *
     * @throws RuntimeException if dimensions of matrices are not identical.
     */
    public static Matrix add(Matrix a, Matrix b) {
        if (checkEqualSize(a, b)) {
            Matrix c = new Matrix(a);
            for (int i = 1; i <= a.getRowNum(); i++) {
                for (int j = 1; j <= a.getColNum(); j++) {
                    c.set(i, j, a.get(i, j) + b.get(i, j));
                }
            }
            return c;
        } else
            throw new RuntimeException("Dimension error in Matrix add.");
    }

    /**
     * Subtracts the matrices a and b and returns the difference.
     * The dimensions of the matrices must be identical.
     *
     * @throws RuntimeException if dimensions of matrices are not identical.
     */
    public static Matrix sub(Matrix a, Matrix b) {
        if (checkEqualSize(a, b)) {
            Matrix c = new Matrix(a);
            for (int i = 1; i <= a.getRowNum(); i++) {
                for (int j = 1; j <= a.getColNum(); j++) {
                    c.set(i, j, a.get(i, j) - b.get(i, j));
                }
            }
            return c;
        } else
            throw new RuntimeException("Dimension error in Matrix sub.");
    }

    /**
     * Multiplies the matrices a and b and returns the product.
     * The number of columns in a must equal the number of rows
     * in b for matrix multiplication to be valid.
     *
     * @throws RuntimeException if the number of columns in a differs
     *                          from the number of rows in b.
     */
    public static Matrix mul(Matrix a, Matrix b) {
        int rows = a.getRowNum();
        int cols = b.getColNum();
        float temp = 0.0f;
        if (a.getColNum() == b.getRowNum()) {
            Matrix c = new Matrix(rows, cols, 0);
            for (int i = 1; i <= rows; i++) {
                for (int k = 1; k <= cols; k++) {
                    temp = 0.0f;
                    for (int j = 1; j <= a.getColNum(); j++) {
                        temp += (a.get(i, j) * b.get(j, k));
                    }
                    c.set(i, k, temp);
                }
            }
            return c;
        } else
            throw new RuntimeException("Dimension error in Matrix mul.");
    }


    /**
     * Returns the transposed <B>Matrix</B> of the parameter.
     */
    public static Matrix trans(Matrix a) {
        int rows = a.getRowNum();
        int cols = a.getColNum();
        Matrix b = new Matrix(cols, rows);
        for (int i = 1; i <= cols; i++) {
            b.setCol(i, a.getRow(i));
        }
        return b;
    }

    /**
     * Change column c of the <B>Matrix</B> to a <B>Vector</B> a.
     *
     * @throws RuntimeException if the designated column number is larger
     *                          than the number of columns in the <B>Matrix</B>.
     * @throws RuntimeException if the <B>Vector</B>s length
     *                          differs from the number of rows in the <B>Matrix</B>.
     */
    public void setCol(int c, Vector a) {
        if (c <= this.getColNum()) {
            if (a.size() == this.getRowNum()) {
                for (int i = 1; i <= this.getRowNum(); i++)
                    this.set(i, c, a.get(i));

            } else
                throw new RuntimeException("Dimension error in setCol (1).");
        } else
            throw new RuntimeException("Dimension error in setCol (2).");
    }

    /*public Matrix inv(Matrix a);*/

    /**
     * Returns the negated <B>Matrix</B> of the parameter,
     * i.e. the <B>Matrix</B> where all elements have changed sign.
     */
    public static Matrix neg(Matrix a) {
        Matrix b = new Matrix(a.getRowNum(), a.getColNum());
        for (int i = 1; i <= a.getRowNum(); i++) {
            for (int j = 1; j <= a.getColNum(); j++) {
                b.set(i, j, -a.get(i, j));
            }
        }
        return b;
    }

    /*solve(Matrix a,Matrix b);(Matrix a,Vector b);(Matrix a);*/

    /**
     * Returns an n by n unit <B>Matrix</B>.
     */
    public static Matrix unit(int n) {
        Matrix a = new Matrix(n, n, 0);
        for (int i = 1; i <= n; i++) {
            a.set(i, i, 1.0f);
        }
        return a;
    }

    // Instance Methods

    /**
     * Set value of field(i,j) to val.
     */
    public void set(int i, int j, float val) {
        this.val[i - 1][j - 1] = val;
    }

    /**
     * Change a sub Matrix with upper left corner in r,c.
     */
    public void setSubMatrix(int r, int c, Matrix a) {
        for (int i = 1; i <= a.getRowNum(); i++)
            for (int j = 1; j <= a.getColNum(); j++)
                this.set(r + i - 1, c + j - 1, a.get(i, j));
    }


    /**
     * Get value of field r,c
     */
    public float get(int r, int c) {
        return this.val[r - 1][c - 1];
    }

    /**
     * Get sub Matrix with upper left corner in rb,cb
     * and lower rigth corner in re,ce
     */
    public Matrix getSubMatrix(int rb, int cb, int re, int ce) {
        if (re <= this.getRowNum()) {
            if (ce <= this.getColNum()) {
                int r = re - rb + 1;
                int c = ce - cb + 1;
                Matrix a = new Matrix(r, c);
                for (int i = 1; i <= r; i++)
                    for (int j = 1; j <= c; j++)
                        a.set(i, j, this.get(rb - 1 + i, cb - 1 + j));
                return a;
            } else
                throw new RuntimeException("Index exceeds Matrix in getSubMatrix(1).");
        }
        throw new RuntimeException("Index exceeds Matrix in getSubMatrix (2).");
    }

    /**
     * Get number of columns
     *
     * @return int number of coulumns
     */
    public int getColNum() {
        return this.col;
    }

    /**
     * Get number of rows
     *
     * @return int number of rows
     */
    public int getRowNum() {
        return this.row;
    }

    /**
     * Get column c
     *
     * @param c
     * @return Vector
     */
    public Vector getCol(int c) {
        int c2 = this.getColNum();
        if (c <= c2) {
            int r = this.getRowNum();
            Vector out = new Vector(r);
            for (int i = 1; i <= r; i++)
                out.set(i, this.get(i, c));
            return out;
        } else
            throw new RuntimeException("Column does not exist");
    }

    /**
     * Get row r
     *
     * @param r
     * @return Vector
     */
    public Vector getRow(int r) {
        int r2 = this.getRowNum();
        if (r <= r2) {
            int c = this.getColNum();
            Vector out = new Vector(c);
            for (int i = 1; i <= c; i++)
                out.set(i, this.get(r, i));
            return out;
        } else
            throw new RuntimeException("Row does not exist");
    }


    /**
     * Checks whether the <B>Matrix</B> this is identical
     * to the <B>Matrix</B> a.
     *
     * @return true if a and this have the same elements in all places
     *         else false is returned.
     */
    public boolean equals(Matrix a) {
        if (checkEqualSize(this, a)) {
            for (int i = 1; i <= a.getRowNum(); i++)
                for (int j = 1; j <= a.getRowNum(); j++)
                    if (this.get(i, j) != a.get(i, j))
                        return false;
            return true;
        }
        return false;
    }


    /**
     * Multiplies each field in a <B>Matrix</B> by the
     * corresponding field in a specified <B>Matrix</B>, b.
     *
     * @throws RuntimeException if dimensions of matrices differ.
     */
    public void mul(Matrix b) {
        if (checkEqualSize(this, b)) {
            int rows = this.getRowNum();
            int cols = this.getColNum();
            for (int i = 1; i <= rows; i++) {
                for (int j = 1; j <= cols; j++) {
                    this.set(i, j, this.get(i, j) * b.get(i, j));
                }
            }
        } else
            throw new RuntimeException("Error in elementwise mul of Matrix and Matrix.");
    }

    /**
     * Multiplies each elmement in the <B>Matrix</B> this by a constant c.
     */
    public void mul(float c) {
        int rows = this.getRowNum();
        int cols = this.getColNum();
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                this.set(i, j, c * this.get(i, j));
            }
        }
    }

    /**
     * Internal work method for swapping two rows.
     * This method will not be available for long.
     */
    private static Matrix rowswap(Matrix m) {
        float temp;
        for (int i = 1; i <= 2; i++) {
            temp = m.get(1, i);
            m.set(1, i, m.get(2, i));
            m.set(2, i, temp);
        }
        return m;
    }

    /**
     * Solves the <B>Matrix</B> equation: A*X=B,
     * where A, X and B are matrices.
     * Returns new matrix X.
     * Works only for 2x2-matrices.
     */
    public static Matrix solve(Matrix m3, Matrix m4) {

        Matrix m1 = new Matrix(m3);
        Matrix m2 = new Matrix(m4);

        if (m1.getRowNum() == 2 &&
                m2.getRowNum() == 2 &&
                m1.getColNum() == 2 &&
                m2.getColNum() == 2) {
            float m12, m22;
            float m11 = m1.get(1, 1);
            float m21 = m1.get(2, 1);
            if (m11 != 0) {
                if (m11 != 1) {
                    m1.set(1, 1, 1);

                    m1.set(1, 2, m1.get(1, 2) / m11);
                    m2.set(1, 1, m2.get(1, 1) / m11);
                    m2.set(1, 2, m2.get(1, 2) / m11);

                }
            } else if (m21 != 0) {
                m1 = rowswap(m1);
                m2 = rowswap(m2);
                m11 = m21;
                if (m11 != 1) {
                    m1.set(1, 1, 1);
                    m1.set(1, 2, m1.get(1, 2) / m11);
                    m2.set(1, 1, m2.get(1, 1) / m11);
                    m2.set(1, 2, m2.get(1, 2) / m11);
                }
            } else
                throw new RuntimeException("Singular Matrix");
            m21 = m1.get(2, 1);
            if (m21 != 0) {
                float factor = m21;
                //m1.set(1,2,0);
                m1.set(2, 2, m1.get(2, 2) - factor * m1.get(1, 2));
                m2.set(2, 1, m2.get(2, 1) - factor * m2.get(1, 1));
                m2.set(2, 2, m2.get(2, 2) - factor * m2.get(1, 2));
            }
            m22 = m1.get(2, 2);
            if (m22 != 0) {
                m1.set(2, 2, 1);
                m2.set(2, 1, m2.get(2, 1) / m22);
                m2.set(2, 2, m2.get(2, 2) / m22);
            } else
                throw new RuntimeException("Singular Matrix.");
            m12 = m1.get(1, 2);
            if (m12 != 0) {
                /*      m1.set(1,2)=1;*/
                m2.set(1, 1, m2.get(1, 1) - m12 * m2.get(2, 1));
                m2.set(1, 2, m2.get(1, 2) - m12 * m2.get(2, 2));
            }
            return m2;
        } else
            throw new RuntimeException("\"solve\" works only for 2x2 matrices");

    }


    /**
     * Converts the <B>Matrix</B> this into a <B>String</B>.
     *
     * @return A <B>String</B> representing a <B>Matrix</B>.
     */
    public String toString() {
        String retur = "[";
        for (int i = 1; i <= this.getRowNum(); i++) {
            retur += "[";
            for (int j = 1; j <= this.getColNum(); j++)
                retur += this.get(i, j) + " ";
            retur += "]";
        }
        retur += "]";
        return retur;
    }
}

