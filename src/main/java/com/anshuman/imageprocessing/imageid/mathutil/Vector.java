package com.anshuman.imageprocessing.imageid.mathutil;

import java.util.StringTokenizer;

/**
 * .
 * User: anshu
 * Date: Nov 23, 2010
 * Time: 4:21:29 PM
 */
public class Vector {
    /**
     * ** Instance Fields ********************************
     */

    private float[] v;

    /***** Constructors ************************************/

    /**
     * Constructs a new <b>Vector</b> of the elements specified by
     * float array <i>d</i>.
     *
     * @param d float array of the future elements of this vector
     */
    public Vector(float[] d) {
        v = new float[d.length];
        for (int i = 0; i < d.length; i++)
            v[i] = d[i];
    }

    /**
     * Constructs a new <b>Vector</b> of length <i>n</i> with no elements
     * initialized.
     *
     * @param n length of this vector
     */
    public Vector(int n) {
        v = new float[n];
    }

    /**
     * Constructs a new <b>Vector</b> of length <i>n</i> with all elements
     * initialized to <i>d</i>.
     *
     * @param n length of this vector
     * @param d initial value of the elements of this vector
     */
    public Vector(int n, float d) {
        v = new float[n];
        for (int i = 0; i < n; i++)
            v[i] = d;
    }

    /**
     * Constructs a new <b>Vector</b> containing a copy of <i>w</i>.
     *
     * @param w vector to be copied
     */
    public Vector(Vector w) {
        v = new float[w.v.length];
        for (int i = 0; i < v.length; i++)
            v[i] = w.v[i];
    }

    /**
     * Constructs a new <b>Vector</b> of the elements specified by <i>s</i>,
     * with <i>s</i> assumed to be a string containing <i>comma-</i>,
     * <i>semi colon-</i> or <i>space-</i> separated floats. Any brackets are
     * ignored.
     * <p/>
     * Example of use:<br>
     * <pre>
     * Vector a = new Vector( "1,2.5,-4.1,0,0" );
     * Vector b = new Vector( "( 1; 2.5; -4.1; 0; 0; )" );
     * Vector c = new Vector( " 1 2.5 -4.1 0 0 " );
     * <p/>
     * Vector d = new Vector( "[(1];, ()2.5,,,, -4.1, 0 0 (" );
     * </pre>
     * Vector <i>d</i> is an example of a constructor that does the job, but
     * the notation is not in general adviced.
     *
     * @param s string of comma-, semi colon- or space- separated floats
     */
    public Vector(String s) {
        StringTokenizer tokenizer = new StringTokenizer(s, "()[]{},; ", false);
        v = new float[tokenizer.countTokens()];

        for (int i = 0; i < v.length; i++)
            v[i] = Float.parseFloat(tokenizer.nextToken());
    }

    /***** Instance Methods ********************************/

    /**
     * Get the element at index <i>i</i>.
     *
     * @param i index in range 1 to length of this vector
     * @return element at <i>i</i>
     */
    public float get(int i) {
        return v[i - 1];
    }

    /**
     * Set the element at index <i>i</i> to <i>d</i>.
     *
     * @param i index in range 1 to length of this vector
     * @param d value to be set
     */
    public void set(int i, float d) {
        v[i - 1] = d;
    }

    /**
     * True if all elements are equal.
     *
     * @param a <b>Vector</b> to be compared with
     */
    public boolean equals(Vector a) {
        if (v.length != a.v.length)
            return false;

        for (int i = 0; i < v.length; i++) {
            if (v[i] != a.v[i])
                return false;
        }
        return true;
    }

    /**
     * Sum of elements.
     *
     * @return sum of all elements of this vector
     */
    public float sum() {
        float sum = v[0];
        for (int i = 1; i < v.length; i++)
            sum += v[i];
        return sum;
    }

