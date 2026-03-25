package pl.psi;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Segment {
    Point2D start;
    Point2D end;


    @Override
    public boolean equals(Object obj) {
        return obj instanceof Segment && Math.sqrt(Math.pow(start.getX() - ((Segment) obj).start.getX(), 2) + Math.pow(start.getY() - ((Segment) obj).start.getY(), 2)) == 0;
    }

    void xyz(){
        System.out.print("Segment");
    }


}
