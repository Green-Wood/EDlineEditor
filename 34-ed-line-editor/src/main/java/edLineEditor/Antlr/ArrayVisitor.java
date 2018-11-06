// Generated from D:/program/AES (software engineering 1)/Java/����ҵ/7e2a49212504424a857076a17e279ec3/34-ed-line-editor/src/main/java/Antlr\Array.g4 by ANTLR 4.7
package edLineEditor.Antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ArrayParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ArrayVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ArrayParser#init}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInit(ArrayParser.InitContext ctx);
	/**
	 * Visit a parse tree produced by {@link ArrayParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(ArrayParser.ValueContext ctx);
}