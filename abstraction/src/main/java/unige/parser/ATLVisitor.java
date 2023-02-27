package unige.parser;// Generated from /media/angelo/WorkData/git/A-Tool-for-Verifying-Strategic-Properties-in-MAS-with-Imperfect-Information/abstraction/src/main/antlr4/ATL.g4 by ANTLR 4.9.2
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ATLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ATLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code Evaluation}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvaluation(ATLParser.EvaluationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Universal}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUniversal(ATLParser.UniversalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(ATLParser.NegationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Grouping}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrouping(ATLParser.GroupingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Disjunction}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDisjunction(ATLParser.DisjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Implies}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImplies(ATLParser.ImpliesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Next}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNext(ATLParser.NextContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Eventually}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEventually(ATLParser.EventuallyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Conjunction}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConjunction(ATLParser.ConjunctionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Existential}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistential(ATLParser.ExistentialContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Always}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlways(ATLParser.AlwaysContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Release}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRelease(ATLParser.ReleaseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Until}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUntil(ATLParser.UntilContext ctx);
	/**
	 * Visit a parse tree produced by {@link ATLParser#atomExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAtomExpr(ATLParser.AtomExprContext ctx);
}