// Generated from /media/angelo/WorkData/git/A-Tool-for-Verifying-Strategic-Properties-in-MAS-with-Imperfect-Information/abstraction/src/main/antlr4/ATL.g4 by ANTLR 4.9.1
package fr.univ_evry.ibisc.atl.parser;
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
	 * Visit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(ATLParser.NegationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Strategic}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStrategic(ATLParser.StrategicContext ctx);
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
	 * Visit a parse tree produced by the {@code Grouping}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitGrouping(ATLParser.GroupingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Always}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlways(ATLParser.AlwaysContext ctx);
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