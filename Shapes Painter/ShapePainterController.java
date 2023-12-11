import java.awt.Point;

import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

public class ShapePainterController {

	
	@FXML    private Pane drawingAreaPane;	//	The pane.
	@FXML    private Button undoButton;		//	Undo button.
	@FXML    private Button clearButton;	//	Clear button.
	/*Radio Buttons*/
	@FXML    private RadioButton lineRadioButton;		//	Line radio button.
	@FXML    private RadioButton ellipseRadioButton;	//	Ellipse radio button.
	@FXML    private RadioButton rectangleRadioButton;	//	Rectangle radio button.
	@FXML    private ToggleGroup shapeToggleGroup;		//	Group the ration buttons above.
	
	@FXML    private ColorPicker drawingColorPicker;	//	Color picker.
	@FXML    private ToggleButton fillShapeToggleButton;//	Toggle button to fill the shape.
    
	// startPoint - assign to the coordinates where mouse is pressed.
	// endPoint	- assign to the coordinates where mouse is released.
	private Point startPoint, endPoint;	
	// shape - assign to the shape.
	private Shape shape;
	// shapeColor - assign to the color.
	private Paint shapeColor;
	// fill - determine whether the shape is fill.
	private Boolean fill;

	/* Initialize the default values of the shape. */
	public void initialize() {
		startPoint = new Point();
		endPoint = new Point();
		// Set the line as default shape.
		shape = new Line();
		// Set the color to black.
		shapeColor = Color.BLACK;
		// Set the fill attribute to false, empty shape.
		fill = false;
		
		// Give each radioButton value.
		lineRadioButton.setUserData(new Line());
		ellipseRadioButton.setUserData(new Ellipse());
		rectangleRadioButton.setUserData(new Rectangle());

	}

	/* Handle mouse released event, determine the second point to make the shape as mention below:
	 * 1. Line, determine the coordinate of the end edge.
	 * 2. Ellipse, determine the radius length, the first point determine the center point.
	 * 3. Rectangle, determine the point of the diagonal.
	 * if the shape doesn't exceed the bounds of the pane then display the shape, otherwise infer the user.
	 */ 
	@FXML
	void drawingAreaMouseReleased(MouseEvent e) {
		// Initialize the second point according to released mouse coordinates.
		endPoint.setLocation(e.getX(), e.getY());

		/* Determine the specified shape. the division of the statements between 
		 * shapes that have fill shape attribute to those who not.
		 */
		if(shape instanceof Line) {
			// Create line according to the startPoint and endPoint coordinates.
			shape = (Line) new Line(startPoint.getX(),startPoint.getY(),endPoint.getX(),endPoint.getY());
			// Check if the line exceed from the bounds of the pane.
			if(!inTheWindow(shape)) {
				JOptionPane.showMessageDialog(null, "Line is out of Pane range.");
				return;
			}
		}
		// Shape with fill shape attribute.
		else {
			if(shape instanceof Ellipse) {
				// Calculate xRadius and yRadius.
				double xRadius = Math.abs(startPoint.getX() - endPoint.getX());
				double yRadius = Math.abs(startPoint.getY() - endPoint.getY());
				// Create ellipse according to the startPoint and endPoint coordinates.
				shape = (Ellipse) new Ellipse(startPoint.getX(),startPoint.getY(),xRadius,yRadius);
				// Check if the ellipse exceed from the bounds of the pane.
				if(!inTheWindow(shape)) {
					JOptionPane.showMessageDialog(null, "Ellipse is out of Pane range.");
					return;
				}
			}
			// If the shape is Rectangle.
			else {
				// Calculate the width and height of the rectangle.
				double width = endPoint.getX()-startPoint.getX();
				double height = endPoint.getY()-startPoint.getY();
				
				/*	The statements division to create real drawing illusion for the user on the screen
				 *	say, startPoint is the point (0, 0) in the cartesian coordinate system and statments are: 
				 *	1. if endPoint in the 1st quarter width < 0 and height < 0.
				 *	2. if endPoint in the 2nd quarter width > 0 and height < 0.
				 *	3. if endPoint in the 3rd quarter width < 0 and height > 0.
				 *	4. if endPoint in the 4th quarter width > 0 and height > 0.
				 */
				if(width < 0) {
					if(height < 0)
						shape = (Rectangle) new Rectangle(endPoint.getX(), endPoint.getY(), Math.abs(width), Math.abs(height));
					else 
						shape = (Rectangle) new Rectangle(endPoint.getX(), startPoint.getY(), Math.abs(width), height);
				}
				else {
					if(height < 0)
						shape = (Rectangle) new Rectangle(startPoint.getX(), endPoint.getY(), width, Math.abs(height));
					else 
						shape = (Rectangle) new Rectangle(startPoint.getX(), startPoint.getY(), width, height);
				}
				
				// Check if the rectangle exceed the bounds of the pane.
				if(!inTheWindow(shape)) {
					JOptionPane.showMessageDialog(null, "Rectangle is out of Pane range.");
					return;
				}
			}
			// Fill the shape as user specify.
			if(fill == false)
				shape.setFill(null);
			else shape.setFill(shapeColor);

		}
		
		// set the color that user specify.
		shape.setStroke(shapeColor);
		// Add the shape to the pane.
		drawingAreaPane.getChildren().add(shape);
	}
	
	/* Handle Mouse pressed event to determine the first point of the shape. */
	@FXML
	void drawingAreaMousePressed(MouseEvent e) {
		// Initialize the coordinates of startPoint.
		startPoint.setLocation(e.getX(), e.getY());
	}

	/* Handle shape selection Radio Buttons event to determine the needed shape to draw. */
	@FXML
	void shapeRadioButtonSelected(ActionEvent event) {
		// Initialize shape with the specified shape from the user.
		shape = (Shape) shapeToggleGroup.getSelectedToggle().getUserData();
	}

	/* Handle Color Picker event to determine the needed color from the user. */
	@FXML
	void drawingColorPickerSelected(ActionEvent event) {
		// Initialize the shapeColor variable with the specify color.
		shapeColor = (Color) drawingColorPicker.getValue();
	}

	/* Handle fill toggle button to determine either the shape is filled or not. */
	@FXML
	void fillShapeToggleButtonSelected(ActionEvent e) {
		// Initialize the fill button according to user needs.
		fill = (Boolean) fillShapeToggleButton.isSelected();
	}

	/* Handle undo button to remove the last added shape from the pane. */
	@FXML
	void undoButtonPressed(ActionEvent event) {
		// count - the number of the shapes into the root (Pane board).
		int count = drawingAreaPane.getChildren().size();

		// Check if the root not empty.
		if(count > 0)
			drawingAreaPane.getChildren().remove(count - 1);
	}

	/* Handle clear button to clear the pane board totally. */
	@FXML
	void clearButtonPressed(ActionEvent event) {
		drawingAreaPane.getChildren().clear();
	}
	
	/* Check if the shape bounds are contained into the pane board bounds. */
	private boolean inTheWindow(Shape shape) {
	    return drawingAreaPane.getLayoutBounds().contains(shape.getBoundsInParent());
	}

}
