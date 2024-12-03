package Domain;

import Common.DTO.CutDTO;
import Common.DTO.RefCutDTO;
import Common.DTO.VertexDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CutFactory {

    // 5 possibilies :

    // 1 : ref list is empty : just return the points

    // 2 : CutType = RETAILLER :    -number of refs : >=1
    //                              -number of relative points : 2 - first stores the widthLeft and heightBottom (X, Y) - second stores the widthRight and heightTop(X,Y)
    //                              -how absolute points are computed : based on the center anchor point and the width/height parameter, generate a rectangle

    // 3 : CutType = Line_Vertical or Line_Horizontal or Free_Line :
    //                              -number of refs : >=1
    //                              -number of relative points : 2, the points stored are EdgeEdge
    //                              -how absolute points are computed : based on it's first reference point, returns the shifted relative points by the ref offset

    // 4 : CutType = L :            -number of refs : >=2
    //                              -number of relative points : 1 - the first stores the internal width and height (X, Y)
    //                              -how absolute points are computed : gets the intersection points of it's first two references, and offset the relative point by this offset. Then the first, third points are found by looking for the intersection of the rectangular lines to the ref

    // 5 : CutType = Rectangular ;  -number of refs : >=2
    //                              -number of relative points : 2 - the first stores the internal central offset (X, Y) - second stores the internal width and the internal height (X, Y)

    //                              -how absolute points are computed : gets the intersection points of it's first two references, and offset the relative point by this offset.
    //


    public static List<VertexDTO> generateRectanglePoints(VertexDTO centerAnchor, double width, double height){
        if (width < 0) {throw new IllegalArgumentException("Width of rectangle points is negative, should be positive");}
        if (height < 0) {throw new IllegalArgumentException("Height of rectangle points is negative, should be positive");}
        VertexDTO p1 = new VertexDTO(centerAnchor.getX() - width/2, centerAnchor.getY() - height/2, centerAnchor.getZ());
        VertexDTO p2 = new VertexDTO(centerAnchor.getX() - width/2, centerAnchor.getY()  + height/2, centerAnchor.getZ());
        VertexDTO p3 = new VertexDTO(centerAnchor.getX() + width/2, centerAnchor.getY()  + height/2, centerAnchor.getZ());
        VertexDTO p4 = new VertexDTO(centerAnchor.getX() + width/2, centerAnchor.getY()  - height/2, centerAnchor.getZ());
        VertexDTO p5 = new VertexDTO(p1);
        return new ArrayList<>(List.of(p1,p2,p3,p4,p5));
    }

    public static List<VertexDTO> generateRectanglePoints(VertexDTO centerAnchor, double widthLeft, double widthRight, double heightBottom, double heightTop){
        if (widthLeft < 0 || widthRight < 0) {throw new IllegalArgumentException("Width of rectangle points is negative, should be positive");}
        if (heightTop < 0 || heightBottom < 0) {throw new IllegalArgumentException("Height of rectangle points is negative, should be positive");}
        VertexDTO p1 = new VertexDTO(centerAnchor.getX() - widthLeft, centerAnchor.getY() - heightBottom, centerAnchor.getZ());
        VertexDTO p2 = new VertexDTO(centerAnchor.getX() - widthLeft, centerAnchor.getY()  + heightTop, centerAnchor.getZ());
        VertexDTO p3 = new VertexDTO(centerAnchor.getX() + widthRight, centerAnchor.getY()  + heightTop, centerAnchor.getZ());
        VertexDTO p4 = new VertexDTO(centerAnchor.getX() + widthRight, centerAnchor.getY()  - heightBottom, centerAnchor.getZ());
        VertexDTO p5 = new VertexDTO(p1);
        return new ArrayList<>(List.of(p1,p2,p3,p4,p5));
    }

    public static List<VertexDTO> getAbsolutePointsPositionOfCutDTO(CutDTO cutDTO, CNCMachine cncMachine){
        Cut c = cncMachine.getPanel().createPanelCut(cutDTO);
        return c.getAbsolutePointsPosition(cncMachine);
    }

    public static List<VertexDTO> generateBorderPointsRelativeEdgeEdgeFromAbsolute(int bitIndex, Controller controller, CNCMachine cncMachine){
        VertexDTO panelDimension = cncMachine.getPanel().getPanelDimension();
        double baseBorderSize = 30;
        VertexDTO widthLeftHeightBottom = new VertexDTO(panelDimension.getX() / 2 - baseBorderSize, panelDimension.getY() / 2 - baseBorderSize, 0);
        VertexDTO widhtRightHeightTop = new VertexDTO(panelDimension.getX() / 2 - baseBorderSize, panelDimension.getY() / 2 - baseBorderSize, 0);
        List<VertexDTO> outputList = new ArrayList<>();
        outputList.add(widhtRightHeightTop);
        outputList.add(widthLeftHeightBottom);
        return  outputList;
    }

    public static List<VertexDTO> generateFreeCutPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p2Abs, int bitIndex, List<RefCutDTO> refs, Controller controller, CNCMachine cncMachine){
        VertexDTO anchor = refs.getFirst().getAbsoluteOffset(controller);
        List<VertexDTO> output = new ArrayList<>();
        output.add(p1Abs.sub(anchor));
        output.add(p2Abs.sub(anchor));
        return output;
    }

    public static List<VertexDTO> generateHorizontalPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p2Abs, int bitIndex, List<RefCutDTO> refs, Controller controller, CNCMachine cncMachine){
        int bit1Index = bitIndex;
        int bit2Index = refs.getFirst().getCut().getBitIndex();
        VertexDTO anchor = refs.getFirst().getAbsoluteOffset(controller);
        double diameter1 = cncMachine.getBitStorage().getBitDiameter(bit1Index);
        double diameter2 = cncMachine.getBitStorage().getBitDiameter(bit2Index);
        if(p1Abs.getY() - anchor.getY() != 0){
            VertexDTO directionAnchor = (new VertexDTO(0, p1Abs.getY() - anchor.getY(), 0)).normalize();
            anchor = anchor.add(directionAnchor.mul(diameter1/2));
            anchor = anchor.add(directionAnchor.mul(diameter2/2));
        }

        List<VertexDTO> output = new ArrayList<>();
        output.add(p1Abs.sub(anchor));
        output.add(p2Abs.sub(anchor));
        return output;
    }

    public static List<VertexDTO> generateVerticalPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p2Abs, int bitIndex, List<RefCutDTO> refs, Controller controller, CNCMachine cncMachine){
        int bit1Index = bitIndex;
        int bit2Index = refs.getFirst().getCut().getBitIndex();
        VertexDTO anchor = refs.getFirst().getAbsoluteOffset(controller);
        double diameter1 = cncMachine.getBitStorage().getBitDiameter(bit1Index);
        double diameter2 = cncMachine.getBitStorage().getBitDiameter(bit2Index);
        if(p1Abs.getX() - anchor.getX() != 0){
            VertexDTO directionAnchor = (new VertexDTO(p1Abs.getX() - anchor.getX(), 0, 0)).normalize();
            anchor = anchor.add(directionAnchor.mul(diameter1/2));
            anchor = anchor.add(directionAnchor.mul(diameter2/2));
        }

        List<VertexDTO> output = new ArrayList<>();
        output.add(p1Abs.sub(anchor));
        output.add(p2Abs.sub(anchor));
        return output;
    }

    public static List<VertexDTO> generateLPointsRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, int bitIndex, List<RefCutDTO> refs, Controller controller, CNCMachine cncMachine){
        // Compute the diameters of the bits used
        int bitIndexRef1 = refs.getFirst().getCut().getBitIndex();
        int bitIndexRef2 = refs.get(1).getCut().getBitIndex();
        int bitIndexL = bitIndex;
        double diameterRef1 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef1);
        double diameterRef2 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef2);
        double diameterL = cncMachine.getBitStorage().getBitDiameter(bitIndexL);

        // Needs to calculate the absolute two points
        VertexDTO p1a = refs.getFirst().getAbsoluteOffset(controller);
        VertexDTO p1b = refs.getFirst().getAbsoluteFirstPoint(controller);
        if(p1b.getDistance(p1a) < VertexDTO.doubleTolerance){
            p1b = refs.getFirst().getAbsoluteSecondPoint(controller);// Changing the other ref point to prevent accidental colinearity
        }

        // Get the first absolute reference point
        VertexDTO p2a = refs.get(1).getAbsoluteOffset(controller);
        VertexDTO p2b = refs.get(1).getAbsoluteFirstPoint(controller);
        if(p2b.getDistance(p2a) < VertexDTO.doubleTolerance){
            p2b = refs.get(1).getAbsoluteSecondPoint(controller);// Changing the other ref point to prevent accidental colinearity
        }


        Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(p1a,
                p1b, p2a, p2b);

        if(intersectionPoint.isPresent()){

            VertexDTO intersection = intersectionPoint.get();
            VertexDTO diff = p1Abs.sub(intersection);
            double dirX = 0;
            double dirY = 0;
            if(diff.getX() < 0){dirX = -1;}
            else if(diff.getX() > 0){dirX = 1;}
            if(diff.getY() < 0){dirY = -1;}
            else if(diff.getY() > 0){dirY = 1;}

            VertexDTO ref1Diff = p1b.sub(p1a);
            double dotX = Math.abs(ref1Diff.dotProduct(new VertexDTO(1, 0, 0)));
            double dotY = Math.abs(ref1Diff.dotProduct(new VertexDTO(0 , 1, 0)));

            if(dotY < dotX){ // if the first ref is more horizontal, flip the diameters
                double diameterTemp = diameterRef1;
                diameterRef1 = diameterTemp;
                diameterRef2 = diameterTemp;
            }

            // Get the internal width and height
            double width = diff.getX() - dirX * diameterL/2 - dirX * diameterRef1/2;
            double height = diff.getY() - dirY * diameterL/2 - dirY * diameterRef2/2;

            if(width < 0 && dirX > 0 || width > 0 && dirX < 0){
                width = 0;
            }
            if(height < 0 && dirY > 0 || height > 0 && dirY < 0){
                height = 0;
            }

            ArrayList<VertexDTO> outputs = new ArrayList<>();
            outputs.add(new VertexDTO(width, height ,0));
            return outputs;
        }
        else{
            throw new IllegalArgumentException("Cut arguments invalid");
        }
    }

    public static List<VertexDTO> generateRectangleRelativeEdgeEdgeFromAbsolute(VertexDTO p1Abs, VertexDTO p3Abs, int bitIndex, List<RefCutDTO> refs, Controller controller, CNCMachine cncMachine){
        // Compute the diameters of the bits used
        int bitIndexRef1 = refs.getFirst().getCut().getBitIndex();
        int bitIndexRef2 = refs.get(1).getCut().getBitIndex();
        int bitIndexL = bitIndex;
        double diameterRef1 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef1);
        double diameterRef2 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef2);
        double diameterL = cncMachine.getBitStorage().getBitDiameter(bitIndexL);

        // Needs to calculate the absolute two points
        VertexDTO p1a = refs.getFirst().getAbsoluteOffset(controller);
        VertexDTO p1b = refs.getFirst().getAbsoluteFirstPoint(controller);
        if(p1b.getDistance(p1a) < VertexDTO.doubleTolerance){
            p1b = refs.getFirst().getAbsoluteSecondPoint(controller);// Changing the other ref point to prevent accidental colinearity
        }

        // Get the first absolute reference point
        VertexDTO p2a = refs.get(1).getAbsoluteOffset(controller);
        VertexDTO p2b = refs.get(1).getAbsoluteFirstPoint(controller);
        if(p2b.getDistance(p2a) < VertexDTO.doubleTolerance){
            p2b = refs.get(1).getAbsoluteSecondPoint(controller);// Changing the other ref point to prevent accidental colinearity
        }

        Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(p1a,
                p1b, p2a, p2b);

        if(intersectionPoint.isPresent()){

            List<VertexDTO> outList = new ArrayList<>();

            VertexDTO diagonal = p3Abs.sub(p1Abs);
            VertexDTO midpoint = diagonal.mul(0.5);

            VertexDTO intersection = intersectionPoint.get();
            VertexDTO diff = midpoint.add(p1Abs).sub(intersection);
            double dirX = 0;
            double dirY = 0;
            if(diff.getX() < 0){dirX = -1;}
            else if(diff.getX() > 0){dirX = 1;}
            if(diff.getY() < 0){dirY = -1;}
            else if(diff.getY() > 0){dirY = 1;}

            VertexDTO ref1Diff = p1b.sub(p1a);
            double dotX = Math.abs(ref1Diff.dotProduct(new VertexDTO(1, 0, 0)));
            double dotY = Math.abs(ref1Diff.dotProduct(new VertexDTO(0 , 1, 0)));

            if(dotY < dotX){ // if the first ref is more horizontal, flip the diameters
                double diameterTemp = diameterRef1;
                diameterRef1 = diameterTemp;
                diameterRef2 = diameterTemp;
            }

            double offsetX = diff.getX() - dirX * diameterRef1/2;
            double offsetY = diff.getY() - dirY * diameterRef2/2;
            outList.add(new VertexDTO(offsetX, offsetY, 0));

            double widthEdgeEdge = Math.abs(diagonal.getX()) - diameterL;
            double heightEdgeEdge = Math.abs(diagonal.getY()) - diameterL;
            outList.add(new VertexDTO(widthEdgeEdge, heightEdgeEdge, 0));

            return outList;
        }
        else{
            throw new IllegalArgumentException("Cut arguments invalid");
        }
    }

    public static List<VertexDTO> getAbsoluteBorderPoints(Cut c, CNCMachine cncMachine){
        double bitDiameter = cncMachine.getBitStorage().getBitDiameter(c.getBitIndex());
        VertexDTO centerAnchor = cncMachine.getPanel().getPanelDimension().mul(0.5);
        double widthLeft = c.getPoints().getFirst().getX() + bitDiameter/2;
        double heightBottom = c.getPoints().getFirst().getY() + bitDiameter/2;
        double widthRight = c.getPoints().get(1).getX() + bitDiameter/2;
        double heightTop = c.getPoints().get(1).getY() + bitDiameter/2;
        return CutFactory.generateRectanglePoints(centerAnchor, widthLeft, widthRight, heightBottom, heightTop);
    }

    public static List<VertexDTO> getAbsoluteLineFreePoints(Cut c, CNCMachine cncMachine){
        return  c.getCopyPointsWithOffset(c.getRefs().getFirst().getAbsoluteOffset(cncMachine));
    }

    public static List<VertexDTO> getAbsoluteLineVerticalPoints(Cut c, CNCMachine cncMachine){
        int bit1Index = c.getBitIndex();
        int bit2Index = c.getRefs().getFirst().getCut().getBitIndex();
        VertexDTO anchor = c.getRefs().getFirst().getAbsoluteOffset(cncMachine);
        double diameter1 = cncMachine.getBitStorage().getBitDiameter(bit1Index);
        double diameter2 = cncMachine.getBitStorage().getBitDiameter(bit2Index);
        if(c.getPoints().getFirst().getX() != 0){
            VertexDTO directionAnchor = (new VertexDTO(c.getPoints().getFirst().getX(), 0, 0)).normalize();
            anchor = anchor.add(directionAnchor.mul(diameter1/2));
            anchor = anchor.add(directionAnchor.mul(diameter2/2));
        }

        return c.getCopyPointsWithOffset(anchor);
    }

    public static List<VertexDTO> getAbsoluteLineHorizontalPoints(Cut c, CNCMachine cncMachine){
        int bit1Index = c.getBitIndex();
        int bit2Index = c.getRefs().getFirst().getCut().getBitIndex();
        VertexDTO anchor = c.getRefs().getFirst().getAbsoluteOffset(cncMachine);
        double diameter1 = cncMachine.getBitStorage().getBitDiameter(bit1Index);
        double diameter2 = cncMachine.getBitStorage().getBitDiameter(bit2Index);
        if(c.getPoints().getFirst().getY() != 0){
            VertexDTO directionAnchor = (new VertexDTO(0, c.getPoints().getFirst().getY(), 0)).normalize();
            anchor = anchor.add(directionAnchor.mul(diameter1/2));
            anchor = anchor.add(directionAnchor.mul(diameter2/2));
        }

        return c.getCopyPointsWithOffset(anchor);
    }

    public static List<VertexDTO> getAbsoluteRectangularPoints(Cut c, CNCMachine cncMachine){
        if(c.getRefs().size() < 2){throw new AssertionError(c.getType() + " needs two refs, it has " + c.getRefs().size());}

        // Compute the diameters of the bits used
        int bitIndexRef1 = c.getRefs().getFirst().getCut().getBitIndex();
        int bitIndexRef2 = c.getRefs().get(1).getCut().getBitIndex();
        int bitIndexL = c.getBitIndex();
        double diameterRef1 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef1);
        double diameterRef2 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef2);
        double diameterL = cncMachine.getBitStorage().getBitDiameter(bitIndexL);

        // Needs to calculate the absolute two points
        VertexDTO p1a = c.getRefs().getFirst().getAbsoluteOffset(cncMachine);
        VertexDTO p1b = c.getRefs().getFirst().getAbsoluteFirstPoint(cncMachine);
        if(p1b.getDistance(p1a) < VertexDTO.doubleTolerance){
            p1b = c.getRefs().getFirst().getAbsoluteSecondPoint(cncMachine);// Changing the other ref point to prevent accidental colinearity
        }

        // Get the first absolute reference point
        VertexDTO p2a = c.getRefs().get(1).getAbsoluteOffset(cncMachine);
        VertexDTO p2b = c.getRefs().get(1).getAbsoluteFirstPoint(cncMachine);
        if(p2b.getDistance(p2a) < VertexDTO.doubleTolerance){
            p2b = c.getRefs().get(1).getAbsoluteSecondPoint(cncMachine);// Changing the other ref point to prevent accidental colinearity
        }

        Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(p1a,
                p1b, p2a, p2b);


        if(intersectionPoint.isPresent()){

            VertexDTO intersection = intersectionPoint.get();

            // Get the L orientation
            VertexDTO diff = c.getPoints().getFirst();
            double dirX = 0;
            double dirY = 0;
            if(diff.getX() < 0){dirX = -1;}
            else if(diff.getX() > 0){dirX = 1;}
            if(diff.getY() < 0){dirY = -1;}
            else if(diff.getY() > 0){dirY = 1;}

            // Find which ref is more horizontal, which is more vertical
            VertexDTO ref1Diff = p1b.sub(p1a);
            double dotX = Math.abs(ref1Diff.dotProduct(new VertexDTO(1, 0, 0)));
            double dotY = Math.abs(ref1Diff.dotProduct(new VertexDTO(0 , 1, 0)));

            if(dotY < dotX){ // if the first ref is more horizontal, flip the diameters
                double diameterTemp = diameterRef1;
                diameterRef1 = diameterTemp;
                diameterRef2 = diameterTemp;
            }

            double centerCenterOffsetX = intersection.getX() + diff.getX() + dirX * diameterRef1/2;
            double centerCenterOffsetY = intersection.getY() + diff.getY() + dirY * diameterRef2/2;
            VertexDTO centerCenterOffset = new VertexDTO(centerCenterOffsetX, centerCenterOffsetY, 0);

            double centerCenterWidth = c.getPoints().get(1).getX() + diameterL;
            double centerCenterHeight = c.getPoints().get(1).getY() + diameterL;

            return CutFactory.generateRectanglePoints(centerCenterOffset, centerCenterWidth, centerCenterHeight);

        }
        else{
            throw new ArithmeticException("The L refs are colinear");
        }
    }

    public static List<VertexDTO> getAbsoluteLPoints(Cut c, CNCMachine cncMachine) {
        if(c.getRefs().size() < 2){throw new AssertionError(c.getType() + " needs two refs, it has " + c.getRefs().size());}

        ArrayList<VertexDTO> outputPoints = new ArrayList<>();

        // Compute the diameters of the bits used
        int bitIndexRef1 = c.getRefs().getFirst().getCut().getBitIndex();
        int bitIndexRef2 = c.getRefs().get(1).getCut().getBitIndex();
        int bitIndexL = c.getBitIndex();
        double diameterRef1 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef1);
        double diameterRef2 = cncMachine.getBitStorage().getBitDiameter(bitIndexRef2);
        double diameterL = cncMachine.getBitStorage().getBitDiameter(bitIndexL);

        // Needs to calculate the absolute two points
        VertexDTO p1a = c.getRefs().getFirst().getAbsoluteOffset(cncMachine);
        VertexDTO p1b = c.getRefs().getFirst().getAbsoluteFirstPoint(cncMachine);
        if(p1b.getDistance(p1a) < VertexDTO.doubleTolerance){
            p1b = c.getRefs().getFirst().getAbsoluteSecondPoint(cncMachine);// Changing the other ref point to prevent accidental colinearity
        }

        // Get the first absolute reference point
        VertexDTO p2a = c.getRefs().get(1).getAbsoluteOffset(cncMachine);
        VertexDTO p2b = c.getRefs().get(1).getAbsoluteFirstPoint(cncMachine);
        if(p2b.getDistance(p2a) < VertexDTO.doubleTolerance){
            p2b = c.getRefs().get(1).getAbsoluteSecondPoint(cncMachine);// Changing the other ref point to prevent accidental colinearity
        }

        Optional<VertexDTO> intersectionPoint = VertexDTO.isLineIntersectNoLimitation(p1a,
                p1b, p2a, p2b);


        if(intersectionPoint.isPresent()){

            VertexDTO cornerRelatif = c.getPoints().getFirst();
            VertexDTO intersection = intersectionPoint.get();
            VertexDTO cornerInternal = intersection.add(cornerRelatif);

            // Get the L orientation
            VertexDTO diff = cornerRelatif;
            double dirX = 0;
            double dirY = 0;
            if(diff.getX() < 0){dirX = -1;}
            else if(diff.getX() > 0){dirX = 1;}
            if(diff.getY() < 0){dirY = -1;}
            else if(diff.getY() > 0){dirY = 1;}

            // Find which ref is more horizontal, which is more vertical
            VertexDTO ref1Diff = p1b.sub(p1a);
            double dotX = Math.abs(ref1Diff.dotProduct(new VertexDTO(1, 0, 0)));
            double dotY = Math.abs(ref1Diff.dotProduct(new VertexDTO(0 , 1, 0)));

            if(dotY < dotX){ // if the first ref is more horizontal, flip the diameters
                double diameterTemp = diameterRef1;
                diameterRef1 = diameterTemp;
                diameterRef2 = diameterTemp;

                VertexDTO tempPoint = new VertexDTO(p1a);
                p1a = new VertexDTO(p1b);
                p1b = new VertexDTO(tempPoint);

                tempPoint = new VertexDTO(p2a);
                p2a = new VertexDTO(p2b);
                p2b = new VertexDTO(tempPoint);
            }

            double centerCenterX = cornerInternal.getX() + dirX * diameterL/2 + dirX * diameterRef1/2;
            double centerCenterY = cornerInternal.getY() + dirY * diameterL/2 + dirY * diameterRef2/2;
            VertexDTO cornerCenterCenter = new VertexDTO(centerCenterX, centerCenterY, 0);

            VertexDTO diffCenterCenter = intersection.sub(cornerCenterCenter);
            VertexDTO firstLineLineIntersect = cornerCenterCenter.add(new VertexDTO(diffCenterCenter.getX(), 0, 0));
            VertexDTO secondLineLineIntersect = cornerCenterCenter.add(new VertexDTO(0, diffCenterCenter.getY(), 0));
            Optional<VertexDTO> intersection1 = VertexDTO.isLineIntersectNoLimitation(cornerCenterCenter, firstLineLineIntersect, p1a, p1b);
            Optional<VertexDTO> intersection2 = VertexDTO.isLineIntersectNoLimitation(cornerCenterCenter, secondLineLineIntersect, p2a, p2b);

            if(intersection1.isPresent() && intersection2.isPresent()){
                outputPoints.add(intersection1.get());
                outputPoints.add(cornerCenterCenter);
                outputPoints.add(intersection2.get());
            }
            else{ // No intersection, so when the cursor points on the anchor
                outputPoints.add(firstLineLineIntersect);
                outputPoints.add(cornerCenterCenter);
                outputPoints.add(secondLineLineIntersect);
            }
        }
        else{
            throw new ArithmeticException("The L refs are colinear");
        }
        return outputPoints;
    }
}
