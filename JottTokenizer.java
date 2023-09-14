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
}