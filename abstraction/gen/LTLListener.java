// Generated from /media/angelo/WorkData/git/A-Tool-for-Verifying-Strategic-Properties-in-MAS-with-Imperfect-Information/abstraction/src/main/antlr4/LTL.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LTLParser}.
 */
public interface LTLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code Evaluation}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void enterEvaluation(LTLParser.EvaluationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Evaluation}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void exitEvaluation(LTLParser.EvaluationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Disjunction}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void enterDisjunction(LTLParser.DisjunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Disjunction}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void exitDisjunction(LTLParser.DisjunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Implies}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void enterImplies(LTLParser.ImpliesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Implies}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void exitImplies(LTLParser.ImpliesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void enterNegation(LTLParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Negation}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void exitNegation(LTLParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Next}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void enterNext(LTLParser.NextContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Next}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void exitNext(LTLParser.NextContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Eventually}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void enterEventually(LTLParser.EventuallyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Eventually}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void exitEventually(LTLParser.EventuallyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Conjunction}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void enterConjunction(LTLParser.ConjunctionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Conjunction}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void exitConjunction(LTLParser.ConjunctionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Grouping}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void enterGrouping(LTLParser.GroupingContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Grouping}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void exitGrouping(LTLParser.GroupingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Always}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void enterAlways(LTLParser.AlwaysContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Always}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void exitAlways(LTLParser.AlwaysContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Until}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void enterUntil(LTLParser.UntilContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Until}
	 * labeled alternative in {@link LTLParser#ltlExpr}.
	 * @param ctx the parse tree
	 */
	void exitUntil(LTLParser.UntilContext ctx);
	/**
	 * Enter a parse tree produced by {@link LTLParser#atomExpr}.
	 * @param ctx the parse tree
	 */
	void enterAtomExpr(LTLParser.AtomExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link LTLParser#atomExpr}.
	 * @param ctx the parse tree
	 */
	void exitAtomExpr(LTLParser.AtomExprContext ctx);
}