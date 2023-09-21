package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author Andy Malak, Makenzie Dorsey, Tristan Lincoln, Mya Richardson, Uzo Ukekwe
 **/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class JottTokenizer {

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename){

		ArrayList<String> inputList = new ArrayList<>();
		ArrayList<Token> tokens = new ArrayList<>();

		try {
			File file = new File(filename);
			Scanner scan = new Scanner(file);

			while (scan.hasNextLine()) {
				// get line and add to inputList, include \n
				// char because .newLine() consumes it
				String line = scan.nextLine() + "\n";
				inputList.add(line);
			}
			scan.close();

		} catch (FileNotFoundException e) {
			System.err.println("File Not Found");
			return null;
		}

		// parse lines in inputList
		for (int i = 0; i < inputList.size(); i++) {

			int j = 0;
			boolean commentFlag = false;
			while (j < inputList.get(i).length()) {
				// current character of the string line
				String curr = String.valueOf(inputList.get(i).charAt(j));

				// if comment & not new line, continue
				if (commentFlag && !curr.matches("\n")) {
					j++;
					continue;
				}

				// if blank, continue
				if (curr.isBlank()) {
					// if new line, end comment
					if (curr.matches("\n")) {
						commentFlag = false;
					}
					j++;
					continue;

				// if #, begin comment and continue
				} else if (curr.equals("#")) {
					commentFlag = true;
					j++;
					continue;

				// if letter, tokenize id/keyword
				} else if (curr.matches("^[a-zA-Z]+$")) {
					StringBuilder tokenStr = new StringBuilder();
					// add first letter to token
					do {
						tokenStr.append(curr);
						j++;
						curr = String.valueOf(inputList.get(i).charAt(j));
					}
					// add additional chars to token if they're letters or digits
					while (j < inputList.get(i).length() && curr.matches("^[a-zA-Z0-9]+$"));
					tokens.add(new Token(tokenStr.toString(), filename, i, TokenType.ID_KEYWORD));

				// if !, tokenize notEquals
				} else if (curr.equals("!")) {
					// only success path is having an equal sign as the next char
					if (j+1 < inputList.get(i).length() && inputList.get(i).charAt(j+1) == '=') {
						tokens.add(new Token("!=", filename, i, TokenType.REL_OP));
						j += 2;
					}
					else {
						reportError(curr, filename, i);
						return null;
					}

				// if ", tokenize string
				} else if (curr.equals("\"")) {
					StringBuilder tokenStr = new StringBuilder();
					// add opening quotation mark to token
					do {
						tokenStr.append(curr);
						j++;
						curr = String.valueOf(inputList.get(i).charAt(j));
					}
					// add additional chars to token if they're letters, digits, or spaces
					while (j < inputList.get(i).length() && curr.matches("^[a-zA-Z0-9 ]+$"));
					// if the first non-letter/digit/space is the closing quote, create the token
					if (curr.equals("\"")) {
						tokenStr.append(curr);
						tokens.add(new Token(tokenStr.toString(), filename, i, TokenType.STRING));
						j++;
					}
					else {
						reportError(curr, filename, i);
						return null;
					}
				}

				// makes number token
				if (curr.equals(".")){
					StringBuilder num1 = new StringBuilder();
					num1.append("0");
					num1.append(curr);
					j++;

					boolean digitFlag = false;
					while (true){
						if (Character.isDigit(inputList.get(i).charAt(j))){
							num1.append(curr);
							j++;
							digitFlag = true;
						}
						else{
							if (!digitFlag){
								System.out.println("Expecting a digit, got " + inputList.get(i).charAt(j));
								System.exit(0);
							}
							Token tok = new Token(num1.toString(), filename, i, TokenType.NUMBER);
							tokens.add(tok);
							j++;
							break;
						}
					continue;

				}

				// makes number token
				if (Character.isDigit(curr.charAt(0))) {
					StringBuilder num = new StringBuilder();
					num.append(curr);
					j++;

					boolean decimalFlag = false;
					while (Character.isDigit(inputList.get(i).charAt(j)) || inputList.get(i).charAt(j) == '.') {
						if (inputList.get(i).charAt(j) == '.'){
							if (decimalFlag){
								System.out.println("Expecting a digit, got " + inputList.get(i).charAt(j));
								System.exit(0);
							}
							else{
								decimalFlag = true;
							}
						}
						num.append(curr);
						j++;

					}
					Token tok = new Token(num.toString(), filename, i, TokenType.NUMBER);
					tokens.add(tok);
					j++;

						/*
						if (Character.isDigit(inputList.get(i).charAt(j)) || inputList.get(i).charAt(j) == '.') {

							num.append(curr);
							j++;
						} else if (curr.isBlank()) {
							Token tok = new Token(num.toString(), filename, i, TokenType.NUMBER);
							tokens.add(tok);
							j++;
							break;
						} else {
							System.out.println("Expecting a digit, got " + inputList.get(i).charAt(j));
							System.exit(0);
						}
						*/
					}
				}
					// j++;
			}
		}

		return tokens;
	}

	private static void reportError(String invalidToken, String filename, int lineNumber) {
		System.err.printf("Syntax Error\nInvalid token \"%s\"\n%s:%d\n", invalidToken, filename, lineNumber);
	}
}
