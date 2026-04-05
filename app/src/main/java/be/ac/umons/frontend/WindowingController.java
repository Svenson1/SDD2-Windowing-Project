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
 * Controls the user interface for visualizing axis-aligned segments and applying windowing queries.
 * Handles file loading, segment rendering on a canvas, and query window display.
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

    /**
     * Creates a controller bound to the given JavaFX stage.
     *
     * @param stage the primary stage used for file dialogs
     */
    public WindowingController(Stage stage)
    {
        stage.setResizable(false);
        
        this.stage = stage;
    }

    /**
     * Builds and returns the root layout of the user interface.
     * The layout includes a control panel on the left with load, display, and query controls,
     * and a scrollable canvas on the right for rendering segments.
     *
     * @return the root BorderPane of the interface
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

    /**
     * Opens a file chooser dialog and loads segments from the selected file.
     * On success, initializes the windowing engine and displays all segments.
     * On failure, shows an error dialog.
     */
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

    /**
     * Renders the given horizontal and vertical segments on the canvas.
     * If a query window is provided, it is drawn as a highlighted rectangle over the canvas.
     *
     * @param horizontalSegments the horizontal segments to draw
     * @param verticalSegments   the vertical segments to draw
     * @param queryWindow        the query window to highlight, or null to skip
     */
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

        }
    }

    /**
     * Reads the query bounds from the input fields, runs the windowing query,
     * and displays the resulting segments along with the query window.
     * Shows an error dialog if the window bounds are invalid.
     */
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

    /**
     * Converts a world x coordinate to a screen x coordinate.
     *
     * @param x       the world x coordinate
     * @param bw      the bounding window used for scaling
     * @param offsetX the horizontal canvas offset
     * @param scale   the scale factor
     * @return the screen x coordinate
     */
    private double toScreenX(double x, Window bw, double offsetX, double scale) {
        return offsetX + (x - bw.xMin) * scale;
    }

    /**
     * Converts a world y coordinate to a screen y coordinate.
     * The y axis is flipped so that higher world values appear higher on screen.
     *
     * @param y       the world y coordinate
     * @param bw      the bounding window used for scaling
     * @param offsetY the vertical canvas offset
     * @param scale   the scale factor
     * @return the screen y coordinate
     */
    private double toScreenY(double y, Window bw, double offsetY, double scale) {
        return offsetY + (bw.yMax - y) * scale;
    }

    /**
     * Parses a string as a double, returning a default value if the string is null or blank.
     *
     * @param text the string to parse
     * @param def  the default value to return if parsing is not possible
     * @return the parsed value, or def if the string is null or blank
     */
    private double parseOrDefault(String text, double def) {
        if (text == null || text.isBlank()) return def;
        return Double.parseDouble(text.trim());
    }

    /**
     * Draws a single segment on the canvas by converting both endpoints to screen coordinates.
     *
     * @param gc      the graphics context to draw on
     * @param bw      the bounding window used for scaling
     * @param offsetX the horizontal canvas offset
     * @param offsetY the vertical canvas offset
     * @param p1      the first endpoint of the segment
     * @param p2      the second endpoint of the segment
     * @param scale   the scale factor
     */
    private void drawSegment(GraphicsContext gc, Window bw, double offsetX, double offsetY, Point p1, Point p2, double scale) {
        double sx1 = toScreenX(p1.getX(),bw, offsetX, scale);
        double sy1 = toScreenY(p1.getY(),bw, offsetY, scale);
        double sx2 = toScreenX(p2.getX(),bw, offsetX, scale);
        double sy2 = toScreenY(p2.getY(),bw, offsetY, scale);
        gc.strokeLine(sx1, sy1, sx2, sy2);
    }

    /**
     * Displays an error dialog with the given title and message.
     *
     * @param title the dialog title
     * @param msg   the error message
     */
    private void showError(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
