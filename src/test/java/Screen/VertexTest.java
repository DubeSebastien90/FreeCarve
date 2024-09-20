package Screen;

import org.junit.jupiter.api.Test;

class VertexTest {
    @Test
    void vertex_subtraction_test() {
        Vertex v1 = new Vertex(-1, 2, 3);
        Vertex v2 = new Vertex(-1, 2, 3);
        v1.subtraction(v2);

        assert v1.equals(new Vertex(0, 0, 0));

        v1 = new Vertex(v2);
        v1.subtraction(v2);
        assert v1.equals(new Vertex(0, 0, 0));

        v1 = new Vertex(5, 99, -200);
        v1.subtraction(v2);
        assert v1.equals(new Vertex(6, 97, -203));
    }

    @Test
    void vertex_addition_test() {
        Vertex v1 = new Vertex(-1, 2, 3);
        Vertex v2 = new Vertex(-1, 2, 3);
        v1.addition(v2);

        assert v1.equals(new Vertex(-2, 4, 6));

        v1 = new Vertex(5, 5, 5);
        v1.addition(v2);
        assert v1.equals(new Vertex(4, 7, 8));

        v1 = new Vertex(5, 99, -200);
        v1.addition(v2);
        assert v1.equals(new Vertex(4, 101, -197));
    }

    @Test
    void vertex_multiplication_test() {
        Vertex v1 = new Vertex(0, 0, 0);
        v1.multiplication(213);
        assert v1.equals(new Vertex(0, 0, 0));

        v1 = new Vertex(1, 1, -1);
        v1.multiplication(213);
        assert v1.equals(new Vertex(213, 213, -213));

        v1 = new Vertex(3, 0, -1);
        v1.multiplication(-1);
        assert v1.equals(new Vertex(-3, 0, 1));
    }

    @Test
    void vertex_parallel_test() {
        Vertex v1 = new Vertex(1, 1, 1);
        Vertex v2 = new Vertex(2, 2, 2);
        assert v1.isParallel(v2);

        v1 = new Vertex(1, 1, 1);
        v2 = new Vertex(1, 1, 1);
        assert v1.isParallel(v2);

        v1 = new Vertex(0, 0, 0);
        v2 = new Vertex(1, 132, -1);
        assert v1.isParallel(v2);
    }

    @Test
    void vertex_static_addition() {
        Vertex v1 = new Vertex(-1, 2, 3);
        Vertex v2 = new Vertex(-1, 2, 3);
        Vertex v3 = Vertex.addition(v1, v2);

        assert v3.equals(new Vertex(-2, 4, 6));

        v1 = new Vertex(5, 5, 5);
        v3 = Vertex.addition(v1, v2);
        assert v3.equals(new Vertex(4, 7, 8));

        v1 = new Vertex(5, 99, -200);
        v3 = Vertex.addition(v1, v2);
        assert v3.equals(new Vertex(4, 101, -197));
    }
}