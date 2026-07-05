package model;

public record Point2D(double x, double y) implements Comparable<Point2D> {
    @Override
    public int compareTo(Point2D other) {
        if (this.x != other.x) {
            return Double.compare(this.x, other.x);
        }
        return Double.compare(this.y, other.y);
    }

    /**
     * Devuelve la orientación de tres puntos (p, q, r).
     * > 0 : Giro a la izquierda (Antihorario)
     * < 0 : Giro a la derecha (Horario)
     * = 0 : Colineales
     */
    public static double orientation(Point2D p, Point2D q, Point2D r) {
        return (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
    }
    
    public double distanceSq(Point2D other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return dx * dx + dy * dy;
    }
}