package Domain.ThirdDimension;

import java.security.InvalidKeyException;
import java.security.KeyException;
import java.util.*;
import java.util.stream.Collectors;

public class Scene {
    Map<UUID, Mesh> meshes;

    public Scene(List<Mesh> meshes) {
        setMeshes(meshes);
    }

    public Scene(){
        meshes = new HashMap<>();
    }

    public Collection<Mesh> getMeshes(){
        return meshes.values();
    }

    public boolean hasMesh(UUID uuid){
        return meshes.containsKey(uuid);
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
        mesh.getRotationEuler().add(new Vertex(rotationChange));
        mesh.setScale(mesh.getScale() + scaleChange);
    }
}
