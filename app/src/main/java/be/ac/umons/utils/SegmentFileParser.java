package be.ac.umons.utils;

import be.ac.umons.backend.geometry.HorizontalSegment;
import be.ac.umons.backend.geometry.Point;
import be.ac.umons.backend.geometry.Segment;
import be.ac.umons.backend.geometry.VerticalSegment;
import be.ac.umons.backend.windowing.Window;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * Class use to parse a file and use this metric for the windowing exercises
 */
public class SegmentFileParser {

    /**
     * Represents the result of parsing a file containing segment information.
     */
    public static class ParseResult{
        // ajout de la fenetre
        public final List<Segment> horizontalSegments;
        public final List<Segment> verticalSegments;
        public final Window boundingWindow;
        
        public ParseResult(List<Segment> hs, List<Segment> vs, Window bw) {
            this.horizontalSegments = hs;
            this.verticalSegments = vs;
            this.boundingWindow = bw;
        }
    }

    /**
     * function use for parsing the document
     * @param file the file need to follow a expected structure
     * @return PareseResult an intern class use for the date extract
     * @throws Exception
     */
    public static ParseResult parse(File file) throws Exception{
        List<Segment> horizontalSegments = new ArrayList<>();
        List<Segment> verticalSegments = new ArrayList<>();
        Window boundingWindow = null;
        
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            while( (line = br.readLine()) != null ) {
                if (!line.isEmpty()) {
                    String[] split = line.split(" ");
                    if (split.length == 4) {
                        double a = Double.parseDouble(split[0]);
                        double b = Double.parseDouble(split[1]);
                        double c = Double.parseDouble(split[2]);
                        double d = Double.parseDouble(split[3]);
                        
                        if(first){
                            first = false;
                            boundingWindow = new Window(a,b,c,d);
                        } else {
                            Point p1 = new Point(a,b);
                            Point p2 = new Point(c,d);
                            if (b  == d){ // horizontal segment
                                horizontalSegments.add(new HorizontalSegment(p1, p2));
                            } else if (a == c) {
                                verticalSegments.add(new VerticalSegment(p1, p2));
                            }
                        }
                    }
                }
            }
            if ( boundingWindow == null) throw new Exception("No bounding window found in the file");
            return new ParseResult(horizontalSegments, verticalSegments, boundingWindow);
        }
    }
}
