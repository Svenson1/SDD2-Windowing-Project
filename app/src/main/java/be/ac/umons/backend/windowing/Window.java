package be.ac.umons.backend.windowing;
/**
 * Represents a rectangular query window defined by its bounds
 * in the Cartesian plane: [xMin, xMax] × [yMin, yMax].
 *
 */
public class Window {
    public final double xMin, xMax, yMin, yMax;
    
    /**
     * Constructs a rectangular window from four boundary values.
     *
     * @param xMin lower bound on the x-axis
     * @param xMax upper bound on the x-axis
     * @param yMin lower bound on the y-axis
     * @param yMax upper bound on the y-axis
     */
    public Window(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    /**
     * cheks whether this window is geometrically valid
     * @return True if its true False otherwise
     */
    public boolean isValid(){
        return xMin < xMax && yMin < yMax;
    }

    @Override
    public String toString() {
        return "Window{" +
                "xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                '}';
    }
}
