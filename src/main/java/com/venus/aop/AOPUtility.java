package com.venus.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import com.venus.aop.annotation.After;
import com.venus.aop.annotation.AfterReturning;
import com.venus.aop.annotation.AfterThrowing;
import com.venus.aop.annotation.Around;
import com.venus.aop.annotation.Before;
import com.venus.aop.exception.VenusAOPFormatNotSupported;
import com.venus.exception.ProcessException;

public class AOPUtility {
	private static final String EMPTY_STRING = "";
	private static final String SPACE_STRING = " ";
	private static final String VERSATILE_VALUE_STRING = "*";
	private static final String LEFT_BRACKET_STRING = "(";
	private static final String RIGHT_BRACKET_STRING = ")";
	private static final String DOUBLE_BRACKET_STRING = "()";
	private static final String COMMA_STRING = ",";
	private static final String DOT_STRING = ".";
	private static final String AT_STRING = "@";
	private static final String AND_STRING = "&&";
	private static final String OR_STRING = "||";
	
	/**
	 * Checks bean's eligibility to go through the aspect process
	 * 
	 * @param bean the object to be checked
	 * @param aspects list of aspect objects
	 * @return boolean value resulting from the check
	 */

	public static boolean isEligibleToAspectProcess(Object bean, ArrayList<Object> aspects) {
		for (Object aspect : aspects) {
			for (Method m : aspect.getClass().getDeclaredMethods()) {
				if (m.isAnnotationPresent(Before.class) || m.isAnnotationPresent(After.class)
						|| m.isAnnotationPresent(Around.class) || m.isAnnotationPresent(AfterReturning.class)
						|| m.isAnnotationPresent(AfterThrowing.class)) {
					for (Method beanMethod : bean.getClass().getDeclaredMethods()) {
						try {
							if (checkExpressionMatching(beanMethod, getExpressionValueFromAnnotation(m)))
								return true;
						} catch (VenusAOPFormatNotSupported e) {
							throw new ProcessException(e);
						}
					}
				}
			}
		}
		return false;
	}

	private static String getExpressionValueFromAnnotation(Method method) {
		if (method.isAnnotationPresent(Before.class)) {
			return method.getAnnotation(Before.class).value();
		} else if (method.isAnnotationPresent(After.class)) {
			return method.getAnnotation(After.class).value();
		} else if (method.isAnnotationPresent(Around.class)) {
			return method.getAnnotation(Around.class).value();
		} else if (method.isAnnotationPresent(AfterReturning.class)) {
			return method.getAnnotation(AfterReturning.class).value();
		} else if (method.isAnnotationPresent(AfterThrowing.class)) {
			return method.getAnnotation(AfterThrowing.class).value();
		} else
			return null;
	}
	/**
	 * Checks if the method passed in parameter matches the pointcut expression
	 * 
	 * @param m the method that will be checked if it matches the pointcut expression
	 * @param expression the pointcut expression that will undergo the matching test
	 * @return boolean value resulting from the matching check
	 */
	public static boolean checkExpressionMatching(Method m, String expression) throws VenusAOPFormatNotSupported {
		if (expression.contains(AND_STRING) && expression.contains(OR_STRING)) {
			throw new VenusAOPFormatNotSupported("This format is not supported");
		} else if (expression.contains(AND_STRING) || expression.contains(OR_STRING)) {
			String[] expressions;
			if (expression.contains(AND_STRING)) {
				expressions = expression.split(AND_STRING);
				boolean isEligible = true;
				for (String exp : expressions) {
					// verify
					if (exp.contains(AT_STRING)) {
						isEligible = isEligible && evaluateAnnotationsMatching(rightLeftTrimForSingleWord(exp),
								getMethodAnnotations(m));
					} else
						isEligible = isEligible
								&& evaluateExpression(buildMethodSignature(m), rightLeftTrimForSingleWord(exp));
				}
				return isEligible;
			} else {
				expressions = expression.split("\\|\\|");
				boolean isEligible = false;
				for (String exp : expressions) {
					if (exp.contains(AT_STRING)) {
						isEligible = isEligible || evaluateAnnotationsMatching(rightLeftTrimForSingleWord(exp),
								getMethodAnnotations(m));
					} else
						isEligible = isEligible
								|| evaluateExpression(buildMethodSignature(m), rightLeftTrimForSingleWord(exp));
				}
				return isEligible;
			}
		} else {
			if (expression.contains(AT_STRING)) {
				return evaluateAnnotationsMatching(rightLeftTrimForSingleWord(expression), getMethodAnnotations(m));
			} else {
				return evaluateExpression(buildMethodSignature(m), rightLeftTrimForSingleWord(expression));
			}
		}
	}

