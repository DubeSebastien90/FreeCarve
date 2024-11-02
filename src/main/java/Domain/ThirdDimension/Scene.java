package Domain.ThirdDimension;

import java.awt.*;
import java.security.InvalidKeyException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Groups different meshes together and gives an absolute coordinate system.
 *
 * @author Kamran Charles Nayebi
 * @since 2024-11-01
 */
public class Scene {
    /**
     * Map of UUIDs and meshes for easy retrieval by ID
     */
    Map<UUID, Mesh> meshes;

    /**
     * Constructs scene with a list of meshes
     * @param meshes list of meshes
     */
    public Scene(List<Mesh> meshes) {
        setMeshes(meshes);
    }

    /**
     * Default constructor with temporary code
     */
    public Scene(){
        // Sets the default scene, the car for now
        try {
            Mesh car = new Mesh(Vertex.zero(), Color.GRAY, "car.stl", 100);
            car.setRotationEuler(new Vertex(Math.PI, Math.PI/4, 0));
            setMeshes(List.of(car));
        } catch (Exception e){
            System.out.println("File not found");
        }
    }

    public Collection<Mesh> getMeshes(){
        return meshes.values();
    }

    /**
     * Retrieves a mesh with an id
     * @param uuid id of the mesh
     * @return the mesh
     * @throws InvalidKeyException if there is no mesh associated with that id
     */
    public Mesh getMesh(UUID uuid) throws InvalidKeyException {
        if (!meshes.containsKey(uuid)) {throw new InvalidKeyException("Mesh with that id does not exist in the scene");}
        return meshes.get(uuid);
    }

    public void setMeshes(List<Mesh> meshes){
        this.meshes = meshes.stream().collect(Collectors.toMap(Mesh::getId, mesh -> mesh));
    }

    /**
     * Applies a transform to a mesh in the scene
     * @param transformId id of the mesh
     * @param positionChange delta of the position change
     * @param rotationChange delta of the rotation change
     * @param scaleChange delta fo the scale change
     * @throws InvalidKeyException if the mesh with that id doesn't exist in the scene
     */
    public void applyTransform(UUID transformId, VertexDTO positionChange, VertexDTO rotationChange, float scaleChange) throws InvalidKeyException {
        Mesh mesh = getMesh(transformId);
        mesh.getPosition().add(new Vertex(positionChange));
        mesh.setRotationEuler(Vertex.add(mesh.getRotationEuler(), new Vertex(rotationChange)));
        mesh.setScale(mesh.getScale() + scaleChange);
    }
}