    /**
     * The 2-norm, i.e. ||<b>x</b>|| = (<i>x<sub>1</sub><sup>2</sup> +
     * x<sub>2</sub><sup>2</sup> + x<sub>3</sub><sup>2</sup> + ... +
     * x<sub>n</sub><sup>2</sup></i>)<i><sup>1/2</sup></i>.
     *
     * @return the 2-norm of this vector
     */
    // NOTICE COULD BE OPTIMIZED BY FACTORING OUT THE LARGEST ELEMENT OF THE VECTOR - SEE Complex.abs()
    public float norm() {
        float norm = v[0] * v[0];
        for (int i = 1; i < v.length; i++)
            norm += v[i] * v[i];
        return (float) Math.sqrt(norm);
    }

    /**
     * The 2-norm squared, i.e. ||<b>x</b>||<sup>2</sup> = (<i>x<sub>1</sub><sup>2</sup> +
     * x<sub>2</sub><sup>2</sup> + x<sub>3</sub><sup>2</sup> + ... +
     * x<sub>n</sub><sup>2</sup></i>).
     *
     * @return the squared 2-norm of this vector
     */
    public float sqrNorm() {
        float norm2 = v[0] * v[0];
        for (int i = 1; i < v.length; i++)
            norm2 += v[i] * v[i];
        return norm2;
    }

    /**
     * Number of elements.
     *
     * @return the number of elements of this vector
     */
    public int length() {
        return v.length;
    }

    /**
     * String representation, e.g. <code>(1.3,-5,2.1,6)</code>
     *
     * @return this vector as a string of comma separated floats
     *         enclosed in rounded brackets.
     */
    public String toString() {
        String s = "(" + v[0];
        for (int i = 1; i < v.length; i++)
            s += "," + v[i];
        return s + ")";
    }

    /**
     * Index of maximum element.
     *
     * @return index of first occurence of maximum element of this vector
     *         in the range 1 to the length of this vector
     */
    public int imax() {
        int imax = 0;
        for (int i = 1; i < v.length; i++) {
            if (v[i] > v[imax])
                imax = i;
        }
        return imax + 1;
    }

    /**
     * Index of minimum element.
     *
     * @return index of first occurence of minumum element of this vector
     *         in the range 1 to the length of this vector
     */
    public int imin() {
        int imin = 0;
        for (int i = 1; i < v.length; i++) {
            if (v[i] < v[imin])
                imin = i;
        }
        return imin + 1;
    }

    /**
     * Absolute value instance method.
     */
    public Vector abs() {
        for (int i = 0; i < v.length; i++)
            v[i] = Math.abs(v[i]);
        return this;
    }

    /**
     * <b>+=</b> operator. Set this vector to the sum of this and <i>a</i>,
     * and returns the result.
     * <p/>
     * <i>a</i> must be of the same length as this.
     *
     * @param a vector to be added
     * @return this after addition of <i>a</i>
     * @throw ArithmeticException    if <i>this</i> and <i>a</i> are of unequal length
     */
    public Vector add(Vector a) throws ArithmeticException {
        if (v.length != a.v.length)
            throw new ArithmeticException("using += operator, length of Vectors don't match: " + v.length + "!=" + a.v.length);

        for (int i = 0; i < v.length; i++)
            v[i] += a.v[i];
        return this;
    }

    /**
     * <b>+=</b> operator. Increses every element in this vector by scalar
     * <i>d</i>,
     * and returns the result.
     * <p/>
     *
     * @param d scalar to be added
     * @return this after addition of <i>d</i>
     */
    public Vector add(float d) {
        for (int i = 0; i < v.length; i++)
            v[i] += d;
        return this;
    }

    /**
     * <b>-=</b> operator. Set this vector to the difference of this and
     * <i>a</i>, and returns the result.
     * <p/>
     * <i>a</i> must be of the same length as this.
     *
     * @param a vector to be subtracted
     * @return this after subtraction of <i>a</i>
     * @throw ArithmeticException    if <i>this</i> and <i>a</i> are of unequal length
     */
    public Vector sub(Vector a) throws ArithmeticException {
        if (v.length != a.v.length)
            throw new ArithmeticException("using -= operator, length of Vectors don't match: " + v.length + "!=" + a.v.length);

        for (int i = 0; i < v.length; i++)
            v[i] -= a.v[i];
        return this;
    }

