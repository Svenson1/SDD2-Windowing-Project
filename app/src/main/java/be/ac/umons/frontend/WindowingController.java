package be.ac.umons.frontend;

import be.ac.umons.backend.geometry.HorizontalSegment;
import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.Segment;
import be.ac.umons.backend.geometry.VerticalSegment;
import be.ac.umons.backend.pst.PST;
import be.ac.umons.backend.windowing.Window;
import be.ac.umons.backend.windowing.WindowerEngine;
import be.ac.umons.utils.SegmentFileParser;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that build and control the interface for vizualise segment
 */
public class WindowingController
{
    // model
    private final Stage stage;
    private WindowerEngine windowerEngine;
    private List<Segment> horizontalSegments;
    private List<Segment> verticalSegments;
    private Window boundingWindow;
    
    // UI
    private Canvas canvas = new Canvas(700, 500);
    private Label statusLabel = new Label("No file loaded");
    
    // query
    private final TextField tfMinX = new TextField();
    private final TextField tfMaxX = new TextField();
    private final TextField tfMinY = new TextField();
    private final TextField tfMaxY = new TextField();
    
    public WindowingController(Stage stage)
    {
        stage.setResizable(false);
        
        this.stage = stage;
    }

    /**
     * function use for generate an interface to vizualise segment and the query results
     * @return void
     */
    public BorderPane buildUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        root.setPrefSize(1000, 600);
        
        // control panel
        VBox controlPanel = new VBox(12);
        controlPanel.setPadding(new Insets(10));
        controlPanel.setPrefWidth(200);
        
        Button btnLoad = new Button("Load");
        btnLoad.setMaxWidth(Double.MAX_VALUE);
        btnLoad.setOnAction(e -> loadFile());
        
        Button btnDisplay = new Button("Display");
        btnDisplay.setMaxWidth(Double.MAX_VALUE);
        btnDisplay.setOnAction(e -> displaySegments(horizontalSegments, verticalSegments, null));
        
        TitledPane queryPane = new TitledPane();
        queryPane.setText("enter query");
        GridPane queryGrid = new GridPane();
        queryGrid.setHgap(6);
        queryGrid.setVgap(6);
        queryGrid.setPadding(new Insets(8));

        queryGrid.add(new Label("X min:"), 0, 0);
        queryGrid.add(tfMinX, 1, 0);
        queryGrid.add(new Label("X max:"), 0, 1);
        queryGrid.add(tfMaxX, 1, 1);
        queryGrid.add(new Label("Y min:"), 0, 2);
        queryGrid.add(tfMinY, 1, 2);
        queryGrid.add(new Label("Y max:"), 0, 3);
        queryGrid.add(tfMaxY, 1, 3);

        for (TextField tf : new TextField[]{tfMinX, tfMaxX, tfMinY, tfMaxY}) {
            tf.setPrefWidth(80);
            tf.setPromptText("∞");
        }

        Button btnQuery = new Button("Apply Windowing");
        btnQuery.setMaxWidth(Double.MAX_VALUE);
        btnQuery.setOnAction(e -> applyWindowing());
        queryGrid.add(btnQuery, 0, 4, 2, 1);

        queryPane.setContent(queryGrid);
        
        controlPanel.getChildren().addAll(btnLoad, btnDisplay, queryPane, statusLabel);
        statusLabel.setWrapText(true);
        
        root.setLeft(controlPanel);
        ScrollPane scrollPane = new ScrollPane(canvas);
        scrollPane.setPannable(true);
        root.setCenter(scrollPane);

        btnLoad.getStyleClass().add("button-load");
        statusLabel.getStyleClass().add("label-status");

