package view.scene;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.jruby.RubyModule;
import org.jruby.embed.ScriptingContainer;
import org.jruby.runtime.builtin.IRubyObject;

import controler.IControler;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.principal.DataModel;
import model.principal.Model;
import model.ruby.GraphNodeData;
import model.sprite.SpriteFactory;

import view.principal.IViewMain;
import view.principal.ViewMain;

public class ViewScene {
	private Pane root;
	private SpriteFactory spriteFactory;
	private Map<String, Node> spriteCache;
	private DoubleProperty ratio;
	private Pane spritePane;

	private Canvas spriteCanvas;
	private GraphicsContext gc;
	private PixelWriter px;
	private int[] buffCache;
	private WritablePixelFormat<IntBuffer> format = PixelFormat.getIntArgbInstance();

	private int COLOR_GREY_75 = toInt(Color.rgb(85, 85, 85));
	private int COLOR_WHITE = -1;
	private double startX, startY, tX, tY;

	public static int toInt(double re, double g, double b, double a) {

		return ((int) (a * 255) << 24) | ((int) (re * 255) << 16) | ((int) (g * 255) << 8) | ((int) (b * 255));
	}

	public static int toInt(javafx.scene.paint.Color c) {
		return toInt(c.getRed(), c.getGreen(), c.getBlue(), c.getOpacity());
	}

	public ViewScene(IViewMain pViewMain, IControler pControler) {
		root = new StackPane();
		root.setManaged(false);

		spritePane = new Pane();
		spritePane.setManaged(false);

		spriteCanvas = new Canvas();

		spriteCanvas.setManaged(false);
		gc = spriteCanvas.getGraphicsContext2D();
		px = gc.getPixelWriter();

		root.widthProperty().addListener((obs, o, n) -> drawCanvas());
		root.heightProperty().addListener((obs, o, n) -> drawCanvas());
		spriteCanvas.widthProperty().bind(root.widthProperty());
		spriteCanvas.heightProperty().bind(root.heightProperty());

		Label lblMousePos = new Label();
		root.addEventHandler(MouseEvent.ANY, e -> {

			if (e.getEventType() == MouseEvent.MOUSE_MOVED) 
				updateLblCoords(lblMousePos, e.getX(), e.getY());
			
			if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
				root.requestFocus();
				updateLblCoords(lblMousePos, e.getX(), e.getY());
				if (((ViewMain) pViewMain).dragAllowed) {
					startX = e.getX();
					tX = spritePane.getLayoutX();
					startY = e.getY();
					tY = spritePane.getLayoutY();
				}

			}
			if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				root.requestFocus();
				updateLblCoords(lblMousePos, e.getX(), e.getY());
				if (((ViewMain) pViewMain).dragAllowed) {
					spritePane.setLayoutX(e.getX() - startX + tX);
					spritePane.setLayoutY(e.getY() - startY + tY);
					drawCanvas();
				}
			}

			e.consume();
		});

		root.setOnScroll(e -> {
			
			double old = ratio.getValue();
			ratio.set(Math.max(0.1, ratio.getValue() * Math.pow(1.2, Math.signum(e.getDeltaY()))));
			updateLblCoords(lblMousePos, e.getX(), e.getY());
			
			

			double px = e.getX() / root.getWidth();
			double py = e.getY() / root.getHeight();
			double cw = root.getWidth() / old, ch = root.getHeight() / old;
			double nw = root.getWidth() / ratio.getValue(), nh = root.getHeight() / ratio.getValue();
			double dx = (nw - cw) * (px - 0.5), dy = (nh - ch) * (py - 0.5);

			spritePane.setLayoutX(root.getWidth() / 2 - (-spritePane.getLayoutX() + e.getX()) - dx);
			spritePane.setLayoutY(root.getHeight() / 2 - (-spritePane.getLayoutY() + e.getY()) - dy);

			drawCanvas();
		});
		Label s = new Label("SCENE");
		
		root.getChildren().addAll(spriteCanvas, spritePane, s, lblMousePos);
		StackPane.setAlignment(s, Pos.TOP_LEFT);
		StackPane.setAlignment(lblMousePos, Pos.TOP_CENTER);
		ratio = new SimpleDoubleProperty(1d);
		spriteFactory = new SpriteFactory();
		spriteCache = new HashMap<>();

		pControler.eventSetSceneProperties(root.widthProperty(), root.heightProperty(), ratio);

	}
	private void updateLblCoords(Label pLbl, double px, double py) {
		pLbl.setText(String.format("[%.1f,%.1f]", (px-root.getWidth()/2-spritePane.getLayoutX())/ratio.getValue(), (py-root.getHeight()/2-spritePane.getLayoutY())/ratio.getValue()));
	}
	// grid stuff
	private void drawCanvas() {
		// compute buff cache
		int s = (int) (spriteCanvas.getWidth() * spriteCanvas.getHeight());
		buffCache = new int[s];
		// for (int i = 0; i < s; i++)
		// buffCache[i] = COLOR_WHITE;
		int c = 100;
		int a;

		for (int j = 0, inc = (int) (c * ratio.getValue()), h = ((int) spriteCanvas.getHeight()) / 2; j < h
				+ inc; j += inc) {
			for (int i = 0, w = (int) spriteCanvas.getWidth(); i < w; i++) {
				a = (int) spritePane.getLayoutY() % inc;
				if (j + a < h)
					buffCache[(h + j + a) * w + i] = COLOR_GREY_75;
				if (j - a < h)
					buffCache[(h - j + a) * w + i] = COLOR_GREY_75;
			}
		}

		for (int i = 0, inc = (int) (c * ratio.getValue()), w = (int) spriteCanvas.getWidth(); i < w / 2
				+ inc; i += inc) {
			for (int j = 0, h = (int) spriteCanvas.getHeight(); j < h; j++) {
				a = (int) spritePane.getLayoutX() % inc;
				if (i + a < w / 2)
					buffCache[j * w + w / 2 + i + a] = COLOR_GREY_75;
				if (i - a < w / 2)
					buffCache[j * w + w / 2 - i + a] = COLOR_GREY_75;

			}
		}

		px.setPixels(0, 0, (int) spriteCanvas.getWidth(), (int) spriteCanvas.getHeight(), format, buffCache, 0,
				(int) spriteCanvas.getWidth());

	}

	// Nodes stuffs
	public Node getNode() {
		return root;
	}

	private void addNode(String pKey, Node sp, int pRubyClassIndex) {
		if (spriteCache.containsKey(pKey))
			spritePane.getChildren().remove(spriteCache.get(pKey));

		spriteCache.put(pKey, sp);
		spritePane.getChildren().add(sp);
		if (pRubyClassIndex == 0)
			sp.toFront();
		else if (pRubyClassIndex == 1)
			sp.toBack();
	}

	public void registerVarNode(DataModel pDataModel) {

		
		if (!(pDataModel.getRbObject().getRbObject() instanceof IRubyObject))
			return;
		
		spriteFactory.setContainer(pDataModel.getContainer());
		IRubyObject rbObject = ((IRubyObject) pDataModel.getRbObject().getRbObject());
		RubyModule[] savedConsts = pDataModel.getSavedConsts();
		for (int i = 0; i < pDataModel.getNbSavedConsts(); i++) {
			if (savedConsts[i].kindOf.isKindOf(rbObject, savedConsts[i])) {

				Node sp = spriteFactory.generate(Model.rubyClassesNames[i], Model.spriteClassesNames[i], rbObject);
				addNode(pDataModel.getKey(), sp, i);

			}
		}
	}

}