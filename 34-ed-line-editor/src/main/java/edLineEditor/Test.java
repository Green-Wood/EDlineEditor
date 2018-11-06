package edLineEditor;


import edLineEditor.Antlr.ArrayLexer;
import edLineEditor.Antlr.ArrayParser;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;

public class Test {
    public static void main(String[] args) throws Exception{
        ANTLRInputStream input = new ANTLRInputStream(System.in);

        ArrayLexer lexer = new ArrayLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        ArrayParser parser = new ArrayParser(tokens);

        ParseTree tree = parser.init();

        System.out.println(tree.toStringTree(parser));

    }
}
