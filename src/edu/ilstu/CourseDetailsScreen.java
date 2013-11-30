package edu.ilstu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.android_grader.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Description: Class that displays all the category details and dynamically
 * loads the respective content.
 * @author Rachel A Schifano, Corbin Sumner, John Boomgarden
 *
 */

public class CourseDetailsScreen extends Activity {

	// Instantiate variables
	private Course course = null;
	private String XMLfile = "course-226.xml";
	private TextView myText = null;
	
	private ArrayList<EditText> editText;
	
	/**
	 * Method that is used to start an activity.
	 * 1. Checks to see if the passed intent is not null and then
	 * retrieves the passed category from the button action and
	 * updates the current TextView on the screen.
	 * 2. Determines if the course XML will be read from memory or the 
	 * default assets location in which it is stored.
	 * 3. Sets up table to be used for dynamically loading XML grade items.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Android generated code that launches the next activity screen
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_details);
		
		editText = new ArrayList<EditText>();
		
		// instantiate variables
		String categoryName = "";
		ArrayList<GradeItem> gradeItems = null;
		Intent intent = getIntent();
		
		// check if intent is null
		// if not, update the CourseDetailsScreen with the current category
		if (intent != null) {
			categoryName = intent.getStringExtra("category");
			myText = (TextView)findViewById(R.id.category_name);
			myText.setText(categoryName);
		}
		
		// check if course object is null
		// if it is not, app will read the current XML file from memory
		// if it is, app will read the default XML file
		if (course == null){
			System.out.println("Reading file from memory.");
			course = getCourseXML();
			
			gradeItems = parseGradeItem(course, categoryName);
		}
		else {
			System.out.println("Reading default XML file.");
		}
		
		// sets up the table to be used for the selected grade items
		TableLayout table = (TableLayout)findViewById(R.id.grade_table);
		setTableRows(table, gradeItems);
	}
	
	/**
	 * Reads the current XML file and returns a complete
	 * course object with the current data.
	 */
	private Course getCourseXML() {
		Course aCourse = null;
		
		// read the current XML file
		try {
			InputStream stream = getResources().getAssets().open(XMLfile);
			aCourse = CourseXMLParser.readCourse(stream);
			stream.close();
		} catch(IOException ex)
		{
			ex.printStackTrace();
			System.out.println("Cannot read file.");
		}
	
		return aCourse;
	}
	
	
	/**
	 * Method that parses grade items.
	 * @param aCourse The current course object
	 * @param aCategory The current course category
	 * @return gradeItems Array list of matching grade items that correspond to a category
	 */
	private ArrayList<GradeItem> parseGradeItem(Course aCourse, String aCategory) {
		
		// create new array list for storing all grade items to return for a category
		ArrayList<GradeItem> gradeItems = new ArrayList<GradeItem>();
		// instantiate category and grade items
		Category currentCategory = null;
		GradeItem gradeItem = null;
		
		// retrieve all grade items from a given course
		ArrayList<String> itemNames = aCourse.getItemNames();
		
		// retrieve grade items that correspond to the passed category
		currentCategory = (Category)aCourse.getItem(aCategory);
		
		// loop through the given item names, and store matching items
		// within the array list to be returned.
		for (int i=0; i < itemNames.size(); i++) {
			
			String itemName = itemNames.get(i);
			gradeItem = (GradeItem)currentCategory.getItem(itemName);
			
			if (gradeItem != null) {
				gradeItems.add(gradeItem);
			}
		}
		return gradeItems;
	}
	
	/**
	 * Method that writes the current course to the
	 * respective XML file.
	 * @param filename The name of the XML file
	 * @param course The current course objct
	 */
	private void writeCourseFile(String filename, Course course) {
		FileOutputStream outputStream;
		String xml = course.toXML();
		
		try 
		{
			outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
			outputStream.write(xml.getBytes()); // 
			outputStream.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that reads the current course file.
	 * @param filename The name of the XML file.
	 * @return readCourse The current course that was read.
	 */
	public Course readCourseFile(String filename) {
		File file;
		FileInputStream stream;
		Course readCourse = null;
		
		try 
		{
			file = new File(this.getFilesDir(), filename); // Android files directory
			stream = new FileInputStream(file);
			readCourse = CourseXMLParser.readCourse(stream);
			stream.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		return readCourse;
	}
	
	/**
	 * Method that dynamically generates table rows to display the
	 * course data.
	 * @param table The Android TableLayout generated.
	 * @param rowItems The table rows which hold information for each grade item.
	 */
	@SuppressWarnings("null")
	private void setTableRows(TableLayout table, ArrayList<GradeItem> rowItems) {
		
		
		for( int i=0; i < rowItems.size(); i++){
			GradeItem item = rowItems.get(i);
			TableRow row = new TableRow(this); // create table rows
			TextView title = new TextView(this); // create new text
			EditText input = new EditText(this); // create input field
			
			System.out.println("adding EditText "+i);
			editText.add(i,input); // store each unique input in an arraylist
			
			title.setText(item.getName());
			
			row.addView(title); // loads each grade item text
			row.addView(input); // loads input field for each item
			row.setBackgroundColor(getResources().getColor(R.color.blue)); // sets row colors
			table.addView(row);
		}		
	}
	
	/**
	 * Method that updates the XML when the Actual button is pressed.
	 * @param view The view object that holds the action from the button
	 */
	public void updateGrade(View button) {
	
		int id = button.getId();
		
		String itemName = "";
		String input = "";
		double score = 0.0;
		boolean fieldIsEmpty = false;
		
		TableLayout table = (TableLayout)findViewById(R.id.grade_table);
		int numRows = table.getChildCount(); // determine number of rows
		
		// check each row for a child
		for (int rowIndex = 0; rowIndex < numRows; rowIndex++){
			
			TableRow row = (TableRow)table.getChildAt(rowIndex); // reference row object
			TextView titleLabel = (TextView)row.getChildAt(0); // get first child of the row
			
			// retrieve name of item from the corresponding row
			itemName = titleLabel.getText().toString();
			
			EditText textField = editText.get(rowIndex);
			
			if (textField != null) {
				
				try {
				input = textField.getText().toString();
				score = Double.parseDouble(input);
				}
				catch (Exception ex) {
					fieldIsEmpty = true;
				}
				
				if (!fieldIsEmpty) {
					if (score != 0.0 && id == R.id.actual)
					{
						course.setEvaluatedPoints(itemName, score);
					}
					else if (score != 0.0 && id == R.id.estimated){
						course.setEstimatedPoints(itemName, score);
					}
				}
			}
		}
		writeCourseFile(XMLfile, course);		
	}
	
	
}