    /**
     * <b>-=</b> operator. Decreses every element in this vector by scalar
     * <i>d</i>, and returns the result.
     *
     * @param d scalar to be subtracted
     * @return this after subtraction of <i>d</i>
     */
    public Vector sub(float d) {
        for (int i = 0; i < v.length; i++)
            v[i] -= d;
        return this;
    }

    /**
     * <b>*=</b> operator. Set this vector to the product of this and scalar
     * <i>d</i>, and returns the result.
     *
     * @param d scalar to be multiplied
     * @return this after multiplication with <i>d</i>
     */
    public Vector mul(float d) {
        for (int i = 0; i < v.length; i++)
            v[i] *= d;
        return this;
    }

    /**
     * <b>*=</b> <i>array</i> product operator. Multiplies every element
     * in this vector with corresponding element in <i>a</i>, stores the
     * result in this, and returns this.
     * <p/>
     * <i>a</i> must be of the same length as this.
     *
     * @param a vector to be multiplied
     * @return this after array multiplication with <i>a</i>
     * @throw ArithmeticException   if <i>this</i> and <i>a</i> are of unequal length
     */
    public Vector mul(Vector a) throws ArithmeticException {
        if (v.length != a.v.length)
            throw new ArithmeticException("using *= operator, length of Vectors don't match: " + v.length + "!=" + a.v.length);

        for (int i = 0; i < v.length; i++)
            v[i] *= a.v[i];
        return this;
    }

    /**
     * <b>/=</b> operator. Set this vector to the division of this and
     * scalar <i>d</i>, and returns the result.
     *
     * @param d scalar with which to divide
     * @return this after division with <i>d</i>
     */
    public Vector div(float d) {
        for (int i = 0; i < v.length; i++)
            v[i] /= d;
        return this;
    }

    /**
     * <b>/=</b> <i>array</i> division operator. Divides every element
     * in this vector with corresponding element in <i>a</i>, stores the
     * result in this, and returns this.
     * <p/>
     * <i>a</i> must be of the same length as this.
     *
     * @param a vector with which to divide
     * @return this after array division with <i>a</i>
     * @throw ArithmeticException    if <i>this</i> and <i>a</i> are of unequal length
     */
    public Vector div(Vector a) throws ArithmeticException {
        if (v.length != a.v.length)
            throw new ArithmeticException("using /= operator, length of Vectors don't match: " + v.length + "!=" + a.v.length);

        for (int i = 0; i < v.length; i++)
            v[i] /= a.v[i];
        return this;
    }

    /***** Class Methods ***********************************/

    /**
     * Returns a Vector with absolute values of the given. <br>
     * <i>Note:</i> Added by Martin E. Nielsen, 160600
     *
     * @param a A Vector object.
     * @return A Vector object holding positive values of <b>a</b>.
     * @since 1.1
     */
    public static Vector abs(Vector a) {
        Vector aRet = new Vector(a.length());

        for (int i = 1; i <= a.length(); i++) {
            aRet.set(i, Math.abs(a.get(i)));
        } // for

        return aRet;
    } // abs


    /**
     * Addition of <i>a</i> and <i>b</i>.
     * <p/>
     * <i>a</i> and <i>b</i> must be of the same length.
     *
     * @param a
     * @param b vectors to be added
     * @return addition of <i>a</i> and <i>b</i>
     * @throw ArithmeticException    if <i>a</i> and <i>b</i> are of unequal length
     */
    public static Vector add(Vector a, Vector b) throws ArithmeticException {
        if (a.v.length != b.v.length)
            throw new ArithmeticException("using + operator, length of Vectors don't match: " + a.v.length + "!=" + b.v.length);

        Vector w = new Vector(a.v.length);
        for (int i = 0; i < w.v.length; i++)
            w.v[i] = a.v[i] + b.v[i];
        return w;
    }

