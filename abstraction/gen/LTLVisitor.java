// Generated from /media/angelo/WorkData/git/A-Tool-for-Verifying-Strategic-Properties-in-MAS-with-Imperfect-Information/abstraction/src/main/antlr4/LTL.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LTLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LTLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code Evaluation}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvaluation(LTLParser.EvaluationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Disjunction}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunction(LTLParser.DisjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Implies}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplies(LTLParser.ImpliesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(LTLParser.NegationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Next}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNext(LTLParser.NextContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Eventually}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventually(LTLParser.EventuallyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Conjunction}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunction(LTLParser.ConjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Grouping}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrouping(LTLParser.GroupingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Always}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlways(LTLParser.AlwaysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Until}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntil(LTLParser.UntilContext ctx);
	/**
	 * Visit a parse tree produced by {@link LTLParser#atomExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomExpr(LTLParser.AtomExprContext ctx);
}