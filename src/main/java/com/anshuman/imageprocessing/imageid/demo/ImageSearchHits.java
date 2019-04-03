package com.anshuman.imageprocessing.imageid.demo;


/**
 * User: Anshuman
 * Date: Sep 24, 2010
 * Time: 1:17:05 PM
 */
public class ImageSearchHits {

    /**
     * An array of Result objects
     */
    public Result[] rankList;

    /**
     * The last index in the rankList attribute, which is currently null
     */
    public int lastNullIndex;

    /**
     * The maximum allowed size for the ranking list
     */
    int maxSize;


    /**
     * Constructs an empty ResultRankingList object having size equal to the one specified
     *
     * @param size the maximum allowed size of the ranking list
     */
    public ImageSearchHits(int size) {
        this.rankList = new Result[size];
        this.lastNullIndex = 0;
        this.maxSize = size;
    }

    /**
     * Adds the given Result object to its sorted position, according to distance,
     * and shifts all other results downwards. The worst result is removed, if the
     * maximum number of results in the list has been reached. If the given result
     * is not better than any other result in the list, and the max size has been
     * reached, the given result is ignored.
     *
     * @param theNewResult the Result object to add to the list
     * @return the number of results currently in the list
     */
    public int RankThis(Result theNewResult) {
        Result currentResult;

        if (lastNullIndex == 0) {
            rankList[0] = theNewResult;
            lastNullIndex++;
        } else {
            for (int i = 0; i < maxSize; i++) {
                currentResult = rankList[i];
                if (currentResult == null) {
                    //shift all results downwards, deleting the last one
                    for (int j = lastNullIndex - 1; j > i; j--) {
                        rankList[j] = rankList[j - 1];
                    }
                    // insert into position i
                    rankList[i] = theNewResult;
                    lastNullIndex++;
                    return lastNullIndex;
                }
                if (theNewResult.val <= currentResult.val) {
                    if (lastNullIndex == maxSize) {
                        for (int j = lastNullIndex - 1; j > i; j--) {
                            rankList[j] = rankList[j - 1];
                        }
                        // insert into position i
                        rankList[i] = theNewResult;
                    } else {
                        for (int j = lastNullIndex; j > i; j--) {
                            rankList[j] = rankList[j - 1];
                        }
                        // insert into position i
                        rankList[i] = theNewResult;
                        lastNullIndex++;
                    }
                    return lastNullIndex;
                }
            }
        }
        return lastNullIndex;
    }

    /**
     * Returns the ranking list
     *
     * @return rankList the ranking list
     */
    public Result[] getRankList() {
        return rankList;
    }

    /**
     * Returns the size that the ranking list has been initialized to
     *
     * @return the ranking list's initialization size
     */
    public int getSize() {
        return rankList.length;
    }


    /**
     * Returns the Result object located in the given index. If the given index
     * is larger than the index of the last known valid entry, null is returned.
     *
     * @param index the index (starting at zero) of the wanted Result object
     * @return the Result object stored in the ranking index specified
     */
    public Result getResultAtIndex(int index) {
        if (index <= lastNullIndex) return rankList[index];
        else return null;
    }


    public String getPath(int row) {
        Result index = getResultAtIndex(row);
        if (null != index)
            return index.path;
        return null;
    }

    public double score(int row) {
        Result index = getResultAtIndex(row);
        if (null != index)
            return index.val;
        return 1.0d;
    }

    public int length() {
        return lastNullIndex;
    }

   
}
