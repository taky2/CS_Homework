/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * This program take a website's url, builds an index for that page, and builds an 
 * index for for pages linked within that page. No more than 30 pages altogether will
 * be indexed. Once the index is built, the program can be used to search through the
 * list of indexed URLs whos pages match the user's search term. 
 *
 *
 * WARNINGS:    This application uses JavaFX and requires the latest version of Java 
 *              in order to insure proper functionality. Java8 recommended. 
 *
 *              EXTERNAL LIBRARY USED TO VALIDATE URL INPUT FROM USER:
 *
 *              The Apache Commons Validator Library is REQUIRED. Please include
 *              in the source directory. Download can be found at
 *                  
 *              http://commons.apache.org/proper/commons-validator/
 *
 *              PLEASE NOTE: Source directory should also include a file containing
 *              preferred stopwords. Filename is currently set to 'stopwords.txt'
 *
 *  Requires 8 additional files to run: A5_Keyword.java, A5_KeywordNode.java, A5_MenuFrame.java,
 *                                      A5_MenuFrame.fxml, A5_Page.java, A5_SearchEngineGUI.java,
 *                                      A5_SearchEngineGUI.fxml, and A5_URLs.java
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import static javafx.application.Application.STYLESHEET_MODENA;
import static javafx.application.Application.launch;
import static javafx.application.Application.setUserAgentStylesheet;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.commons.validator.routines.*; // Import URL Handler Library (external lib)

public class A5_SearchEngine extends Application{

    private String[] stopWords;         // List of words to ignore (input from file)
    private final int MAXSITES = 30;    // Maximum number of sites to index
    private int siteCount = 0;          // Counter for number of sites indexed
    private int UrlCount = 0;           // Counter for number of URLs found on given site.
    private ArrayList<String> urls;     // Arraylist of URLs
    private boolean stopwordsLoaded;    // Flag to know if stopwords.txt loaded properly.  Will run regardless
    private KeywordNode keyList;        // List of keywords from user
    private Page p;                     // Page object to handle indexing
    private Stage stageOne;             // JavaFX Primary stage
    private BorderPane menuFrame;       // Windowpane to impliment a file menu

    /**
     * Launches the program
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Sets the JavaFX stage
     * @param stageOne 
     */
    @Override
    public void start(Stage stageOne) {
        this.stageOne = stageOne;
        this.stageOne.setTitle("URL Search Tool");
        setUserAgentStylesheet(STYLESHEET_MODENA);

        initializeFrameGUI();
        showMenu();
        initializeStopwords();
    }//end Start()

