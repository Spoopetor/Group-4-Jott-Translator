package provided;

import java.util.ArrayList;

public class JottValidator {
    /**
     * Validates all the nodes of a JottTree through semantic analysis
     * @param tree JottTree to analyze for semantic validity
     * @return true if tree is semantically valid
     */
    public static boolean validate(JottTree tree) { return tree.validateTree(); }
}
