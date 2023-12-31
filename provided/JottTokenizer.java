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
		int lineNumber = 1;
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
				
				// if [] {} , ;
				} else if(curr.matches("[\\[\\]\\{\\},;]")) {
					switch (curr) {
						case "[": tokens.add(new Token(curr, filename, lineNumber, TokenType.L_BRACKET));
							break;
							
						case "]": tokens.add(new Token(curr, filename, lineNumber, TokenType.R_BRACKET));
							break;
							
						case "{": tokens.add(new Token(curr, filename, lineNumber, TokenType.L_BRACE));
							break;
							
						case "}": tokens.add(new Token(curr, filename, lineNumber, TokenType.R_BRACE));
							break;
							
						case ",": tokens.add(new Token(curr, filename, lineNumber, TokenType.COMMA));
							break;
							
						case ";": tokens.add(new Token(curr, filename, lineNumber, TokenType.SEMICOLON));
							break;
						default:
							break;
					}
					j++;
				
				// check mathOps
				} else if("[+-*/]".contains(curr)) {
					tokens.add(new Token(curr, filename, lineNumber, TokenType.MATH_OP));
					j++;
				
				// check singular and double colon
				} else if(curr.equals(":")) {
					if(String.valueOf(inputList.get(i).charAt(j+1)).equals(":")) {
						j++;
						tokens.add(new Token("::", filename, lineNumber, TokenType.FC_HEADER));
					} else {
						tokens.add(new Token(":", filename, lineNumber, TokenType.COLON));
					}
					j++;
					
				// if <, > or = -> relOp or Assign
				} else if (curr.matches("[<>=]")) {
					StringBuilder tokenStr = new StringBuilder(curr);
					// single = is Assign
					boolean relOpFlag = !curr.equals("=");

					j++;
					curr = String.valueOf(inputList.get(i).charAt(j));
					// check if <=, >=, or ==
					if (curr.equals("=")){
						tokenStr.append(curr);
						// <=, >=, == are all relOps
						relOpFlag = true;
						j++;
					}

					tokens.add(
							new Token(
									tokenStr.toString(),
									filename,
									lineNumber,
									relOpFlag ? TokenType.REL_OP : TokenType.ASSIGN
							)
					);

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
					tokens.add(new Token(tokenStr.toString(), filename, lineNumber, TokenType.ID_KEYWORD));

					// if !, tokenize notEquals
				} else if (curr.equals("!")) {
					// only success path is having an equal sign as the next char
					if (j+1 < inputList.get(i).length() && inputList.get(i).charAt(j+1) == '=') {
						tokens.add(new Token("!=", filename, lineNumber, TokenType.REL_OP));
						j += 2;
					}
					else {
						reportError(curr, filename, lineNumber);
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
						tokens.add(new Token(tokenStr.toString(), filename, lineNumber, TokenType.STRING));
						j++;
					}
					else {
						reportError(tokenStr.toString(), filename, lineNumber);
						return null;
					}
				}

				// makes number token
				else if (curr.equals(".")){
					StringBuilder num1 = new StringBuilder();
					num1.append(curr);
					j++;

					boolean digitFlag = false;
					while (true){
						char cur = inputList.get(i).charAt(j);
						if (Character.isDigit(inputList.get(i).charAt(j))){
							num1.append(cur);
							j++;
							digitFlag = true;
						}
						else{
							if (!digitFlag){
								num1.append(cur);
								reportError(num1.toString(), filename, lineNumber);
								return null;
							}
							Token tok = new Token(num1.toString(), filename, lineNumber, TokenType.NUMBER);
							tokens.add(tok);
							break;
						}
					}

				}

				// makes number token
				else if (Character.isDigit(curr.charAt(0))) {
					StringBuilder num = new StringBuilder();
					num.append(curr);
					j++;

					boolean decimalFlag = false;
					while (Character.isDigit(inputList.get(i).charAt(j)) || inputList.get(i).charAt(j) == '.') {
						char cur = inputList.get(i).charAt(j);
						if (inputList.get(i).charAt(j) == '.'){
							if (decimalFlag){
								num.append(cur);
								reportError(num.toString(), filename, lineNumber);
								return null;
							}
							else{
								decimalFlag = true;
							}
						}
						num.append(cur);
						j++;

					}
					Token tok = new Token(num.toString(), filename, lineNumber, TokenType.NUMBER);
					tokens.add(tok);
				}

				// char found does not start any possible token
				else {
					reportError(curr, filename, lineNumber);
					return null;
				}
			}
			lineNumber++;
		}
		return tokens;
	}

	private static void reportError(String invalidToken, String filename, int lineNumber) {
		if (invalidToken.equals("\n")) {
			invalidToken = "\\n";
		}
		System.err.printf("Syntax Error\nInvalid token \"%s\"\n%s:%d\n", invalidToken, filename, lineNumber);
	}
}