        return root;
    }
    

    private void loadFile() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a file to load");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File", "*.txt", "*.seg", "*.*"));
        File file = fc.showOpenDialog(stage);
        if (file == null) return;

        try {
            SegmentFileParser.ParseResult result = SegmentFileParser.parse(file);
            horizontalSegments = result.horizontalSegments;
            verticalSegments = result.verticalSegments;
            boundingWindow = result.boundingWindow;
            windowerEngine = new WindowerEngine(horizontalSegments, verticalSegments);

            
            statusLabel.setText(" segments loaded.");

            displaySegments(horizontalSegments,verticalSegments, null);
        } catch (Exception ex) {
            showError("Erreur de lecture", ex.getMessage());
        }
    }
    
    private void displaySegments(List<Segment> segments, Window queryWindow) {
        displaySegments(segments, new ArrayList<>(), queryWindow);
    }
    
    private void displaySegments(List<Segment> horizontalSegments, List<Segment> verticalSegments, Window queryWindow) {
        if (horizontalSegments == null || verticalSegments == null ||boundingWindow == null) {
            statusLabel.setText("No segments loaded");
            return;
        }
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        
        double w = canvas.getWidth();
        double h = canvas.getHeight();

        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.web("#12141f"));
        gc.fillRect(0, 0, w, h);

        Window bw = boundingWindow;
        double rangeX = bw.xMax - bw.xMin;
        double rangeY = bw.yMax - bw.yMin;
        double scale = Math.min(w / rangeX, h / rangeY);
        double offsetX =  (w- rangeX * scale) / 2;
        double offsetY =  (h - rangeY * scale) / 2;



        gc.setFill(Color.web("#1a1d2e"));
        gc.fillRect(offsetX, offsetY, rangeX * scale, rangeY * scale);


        gc.setStroke(Color.web("#5e81f4"));
        gc.setLineWidth(1.2);
        for (Segment hs : horizontalSegments) {
            drawSegment(gc, bw, offsetX, offsetY, hs.getP1(), hs.getP2(), scale);
        }
        for (Segment vs : verticalSegments) {
            drawSegment(gc, bw, offsetX, offsetY, vs.getP1(), vs.getP2(), scale);
        }
        

        if (queryWindow != null) {
            double rx = toScreenX(queryWindow.xMin, bw, offsetX, scale);
            double ry = toScreenY(queryWindow.yMax, bw, offsetY, scale);
            double rw = (queryWindow.xMax - queryWindow.xMin) * scale;
            double rh = (queryWindow.yMax - queryWindow.yMin) * scale;
            gc.setFill(new Color(0.96, 0.64, 0.37, 0.15));
            gc.fillRect(rx, ry, rw, rh);

            gc.setStroke(new Color(0.96, 0.64, 0.37, 1.0));
            gc.setLineWidth(1.5);
            gc.strokeRect(rx, ry, rw, rh);
        }
    }


    private void applyWindowing() {
        double xMin = parseOrDefault(tfMinX.getText(), Double.NEGATIVE_INFINITY);
        double xMax = parseOrDefault(tfMaxX.getText(), Double.POSITIVE_INFINITY);
        double yMin = parseOrDefault(tfMinY.getText(), Double.NEGATIVE_INFINITY);
        double yMax = parseOrDefault(tfMaxY.getText(),Double.POSITIVE_INFINITY);
        

        Window queryWin = new Window(xMin, xMax, yMin, yMax);
        if (!queryWin.isValid()) {
            showError("invalid window", "look is  xMin < xMax and yMin < yMax.");
        }else {
            List<Segment> res = windowerEngine.query(queryWin);
            //displaySegments(horizontalSegments, verticalSegments, queryWin);
            System.out.println(res);
            displaySegments(res, queryWin);
        }

    }
    
    private double toScreenX(double x, Window bw, double offsetX, double scale) {
        return offsetX + (x - bw.xMin) * scale;
    }

    private double toScreenY(double y, Window bw, double offsetY, double scale) {
        return offsetY + (bw.yMax - y) * scale;
    }
    
    private double parseOrDefault(String text, double def) {
        if (text == null || text.isBlank()) return def;
        return Double.parseDouble(text.trim());
    }

    private void drawSegment(GraphicsContext gc, Window bw, double offsetX, double offsetY, Point p1, Point p2, double scale) {
        double sx1 = toScreenX(p1.getX(),bw, offsetX, scale);
        double sy1 = toScreenY(p1.getY(),bw, offsetY, scale);
        double sx2 = toScreenX(p2.getX(),bw, offsetX, scale);
        double sy2 = toScreenY(p2.getY(),bw, offsetY, scale);
        gc.strokeLine(sx1, sy1, sx2, sy2);
    }
    
    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
