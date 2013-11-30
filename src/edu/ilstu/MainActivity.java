package edu.ilstu;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.android_grader.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Description: MainActivity class that handles the main screen interaction on the Android device.
 * @author Rachel A Schifano, Corbin Sumner, John Boomgarden
 *
 */
public class MainActivity extends Activity {

	Course course=null;
	private String XMLfile = "course-226.xml";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		course = readCourseFile(XMLfile);
		
		if(course == null){
			course = getCourseXML(); 
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	
		course = readCourseFile(XMLfile);
		
		if(course == null){
			course = getCourseXML(); 
		}
		
		//double performance = getPerformance();
		
		// print percent total
		//String str = Double.toString(performance);
		//Log.d("Total", str);
		
		// print percent score
		/*double score = course.getPercentageScore();
		String str2 = Double.toString(score);
		Log.d("Score", str2); */
		
		
		//String formattedPerf = String.format("%.2f", performance);
		
		// Course Performance
		TextView textView1 = (TextView) findViewById(R.id.course_performance);
		textView1.setText(getPerformance(course));
		
		
		
		
		/*
		String str_score = Double.toString(score);
		String formattedString1 = String.format("%.2f", score);
		
		// Exams Performance
		
		// Individual Assignments Performance
		TextView textView3 = (TextView) findViewById(R.id.individual_assignments_perf);
		textView3.setText(formattedString1);
		
		// Group Projects Performance
		TextView textView5 = (TextView) findViewById(R.id.group_projects_perf);
		textView5.setText(formattedString1);
		
		// Homework, Quizzes, Participation Performance
		TextView textView8 = (TextView) findViewById(R.id.hw_quizzes_participation_perf);
		textView8.setText(formattedString1); */
		
		
	}
	
	/**
	 * Method that returns a String of the current course performance
	 * as a readable percentage score.
	 * @return displayPerformance The percentage performance of the total score
	 */
	private String getPerformance(Course aCourse) {

		Double coursePerformance = aCourse.getPercentageScore() / aCourse.getPercentageTotal();
				
		NumberFormat defaultFormat = NumberFormat.getPercentInstance();
		defaultFormat.setMinimumFractionDigits(2);
		
		String displayPerformance = "  " + defaultFormat.format(coursePerformance);
		
		return displayPerformance;
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
	
	// Everything is inherited from view.
	/**
	 * Method that gets the current button ID that was selected, and determines which
	 * category should be passed through the loadCategory helper method.
	 * @param view The view object that holds the action from the button
	 */
	public void loadCourseDetailScreen(View button) {
		
		// retrieves the selected button ID
		int id = button.getId();
		
		// determine which category to load
		switch(id) {
			case R.id.exams: loadCategory(getString(R.string.exams));
			break;
			
			case R.id.individual_assignments: loadCategory(getString(R.string.individual_assignments));
			break;
			
			case R.id.group_projects: loadCategory(getString(R.string.group_projects));
			break;
			
			case R.id.hw_quizzes_participation: loadCategory(getString(R.string.hw_quizzes_participation));
			break;
		
			default:
				break;
		}
	}	
		
	/**
	 * Private helper method that passes the intent from the current MainActivity
	 * to CourseDetailsScreen. Attaches an extra to the intent that serves as a
	 * key for the specified category name.
	 * @param categoryName The string name for each category.
	 */
	private void loadCategory(String categoryName) {
		
		Intent i = new Intent(this, CourseDetailsScreen.class);
		i.putExtra("category", categoryName);
		startActivity(i);
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
