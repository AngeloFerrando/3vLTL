// Generated from /media/angelo/WorkData/git/A-Tool-for-Verifying-Strategic-Properties-in-MAS-with-Imperfect-Information/abstraction/src/main/antlr4/LTL.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LTLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, ATOM=19, BOOLEAN=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
			"T__17", "ATOM", "BOOLEAN"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'!'", "'not'", "'next'", "'X'", "'eventually'", "'F'", "'always'", 
			"'G'", "'until'", "'U'", "'&&'", "'and'", "'||'", "'or'", "'->'", "'implies'", 
			"'('", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "ATOM", "BOOLEAN"
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


	public LTLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "LTL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\26\u0084\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4\3"+
		"\4\3\4\3\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13"+
		"\3\f\3\f\3\f\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3"+
		"\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3"+
		"\24\7\24u\n\24\f\24\16\24x\13\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3"+
		"\25\3\25\5\25\u0083\n\25\2\2\26\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23"+
		"\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26\3\2\4\5\2C"+
		"\\aac|\6\2\62;C\\aac|\2\u0085\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2"+
		"\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2"+
		"\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\3"+
		"+\3\2\2\2\5-\3\2\2\2\7\61\3\2\2\2\t\66\3\2\2\2\138\3\2\2\2\rC\3\2\2\2"+
		"\17E\3\2\2\2\21L\3\2\2\2\23N\3\2\2\2\25T\3\2\2\2\27V\3\2\2\2\31Y\3\2\2"+
		"\2\33]\3\2\2\2\35`\3\2\2\2\37c\3\2\2\2!f\3\2\2\2#n\3\2\2\2%p\3\2\2\2\'"+
		"r\3\2\2\2)\u0082\3\2\2\2+,\7#\2\2,\4\3\2\2\2-.\7p\2\2./\7q\2\2/\60\7v"+
		"\2\2\60\6\3\2\2\2\61\62\7p\2\2\62\63\7g\2\2\63\64\7z\2\2\64\65\7v\2\2"+
		"\65\b\3\2\2\2\66\67\7Z\2\2\67\n\3\2\2\289\7g\2\29:\7x\2\2:;\7g\2\2;<\7"+
		"p\2\2<=\7v\2\2=>\7w\2\2>?\7c\2\2?@\7n\2\2@A\7n\2\2AB\7{\2\2B\f\3\2\2\2"+
		"CD\7H\2\2D\16\3\2\2\2EF\7c\2\2FG\7n\2\2GH\7y\2\2HI\7c\2\2IJ\7{\2\2JK\7"+
		"u\2\2K\20\3\2\2\2LM\7I\2\2M\22\3\2\2\2NO\7w\2\2OP\7p\2\2PQ\7v\2\2QR\7"+
		"k\2\2RS\7n\2\2S\24\3\2\2\2TU\7W\2\2U\26\3\2\2\2VW\7(\2\2WX\7(\2\2X\30"+
		"\3\2\2\2YZ\7c\2\2Z[\7p\2\2[\\\7f\2\2\\\32\3\2\2\2]^\7~\2\2^_\7~\2\2_\34"+
		"\3\2\2\2`a\7q\2\2ab\7t\2\2b\36\3\2\2\2cd\7/\2\2de\7@\2\2e \3\2\2\2fg\7"+
		"k\2\2gh\7o\2\2hi\7r\2\2ij\7n\2\2jk\7k\2\2kl\7g\2\2lm\7u\2\2m\"\3\2\2\2"+
		"no\7*\2\2o$\3\2\2\2pq\7+\2\2q&\3\2\2\2rv\t\2\2\2su\t\3\2\2ts\3\2\2\2u"+
		"x\3\2\2\2vt\3\2\2\2vw\3\2\2\2w(\3\2\2\2xv\3\2\2\2yz\7v\2\2z{\7t\2\2{|"+
		"\7w\2\2|\u0083\7g\2\2}~\7h\2\2~\177\7c\2\2\177\u0080\7n\2\2\u0080\u0081"+
		"\7u\2\2\u0081\u0083\7g\2\2\u0082y\3\2\2\2\u0082}\3\2\2\2\u0083*\3\2\2"+
		"\2\5\2v\u0082\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}