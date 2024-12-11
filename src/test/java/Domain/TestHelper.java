package Domain;

import Common.DTO.PanelDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.RequestCutDTO;
import Common.DTO.VertexDTO;

import java.util.ArrayList;

public class TestHelper {
    public static PanelDTO createPanelDTO(){
        PanelCNC panelCNC = new PanelCNC(new UndoRedoManager());

        ArrayList<VertexDTO> pointList = new ArrayList<>();
        pointList.add(new VertexDTO(13, 14, 15));
        pointList.add(new VertexDTO(16, 17, 18));

        RequestCutDTO rcDTO = new RequestCutDTO(pointList, CutType.LINE_VERTICAL, 0, 3.0f, new ArrayList<RefCutDTO>());
        CNCMachine cnc = new CNCMachine(new UndoRedoManager());
        panelCNC.requestCut(cnc, rcDTO);
        return panelCNC.getDTO();
    }
}