	private static boolean evaluateExpression(String methodSignature, String expression) {
		String strM = EMPTY_STRING;
		String strE = EMPTY_STRING;
		if (methodSignature.equals(expression))
			return true;
		else if (expression.startsWith(VERSATILE_VALUE_STRING)
				|| methodSignature.startsWith(expression.substring(0, expression.indexOf(SPACE_STRING)))) {
			strM = methodSignature.substring(methodSignature.indexOf(SPACE_STRING));
			strE = expression.substring(expression.indexOf(SPACE_STRING));
			// check return type
			String strMReturnType = rightLeftTrimForSingleWord(strM.substring(strM.indexOf(SPACE_STRING)));
			String strEReturnType = rightLeftTrimForSingleWord(strE.substring(strE.indexOf(SPACE_STRING)));
			strMReturnType = strMReturnType.substring(0, strMReturnType.indexOf(SPACE_STRING));
			strEReturnType = strEReturnType.substring(0, strEReturnType.indexOf(SPACE_STRING));
			if (strEReturnType.equals(strMReturnType) || strEReturnType.equals(VERSATILE_VALUE_STRING)) {
				strM = strM.replace(strMReturnType, EMPTY_STRING);
				if (strEReturnType.equals(VERSATILE_VALUE_STRING)) {
					strE = strE.replaceFirst("\\" + VERSATILE_VALUE_STRING, EMPTY_STRING);
				} else {
					strE = strE.replaceFirst(strEReturnType, EMPTY_STRING);
				}
			} else
				return false;
			strM = rightLeftTrimForSingleWord(strM);
			strE = rightLeftTrimForSingleWord(strE);
			if (strM.equals(strE))
				return true;
			else if (checkParametersMatching(methodSignature, expression)) {
				if (methodSignature.contains("throws")) {
					String methodException = methodSignature.substring(methodSignature.indexOf("throws"));
					if (expression.contains(RIGHT_BRACKET_STRING)) {
						String expressionException = expression.substring(expression.indexOf(RIGHT_BRACKET_STRING) + 1);
						if (!methodException.equals(expressionException)
								&& !expressionException.contains(VERSATILE_VALUE_STRING))
							return false;
					}
				}
				if (strE.contains(VERSATILE_VALUE_STRING)) {
					String[] strMArray = (strM.trim().substring(0, strM.indexOf(LEFT_BRACKET_STRING)))
							.split("\\" + DOT_STRING);
					String[] strEArray = (strE.trim().substring(0, strE.indexOf(LEFT_BRACKET_STRING)))
							.split("\\" + DOT_STRING);

					ArrayList<String> strEList = new ArrayList<>(Arrays.asList(strEArray));
					ArrayList<String> strMList = new ArrayList<>(Arrays.asList(strMArray));
					ArrayList<String> subMList = null;
					if (strMList.size() == strEList.size()) {
						subMList = strMList;
					} else if (strMList.size() < strEList.size())
						return false;
					else {
						subMList = new ArrayList<>(
								strMList.subList(strMList.size() - strEList.size(), strMList.size()));
					}
					for (int i = (Arrays.asList(strEArray)).size(); i > 0; i--) {
						if (!strEList.get(i - 1).equals(VERSATILE_VALUE_STRING)
								&& !strEList.get(i - 1).equals(subMList.get(i - 1)))
							return false;
						else {
							if (strEArray.length == 3 && i == 1) {
								return true;
							}
							if (strEList.get(i - 1).equals(subMList.get(i - 1))
									|| strEList.get(i - 1).equals(VERSATILE_VALUE_STRING)) {
								strEList.remove(i - 1);
								subMList.remove(i - 1);
							}
							if (strEList.size() == 0)
								return true;
						}

					}

				}
				strM = strM.substring(0, strM.indexOf(LEFT_BRACKET_STRING));
				strE = strE.substring(0, strE.indexOf(LEFT_BRACKET_STRING));
				if (strM.equals(strE))
					return true;
			}

		}
		return false;
	}

