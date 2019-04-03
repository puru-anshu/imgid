package com.anshuman.imageprocessing.imageid.mp7;

//
// This is a basic image processing utility written in Java
// Written by Paul Browne DCU, Dublin Ireland
//
// It should be fairly easy to understand and modify. Useful for image retrieval, search
// video retrieval and search
//
// open_image(String thefilename) <<<< this opens the image and gets the pixels
// get_the_width() << gets the image width
// get_the_height() <<
//
//
// detectEdge()  << do edge detection on image, image is smoothed, dynamic threshold calculated and edges found on X and Y axis
// get_edges() << returns a 2 dimensional array of boolean values (use get_the_width() , get_the_height() to ensure large enough array
// getEdgeShapes() << returns 144 int array with edge histogram shapes
//
// colorHistorgram()  << process colour histogram  (hue histogram , 18 * 4 regions just colour component
// int getColorHistogram()  <<  get 75 bin int hue array
// float getColorAverage() << get global image RGB average
// float getRegionRedAvg() << get red region avg
// float getRegionGreenAvg() << get green region avg
// float getRegionBlueAvg() << get blue region avg
//
//
// For Video Shot Boundary or scene detection +++++++++++++++
// Extract a frame every second or n seconds
// When all frames have been extracted
// Run processing methods to get colour and edge information
// save result data to file
// Open text file and compare image values, when your threshold is passed register a shot change
//
// See main() for an example of usage
//
/////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.awt.image.*;

public class Mp7ImageProcessor {

    private int image_width = 0;
    private int image_height = 0;
    private int region_width = 0;
    private int region_height = 0;
    private BufferedImage image1;
    private int[] thepixels;

    private int edge_luminance[][];
    private boolean edge_array[][];
    private int current_y_pos, current_x_pos;
    private Color m_clrColor;
    private int theblue, thered, thegreen;
    private int the_hue = 0;
    private int the_sat = 0;
    private int dlum = 0;
    private int pcounterx, pcountery;
    private int edge_total;
    double svalue;

    private int hue_array[] = new int[75];
    private int edge_shapes[] = new int[144];

    private int themonth, theyear, theday, thehour, theminute, thesecond = 0;
    private int counter, counterb;

    /*
        private Date myCalender;
        private FileWriter p_out;
        private PrintWriter out;
        private FileReader p_in;
        private BufferedReader b_in;
        private int largest_red, largest_green, largest_blue, index_largest_red, index_largest_green, index_largest_blue = 0;
    */
    private float[] red_region_average = new float[9];
    private float[] green_region_average = new float[9];
    private float[] blue_region_average = new float[9];
    private int[][] red_region_largest = new int[9][26];
    private int[][] green_region_largest = new int[9][26];
    private int[][] blue_region_largest = new int[9][26];
    /*
        private int[] red_largest = new int[9];
        private int[] green_largest = new int[9];
        private int[] blue_largest = new int[9];
    */
/*
    private int[] red_largest_index = new int[9];
    private int[] green_largest_index = new int[9];
    private int[] blue_largest_index = new int[9];

*/
    private int hue_working = 0;
    //    private String tempString;
    private int EdgeThreshold = 20;
    //    private String toreturn;
//    private int NUMBER_TO_READ;
    private int the_x = 0, the_y = 0;
    private float ptotal_red = 0, ptotal_green = 0, ptotal_blue = 0, ptotal_luminance = 0;
    private double max, min, r, g, b, h, s, v, delta;
    private int theregion = 0;


    public int get_the_width() {
        return image_width;
    }

    public int get_the_height() {
        return image_height;
    }


