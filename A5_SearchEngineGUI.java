import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import java.util.ArrayList;

/*
 * This class controls all the GUI for the main searching functions within the interface 
 * 
 *      Requires SearchEngineGUI.fxml
 */
public class A5_SearchEngineGUI {
    // Reference to Main Application
    private SearchEngine searchEngine;
    // FXML stuff (JAVAFX)
    @FXML private TextField urlTextField;           // Text Field for URL (input)
    @FXML private TextField keyTextField;           // Text Field for keywords (input)
    @FXML private Label connectionStatus;           // Status update to inform user if URL connected correctly
    @FXML private Label stopwordLoadStatus;         // Status update to inform user if stopwords load correctly
    @FXML private Button urlButton;                 // Initiates connection attempt to given website
    @FXML private Button keywordButton;             // Initiates keyword search
    @FXML private TextArea results;                 // Text Field displays results of search (output)

    /**
     * Initiates connection to given URL. 
     * (URL index button)
     * Attempts to connect to the given website.
     * @param event Button click.
     */
    public void urlButton(ActionEvent event) {
            String userInput = urlTextField.getText();
            // Formats string for Page class
            userInput = searchEngine.formatForUrl(userInput);	
            if (searchEngine.isValidUrl(userInput)) {
                    setConnectionStatus("Website is connecting.  Please wait...", 0);
                    searchEngine.loadWebsite(userInput);
                    // Alert user of status for various actions...
                    if (searchEngine.getPage().isConnected()) { 
                            setConnectionStatus("Successfully connected!", 1);
                            urlButton.setDisable(true);
                    }
                    setSearchButtonEnable(true);
                    if (searchEngine.isLoadedStopwords()) {
                            stopwordLoadStatus.setTextFill(Color.GREEN);
                            stopwordLoadStatus.setText("Stopwords loaded.");
                    } else {
                            stopwordLoadStatus.setTextFill(Color.RED);
                            stopwordLoadStatus.setText("Error loading stopwords.txt. Check format (CSV) and directory.");
                    }
            } else {
                    setSearchButtonEnable(false);
                    setConnectionStatus("Not a valid url.", 0);
            }
    }

    /**
     * Method to set connection status label.
     * @param toSet String to display.
     * @param color Color to display:  0 = Red, 1 = Green.
     */
    public void setConnectionStatus(String toSet, int color) {
            if (color == 1) { // If color should be green aka yes
                    connectionStatus.setTextFill(Color.GREEN);
                    connectionStatus.setText(toSet);
            } else if (color == 0) { // If color should be red aka no
                    connectionStatus.setTextFill(Color.RED);
                    connectionStatus.setText(toSet);
            } else {
                    connectionStatus.setTextFill(Color.BLACK);
                    connectionStatus.setText(toSet);
            }
    }

    /**
     * Resets connection when called. 
     * @param event Button click.
     */
    public void resetButton(ActionEvent event) {
            searchEngine.resetConnection();
            reset();
    }

    /**
     * Searches for a keyword in the connected page.  
     * Outputs results in text area.
     * @param event Button click.
     */
    public void keywordButton(ActionEvent event) {
            results.clear();
            if (keyTextField.getText().length() < 3) {
                    results.appendText("Search term must contain more than 2 characters.");
            } else {
                    // This breaks up the user input by whitespace, OR, and commas
                    Keyword[] kMultiple = searchEngine.search(parseUserInput(keyTextField.getText()));
                    for (int i = 0; i < kMultiple.length; i++) {
                            if (kMultiple[i] != null) { // Keyword was found
                                    // If key is in list of ignored words
                                    if (kMultiple[i].getKey().equals("Search term is a stopword and has been omitted.\n\n")) {
                                            results.appendText(kMultiple[i].getKey() + "\n\n");
                                    } else { // Key is not in list of ignored words
                                            String[] kUrls = kMultiple[i].getUrls().getAll();
                                            results.appendText("'" + kMultiple[i].getKey() + "' found " + kUrls.length 
                                                                + " times:\n\n");
                                            for (int j = 0; j < kUrls.length; j++) {
                                                    results.appendText(kUrls[j] + "\n");
                                            }
                                            results.appendText("__________________________________________________");
                                            results.appendText("__________________________________________________\n\n");
                                    }
                            } else {
                                    results.setText("Search term not found.\n");
                            }
                            System.out.println();
                    }
            } // end else
    } // end keywordButton()

    /**
     * Method to split inputs using comma/space separated value and trim whitespace.
     */
    public String[] parseUserInput(String userInput) {
            String[] toReturn;
            if (userInput.contains(",")) {
                    toReturn = userInput.split(",");
            } else if (userInput.contains(" ")){
                    toReturn = userInput.split(" ");
                    for (int i = 0; i < toReturn.length; i++) {
                        toReturn[i] = toReturn[i].trim();		
                    }
            } else {
                    toReturn = new String[1];
                    toReturn[0] = userInput;
            }
            return toReturn;
    }

    /**
     * Method to reset program.
     */
    public void reset() {
            urlButton.setDisable(false);
            keywordButton.setDisable(true);
            connectionStatus.setText("");
            stopwordLoadStatus.setText("");
            urlTextField.clear();
            keyTextField.clear();
            results.clear();
    }

    /**
     * Mutator status updater for search button.
     * @param enabled Boolean of enabled (true) or disabled (false)
     */
    public void setSearchButtonEnable(boolean enabled) {
            keywordButton.setDisable(!enabled);
    }

    /**
     * Mutator reference to searchEngine class.
     * @param searchEngine
     */
    public void setSearchEngine(SearchEngine searchEngine) {
            this.searchEngine = searchEngine;
    }

    /**
     * Mutator to set results text box.
     * @param res Array of Strings to set
     */
    public void setResults(String[] res) {
            for (int i = 0; i < res.length; i++) {
                    results.appendText(res[i] + "\n");
            }
    }

    /**
     * Mutator to set results text box.
     * @param res ArrayList of Strings to set
     */
    public void setResults(ArrayList<String> res) {
            for (int i = 0; i < res.size(); i++) {
                    results.appendText(res.get(i) + "\n");
            }
    }

    /**
     * Mutator to set results text box.
     * @param res Single String to set
     */
    public void setResults(String res) {
            results.appendText(res + "\n");
    }

}//end SearchEngineGUI
