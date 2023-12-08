import provided.JottParser;
import provided.JottTokenizer;
import provided.JottTree;
import provided.Token;

import java.io.FileWriter;
import java.util.ArrayList;

public class Jott {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Jott args = [input .jott file] [output file] [language of output file]");
            System.exit(1);
        }

        if (!args[0].endsWith(".jott")) {
            System.err.println("Input file must end in .jott");
            System.exit(1);
        }

        try {
            ArrayList<Token> programTokens = JottTokenizer.tokenize(args[0]);
            JottTree programTree = JottParser.parse(programTokens);
            if (programTree.validateTree()) {
                FileWriter writer = new FileWriter(args[1]);
                if (args[2].equals("Jott")) {
                    if (args[1].endsWith(".jott")) {
                        writer.write(programTree.convertToJott());
                    }
                    else {
                        System.err.println("Output file must end in .jott to convert to Jott");
                        System.exit(1);
                    }
                }
                else if (args[2].equals("Java")) {
                    if (args[1].endsWith(".java")) {
                        String[] outputDir = args[1].split("/");
                        String outputFile = outputDir[outputDir.length-1];
                        writer.write(programTree.convertToJava(outputFile.substring(0, outputFile.length() - 5)));
                    }
                    else {
                        System.err.println("Output file must end in .java to convert to Java");
                        System.exit(1);
                    }
                }
                else if (args[2].equals("C")) {
                    if (args[1].endsWith(".c")) {
                        writer.write(programTree.convertToC());
                    }
                    else {
                        System.err.println("Output file must end in .c to convert to C");
                        System.exit(1);
                    }
                }
                else if (args[2].equals("Python")) {
                    if (args[1].endsWith(".py")) {
                        writer.write(programTree.convertToPython());
                    }
                    else {
                        System.err.println("Output file must end in .py to convert to Python");
                        System.exit(1);
                    }
                }
                else {
                    System.err.println("Language of output file must be Jott, Java, C, or Python");
                }
                writer.close();
            }
            else {
                System.err.println("Select a semantically valid .jott input file");
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