	private static boolean checkParametersMatching(String methodSignature, String expression) {
		if (expression.contains("(..)"))
			return true;
		else if (expression.contains(DOUBLE_BRACKET_STRING)) {
			if (methodSignature.contains(DOUBLE_BRACKET_STRING))
				return true;
			else
				return false;
		} else {
			String[] expParametersArray = (expression.substring(expression.indexOf(LEFT_BRACKET_STRING) + 1,
					expression.lastIndexOf(RIGHT_BRACKET_STRING))).split(COMMA_STRING);
			ArrayList<String> expParametersList = new ArrayList<>(
					Arrays.asList((expression.substring(expression.indexOf(LEFT_BRACKET_STRING) + 1,
							expression.lastIndexOf(RIGHT_BRACKET_STRING))).split(COMMA_STRING)));
			ArrayList<String> methParametersList = new ArrayList<>(
					Arrays.asList((methodSignature.substring(methodSignature.indexOf(LEFT_BRACKET_STRING) + 1,
							methodSignature.lastIndexOf(RIGHT_BRACKET_STRING))).split(COMMA_STRING)));
			processRightLeftTrim(expParametersList);
			processRightLeftTrim(methParametersList);
			if (expParametersList.containsAll(methParametersList))
				return true;
			else {
				for (int i = expParametersArray.length; i > 0; i--) {
					if (expParametersList.get(i - 1).equals(VERSATILE_VALUE_STRING)
							|| (expParametersList.get(i - 1).equals(methParametersList.get(i - 1)))) {
						expParametersList.remove(i - 1);
						methParametersList.remove(i - 1);
					}
				}
				if (expParametersList.size() == 0)
					return true;
				else
					return false;
			}
		}
	}

	private static String buildMethodSignature(Method m) {
		String signatureString = m.toGenericString();
		signatureString = signatureString.replaceAll("java.lang.", EMPTY_STRING);
		return signatureString;
	}

	private static ArrayList<String> getMethodAnnotations(Method m) {
		ArrayList<String> annotationsList = new ArrayList<>();
		if (isMethodOverrriden(m)) {
			String annotationName = "@annotation" + LEFT_BRACKET_STRING + "Override" + RIGHT_BRACKET_STRING;
			annotationsList.add(annotationName);
		}
		for (Annotation an : m.getAnnotations()) {
			String annotationName = "@annotation" + LEFT_BRACKET_STRING + an.getClass().getSimpleName()
					+ RIGHT_BRACKET_STRING;
			annotationsList.add(annotationName);
		}
		return annotationsList;
	}

	private static void processRightLeftTrim(ArrayList<String> list) {
		for (int i = 0; i < list.size(); i++) {
			String word = list.get(i);
			word = rightLeftTrimForSingleWord(word);
			list.set(i, word);
		}
	}

	private static String rightLeftTrimForSingleWord(String word) {
		word = word.replaceAll("^\\s+", EMPTY_STRING);
		word = word.replaceAll("\\s+$", EMPTY_STRING);
		return word;
	}

	private static boolean isMethodOverrriden(Method method) {
		Class<?> declaringClass = method.getDeclaringClass();

		try {
			//
			if (declaringClass.isInterface()) {
				declaringClass.getMethod(method.getName(), method.getParameterTypes());
				return true;
			}
			//
			else {
				if (declaringClass.equals(Object.class)) {
					return false;
				}
				declaringClass.getSuperclass().getMethod(method.getName(), method.getParameterTypes());
				return true;
			}
		} catch (NoSuchMethodException e) {
			for (Class<?> iface : declaringClass.getInterfaces()) {
				try {
					iface.getMethod(method.getName(), method.getParameterTypes());
					return true;
				} catch (NoSuchMethodException exception) {
					return false;
				}
			}
			return false;
		}
	}

	private static boolean evaluateAnnotationsMatching(String expression, ArrayList<String> annotations) {
		if (expression.contains(AT_STRING)) {
			String annotationName = expression.substring(expression.indexOf(AT_STRING),
					expression.indexOf(RIGHT_BRACKET_STRING) + 1);
			if (annotations.contains(annotationName))
				return true;
		}
		return false;
	}
}
