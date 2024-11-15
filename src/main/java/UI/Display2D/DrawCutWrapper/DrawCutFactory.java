package UI.Display2D.DrawCutWrapper;

import Common.DTO.CutDTO;
import Domain.CutType;
import UI.Display2D.Rendering2DWindow;
import UI.MainWindow;

import java.util.ArrayList;
import java.util.List;

public class DrawCutFactory {

    /**
     * Create an instance of a DrawCutWrapper based on a CutDTO
     * @param cut cut
     * @param renderer reference to the renderer
     * @param mainWindow reference to the mainWindow
     * @return DrawCutWrapper created
     */
    public static DrawCutWrapper createDrawCutWrapper(CutDTO cut, Rendering2DWindow renderer, MainWindow mainWindow) {
        if (cut.getCutType() == CutType.LINE_FREE){
            return new DrawFreeCut(cut, renderer, mainWindow);
        }
        else if(cut.getCutType() == CutType.LINE_VERTICAL){
            return new DrawCutStraight(cut, renderer, mainWindow);
        }
        else if(cut.getCutType() == CutType.LINE_HORIZONTAL){
            return new DrawCutStraight(cut, renderer, mainWindow);
        }
        else if(cut.getCutType() == CutType.RECTANGULAR){
            return new DrawCutRectangular(cut, renderer, mainWindow);
        }
        else if(cut.getCutType() == CutType.L_SHAPE){
            return new DrawCutL(cut, renderer, mainWindow);
        }
        else if(cut.getCutType() == CutType.RECTANGULAR){
            return new DrawCutRectangular(cut, renderer, mainWindow);
        }
        else if(cut.getCutType() == CutType.L_SHAPE){
            return new DrawCutL(cut, renderer, mainWindow);
        }

        return new DrawFreeCut(cut, renderer, mainWindow);
    }

    /**
     * Create an empty instance of a DrawCutWrapper based on a cutType
     * @param type type
     * @param renderer reference to the renderer
     * @param mainWindow reference to the mainWindow
     * @return DrawCutWrapper created
     */
    public static DrawCutWrapper createEmptyWrapper(CutType type, Rendering2DWindow renderer, MainWindow mainWindow){
        if (type == CutType.LINE_FREE){
            return new DrawFreeCut(type, renderer, mainWindow);
        }
        else if(type == CutType.LINE_VERTICAL){
            return new DrawCutStraight(type, renderer, mainWindow);
        }
        else if(type == CutType.LINE_HORIZONTAL){
            return new DrawCutStraight(type, renderer, mainWindow);
        }
        else if(type == CutType.RECTANGULAR){
            mainWindow.getController().computeGridIntersections(); // Computes the intersections of the board and store them in the domain (the Grid Class)
            return new DrawCutRectangular(type, renderer, mainWindow);
        }
        else if(type == CutType.L_SHAPE){
            mainWindow.getController().computeGridIntersections(); // Computes the intersections of the board and store them in the domain (the Grid Class)
            return new DrawCutL(type, renderer, mainWindow);
        }
        else if(type == CutType.RECTANGULAR){
            mainWindow.getController().computeGridIntersections(); // Computes the intersections of the board and store them in the domain (the Grid Class)
            return new DrawCutRectangular(type, renderer, mainWindow);
        }
        else if(type == CutType.L_SHAPE){
            mainWindow.getController().computeGridIntersections(); // Computes the intersections of the board and store them in the domain (the Grid Class)
            return new DrawCutL(type, renderer, mainWindow);
        }

        return new DrawFreeCut(type, renderer, mainWindow);
    }

    /**
     * Create a List of {@code DrawCutWrapper} based on a List of CutDTO
     * @param cutDTOList list of CutDTO
     * @param renderer reference to renderer
     * @param mainWindow reference to mainWindow instance
     * @return List of {@code DrawCutWrapper}
     */
    public static List<DrawCutWrapper> createListDrawCutWrapper(List<CutDTO> cutDTOList, Rendering2DWindow renderer, MainWindow mainWindow){
        ArrayList<DrawCutWrapper> outputList = new ArrayList<DrawCutWrapper>();
        for (CutDTO cut : cutDTOList){
            outputList.add(DrawCutFactory.createDrawCutWrapper(cut, renderer, mainWindow));
        }
        return outputList;
    }

}
