package com.akshay.calculator;


import java.util.Stack;
import java.util.StringTokenizer;

public class EvaluateExpression
{

	private Stack<String> operatorStack;
	private Stack<String> valueStack;

	public EvaluateExpression() {
		operatorStack=new Stack<String>();
		valueStack=new Stack<String>();

	}

	public double infixOperation(String expression)
	{
		expression=expression.replaceAll("[\t\n ]", "")+"=";
		String operator="*/+-=";

		StringTokenizer tokenizer=new StringTokenizer(expression, operator, true);
		operatorStack=new Stack<String>();
		valueStack=new Stack<String>();

		int count = 0;
		String temp ="";
		while(tokenizer.hasMoreTokens())
		{
			//add the next token to the proper stack
			String token=tokenizer.nextToken();
			if (token.equals("-") && count == 0) { 
				temp = token;
				count++;
				continue;
			}
			if(operator.indexOf(token) < 0  ) {
				if (count == 1) {
					token = temp + token;
					count++;
				}

				//System.out.println("token: " + token);
				valueStack.push(token);
				count = 2;
			} else {

				operatorStack.push(token);
			}
			//perform any pending operations
			resolve();
		}
		//return the top of the value stack
		String lastOne=(String)valueStack.pop();
		return Double.parseDouble(lastOne);   
	}

	public int getPriority(String op)
	{
		if(op.equals("*") || op.equals("/"))
			return 1;
		else if(op.equals("+") || op.equals("-"))
			return 2;
		else if(op.equals("="))
			return 3;
		else
			return Integer.MIN_VALUE;
	}

	public void resolve()
	{
		while(operatorStack.size()>=2)
		{
			String firstOp=(String)operatorStack.pop();
			String secondOp=(String)operatorStack.pop();
			if(getPriority(firstOp)<getPriority(secondOp))
			{
				operatorStack.push(secondOp);
				operatorStack.push(firstOp);
				return;
			}
			else
			{
				String firstValue=(String)valueStack.pop();
				String secondValue=(String)valueStack.pop();
				valueStack.push(getResults(secondValue, secondOp, firstValue));
				operatorStack.push(firstOp);
			}
		}
	}

	public String getResults(String operand1, String operator, String operand2)
	{
		//System.out.println("Performing "+
		//	operand1+operator+operand2);
		double op1=Double.parseDouble(operand1);
		double op2=Double.parseDouble(operand2);
		if(operator.equals("*"))
			return ""+(op1*op2);
		else if(operator.equals("/")) {
			if (op2 == 0) {
				return "" + Double.POSITIVE_INFINITY;
			}
			return ""+(op1/op2);
		} else if(operator.equals("+"))
			return ""+(op1+op2);
		else if(operator.equals("-"))
			return ""+(op1-op2);
		else
			return null;
	}


}