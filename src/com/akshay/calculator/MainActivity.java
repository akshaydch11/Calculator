package com.akshay.calculator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private EditText display;
	private Button zero, one, two, three, four, five,
	six, seven, eight, nine, dot, equal, 
	plus, minus, mul, div, clr;

	private String last = "";
	private boolean flag = false;
	private boolean dotFlag = false;

	StringBuilder displaySb;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		displaySb = new StringBuilder("");
		initializeViews();
		setListeners();
		if (savedInstanceState != null) {
			displaySb = new StringBuilder(savedInstanceState.getString("display_buffer"));
			display.setText(savedInstanceState.getString("display_value"));
			last = savedInstanceState.getString("last");
		}
		display.setSelection(display.getText().toString().length());
	}

	/**
	 * Initialize all buttons and edit text
	 * used in application
	 */
	public void initializeViews() {
		
		display = (EditText) findViewById(R.id.display);
		Log.i("MainActivity", "dis " + display.getText().toString());

		zero = (Button) findViewById(R.id.button_zero);
		one = (Button) findViewById(R.id.button_one);
		two = (Button) findViewById(R.id.button_two);
		three = (Button) findViewById(R.id.button_three);
		four = (Button) findViewById(R.id.button_four);
		five = (Button) findViewById(R.id.button_five);
		six = (Button) findViewById(R.id.button_six);
		seven = (Button) findViewById(R.id.button_seven);
		eight = (Button) findViewById(R.id.button_eight);
		nine = (Button) findViewById(R.id.button_nine);

		dot = (Button) findViewById(R.id.button_dot);
		equal = (Button) findViewById(R.id.button_equal);
		plus = (Button) findViewById(R.id.button_plus);
		minus = (Button) findViewById(R.id.button_minus);
		mul = (Button) findViewById(R.id.button_mul);
		div = (Button) findViewById(R.id.button_div);
		clr = (Button) findViewById(R.id.button_clr);
		clr.setText("D");
	}

	/**
	 * Set listeners to required view elements
	 */
	public void setListeners () {
		zero.setOnClickListener(this);
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
		five.setOnClickListener(this);
		six.setOnClickListener(this);
		seven.setOnClickListener(this);
		eight.setOnClickListener(this);
		nine.setOnClickListener(this);

		dot.setOnClickListener(this);
		equal.setOnClickListener(this);
		plus.setOnClickListener(this);
		minus.setOnClickListener(this);
		mul.setOnClickListener(this);
		div.setOnClickListener(this);
		clr.setOnClickListener(this);
		clr.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				displaySb = new StringBuilder("");
				last = "";
				display.setText(displaySb);
				clr.setText("D");
				return true;
			}
		});

		OnTouchListener otl = new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.onTouchEvent(event);
				InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}                
				return true;

			}
		};

		display.setOnTouchListener(otl);
	}

	/**
	 * This function appends operators if you want to 
	 * do operation on an answer of first result.
	 * @param ch character input from button
	 */
	public void appendAfterAnswer(String ch) {
		if (ch.matches("[*/+-]")) {
			displaySb.append(ch);
			
		} else if ((ch.matches("[0-9]") || ch.equals("."))) {
			displaySb = new StringBuilder(ch);
		}
		last = ch;
		flag = false;
	}
	
	
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		//super.onSaveInstanceState(outState);
		outState.putString("display_value", display.getText().toString());
		outState.putString("last", last);
		outState.putString("display_buffer", displaySb.toString());
		//Log.i("MainActivity", "onSaveInstanceState");
		
	}

	/**
	 * Handles append for different characters and operators.
	 * @param ch character input from button
	 */
	public void append(String ch) {
		if (last.equals("") && ch.matches("[*/+]")) {
			// if you put *,/,+ without digit
			return;
		} else if (last.equals("") && ch.equals("-")) {
			// for -ve numbers
			last = ch;
			displaySb.append(ch);

		} else if (last.matches("[*/+-]") && ch.matches("[*/+-]")) {
			// if someone tries to add operators twice
			return;
		} else if (last.matches("[.]") && ch.matches("[.]")) {
			return;
		} else {
			Log.i("MainActivity", "1 dotFlag " + dotFlag  + " ch " + ch);
			if (ch.equals(".") && dotFlag) {
				return;
			}
			if (ch.equals(".") && !dotFlag) {
				dotFlag = true;
			}
			if (ch.matches("[*/+-]") && dotFlag) {
				dotFlag = false;
			}
			Log.i("MainActivity", "2 dotFlag " + dotFlag + " ch " + ch);
			displaySb.append(ch);
			last = ch;
		}
	}
	
	/**
	 * Appends characters to display
	 * @param ch character input from button
	 */
	public void appendToDisplay (String ch) {
		if (flag) {
			appendAfterAnswer(ch);
		} else {
			append(ch);
		}
		clr.setText("D");
		display.setText(displaySb);
	}


	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		Button btn = (Button) v;

		if (btn.getText().toString().equals("=")) {
			String exp = display.getText().toString();

			if (exp.equals(".") || exp.equals(String.valueOf(getResources().getString(R.string.infinity)))) {
				display.setText("0");
				displaySb = new StringBuilder("");
				last = "";
				display.setSelection(display.getText().toString().length());
				return;
			}
			
			if (exp.matches(".*[*/+-]$")) {
				exp = exp.substring(0, exp.length()-1);
			}

			double result = 0;
			try {
			 result = 
					new EvaluateExpression().infixOperation(exp);
			} catch (Exception e) {
				Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
				result = 0;
				
			}
			if (Double.isInfinite(result)) {
				display.setText(String.valueOf(getResources().getString(R.string.infinity)));
				displaySb = new StringBuilder("");
				last = "";
			} else {
				if (result % 1 == 0) {
					display.setText(String.valueOf((int)result));
					displaySb = new StringBuilder(String.valueOf((int)result));
					last = String.valueOf((int)result);
				} else { 
					display.setText(String.valueOf(result));
					displaySb = new StringBuilder(String.valueOf(result));
					last = String.valueOf(result);
				}
				flag = true;
				dotFlag = false;
			}

			clr.setText("C");

		} else if (btn.getText().toString().equals("D")) {

			if (displaySb.length() >= 1) { 
				displaySb = displaySb.deleteCharAt(displaySb.length() - 1);
				last = (displaySb.length() >= 1) ? displaySb.substring(displaySb.length() - 1, displaySb.length()) : "";

			}
			display.setText(displaySb);
		} else if (btn.getText().toString().equals("C")) {
			displaySb = new StringBuilder("");
			last = "";
			display.setText(displaySb);
			clr.setText("D");
		} else {
			appendToDisplay(btn.getText().toString());
		}
		display.setSelection(display.getText().toString().length());

	}
}
