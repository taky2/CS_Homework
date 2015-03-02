import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.event.ActionEvent;

/*
 * This class controls the layout of the application and menubar 
 * 
 *      Requires A5_MenuFrame.fxml
 */
public class A5_MenuFrame {
	//Reference to main application
	private SearchEngine searchEngine;

//	@FXML private MenuItem fileCloseMenuItem;

	//Handle fileCloseMenuClick
	public void menuFileClose(ActionEvent event) {
		System.exit(0);
	}

	public void setSearchEngine(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}
}//end MenuFrame
