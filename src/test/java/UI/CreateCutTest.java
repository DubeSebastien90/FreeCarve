package UI;

import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;
import Domain.Controller;
import Domain.CutType;
import Domain.RefCut;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CreateCutTest {

    @Test
    void create_cut(){
        // Arrange
        Controller controller = Controller.initialize();
        ArrayList<VertexDTO> points = new ArrayList<>();
        VertexDTO p1 = new VertexDTO(100, 0, 0);
        VertexDTO p2 = new VertexDTO(100, 200, 0);
        List<RefCutDTO> refs = controller.getRefCutsAndBorderOnPoint(p1);

        p1 = p1.sub(refs.getFirst().getAbsoluteOffset(controller));
        p2 = p2.sub(refs.getFirst().getAbsoluteOffset(controller));
        points.add(p1);
        points.add(p2);
        RequestCutDTO request = new RequestCutDTO(points, CutType.LINE_VERTICAL, 0, 0, refs);
        controller.requestCut(request);

        // Act
        List<CutDTO> cuts = controller.getCutListDTO();

        // Assert
        Assertions.assertEquals(1, cuts.size());

        Assertions.assertEquals(0, cuts.getFirst().getPoints().getFirst().getX());
        Assertions.assertEquals(0, cuts.getFirst().getPoints().getFirst().getY());
        Assertions.assertEquals(0, cuts.getFirst().getPoints().get(1).getX());
        Assertions.assertEquals(200, cuts.getFirst().getPoints().get(1).getY());

        Assertions.assertEquals(100, controller.getAbsolutePointsPosition(cuts.getFirst()).getFirst().getX());
        Assertions.assertEquals(0, controller.getAbsolutePointsPosition(cuts.getFirst()).getFirst().getY());
        Assertions.assertEquals(100, controller.getAbsolutePointsPosition(cuts.getFirst()).get(1).getX());
        Assertions.assertEquals(200, controller.getAbsolutePointsPosition(cuts.getFirst()).get(1).getY());
    }


    @Test
    void move_cut(){
        // Arrange
        Controller controller = Controller.initialize();
        ArrayList<VertexDTO> points = new ArrayList<>();
        VertexDTO p1 = new VertexDTO(100, 0, 0);
        VertexDTO p2 = new VertexDTO(100, 200, 0);
        List<RefCutDTO> refs = controller.getRefCutsAndBorderOnPoint(p1);

        p1 = p1.sub(refs.getFirst().getAbsoluteOffset(controller));
        p2 = p2.sub(refs.getFirst().getAbsoluteOffset(controller));
        points.add(p1);
        points.add(p2);
        RequestCutDTO request = new RequestCutDTO(points, CutType.LINE_VERTICAL, 0, 0, refs);
        controller.requestCut(request);


        points = new ArrayList<>();
        p1 = new VertexDTO(100, 20, 0);
        p2 = new VertexDTO(150, 20, 0);
        refs = controller.getRefCutsAndBorderOnPoint(p1);
        p1 = p1.sub(refs.getFirst().getAbsoluteOffset(controller));
        p2 = p2.sub(refs.getFirst().getAbsoluteOffset(controller));
        points.add(p1);
        points.add(p2);
        request = new RequestCutDTO(points, CutType.LINE_HORIZONTAL, 0, 0, refs);
        controller.requestCut(request);


        // Act
        List<CutDTO> cuts = controller.getCutListDTO();

        // Assert
        Assertions.assertEquals(2, cuts.size());

        Assertions.assertEquals(0, cuts.getFirst().getPoints().getFirst().getX());
        Assertions.assertEquals(0, cuts.getFirst().getPoints().getFirst().getY());
        Assertions.assertEquals(0, cuts.getFirst().getPoints().get(1).getX());
        Assertions.assertEquals(200, cuts.getFirst().getPoints().get(1).getY());

        Assertions.assertEquals(100, controller.getAbsolutePointsPosition(cuts.getFirst()).getFirst().getX());
        Assertions.assertEquals(0, controller.getAbsolutePointsPosition(cuts.getFirst()).getFirst().getY());
        Assertions.assertEquals(100, controller.getAbsolutePointsPosition(cuts.getFirst()).get(1).getX());
        Assertions.assertEquals(200, controller.getAbsolutePointsPosition(cuts.getFirst()).get(1).getY());


        Assertions.assertEquals(0, cuts.get(1).getPoints().getFirst().getX());
        Assertions.assertEquals(0, cuts.get(1).getPoints().getFirst().getY());
        Assertions.assertEquals(50, cuts.get(1).getPoints().get(1).getX());
        Assertions.assertEquals(0, cuts.get(1).getPoints().get(1).getY());

        Assertions.assertEquals(100, controller.getAbsolutePointsPosition(cuts.get(1)).getFirst().getX());
        Assertions.assertEquals(20, controller.getAbsolutePointsPosition(cuts.get(1)).getFirst().getY());
        Assertions.assertEquals(150, controller.getAbsolutePointsPosition(cuts.get(1)).get(1).getX());
        Assertions.assertEquals(20, controller.getAbsolutePointsPosition(cuts.get(1)).get(1).getY());


        // Act 2
        UUID cutId = controller.getCutListDTO().getFirst().getId();
        CutDTO moveCutDTO = controller.getCutListDTO().getFirst();
        double offset = 40;
        moveCutDTO = moveCutDTO.addOffsetToPoints(new VertexDTO(offset, offset , 0));

        Optional<UUID> id = controller.modifyCut(moveCutDTO);

        cuts = controller.getCutListDTO();

        // Assert
        Assertions.assertTrue(id.isPresent());
        Assertions.assertEquals(2, cuts.size());

        Assertions.assertEquals(0 + offset, cuts.getFirst().getPoints().getFirst().getX());
        Assertions.assertEquals(0 + offset, cuts.getFirst().getPoints().getFirst().getY());
        Assertions.assertEquals(0+ offset, cuts.getFirst().getPoints().get(1).getX());
        Assertions.assertEquals(200 + offset, cuts.getFirst().getPoints().get(1).getY());

        Assertions.assertEquals(100 + offset, controller.getAbsolutePointsPosition(cuts.getFirst()).getFirst().getX());
        Assertions.assertEquals(0 + offset, controller.getAbsolutePointsPosition(cuts.getFirst()).getFirst().getY());
        Assertions.assertEquals(100 + offset, controller.getAbsolutePointsPosition(cuts.getFirst()).get(1).getX());
        Assertions.assertEquals(200 + offset, controller.getAbsolutePointsPosition(cuts.getFirst()).get(1).getY());


        // No offset because those positions should be relative
        Assertions.assertEquals(0, cuts.get(1).getPoints().getFirst().getX());
        Assertions.assertEquals(0, cuts.get(1).getPoints().getFirst().getY());
        Assertions.assertEquals(50, cuts.get(1).getPoints().get(1).getX());
        Assertions.assertEquals(0, cuts.get(1).getPoints().get(1).getY());

        Assertions.assertEquals(100 + offset, controller.getAbsolutePointsPosition(cuts.get(1)).getFirst().getX());
        Assertions.assertEquals(20 + offset, controller.getAbsolutePointsPosition(cuts.get(1)).getFirst().getY());
        Assertions.assertEquals(150 + offset, controller.getAbsolutePointsPosition(cuts.get(1)).get(1).getX());
        Assertions.assertEquals(20 + offset, controller.getAbsolutePointsPosition(cuts.get(1)).get(1).getY());
    }

}
