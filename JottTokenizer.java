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
				}

				// j++;

			}
		}

		return tokens;
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