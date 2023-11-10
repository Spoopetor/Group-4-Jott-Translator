package testers;

import nodes.BoolNode;
import provided.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class JottValidatorTester {
    ArrayList<TestCase> testCases;

    private static class TestCase{
        String testName;
        String fileName;
        boolean error;

        public TestCase(String testName, String fileName, boolean error) {
            this.testName = testName;
            this.fileName = fileName;
            this.error = error;
        }
    }

    private boolean tokensEqualNoFileData(Token t1, Token t2){
        return t1.getTokenType() == t2.getTokenType() &&
                t1.getToken().equals(t2.getToken());
    }

    private void createTestCases(){
        this.testCases = new ArrayList<>();
        testCases.add(new TestCase("func call with invalid param (error)", "funcCallParamInvalid.jott", true));
        testCases.add(new TestCase("func called but not defined (error)", "funcNotDefined.jott", true));
        testCases.add(new TestCase("func returns mismatched type in expr (error)", "funcReturnInExpr.jott", true));
        testCases.add(new TestCase("func returns wrong type (error)", "funcWrongParamType.jott", true));
        testCases.add(new TestCase("hello world", "helloWorld.jott", false ));
        testCases.add(new TestCase("if stmt returns", "ifStmtReturns.jott", false));
        testCases.add(new TestCase("larger valid", "largerValid.jott", false));
        testCases.add(new TestCase("main defined with wrong return (error)", "mainReturnNotInt.jott", true));
        testCases.add(new TestCase("func return type doesn't match var asmt type (error)", "mismatchedReturn.jott", true));
        testCases.add(new TestCase("missing param in func call (error)", "missingFuncParams.jott", true));
        testCases.add(new TestCase("missing main func (error)", "missingMain.jott", true));
        testCases.add(new TestCase("missing return from non-void func (error)", "missingReturn.jott", true));
        testCases.add(new TestCase("no return in if branch (error)", "noReturnIf.jott", true));
        testCases.add(new TestCase("no return outside of while loop (error)", "noReturnWhile.jott", true));
        testCases.add(new TestCase("provided writeup example1", "providedExample1.jott", false));
        testCases.add(new TestCase("id returned in void func (error)", "returnId.jott", true));
        testCases.add(new TestCase("valid loop", "validLoop.jott", false));
        testCases.add(new TestCase("func with return called in void func (error)", "voidReturn.jott", true));
        testCases.add(new TestCase("while keyword used as id (error)", "whileKeyword.jott", true));
    }

    private boolean validatorTest(TestCase test, String originalJottCode) {
        try {
            ArrayList<Token> tokens = JottTokenizer.tokenize("phase3testcases/" + test.fileName);

            if (tokens == null) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected a list of tokens, but got null");
                System.err.println("\t\tPlease verify your tokenizer is working properly");
                return false;
            }
            System.out.println(tokenListString(tokens));
            ArrayList<Token> cpyTokens = new ArrayList<>(tokens);
            JottTree root = JottParser.parse(tokens);

            if (!test.error && root == null) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected a JottTree and got null");
                return false;
            } else if (test.error && root == null) {
                return true;
            }

            System.out.println("Original Jott Code:\n");
            System.out.println(originalJottCode);
            System.out.println();

            String jottCode = root.convertToJott();
            System.out.println("Resulting Jott Code:\n");
            System.out.println(jottCode);

            try {
                FileWriter writer = new FileWriter("phase3testcases/validatorTestTemp.jott");
                if (jottCode == null) {
                    System.err.println("\tFailed Test: " + test.testName);
                    System.err.println("Expected a program string; got null");
                    return false;
                }
                writer.write(jottCode);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Boolean validRoot = JottValidator.validate(root);
            if (!test.error && !validRoot) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected semantic validity and caught exception");
                return false;
            }
            else if (test.error && validRoot) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("\t\tExpected semantic invalidity and but caught no exceptions");
                return false;
            }

            ArrayList<Token> newTokens = JottTokenizer.tokenize("phase3testcases/validatorTestTemp.jott");

            if (newTokens == null) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("Tokenization of files dot not match.");
                System.err.println("Similar files should have same tokenization.");
                System.err.println("Expected: " + tokenListString(tokens));
                System.err.println("Got: null");
                return false;
            }

            if (newTokens.size() != cpyTokens.size()) {
                System.err.println("\tFailed Test: " + test.testName);
                System.err.println("Tokenization of files dot not match.");
                System.err.println("Similar files should have same tokenization.");
                System.err.println("Expected: " + tokenListString(cpyTokens));
                System.err.println("Got:    : " + tokenListString(newTokens));
                return false;
            }

            for (int i = 0; i < newTokens.size(); i++) {
                Token n = newTokens.get(i);
                Token t = cpyTokens.get(i);

                if (!tokensEqualNoFileData(n, t)) {
                    System.err.println("\tFailed Test: " + test.testName);
                    System.err.println("Token mismatch: Tokens do not match.");
                    System.err.println("Similar files should have same tokenization.");
                    System.err.println("Expected: " + tokenListString(cpyTokens));
                    System.err.println("Got     : " + tokenListString(newTokens));
                    return false;
                }
            }
            return true;
        }
        catch (Exception e) {
            System.err.println("\tFailed Test: " + test.testName);
            System.err.println("Unknown Exception occured.");
            e.printStackTrace();
            return false;
        }
    }

    private String tokenListString(ArrayList<Token> tokens){
        StringBuilder sb = new StringBuilder();
        for (Token t: tokens) {
            sb.append(t.getToken());
            sb.append(":");
            sb.append(t.getTokenType().toString());
            sb.append(" ");
        }
        return sb.toString();
    }

    private boolean runTest(TestCase test){
        System.out.println("Running Test: " + test.testName);
        String orginalJottCode;
        try {
            orginalJottCode = new String(
                    Files.readAllBytes(Paths.get("phase3testcases/" + test.fileName)));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return validatorTest(test, orginalJottCode);

    }

    public static void main(String[] args) {
        System.out.println("NOTE: System.err may print at the end. This is fine.");
        JottValidatorTester tester = new JottValidatorTester();

        int numTests = 0;
        int passedTests = 0;
        tester.createTestCases();
        for(TestCase test: tester.testCases){
            numTests++;
            if(tester.runTest(test)){
                passedTests++;
                System.out.println("\tPassed\n");
            }
            else{
                System.out.println("\tFailed\n");
            }
        }

        System.out.printf("Passed: %d/%d%n", passedTests, numTests);
    }

}
