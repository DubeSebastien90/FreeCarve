package Domain;

import Domain.ThirdDimension.Vertex;
import Domain.ThirdDimension.VertexDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PanelCNCTest {
    private PanelCNC panelCNC;
    private Vertex boardDimension;
    private float depth;

    @BeforeEach
    void SetUp(){
        boardDimension = new Vertex(1,2,3);
        depth = 13.0f;
        panelCNC = new PanelCNC(boardDimension, depth);
    }

    @Test
    void constructor_with_dimension_and_depth_postconditions(){
        Assertions.assertEquals(panelCNC.getWidth(), 1);
        Assertions.assertEquals(panelCNC.getHeight(), 2);
        Assertions.assertEquals(panelCNC.getBoardDimension().getX(), 1);
        Assertions.assertEquals(panelCNC.getBoardDimension().getY(), 2);
        Assertions.assertEquals(panelCNC.getCutList().size(), 0);
        Assertions.assertEquals(panelCNC.getDepth(), 13.0f);
    }

    @Test
    void add_new_cut(){
        Assertions.assertEquals(panelCNC.getCutList().size(), 0);
        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(13, 14, 15));
        pointList.add(new VertexDTO(16, 17, 18));
        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f);
        panelCNC.newCut(rcDTO);
        Assertions.assertEquals(panelCNC.getCutList().size(), 1);
        ArrayList<Cut> refCutList = panelCNC.getCutList();
        Assertions.assertEquals(refCutList.getFirst().getStartPoint().getX(), 13);
        Assertions.assertEquals(refCutList.getFirst().getStartPoint().getY(), 14);
        Assertions.assertEquals(refCutList.getFirst().getStartPoint().getZ(), 15);
    }



    @Test
    void valid_panelDTO_return(){
        Assertions.assertEquals(panelCNC.getCutList().size(), 0);
        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(13, 14, 15));
        pointList.add(new VertexDTO(16, 17, 18));
        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f);
        panelCNC.newCut(rcDTO);

        PanelDTO pDTO = panelCNC.getPanelDTO();
        Assertions.assertEquals(pDTO.getCutsDTO().size(), panelCNC.getCutList().size());
        Assertions.assertEquals(pDTO.getCutsDTO().getFirst().getPoints().size(), 2);
        Assertions.assertEquals(pDTO.getCutsDTO().getFirst().getPoints().getFirst().getX(), 13);
        Assertions.assertEquals(pDTO.getCutsDTO().getFirst().getPoints().getFirst().getY(), 14);
        Assertions.assertEquals(pDTO.getCutsDTO().getFirst().getPoints().getFirst().getZ(), 15);
    }
}
