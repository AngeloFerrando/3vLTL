// Generated from /media/angelo/WorkData/git/A-Tool-for-Verifying-Strategic-Properties-in-MAS-with-Imperfect-Information/abstraction/src/main/antlr4/ATL.g4 by ANTLR 4.9.1
package package fr.univ_evry.ibisc.atl.parser;;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ATLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, ATOM=21, WS=22;
	public static final int
		RULE_atlExpr = 0, RULE_atomExpr = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"atlExpr", "atomExpr"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'!'", "'not'", "'next'", "'X'", "'eventually'", "'F'", "'always'", 
			"'G'", "'until'", "'U'", "'&&'", "'and'", "'||'", "'or'", "'->'", "'implies'", 
			"'<'", "'>'", "'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, "ATOM", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "ATL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ATLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class AtlExprContext extends ParserRuleContext {
		public AtlExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atlExpr; }
	 
		public AtlExprContext() { }
		public void copyFrom(AtlExprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class EvaluationContext extends AtlExprContext {
		public AtomExprContext child;
		public AtomExprContext atomExpr() {
			return getRuleContext(AtomExprContext.class,0);
		}
		public EvaluationContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitEvaluation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DisjunctionContext extends AtlExprContext {
		public AtlExprContext left;
		public AtlExprContext right;
		public List<AtlExprContext> atlExpr() {
			return getRuleContexts(AtlExprContext.class);
		}
		public AtlExprContext atlExpr(int i) {
			return getRuleContext(AtlExprContext.class,i);
		}
		public DisjunctionContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitDisjunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ImpliesContext extends AtlExprContext {
		public AtlExprContext left;
		public AtlExprContext right;
		public List<AtlExprContext> atlExpr() {
			return getRuleContexts(AtlExprContext.class);
		}
		public AtlExprContext atlExpr(int i) {
			return getRuleContext(AtlExprContext.class,i);
		}
		public ImpliesContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitImplies(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NegationContext extends AtlExprContext {
		public AtlExprContext child;
		public AtlExprContext atlExpr() {
			return getRuleContext(AtlExprContext.class,0);
		}
		public NegationContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitNegation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class StrategicContext extends AtlExprContext {
		public Token group;
		public AtlExprContext child;
		public TerminalNode ATOM() { return getToken(ATLParser.ATOM, 0); }
		public AtlExprContext atlExpr() {
			return getRuleContext(AtlExprContext.class,0);
		}
		public StrategicContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitStrategic(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NextContext extends AtlExprContext {
		public AtlExprContext child;
		public AtlExprContext atlExpr() {
			return getRuleContext(AtlExprContext.class,0);
		}
		public NextContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitNext(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EventuallyContext extends AtlExprContext {
		public AtlExprContext child;
		public AtlExprContext atlExpr() {
			return getRuleContext(AtlExprContext.class,0);
		}
		public EventuallyContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitEventually(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ConjunctionContext extends AtlExprContext {
		public AtlExprContext left;
		public AtlExprContext right;
		public List<AtlExprContext> atlExpr() {
			return getRuleContexts(AtlExprContext.class);
		}
		public AtlExprContext atlExpr(int i) {
			return getRuleContext(AtlExprContext.class,i);
		}
		public ConjunctionContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitConjunction(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class GroupingContext extends AtlExprContext {
		public AtlExprContext atlExpr() {
			return getRuleContext(AtlExprContext.class,0);
		}
		public GroupingContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitGrouping(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlwaysContext extends AtlExprContext {
		public AtlExprContext child;
		public AtlExprContext atlExpr() {
			return getRuleContext(AtlExprContext.class,0);
		}
		public AlwaysContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitAlways(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UntilContext extends AtlExprContext {
		public AtlExprContext left;
		public AtlExprContext right;
		public List<AtlExprContext> atlExpr() {
			return getRuleContexts(AtlExprContext.class);
		}
		public AtlExprContext atlExpr(int i) {
			return getRuleContext(AtlExprContext.class,i);
		}
		public UntilContext(AtlExprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitUntil(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtlExprContext atlExpr() throws RecognitionException {
		return atlExpr(0);
	}

	private AtlExprContext atlExpr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		AtlExprContext _localctx = new AtlExprContext(_ctx, _parentState);
		AtlExprContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_atlExpr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
			case T__1:
				{
				_localctx = new NegationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(5);
				_la = _input.LA(1);
				if ( !(_la==T__0 || _la==T__1) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(6);
				((NegationContext)_localctx).child = atlExpr(11);
				}
				break;
			case T__2:
			case T__3:
				{
				_localctx = new NextContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(7);
				_la = _input.LA(1);
				if ( !(_la==T__2 || _la==T__3) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(8);
				((NextContext)_localctx).child = atlExpr(10);
				}
				break;
			case T__4:
			case T__5:
				{
				_localctx = new EventuallyContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(9);
				_la = _input.LA(1);
				if ( !(_la==T__4 || _la==T__5) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(10);
				((EventuallyContext)_localctx).child = atlExpr(9);
				}
				break;
			case T__6:
			case T__7:
				{
				_localctx = new AlwaysContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(11);
				_la = _input.LA(1);
				if ( !(_la==T__6 || _la==T__7) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(12);
				((AlwaysContext)_localctx).child = atlExpr(8);
				}
				break;
			case T__16:
				{
				_localctx = new StrategicContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(13);
				match(T__16);
				setState(14);
				((StrategicContext)_localctx).group = match(ATOM);
				setState(15);
				match(T__17);
				setState(16);
				((StrategicContext)_localctx).child = atlExpr(3);
				}
				break;
			case T__18:
				{
				_localctx = new GroupingContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(17);
				match(T__18);
				setState(18);
				atlExpr(0);
				setState(19);
				match(T__19);
				}
				break;
			case ATOM:
				{
				_localctx = new EvaluationContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(21);
				((EvaluationContext)_localctx).child = atomExpr();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(38);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(36);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new UntilContext(new AtlExprContext(_parentctx, _parentState));
						((UntilContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_atlExpr);
						setState(24);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(25);
						_la = _input.LA(1);
						if ( !(_la==T__8 || _la==T__9) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(26);
						((UntilContext)_localctx).right = atlExpr(8);
						}
						break;
					case 2:
						{
						_localctx = new ConjunctionContext(new AtlExprContext(_parentctx, _parentState));
						((ConjunctionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_atlExpr);
						setState(27);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(28);
						_la = _input.LA(1);
						if ( !(_la==T__10 || _la==T__11) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(29);
						((ConjunctionContext)_localctx).right = atlExpr(7);
						}
						break;
					case 3:
						{
						_localctx = new DisjunctionContext(new AtlExprContext(_parentctx, _parentState));
						((DisjunctionContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_atlExpr);
						setState(30);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(31);
						_la = _input.LA(1);
						if ( !(_la==T__12 || _la==T__13) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(32);
						((DisjunctionContext)_localctx).right = atlExpr(6);
						}
						break;
					case 4:
						{
						_localctx = new ImpliesContext(new AtlExprContext(_parentctx, _parentState));
						((ImpliesContext)_localctx).left = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_atlExpr);
						setState(33);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(34);
						_la = _input.LA(1);
						if ( !(_la==T__14 || _la==T__15) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(35);
						((ImpliesContext)_localctx).right = atlExpr(5);
						}
						break;
					}
					} 
				}
				setState(40);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class AtomExprContext extends ParserRuleContext {
		public TerminalNode ATOM() { return getToken(ATLParser.ATOM, 0); }
		public AtomExprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_atomExpr; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ATLVisitor ) return ((ATLVisitor<? extends T>)visitor).visitAtomExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AtomExprContext atomExpr() throws RecognitionException {
		AtomExprContext _localctx = new AtomExprContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_atomExpr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			match(ATOM);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0:
			return atlExpr_sempred((AtlExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean atlExpr_sempred(AtlExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		case 2:
			return precpred(_ctx, 5);
		case 3:
			return precpred(_ctx, 4);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\30.\4\2\t\2\4\3\t"+
		"\3\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\5\2\31\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2\'"+
		"\n\2\f\2\16\2*\13\2\3\3\3\3\3\3\2\3\2\4\2\4\2\n\3\2\3\4\3\2\5\6\3\2\7"+
		"\b\3\2\t\n\3\2\13\f\3\2\r\16\3\2\17\20\3\2\21\22\2\65\2\30\3\2\2\2\4+"+
		"\3\2\2\2\6\7\b\2\1\2\7\b\t\2\2\2\b\31\5\2\2\r\t\n\t\3\2\2\n\31\5\2\2\f"+
		"\13\f\t\4\2\2\f\31\5\2\2\13\r\16\t\5\2\2\16\31\5\2\2\n\17\20\7\23\2\2"+
		"\20\21\7\27\2\2\21\22\7\24\2\2\22\31\5\2\2\5\23\24\7\25\2\2\24\25\5\2"+
		"\2\2\25\26\7\26\2\2\26\31\3\2\2\2\27\31\5\4\3\2\30\6\3\2\2\2\30\t\3\2"+
		"\2\2\30\13\3\2\2\2\30\r\3\2\2\2\30\17\3\2\2\2\30\23\3\2\2\2\30\27\3\2"+
		"\2\2\31(\3\2\2\2\32\33\f\t\2\2\33\34\t\6\2\2\34\'\5\2\2\n\35\36\f\b\2"+
		"\2\36\37\t\7\2\2\37\'\5\2\2\t !\f\7\2\2!\"\t\b\2\2\"\'\5\2\2\b#$\f\6\2"+
		"\2$%\t\t\2\2%\'\5\2\2\7&\32\3\2\2\2&\35\3\2\2\2& \3\2\2\2&#\3\2\2\2\'"+
		"*\3\2\2\2(&\3\2\2\2()\3\2\2\2)\3\3\2\2\2*(\3\2\2\2+,\7\27\2\2,\5\3\2\2"+
		"\2\5\30&(";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}