    public Mp7ImageProcessor(BufferedImage image1) {
        this.image1 = image1;
        initialize_data();
        image_width = image1.getHeight(null);
        image_height = image1.getWidth(null);
        System.out.println(image_width + " X  " + image_height);
        PixelGrabber mygrab = new PixelGrabber(image1, 0, 0, image_width, image_height, true);
        mygrab.startGrabbing();
        thepixels = new int[(image_width * image_height)];

        edge_luminance = new int[image_height][image_width];
        edge_array = new boolean[image_width][image_height];

        region_width = (image_width / 3);
        region_height = (image_height / 3);

        try {

            if (!mygrab.grabPixels(5000)) {
            } else {
                thepixels = (int[]) mygrab.getPixels();
            }

        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


    }// open image, get width and heights


    // now that the image has been loaded perform edge detection
    public void detectEdge() {
        current_x_pos = 0;
        current_y_pos = 0;
        for (int countp = 0; countp < (image_width * image_height - 1); countp++) {
            m_clrColor = new Color(thepixels[countp]);
            theblue = m_clrColor.getBlue();
            thegreen = m_clrColor.getGreen();
            thered = m_clrColor.getRed();
            dlum = (int) ((0.299 * thered) + (0.587 * thegreen) + (0.114 * theblue));
            edge_luminance[current_y_pos][current_x_pos] = dlum;

            //System.out.print(""+current_x_pos+" "+current_y_pos+",");

            if (current_x_pos == (image_width - 1)) {
                current_y_pos++;
                current_x_pos = 0;
            } else {
                current_x_pos++;
            }

        }

        /////////////////////////////////////////////////////////
        //
        // Edge Histogram Detection
        //
        ///////////////////////////////////////////////////////

        // first remove noise from the image emphising edges
        // This is smoothing the image using a matrix operation

        for (pcountery = 1; pcountery < (image_height - 1); pcountery++) {
            for (pcounterx = 1; pcounterx < (image_width - 1); pcounterx++) {
                svalue = ((edge_luminance[pcountery - 1][pcounterx - 1] * .0625) + (edge_luminance[pcountery - 1][pcounterx] * .125) + (edge_luminance[pcountery - 1][pcounterx + 1] * .0625) + (edge_luminance[pcountery][pcounterx - 1] * .125) + (edge_luminance[pcountery][pcounterx] * .25) + (edge_luminance[pcountery][pcounterx + 1] * .125) + (edge_luminance[pcountery + 1][pcounterx - 1] * .0625) + (edge_luminance[pcountery + 1][pcounterx] * .125) + (edge_luminance[pcountery + 1][pcounterx + 1] * .0625));
                edge_luminance[pcountery][pcounterx] = (int) Math.round(svalue);
            }
        }

        for (pcountery = 1; pcountery < (image_height - 1); pcountery++) {
            for (pcounterx = 1; pcounterx < (image_width - 1); pcounterx++) {
                svalue = ((edge_luminance[pcountery - 1][pcounterx - 1] + edge_luminance[pcountery - 1][pcounterx] + edge_luminance[pcountery - 1][pcounterx + 1] + edge_luminance[pcountery][pcounterx - 1] + edge_luminance[pcountery][pcounterx] + edge_luminance[pcountery][pcounterx + 1] + edge_luminance[pcountery + 1][pcounterx - 1] + edge_luminance[pcountery + 1][pcounterx] + edge_luminance[pcountery + 1][pcounterx + 1]) / 9);
                edge_luminance[pcountery][pcounterx] = (int) Math.round(svalue);
            }
        }

        // This part is a dynamic threshold which is
        // adjusted depending on the number of edges found in the image

        edge_total = 0;
        int edge_total_counter = 0;
        while (edge_total < 11000 || edge_total > 28000) {

            edge_total = 0;
            edge_total_counter++;

            for (pcountery = 0; pcountery < (image_height - 1); pcountery++) {
                for (pcounterx = 0; pcounterx < (image_width - 1); pcounterx++) {
                    if (edge_luminance[pcountery + 1][pcounterx] - edge_luminance[pcountery][pcounterx] >= EdgeThreshold || edge_luminance[pcountery][pcounterx] - edge_luminance[pcountery + 1][pcounterx] >= EdgeThreshold) {
                        edge_total++;
                    } else
                    if (edge_luminance[pcountery][pcounterx + 1] - edge_luminance[pcountery][pcounterx] >= EdgeThreshold || edge_luminance[pcountery][pcounterx] - edge_luminance[pcountery][pcounterx + 1] >= EdgeThreshold) {
                        edge_total++;
                    }

                }
            }

            if (edge_total > 28000) {
                EdgeThreshold = EdgeThreshold + 2;
            } else if (edge_total < 11000) {
                EdgeThreshold = EdgeThreshold - 2;
            }


            if (edge_total_counter > 6) {
                edge_total = 16000;
            } else if (EdgeThreshold < 6) {
                EdgeThreshold = 6;
                edge_total = 16000;
            }

        }

        if (EdgeThreshold < 6) {
            EdgeThreshold = 6;
        }

        edge_total = 0;
        counter = 0;

        // Find Edges in Image with threshold
        //

        for (pcountery = 0; pcountery < (image_height - 1); pcountery++) {

            for (pcounterx = 0; pcounterx < (image_width - 1); pcounterx++) {
                edge_array[pcounterx][pcountery] = false;

                if ((edge_luminance[pcountery][pcounterx] - edge_luminance[pcountery + 1][pcounterx]) >= EdgeThreshold || (edge_luminance[pcountery + 1][pcounterx] - edge_luminance[pcountery][pcounterx]) >= EdgeThreshold) {
                    edge_array[pcounterx][pcountery] = true;
                } else
                if ((edge_luminance[pcountery][pcounterx] - edge_luminance[pcountery][pcounterx + 1]) >= EdgeThreshold || (edge_luminance[pcountery][pcounterx + 1] - edge_luminance[pcountery][pcounterx]) >= EdgeThreshold) {
                    edge_array[pcounterx][pcountery] = true;
                }
            }
        }
    }

//
// get detected edges  (returns boolean values)

    public boolean[][] get_edges() {
        return edge_array;
    }

// This method gets a type of edge histogram
// 9 * 9 region, 16 basic shapes are searched

    public int[] getEdgeShapes() {

        int countx = 0;
        int county = 0;

        for (county = 0; county < (image_height - 8); county++) {
            for (countx = 0; countx < (image_width - 8); countx++) {
                // edge_array[countx+1][county+7] = false;
                // Find lines sloping down from right to left
                //

                if (!edge_array[countx][county] &&
                        !edge_array[countx + 2][county] &&
                        edge_array[countx + 3][county]
                        && edge_array[countx + 2][county + 2] &&
                        edge_array[countx + 1][county + 4] &&
                        edge_array[countx][county + 6] &&
                        !edge_array[countx][county + 1]
                        && !edge_array[countx][county + 2] &&
                        !edge_array[countx][county + 3] &&
                        !edge_array[countx + 3][county + 6] &&
                        !edge_array[countx + 3][county + 5] &&
                        !edge_array[countx + 3][county + 4] &&
                        !edge_array[countx + 3][county + 3] &&
                        !edge_array[countx + 2][county + 6]) {
                    edge_shapes[(whatRegion(countx, county) * 16)]++;
                }

                // Find lines sloping down from left to right
                //

                if (edge_array[countx][county] &&
                        edge_array[countx + 1][county + 2] &&
                        edge_array[countx + 2][county + 4] &&
                        edge_array[countx + 3][county + 6] &&
                        !edge_array[countx + 3][county] &&
                        !edge_array[countx + 2][county] &&
                        !edge_array[countx + 3][county + 1] &&
                        !edge_array[countx + 3][county + 2] &&
                        !edge_array[countx + 3][county + 3] &&
                        edge_array[countx][county + 6] == false &&
                        edge_array[countx + 1][county + 6] == false &&
                        edge_array[countx][county + 5] == false &&
                        edge_array[countx][county + 4] == false &&
                        edge_array[countx][county + 3] == false) {
                    edge_shapes[1 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like \/
                //

                if (edge_array[countx][county] && edge_array[countx + 4][county] && edge_array[countx][county + 2] == false && edge_array[countx + 4][county + 2] == false && edge_array[countx + 1][county + 1] && edge_array[countx + 2][county + 2] && edge_array[countx + 3][county + 1] && edge_array[countx + 2][county] == false) {
                    edge_shapes[2 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like /\
                //

                if (!edge_array[countx][county] && edge_array[countx + 4][county] == false && edge_array[countx][county + 2] && edge_array[countx + 4][county + 2] && edge_array[countx + 1][county + 1] && edge_array[countx + 2][county] && edge_array[countx + 3][county + 1] && edge_array[countx + 2][county + 2] == false) {
                    edge_shapes[3 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like <
                //

                if (edge_array[countx][county] == false && edge_array[countx][county + 4] == false && edge_array[countx][county + 2] && edge_array[countx + 1][county + 1] && edge_array[countx][county + 2] && edge_array[countx + 1][county + 3] && edge_array[countx + 2][county + 4] && edge_array[countx + 2][county + 2] == false) {
                    edge_shapes[4 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like >
                //

                if (edge_array[countx][county] && edge_array[countx][county + 4] && edge_array[countx + 1][county + 1] && edge_array[countx + 2][county + 2] && edge_array[countx + 1][county + 3] && edge_array[countx + 2][county] == false && edge_array[countx + 2][county + 4] == false && edge_array[countx][county + 2] == false) {
                    edge_shapes[5 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like    //
                //  		  --

                if (edge_array[countx][county] == false && edge_array[countx + 1][county] == false && edge_array[countx][county + 3] && edge_array[countx + 1][county] == false && edge_array[countx + 2][county + 1] && edge_array[countx + 1][county + 2] && edge_array[countx + 3][county + 3] == false && edge_array[countx][county + 3]) {
                    edge_shapes[6 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like   \\
                //  		   --

                if (edge_array[countx][county] && edge_array[countx + 2][county] == false && edge_array[countx + 3][county] == false && edge_array[countx + 1][county + 1] && edge_array[countx + 3][county + 1] == false && edge_array[countx + 2][county + 2] && edge_array[countx][county + 3] == false && edge_array[countx + 3][county + 3]) {
                    edge_shapes[7 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like   __
                //  		    --

                if (edge_array[countx][county] == false && edge_array[countx + 2][county] && edge_array[countx + 3][county] && edge_array[countx][county + 1] && edge_array[countx + 1][county + 1] && edge_array[countx + 3][county + 1] == false && edge_array[countx][county + 2] == false && edge_array[countx + 1][county + 2] == false && edge_array[countx + 3][county + 2] == false) {
                    edge_shapes[8 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like    __
                //  		 --

                if (edge_array[countx][county] && edge_array[countx + 1][county] && edge_array[countx + 3][county] == false && edge_array[countx][county + 1] == false && edge_array[countx + 2][county + 1] && edge_array[countx + 3][county + 1] && edge_array[countx][county + 2] == false && edge_array[countx + 2][county + 2] == false && edge_array[countx + 3][county + 2] == false) {
                    edge_shapes[9 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like    _
                //  		  |

                if (edge_array[countx][county] && edge_array[countx + 1][county] && edge_array[countx + 2][county] && edge_array[countx + 3][county] && edge_array[countx][county + 1] && edge_array[countx][county + 2] && edge_array[countx][county + 3] && edge_array[countx + 1][county + 3] == false && edge_array[countx + 3][county + 3] == false && edge_array[countx + 1][county + 1] == false && edge_array[countx + 3][county + 1] == false && edge_array[countx + 2][county + 2] == false) {
                    edge_shapes[10 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like   _
                //  		   |

                if (edge_array[countx][county] && edge_array[countx + 1][county] && edge_array[countx + 2][county] && edge_array[countx + 3][county] && edge_array[countx][county + 1] == false && edge_array[countx + 2][county + 1] == false && edge_array[countx + 3][county + 1] && edge_array[countx + 1][county + 2] == false && edge_array[countx + 3][county + 2] && edge_array[countx][county + 3] == false && edge_array[countx + 2][county + 3] == false && edge_array[countx + 3][county + 3]) {
                    edge_shapes[11 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like
                //  		  |_

                if (edge_array[countx][county] && edge_array[countx][county + 1] && edge_array[countx][county + 2] && edge_array[countx][county + 3] && edge_array[countx + 1][county + 3] && edge_array[countx + 2][county + 3] && edge_array[countx + 3][county + 3] && edge_array[countx + 1][county] == false && edge_array[countx + 3][county] == false && edge_array[countx + 2][county + 1] == false && edge_array[countx + 1][county + 2] == false && edge_array[countx + 3][county + 2] == false) {
                    edge_shapes[12 + (whatRegion(countx, county) * 16)]++;
                }

                // find pixels like
                //  		  _|


                if (edge_array[countx + 3][county] && edge_array[countx + 3][county + 1] && edge_array[countx + 3][county + 2] && edge_array[countx + 3][county + 3] && edge_array[countx][county + 3] && edge_array[countx + 1][county + 3] && edge_array[countx + 2][county + 3] && edge_array[countx][county] == false && edge_array[countx + 2][county] == false && edge_array[countx + 1][county + 1] == false && edge_array[countx + 2][county] == false && edge_array[countx + 2][county + 2] == false) {
                    edge_array[countx + 3][county] = false;
                    edge_array[countx + 3][county + 1] = false;
                    edge_array[countx + 3][county + 2] = false;
                    edge_array[countx + 3][county + 3] = false;
                    edge_array[countx][county + 3] = false;
                    edge_array[countx + 1][county + 3] = false;
                    edge_array[countx + 2][county + 3] = false;
                    edge_array[countx][county] = false;
                    edge_array[countx + 2][county + 2] = false;
                    edge_shapes[13 + (whatRegion(countx, county) * 16)]++;
                }

                // Find pixels like --------
                //

                if (edge_array[countx][county] && edge_array[countx + 1][county] && edge_array[countx + 2][county] && edge_array[countx + 3][county] && edge_array[countx + 4][county] && edge_array[countx + 5][county] && edge_array[countx + 6][county] && edge_array[countx + 7][county] && edge_array[countx][county + 3] == false && edge_array[countx + 3][county + 3] == false && edge_array[countx + 6][county + 3] == false) {
                    edge_array[countx][county] = false;
                    edge_array[countx][county + 1] = false;
                    edge_array[countx + 1][county] = false;
                    edge_array[countx + 1][county + 1] = false;
                    edge_array[countx + 2][county] = false;
                    edge_array[countx + 2][county + 1] = false;
                    edge_array[countx + 3][county] = false;
                    edge_array[countx + 3][county + 1] = false;
                    edge_array[countx + 4][county] = false;
                    edge_array[countx + 4][county + 1] = false;
                    edge_array[countx + 5][county] = false;
                    edge_array[countx + 5][county + 1] = false;
                    edge_array[countx + 6][county] = false;
                    edge_array[countx + 6][county + 1] = false;
                    edge_array[countx + 7][county] = false;
                    edge_array[countx + 7][county + 1] = false;
                    edge_shapes[14 + (whatRegion(countx, county) * 16)]++;
                }

                // Find pixels like      |
                //   		     |

                if (edge_array[countx][county] && edge_array[countx][county + 1] && edge_array[countx][county + 2] && edge_array[countx][county + 3] && edge_array[countx][county + 4] && edge_array[countx][county + 5] && edge_array[countx][county + 6] && edge_array[countx][county + 7] && edge_array[countx + 3][county] == false && edge_array[countx + 3][county + 3] == false && edge_array[countx + 3][county + 6] == false) {
                    edge_array[countx][county] = false;
                    edge_array[countx + 1][county] = false;
                    edge_array[countx][county + 1] = false;
                    edge_array[countx + 1][county + 1] = false;
                    edge_array[countx][county + 2] = false;
                    edge_array[countx + 1][county + 2] = false;
                    edge_array[countx][county + 3] = false;
                    edge_array[countx + 1][county + 3] = false;
                    edge_array[countx][county + 4] = false;
                    edge_array[countx + 1][county + 4] = false;
                    edge_array[countx][county + 5] = false;
                    edge_array[countx + 1][county + 5] = false;
                    edge_array[countx][county + 6] = false;
                    edge_array[countx + 1][county + 6] = false;
                    edge_array[countx][county + 7] = false;
                    edge_array[countx + 1][county + 7] = false;
                    edge_shapes[15 + (whatRegion(countx, county) * 16)]++;
                }

            }

        }

        return edge_shapes;
    }

    int whatRegion(int thex, int they) {
        int valx = (thex / region_width);
        int valy = (they / region_height);

        if (valx == 0 && valy == 0) {
            return 0;
        } else if (valx == 1 && valy == 0) {
            return 1;
        } else if (valx == 2 && valy == 0) {
            return 2;
        } else if (valx == 0 && valy == 1) {
            return 3;
        } else if (valx == 1 && valy == 1) {
            return 4;
        } else if (valx == 2 && valy == 1) {
            return 5;
        } else if (valx == 0 && valy == 2) {
            return 6;
        } else if (valx == 1 && valy == 2) {
            return 7;
        } else if (valx == 2 && valy == 2) {
            return 8;
        }

        return 0;
    }

// Colour histogram
//  HUE (0-360)  each colour band is 18 * 4 regions = 72 bins + extra unsaturated colour bins

    public void colorHistorgram() {
        int working;

        for (int countp = 0; countp < (image_width * image_height); countp++) {

            m_clrColor = new Color(thepixels[countp]);
            theblue = m_clrColor.getBlue();
            thegreen = m_clrColor.getGreen();
            thered = m_clrColor.getRed();

            if (current_x_pos == image_width) {
                current_y_pos++;
                current_x_pos = 0;
            } else {
                current_x_pos++;
            }

            // colour histogram , HUE (0-360)  each colour band is 18 , 9 regions
            hue_working = rgbToHsv(thered, thegreen, theblue);
            hue_working = hue_working / 20;
            hue_array[(whatSector(current_x_pos, current_y_pos) * 18) + hue_working]++;

            // Regional colour histograms for avg and largest calculation
            ptotal_red += thered;
            ptotal_green += thegreen;
            ptotal_blue += theblue;

            theregion = whatRegion(current_x_pos, current_y_pos);
            red_region_average[theregion] += thered;
            blue_region_average[theregion] += theblue;
            green_region_average[theregion] += thegreen;

            working = (thered / 10);
            red_region_largest[theregion][working]++;
            working = (thegreen / 10);
            green_region_largest[theregion][working]++;
            working = (theblue / 10);
            blue_region_largest[theregion][working]++;

        } // end pixel grab

        // Print the global and 9 regional image colour histograms.
        ptotal_blue = (ptotal_blue / (image_width * image_height));
        ptotal_red = (ptotal_red / (image_width * image_height));
        ptotal_green = (ptotal_green / (image_width * image_height));

        for (counter = 0; counter < 9; counter++) {
            red_region_average[counter] = red_region_average[counter] / (image_width * image_height);
            green_region_average[counter] = green_region_average[counter] / (image_width * image_height);
            blue_region_average[counter] = blue_region_average[counter] / (image_width * image_height);
        }

    } // end colour histogram


    public int[] getColorHistogram() {
        return hue_array;
    }

    public float[] getColorAverage() {
        float[] colour_averages = new float[3];
        colour_averages[0] = ptotal_red;
        colour_averages[1] = ptotal_green;
        colour_averages[2] = ptotal_blue;

        return colour_averages;
    }

    public float[] getRegionRedAvg() {
        return red_region_average;
    }

    public float[] getRegionGreenAvg() {
        return green_region_average;
    }

    public float[] getRegionBlueAvg() {
        return blue_region_average;
    }


    private int rgbToHsv(int red, int green, int blue) {

        r = red / 255.0;
        g = green / 255.0;
        b = blue / 255.0;
        h = the_hue;

        max = Math.max(Math.max(r, g), b);
        min = Math.min(Math.min(r, g), b);
        v = max;

        if (max != 0.0) {
            s = (max - min) / max;
        } else {
            s = 0.0;
        }

        if (s == 0.0) {

        } else {
            delta = (max - min);

            if (r == max) {
                h = (g - b) / delta;
            } else if (g == max) {
                h = 2.0 + (b - r) / delta;
            } else if (b == max) {
                h = 4.0 + (r - g) / delta;
            }

            h *= 60.0;

            while (h < 0.0) {
                h += 360.0;
            }
        }

        the_hue = (int) (h);
        the_sat = (int) (s * 255.0);

        if (the_sat < 18) {
            the_hue = 360;
        }

        return the_hue;
    }

    int whatSector(int x, int y) {
        if (x < (image_width / 2) && y < (image_height / 2)) {
            return 0;
        } else if (x > (image_width / 2) && y < (image_height / 2)) {
            return 1;
        } else if (x < (image_width / 2) && y > (image_height / 2)) {
            return 2;
        } else {
            return 3;
        }
    }


    private void initialize_data() {
        ptotal_red = 0;
        ptotal_green = 0;
        ptotal_blue = 0;
        ptotal_luminance = 0;
        current_x_pos = 0;
        current_y_pos = 0;
        red_region_average = new float[9];
        green_region_average = new float[9];
        blue_region_average = new float[9];
        red_region_largest = new int[9][26];
        green_region_largest = new int[9][26];
        blue_region_largest = new int[9][26];
        the_x = 0;
        the_y = 0;

        for (counter = 0; counter < 75; counter++) {
            hue_array[counter] = 0;
        }

        for (counter = 0; counter < 144; counter++) {
            edge_shapes[counter] = 0;
        }

    }

} // end Mp7ImageProcessor class