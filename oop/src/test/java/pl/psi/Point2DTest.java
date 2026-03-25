package pl.psi;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class Point2DTest {

    @Test
    void test1(){
        Point2D black = new Point2D(1, 1);
        assertThat(black.getX()).isEqualTo(1);
        assertThat(black.getY()).isEqualTo(1);
        Point2D blue = new Point2D(2, 2);
        assertThat(blue.getX()).isEqualTo(2);
        assertThat(blue.getY()).isEqualTo(2);


        Segment s1 = new Segment(black, blue);
        s1.getStart().setX(0);
        assertThat(s1.getStart().getX()).isEqualTo(0);
        assertThat(black.getX()).isEqualTo(0);
        blue.setY(11);
        assertThat(s1.getEnd().getY()).isEqualTo(11);

        Segment s2 = new Segment(black, blue);
        assertThat(s1).isEqualTo(s2);

        s2.getEnd().setX(22);
        assertThat(s1.getEnd().getX()).isEqualTo(22);

        Set<Point2D> set = new HashSet<>();
        set.add(blue);
        set.add(black);

        assertThat(set.size()).isEqualTo(2);

        set.add(new Point2D(0,1));
//        assertThat(set.size()).isEqualTo(3);

        HashMap<Point2D, String> map = new HashMap<>();
        map.put(blue, "a");
        map.put(blue, "b");
        assertThat(map.size()).isEqualTo(1);
        assertThat(map.get(blue)).isEqualTo("b");

        blue.setX(123);
        assertThat(map.get(blue)).isEqualTo("b");
    }

    @Test
    void test2(){
        Point3D interfacee = new Point3DComposition(1, 1, 1);
        Point3DPrint interfacee2 = new Point3DComposition(1, 1, 1);
        Point3DComposition clazz = new Point3DComposition(1, 1, 1);

        Point3D p1 = new Point3DComposition(1, 1, 1);
        Point3D p2 = new Point3DInherit(1,1,1);

        boolean result = PointComparator.compare(p1, p2);

        assertThat(result).isTrue();
    }

    @Test
    void test3() {
        Point3DComposition p1 = new Point3DComposition(1, 2, 3);
        assertThat(p1.getCost()).isEqualTo(9);

        Point3DComposition p2 = new Point3DComposition(-2, 2, 3);
        assertThat(p2.getCost()).isEqualTo(0);
    }

    @Test
    void test4() {
        Point2D p1 = new Point2D(1, 1);
        Point2D p2 = new Point2D(1, 1);

        p2.addObserver(p1);

        p2.setY(2);
        assertThat(p1.getY()).isEqualTo(2);

        p1.setY(3);
        assertThat(p1.getY()).isEqualTo(3);
        assertThat(p2.getY()).isEqualTo(2);
    }
}