    /**
     * Addition of scalar <i>d</i> to every element in vector
     * <i>a</i>.
     *
     * @param d float and
     * @param a vector to be added
     * @return addition of <i>a</i> and <i>d</i>
     */
    public static Vector add(Vector a, float d) {
        Vector w = new Vector(a.v.length);
        for (int i = 0; i < w.v.length; i++)
            w.v[i] = a.v[i] + d;
        return w;
    }

    /**
     * Addition of scalar <i>d</i> to every element in vector
     * <i>a</i>.
     *
     * @param d float and
     * @param a vector to be added
     * @return addition of <i>a</i> and <i>d</i>
     */
    public static Vector add(float d, Vector a) {
        return add(a, d);
    }

    /**
     * Subtraction of <i>a</i> and <i>b</i>, <i>a - b</i>.
     * <p/>
     * <i>a</i> and <i>b</i> must be of the same length.
     *
     * @param a
     * @param b vectors to be subtracted
     * @return subtraction of <i>b</i> from <i>a</i>
     * @throw ArithmeticException    if <i>a</i> and <i>b</i> are of unequal length
     */
    public static Vector sub(Vector a, Vector b) throws ArithmeticException {
        if (a.v.length != b.v.length)
            throw new ArithmeticException("using - operator, length of Vectors don't match: " + a.v.length + "!=" + b.v.length);

        Vector w = new Vector(a.v.length);
        for (int i = 0; i < w.v.length; i++)
            w.v[i] = a.v[i] - b.v[i];
        return w;
    }

    /**
     * Subtraction of scalar <i>d</i> from every element in vector
     * <i>a</i>, <i>a-d</i>.
     *
     * @param a vector and
     * @param d float to be subtracted
     * @return subtraction of <i>d</i> from <i>a</i>
     */
    public static Vector sub(Vector a, float d) {
        Vector w = new Vector(a.v.length);
        for (int i = 0; i < w.v.length; i++)
            w.v[i] = a.v[i] - d;
        return w;
    }

    /**
     * Subtraction of every element in vector <i>a</i> from scalar
     * <i>d</i>, <i>d-a</i>.
     *
     * @param d float and
     * @param a vector to be subtracted
     * @return subtraction of <i>a</i> from <i>d</i>
     */
    public static Vector sub(float d, Vector a) {
        Vector w = new Vector(a.v.length);
        for (int i = 0; i < w.v.length; i++)
            w.v[i] = d - a.v[i];
        return w;
    }

    /**
     * <i>Array</i> multiplication of <i>a</i> and <i>b</i>.
     * <p/>
     * <i>a</i> and <i>b</i> must be of the same length.
     *
     * @param a
     * @param b vectors to be multiplied
     * @return <i>array</i> multiplication of <i>a</i> and <i>b</i>
     * @throw ArithmeticException    if <i>a</i> and <i>b</i> are of unequal length
     */
    public static Vector mul(Vector a, Vector b) throws ArithmeticException {
        if (a.v.length != b.v.length)
            throw new ArithmeticException("using * operator, length of Vectors don't match: " + a.v.length + "!=" + b.v.length);

        Vector w = new Vector(a.v.length);
        for (int i = 0; i < w.v.length; i++)
            w.v[i] = a.v[i] * b.v[i];
        return w;
    }

    /**
     * Multiplication of scalar <i>d</i> and <i>a</i>.
     *
     * @param d float and
     * @param a vector to be multiplied
     * @return multiplication of <i>a</i> and <i>d</i>
     */
    public static Vector mul(Vector a, float d) {
        Vector w = new Vector(a.v.length);
        for (int i = 0; i < w.v.length; i++)
            w.v[i] = a.v[i] * d;
        return w;
    }

    /**
     * Multiplication of scalar <i>d</i> and <i>a</i>.
     *
     * @param d float and
     * @param a vector to be multiplied
     * @return multiplication of <i>a</i> and <i>d</i>
     */
    public static Vector mul(float d, Vector a) {
        return mul(a, d);
    }

