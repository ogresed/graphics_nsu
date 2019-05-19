package ru.nsu.fit.g16207.melnikov;

public class Clipper3D {
    private static final int INSIDE = 0;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int BOTTOM = 4;
    private static final int TOP = 8;
    private static final int FRONT = 16;
    private static final int BACK = 32;

    private final double maxX;
    private final double maxY;
    private final double maxZ;
    private final double minX;
    private final double minY;
    private final double minZ;

    public Clipper3D(double maxX, double maxY, double maxZ, double minX, double minY, double minZ) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
    }

    private int getOutCode(Point3D<Double, Double, Double> point3D) {
        int outcode = INSIDE;

        if (point3D.getX() < minX) {
            outcode |= LEFT;
        } else {
            if (point3D.getX() > maxX) {
                outcode |= RIGHT;
            }
        }

        if (point3D.getY() < minY) {
            outcode |= BOTTOM;
        } else {
            if (point3D.getY() > maxY) {
                outcode |= TOP;
            }
        }

        if (point3D.getZ() < minZ) {
            outcode |= FRONT;
        } else {
            if (point3D.getZ() > maxZ) {
                outcode |= BACK;
            }
        }

        return outcode;
    }

    public Line<Point3D<Double, Double, Double>> getClippedLine(Line<Point3D<Double, Double, Double>> srcLine) {
        Point3D<Double, Double, Double> startPoint = srcLine.getStart();
        Point3D<Double, Double, Double> endPoint = srcLine.getEnd();

        double x0 = startPoint.getX();
        double x1 = endPoint.getX();
        double y0 = startPoint.getY();
        double y1 = endPoint.getY();
        double z0 = startPoint.getZ();
        double z1 = endPoint.getZ();

        int startOutCode = getOutCode(startPoint);
        int endOutCode = getOutCode(endPoint);

        boolean accept = false;

        while (true) {
            if (0 == (startOutCode | endOutCode)) {
                accept = true;
                break;
            } else {
                if ((startOutCode & endOutCode) != 0) {
                    break;
                } else {
                    double x = 0;
                    double y = 0;
                    double z = 0;

                    int outCode = startOutCode != 0 ? startOutCode : endOutCode;

                    if ((outCode & TOP) != 0) {
                        x = x0 + (x1 - x0) * (maxY - y0) /
                                (y1 - y0);
                        y = maxY;
                        z = z0 + (z1 - z0) * (maxY - y0) /
                                (y1 - y0);
                    } else if ((outCode & BOTTOM) != 0) {
                        x = x0 + (x1 - x0) * (minY - y0) /
                                (y1 - y0);
                        y = minY;
                        z = z0 + (z1 - z0) * (minY - y0) /
                                (y1 - y0);
                    } else if ((outCode & RIGHT) != 0) {
                        y = y0 + (y1 - y0) * (maxX - x0) /
                                (x1 - x0);
                        x = maxX;
                        z = z0 + (z1 - z0) * (maxX - x0) /
                                (x1 - x0);

                    } else if ((outCode & LEFT) != 0) {
                        y = y0 + (y1 - y0) * (minX - x0) /
                                (x1 - x0);
                        x = minX;
                        z = z0 + (z1 - z0) * (minX - x0) /
                                (x1 - x0);

                    } else if ((outCode & BACK) != 0) {
                        x = x0 + (x1 - x0) * (maxZ - z0) /
                                (z1 - z0);

                        y = y0 + (y1 - y0) * (maxZ - z0) /
                                (z1 - z0);
                        z = maxZ;
                    } else if ((outCode & FRONT) != 0) {
                        x = x0 + (x1 - x0) * (minZ - z0) /
                                (z1 - z0);

                        y = y0 + (y1 - y0) * (minZ - z0) /
                                (z1 - z0);
                        z = minZ;
                    }

                    if (outCode == startOutCode) {
                        x0 = x;
                        y0 = y;
                        z0 = z;
                        startOutCode = getOutCode(new Point3D<>(x0, y0, z0));
                    } else {
                        x1 = x;
                        y1 = y;
                        z1 = z;
                        endOutCode = getOutCode(new Point3D<>(x1, y1, z1));
                    }
                }
            }
        }

        if (accept) {
            return new Line<>(new Point3D<>(x0, y0, z0), new Point3D<>(x1, y1, z1));
        } else {
            return null;
        }
    }
}
