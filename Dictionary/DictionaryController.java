import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.*;

/* The class define dictionary and operations as mention below:
 * 		add - to add new word and definition (no duplicated word).
 * 		delete - to delete word from the dictionary.
 * 		save - to update new definition to word.
 * 		search - to search the definition of the specified word.
 * Moreover, it support to import\export file and input\output respectively.
 * and include GUI to display the dictionary contents.
 */
public class DictionaryController {
	
	private static final String ERR_MSG1 = "No Word Specified! Please Insert Word.";
	private static final String ERR_MSG2 = "No Definition Specified! Please Insert Definition.";
    @FXML
    private Button addButton, deleteButton, saveButton, searchButton;
    @FXML
    private TextArea definitionTextArea, wordTextArea, dictionaryImageTextArea;
    @FXML
    private HBox hbox;
    private TreeMap<String, String> dic;
    
    @FXML
	public void initialize() {
    	dic = new TreeMap<String, String>();
    	loadFromFile();
    	printDictionary();
    }
    
    @FXML
    void addButtonPressed(ActionEvent event) {
    	// Check if the word text area is empty.
    	if(wordTextArea.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(null, ERR_MSG1);
    		return;
    	}
    	// Check if the specified word is defined already.
    	else if(definitionTextArea.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(null, ERR_MSG2);
    		return;
    	}
    	// Check if the word has already definition; Only One definition to word .
    	else if(dic.containsKey(wordTextArea.getText())) {
    		JOptionPane.showMessageDialog(null, "Unambiguous!! key cannot have more than one definition.");
    		return;
    	}
    	
    	
    	dic.put(wordTextArea.getText(), definitionTextArea.getText());	// Add new entity.
    	printDictionary();	// Print the Dictionary Content.
    	addClosingEvent();	// Save the new content into file.
    }

    /* Delete the specified word content from the data structure. */
    @FXML
    void deleteButtonPressed(ActionEvent event) {
    	// Check if the word text area is empty.
    	if(wordTextArea.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(null, ERR_MSG1);
    		return;
    	}
    	// Check if the specified word is defined already.
    	else if(!dic.containsKey(wordTextArea.getText())) {
    		JOptionPane.showMessageDialog(null, "Can't find " + wordTextArea.getText() + " Key\\Word");
    		return;
    	}
    	
    	dic.remove(wordTextArea.getText());		// Remove the old entity.
    	printDictionary();	// Print the Dictionary Content.
    	addClosingEvent();	// Save the new content into file.
    }

    /* Save (Override) the definition of the specified word. */
    @FXML
    void saveButtonPressed(ActionEvent event) {
    	// Check if the word text area is empty.
    	if(wordTextArea.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(null, ERR_MSG1);
    		return;
    	}
    	// Check if the specified word is defined already.
    	else if(!dic.containsKey(wordTextArea.getText())) {
    		JOptionPane.showMessageDialog(null, "Can't find " + wordTextArea.getText() + " Key\\Word");
    		return;
    	}
    	// Check if the the definition text area is empty.
    	else if(definitionTextArea.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(null, ERR_MSG2);
    		return;
    	}
    	dic.remove(wordTextArea.getText());		// Remove the old entity.
    	dic.put(wordTextArea.getText(), definitionTextArea.getText());	// Add new entity.
    	printDictionary();	// Print the Dictionary Content.
    	addClosingEvent();	// Save the new content into file.
    }

    /* Display the content of word that searched into the definition text area. */
    @FXML
    void searchButtonPressed(ActionEvent event) {
    	// Check if the word text area is empty.
    	if(wordTextArea.getText().isEmpty()) {
    		JOptionPane.showMessageDialog(null, ERR_MSG1);
    		return;
    	}
    	// Check if the specified word is defined already.
    	else if(!dic.containsKey(wordTextArea.getText())) {
    		JOptionPane.showMessageDialog(null, "\"" + wordTextArea.getText() + "\" Word isn't defined.");
    		return;
    	}
    	
    	// Display the definition of the word that specified.
    	definitionTextArea.setText(dic.get(wordTextArea.getText()));
    }
    
    /* Print the TreeMap content to the scene. */
    private void printDictionary() {

    	String buffer = "";    // buffer - Contain the content as String.
    	Iterator<Map.Entry<String, String>> it = dic.entrySet().iterator(); // Define Iterator.
    	dictionaryImageTextArea.clear();	// Clear the TextArea of the scene in order to display the updated content.
    	// Save the content into the buffer.
    	while(it.hasNext()) {
    		Map.Entry<String, String> item = it.next();
    		buffer += item.getKey() + " - " + item.getValue() + "\n";
    	}
    	// Display the content of the buffer.
    	dictionaryImageTextArea.setText(buffer);
    }
    
    /* Save the TreeMap data structure content into the file when close the program. */
    private void addClosingEvent() {
		Stage stage = (Stage)((Node) hbox).getScene().getWindow();
		stage.getScene().getWindow().addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event1 -> {
			saveToFile();
		});	
	}

    /* Load the specified file content into TreeMap data structure. */
	@SuppressWarnings("unchecked")
	private void loadFromFile() {

		File file = getFile();

		if (file != null) {
			try {

				FileInputStream fi = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fi);
				dic = (TreeMap<String, String>)ois.readObject();
				ois.close();
				fi.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/* Save the content of the TreeMap data structure to file. */
	private void saveToFile() {

		File file = getFile();

		try {
			FileOutputStream fo = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fo);
			out.writeObject(dic);
			out.close();
			fo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* Choose file to import content. */
	private File getFile() {
		FileChooser fileChooser = new FileChooser(); 
		fileChooser.setTitle("select a file"); 
		fileChooser.setInitialDirectory(new File(".")); 
		return fileChooser.showOpenDialog(null);
	}
}
