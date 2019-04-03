package com.anshuman.imageprocessing.imageid.demo;

/**
 * User: Anshuman
 * Date: Sep 24, 2010
 * Time: 3:17:00 PM
 */
public class Result {
    public String path;
    public double val;

    public Result(String path, double val) {
        this.path = path;
        this.val = val;

    }

    public boolean isSameWith(Result other) {
        if (other != null) {
            return (other.val == this.val) && (other.path.equals(this.path));
        } else {
            System.out.println("other == null");
            return false;
        }

    }
}