    /**
     * <i>Array</i> division of <i>a</i> and <i>b</i>.
     * <p/>
     * <i>a</i> and <i>b</i> must be of the same length.
     *
     * @param a
     * @param b vectors to be divided.
     * @return <i>array</i> division of <i>a</i> and <i>b</i>
     * @throw ArithmeticException    if <i>a</i> and <i>b</i> are of unequal length
     */
    public static Vector div(Vector a, Vector b) throws ArithmeticException {
        if (a.v.length != b.v.length)
            throw new ArithmeticException("using / operator, length of Vectors don't match: " + a.v.length + "!=" + b.v.length);

        Vector w = new Vector(a.v.length);
        for (int i = 0; i < w.v.length; i++)
            w.v[i] = a.v[i] / b.v[i];
        return w;
    }

    /**
     * Division of vector <i>a</i> with scalar <i>d</i>.
     *
     * @param a vector and
     * @param d float to be divided with
     * @return division of <i>a</i> with <i>d</i>
     */
    public static Vector div(Vector a, float d) {
        Vector w = new Vector(a.v.length);
        for (int i = 0; i < w.v.length; i++)
            w.v[i] = a.v[i] / d;
        return w;
    }

    /**
     * <i>Array</i> division of scalar <i>d</i> with vector <i>a</i>.
     *
     * @param d float and
     * @param a vector to be divided with
     * @return recipocal value of every element of <i>a</i> multiplied
     *         by <i>d</i>
     */
    public static Vector div(float d, Vector a) {
        Vector w = new Vector(a.v.length);
        for (int i = 0; i < w.v.length; i++)
            w.v[i] = d / a.v[i];
        return w;
    }

    /**
     * Value of maximum element of <i>a</i>.
     *
     * @return maximum value of <i>a</i>
     */
    public static float max(Vector a) {
        float max = a.v[0];
        for (int i = 1; i < a.v.length; i++) {
            if (a.v[i] > max)
                max = a.v[i];
        }
        return max;
    }

    /**
     * Value of minimum element of <i>a</i>.
     *
     * @return minimum value of <i>a</i>
     */
    public static float min(Vector a) {
        float min = a.v[0];
        for (int i = 1; i < a.v.length; i++) {
            if (a.v[i] < min)
                min = a.v[i];
        }
        return min;
    }

    /**
     * Dot product or scalar product of <i>a</i> and <i>b</i>.
     *
     * @param a
     * @param b vectors to be multiplied.
     * @return scalar product of <i>a</i> and <i>b</i>
     * @throw ArithmeticException    if <i>a</i> and <i>b</i> are of unequal length
     */
    public static float dot(Vector a, Vector b) throws ArithmeticException {
        if (a.v.length != b.v.length)
            throw new ArithmeticException("using DOT operator, length of Vectors don't match: " + a.v.length + "!=" + b.v.length);

        float d = a.v[0] * b.v[0];
        for (int i = 1; i < a.v.length; i++)
            d += a.v[i] * b.v[i];
        return d;
    }

    /**
     * Cross product of <i>a</i> and <i>b</i>.
     * <p/>
     * Only defined for <b>3-dimensional</b> vectors.
     *
     * @param a
     * @param b vectors to be multiplied.
     * @return cross product of <i>a</i> and <i>b</i>
     * @throw ArithmeticException    if <i>a</i> and <i>b</i> are not of length 3
     */
    public static Vector cross(Vector a, Vector b) throws ArithmeticException {
        if (a.v.length != 3 || b.v.length != 3)
            throw new ArithmeticException("Using CROSS operator - length of Vectors don't match: " + a.v.length + " or " + b.v.length + " != 3");

        Vector w = new Vector(3);
        w.v[0] = a.v[1] * b.v[2] - a.v[2] * b.v[1];
        w.v[1] = a.v[2] * b.v[0] - a.v[0] * b.v[2];
        w.v[2] = a.v[0] * b.v[1] - a.v[1] * b.v[0];
        return w;
    }

    public int size() {
        return v.length;
    }
}
