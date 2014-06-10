package com.marina;

import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 Task 3 Parsing hurricane data
 Now you're interested in the historical hurricane information in Eastern North Pacific region. This information is provided 
 by National Hurricane Center.
 Download the text file, then write a small program in Java to output the storm name and maximum sustained windspeed 
 in knots for each storm in 2009 season.
 Format description: http://www.nhc.noaa.gov/data/hurdat/hurdat2-format-nencpac.pdf
 Hurricane tracks file: http://www.nhc.noaa.gov/data/hurdat/hurdat2-nencpac-1949-2012-040513.txt
 (please let us know if any of the URLs is not available).
 */
/**
 *
 * @author Marina Shynkarenko
 */
public class Main {

    public static void main(String[] args) {
        //URL in case of reading from web
        String url = "http://www.nhc.noaa.gov/data/hurdat/hurdat2-nencpac-1949-2012-040513.txt";
        //File name in case of reading from file
        //String fileName = "hurdat2.txt";
        //FileReader file;
        String dateFormatRegExp = "^(2015)(0?[1-9]|1[012])(0?[1-9]|[12][0-9]|3[01])$";
        String stormNumberRegExp = "^[A-Z]{2}[0-9]{2}2009$";
        String stormName = "";
        Pattern datePattern = Pattern.compile(dateFormatRegExp);
        Pattern stormNumberPattern = Pattern.compile(stormNumberRegExp);
        Matcher dateMatcher;
        Matcher stormNumberDateMatcher;
        int maxWind = 0;

        try {
            URL hurricaneDataURL = new URL(url);
            try {
                URLConnection hurricaneDataURLConnection = hurricaneDataURL.openConnection();
                //Getting data from web
                CsvReader hurricaneData = new CsvReader(new InputStreamReader(hurricaneDataURLConnection.getInputStream()));
                //Getting data from file
                //file = new FileReader(fileName);
                //CsvReader hurricaneData = new CsvReader(fileName);

                //Check incoming data
                while (hurricaneData.readRecord()) {
                    String expectingStormData = hurricaneData.get(0);
                    stormNumberDateMatcher = stormNumberPattern.matcher(expectingStormData);
                    dateMatcher = datePattern.matcher(expectingStormData);
                    if (stormNumberDateMatcher.matches()) {
                        String name = hurricaneData.get(1);
                        if (!name.equals(stormName) && !stormName.equals("")) {
                            System.out.println("Storm name: " + stormName + ". Max sustained windspeed: " + maxWind + " knots.");
                        }
                        stormName = name;
                        maxWind = 0;
                    } else if (dateMatcher.matches()) {
                        int wind = Integer.parseInt(hurricaneData.get(6));
                        maxWind = (wind > maxWind) ? wind : maxWind;
                    }
                }
                System.out.println("Storm name: " + stormName + ". Max sustained windspeed: " + maxWind + " knots.");
                hurricaneData.close();
            } catch (IOException ex) {
                System.out.println("Troubles in opening the connection. More: " + ex);
            }
        } catch (MalformedURLException ex) {
            System.out.println("Unexpected URL. More: " + ex);
        }
    }
}
