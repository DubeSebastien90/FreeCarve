package Domain.ThirdDimension;

import java.awt.*;
import java.security.InvalidKeyException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Scene {
    Map<UUID, Mesh> meshes;

    public Scene(List<Mesh> meshes) {
        setMeshes(meshes);
    }

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

    public Mesh getMesh(UUID uuid) throws InvalidKeyException {
        if (!meshes.containsKey(uuid)) {throw new InvalidKeyException("Mesh with that id does not exist");}
        return meshes.get(uuid);
    }

    public void setMeshes(List<Mesh> meshes){
        this.meshes = meshes.stream().collect(Collectors.toMap(Mesh::getId, mesh -> mesh));
    }

    public void applyTransform(UUID transformId, VertexDTO positionChange, VertexDTO rotationChange, float scaleChange) throws InvalidKeyException {
        Mesh mesh = getMesh(transformId);
        mesh.getPosition().add(new Vertex(positionChange));
        mesh.setRotationEuler(Vertex.add(mesh.getRotationEuler(), new Vertex(rotationChange)));
        mesh.setScale(mesh.getScale() + scaleChange);
    }
}