    /**
     * Method to utilize MenuFrame.fxml.
     */
    private void initializeFrameGUI() {
        try {
            //Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SearchEngine.class.getResource("MenuFrame.fxml"));
            menuFrame = (BorderPane) loader.load();

            //Show the scene containing the root layout
            Scene scene = new Scene(menuFrame);
            stageOne.setScene(scene);
            stageOne.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end initializeFrameGUI()

    /**
     * Method to utilize SearchEngine.fxml.
     */
    private void showMenu() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(SearchEngine.class.getResource("SearchEngineGUI.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();

            //Set SearchEngine to the center of menuFrame
            menuFrame.setCenter(anchorPane);

            // Give the controller access to searchEngine 
            SearchEngineGUI mainGUI = loader.getController();
            mainGUI.setSearchButtonEnable(false);
            mainGUI.setSearchEngine(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//end showMenu()

    /**
     * Method to initialize stopwords list.
     */
    private void initializeStopwords() {
        // Initiate list of stopwords to ignore
        File stopFile = new File("stopwords.txt");
        try {
            BufferedReader getline = new BufferedReader(new FileReader(stopFile));
            Scanner fileScanner = new Scanner(getline);
            stopWords = fileScanner.nextLine().split(",");
            stopwordsLoaded = true;
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find stopwords.txt, make sure it's in same directory as program. " 
                               + "Running without stopwords for now.");
            // Updates result of stopwords load
            stopwordsLoaded = false;    
        }
    }//end initializeStopwords()

    /**
     * Checks to see if a keyword is a stopword. 
     * @param key String of key to check against stopwords list.
     * @return Returns true if keyword is valid, returns false if keyword on stopword list.
     */
    public boolean isValidKey(String key) {
        for (int i = 0; i < stopWords.length; i++) {
            if (key.equalsIgnoreCase(stopWords[i])) {
                return false;
            }
        }
        return true;
    }//end isValidKey
    
    /**
     * Method loads given website into Page class.
     * @param userInputUrl String value of URL to load.
     */
    public void loadWebsite(String userInputUrl) {
        keyList = new KeywordNode();
        p = null;                       // Initializes p.
        urls = new ArrayList<String>();
        userInputUrl = formatForUrl(userInputUrl);

        try {
            System.out.println("Url inputting: " + userInputUrl);
            p = new Page(userInputUrl);
        } catch (Exception e) {
            System.out.println("Error: not acceptable input. (WARNING: https not supported)");
            System.exit(0); //TODO: Handle this better
        }
        if (p.pageDone()) {
            System.out.println("Page loaded");
        }
        while (p != null && !p.pageDone()) {
            while (p != null && !p.pageDone()) {
                String pLine = p.getLine();

                // Clean up pLine and remove HTML tags
                pLine = cleanHTML(pLine);

                // Break up each word by " " whitespace
                String[] pLineArr = pLine.split(" ");

                // Loop through line and add to keyword list
                for (int i = 0; i < pLineArr.length; i++) {
                    pLineArr[i] = cleanString(pLineArr[i]);
                    pLineArr[i] = pLineArr[i].replaceAll(" ", ""); // Get rid of whitespace
                    if (isValidKey(pLineArr[i])) { // If it's not a stopword
                        keyList.append(pLineArr[i], p.getName());
                    }
                }
            }
            ArrayList<String> links = p.getLinks();
            for (int i = 0; i < links.size(); i++) {
                urls.add(links.get(i));
            }
            if (++UrlCount >= urls.size()) {
                p = null;
            } else {
                do {
                    try {
                        p = new Page(urls.get(UrlCount));
                    } catch (Exception e) { // Bad url - ignore
                        UrlCount++;
                    }
                } while (UrlCount < urls.size() && p == null);
            }
            if (++siteCount >= MAXSITES) break;
        }//end while
    }// end loadWebsite()

    /**
     * Method resets SearchEngine to allow for new URL to be indexed.
     */
    public void resetConnection() {
        p = null;               // Clears p to make room for new page
        siteCount = 0;          // Clears site count
        UrlCount = 0;           // Clears urls
        urls.clear();           // Clears List of urls
        keyList = null;         // Clears list of keywords
    }//end resetConnection()

    /**
     * Method to search for a keyword in the KeywordNode
     * @param key String value of Keyword to search for
     * @return Returns a Keyword if found.
     */
    public Keyword search(String key) {
        if (isValidKey(key)) {
            return keyList.search(key.trim());    // Clears whitespace
        } else {
            // A way to handle if the keyword in the list of ignored words
            Keyword k = new Keyword("Search term is in list of stopwords and has been omitted.");
            return k;
        }
    }//end search()

    /**
     * Searches for a keyword - If found added to array.
     * @param keys String[] array of keywords to search for.
     * @return Returns Array of Keywords found.
     */
    public Keyword[] search(String[] keys) {
        Keyword[] toReturn = new Keyword[keys.length];
        for (int i = 0; i < keys.length; i++) {
            toReturn[i] = search(keys[i]);
        }
        return toReturn;
    }//end search()

    /**
     * Checks a string to verify if it is a valid URL.  
     * THIS REQUIRES THE APACHE COMMONS LIBRARY 
     * VISIT http://commons.apache.org/proper/commons-validator/
     * @param url String value of the URL to test
     * @return Returns true if valid, false if not.
     */
    public boolean isValidUrl(String url) {
        UrlValidator defaultValidator = new UrlValidator(); // default schemes
        if (defaultValidator.isValid(url)) {
            return true;
        } else {
            return false;
        }
    }//end isValidUrl

    /**
     * Formats a string for use by Page class. 
     * @param input String input of the url before formatted
     * @return Returns a URL with http:// format.
     */
    public String formatForUrl(String input) {
        if (input.contains("http://")) {
            return input;
        } else {
            return "http://" + input;
        }
    }//end formatForUrl

    /**
     * Cleans up URL by removing HTML.  
     * @param toClean String that may contain unwanted HTML from URLs
     * @return Returns a String without HTML characters
     */
    public String cleanHTML(String toClean) {
        toClean = toClean.replaceAll("\\<.*?\\>", ""); // To replace html tags
        return toClean;
    }//end cleanHTML

    /**
     * Cleans up a string to be inserted in a keyword list.  
     * Removes whitespace, periods, commas, tab spaces, newlines, parenthesis.
     * @param toClean String value of keyword to be cleaned.
     * @return Returns a cleaned up keyword ready for use.
     */
    public String cleanString(String toClean) {
        toClean = toClean.replaceAll(" ", "");      // Remove whitespace
        toClean = toClean.replaceAll("\\.", "");    // Remove period
        toClean = toClean.replaceAll(",", "");      // Remove comma
        toClean = toClean.replaceAll("\t", "");     // Remove tab space
        toClean = toClean.replaceAll("\n", "");     // Remove newlines
        toClean = toClean.replaceAll("[()]", "");   // Remove parenthesis
        return toClean;
    }//end cleanString

    /**
     * Accessor for keyword list.
     * @return Returns keyList from KeywordNode.
     */
    public KeywordNode getKeyList() {
        return keyList;
    }

    /**
     * Accessor for page.
     * @return Returns p from page.
     */
    public Page getPage() {
        return p;
    }

    /**
     * Accessor for urls.
     * @return Returns urls.
     */
    public ArrayList<String> getUrls() {
        return urls;
    }

    /**
     * Boolean flag used to verify if stopwords.txt is loaded correctly.
     * @return Returns true if loaded, false if not.
     */
    public boolean isLoadedStopwords() {
        return stopwordsLoaded;
    }
    
}//end searchEngine class
