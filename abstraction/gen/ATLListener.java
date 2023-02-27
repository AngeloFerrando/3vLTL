// Generated from /media/angelo/WorkData/git/A-Tool-for-Verifying-Strategic-Properties-in-MAS-with-Imperfect-Information/abstraction/src/main/antlr4/ATL.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ATLParser}.
 */
public interface ATLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code Evaluation}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterEvaluation(ATLParser.EvaluationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Evaluation}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitEvaluation(ATLParser.EvaluationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Disjunction}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterDisjunction(ATLParser.DisjunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Disjunction}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitDisjunction(ATLParser.DisjunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Implies}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterImplies(ATLParser.ImpliesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Implies}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitImplies(ATLParser.ImpliesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterNegation(ATLParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitNegation(ATLParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Strategic}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterStrategic(ATLParser.StrategicContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Strategic}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitStrategic(ATLParser.StrategicContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Next}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterNext(ATLParser.NextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Next}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitNext(ATLParser.NextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Eventually}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterEventually(ATLParser.EventuallyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Eventually}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitEventually(ATLParser.EventuallyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Conjunction}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterConjunction(ATLParser.ConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Conjunction}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitConjunction(ATLParser.ConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Grouping}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterGrouping(ATLParser.GroupingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Grouping}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitGrouping(ATLParser.GroupingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Always}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterAlways(ATLParser.AlwaysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Always}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitAlways(ATLParser.AlwaysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Until}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void enterUntil(ATLParser.UntilContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Until}
	 * labeled alternative in {@link ATLParser#atlExpr}.
	 * @param ctx the parse tree
	 */
	void exitUntil(ATLParser.UntilContext ctx);
	/**
	 * Enter a parse tree produced by {@link ATLParser#atomExpr}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpr(ATLParser.AtomExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link ATLParser#atomExpr}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpr(ATLParser.AtomExprContext ctx);
}