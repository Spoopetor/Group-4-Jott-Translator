package provided;

/**
 * This class is responsible for tokenizing Jott code.
 * 
 * @author 
 **/

import java.util.ArrayList;
import java.util.Arrays;

public class JottTokenizer {

	public static final String NEWLINE = System.getProperty("line.separator");

	/**
     * Takes in a filename and tokenizes that file into Tokens
     * based on the rules of the Jott Language
     * @param filename the name of the file to tokenize; can be relative or absolute path
     * @return an ArrayList of Jott Tokens
     */
    public static ArrayList<Token> tokenize(String filename){

		ArrayList<Token> tokens = new ArrayList<>();
		ArrayList<String> charList = new ArrayList<>(Arrays.asList(filename.split("")));

		boolean commentFlag = false;
		while (charList.size() > 0) {

			String curr = charList.get(0);

			if (commentFlag && !curr.equals(NEWLINE)) {
				// throw out all characters within active comment
				charList.remove(0);
				continue;
			}

			if (curr.isBlank()) {
				if (curr.equals(NEWLINE)) {
					// upon new line, all potential line comments end
					commentFlag = false;
				}
				// throw out any and all whitespace
				charList.remove(0);
				continue;

			} else if (curr.equals("#")) {
				// if #, line comment begins
				commentFlag = true;
			}

			// remove character token from charList
			charList.remove(0);
		}

		return tokens;
	}
}