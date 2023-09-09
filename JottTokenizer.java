package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.util.ArrayList;

public class JottTokenizer {

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename){
		return null;
	}

	private void tokenizeIdKeyword(String inputChars, ArrayList<Token> tokens) {
		// initialize new token with first input char
		// while input chars are letters or digits
		// 		add each char to new token
		// after breaking out of loop, add token to tokens
	}

	private boolean tokenizeNotEquals(String inputChars, ArrayList<Token> tokens) {
		// if input char 0 == ! and input char 1 == =
		// 		add != token to tokens
		//		return true
		// else
		//		print to System.err
		//		return false (tokenize() can check for false to return null)
		return true;
	}

	private boolean tokenizeString(String inputChars, ArrayList<Token> tokens) {
		// initialize new token with quotation mark
		// while input chars are letters, digits, or spaces
		// 		add each char to new token
		// if next char is quotation mark
		//		add string token to tokens
		// 		return true
		// else
		//		print to System. err
		//		return false (tokenize() can check for false to return null)
		return true;
	}
}