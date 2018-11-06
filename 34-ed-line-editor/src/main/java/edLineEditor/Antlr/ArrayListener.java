// Generated from D:/program/AES (software engineering 1)/Java/����ҵ/7e2a49212504424a857076a17e279ec3/34-ed-line-editor/src/main/java/Antlr\Array.g4 by ANTLR 4.7
package edLineEditor.Antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ArrayParser}.
 */
public interface ArrayListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ArrayParser#init}.
	 * @param ctx the parse tree
	 */
	void enterInit(ArrayParser.InitContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArrayParser#init}.
	 * @param ctx the parse tree
	 */
	void exitInit(ArrayParser.InitContext ctx);
	/**
	 * Enter a parse tree produced by {@link ArrayParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(ArrayParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ArrayParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(ArrayParser.ValueContext ctx);
}