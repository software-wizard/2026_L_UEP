package pl.psi;

import jdk.jfr.Enabled;
import lombok.EqualsAndHashCode;

public class Point3DInherit extends Point2D implements Point3D {
    private final int z;

    public Point3DInherit(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }


    @Override
    public int getZ() {
        return z;
    }


}
