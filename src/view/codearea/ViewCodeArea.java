package view.codearea;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//import java.util.concurrent.Flow.Subscription;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import com.jfoenix.controls.JFXTabPane;

import controler.IControler;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import de.jensd.fx.glyphs.octicons.OctIconView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import view.childInterface.AbstractChild;
import view.principal.IViewMain;
import view.principal.ViewMain;

public class ViewCodeArea  {

	private static final String[] KEYWORDS = new String[] { "_FILE__", "and", "def", "end", "in", "or", "self",
			"unless", "__ENCODING__", "__LINE__", "begin", "defined?", "ensure", "module", "redo", "super", "until",
			"BEGIN", "break", "do", "false", "next", "rescue", "then", "when", "END", "case", "else", "for", "nil",
			"retry", "true", "while", "alias", "class", "elsif", "if", "not", "return", "undef", "yield" };
	static String t = "\\d[\\d|_]*";
	private static final String NUMBER_PATTERN = "(([^(a-zA-Z0-9)]?|^)\\." + t + ")|(([^(a-zA-Z0-9)]?|^)" + t
			+ "\\.[\\d|_]*[eE]?\\d*)|(([^(a-zA-Z0-9)]?|^)0[a-zA-Z0-9]*)|(([^(a-zA-Z0-9)]?|^)" + t + "[eE]?\\d*)";
	private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"|\'([^\'\\\\]|\\\\.)*\'";
	private static final String COMMENT_PATTERN = "#[^\\n]*|=begin([\\s\\S]*?)=end";

	private static final Pattern PATTERN = Pattern.compile("(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "|(?<PAREN>"
			+ PAREN_PATTERN + ")" + "|(?<BRACE>" + BRACE_PATTERN + ")" + "|(?<BRACK>" + BRACKET_PATTERN + ")"
			+ "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")" + "|(?<STRING>" + STRING_PATTERN + ")" + "|(?<COMMENT>"
			+ COMMENT_PATTERN + ")" + "|(?<NUMBER>" + NUMBER_PATTERN + ")");

	private CodeArea codeArea;
	private ExecutorService executor;
	private TabPane tabPane;

	public ViewCodeArea(IViewMain viewMain, IControler controler) {

		executor = Executors.newSingleThreadExecutor();

		codeArea = new CodeArea();
		codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

		
		ChangeListener<? super String> a = (obs, oldText, newText) -> {
			String t = newText.replaceAll("[\\t ]", " ");
			codeArea.replaceText(t);
			codeArea.setStyleSpans(0, computeHighlighting(t));
			
			controler.eventSetCode(t);
		};

		codeArea.textProperty().addListener(a);

		codeArea.setId("codearea");

		tabPane = new TabPane();
		tabPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		tabPane.setPrefSize(600, 600);
		
		Tab tab = new Tab();
		tab.setText("Scene 0");
		OctIconView m = new OctIconView();
		m.setStyleClass("icoTabRuby");
		
		tab.setGraphic(m);
		tab.setContent(codeArea);
		tabPane.getTabs().add(tab);
		tabPane.setManaged(false);
	}
	
	public Node getNode() {
		return tabPane;
	}
	
	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {
			String styleClass = matcher.group("KEYWORD") != null ? "keyword"
					: matcher.group("PAREN") != null ? "paren"
							: matcher.group("BRACE") != null ? "brace"
									: matcher.group("BRACK") != null ? "brack"
											: matcher.group("SEMICOLON") != null ? "semicolon"
													: matcher.group("STRING") != null ? "string"
															: matcher.group("COMMENT") != null ? "comment"
																	: matcher.group("NUMBER") != null ? "number" : null;
			/* never happens */ assert styleClass != null;

			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();

		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}


	
}
