package com.imt.raytracing.raytracer.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.imt.raytracing.geometry.Intersection;
import com.imt.raytracing.geometry.shape.Sphere;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Camera;
import com.imt.raytracing.raytracer.Ray;
import com.imt.raytracing.raytracer.light.Light;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Scene {
    private int width;
    private int height;
    private Camera camera;
    private String output = "img/jalon3/output.png";
    private Color ambient = new Color();
    private List<Light> lights = new ArrayList<>();
    private List<Sphere> shapes = new ArrayList<>();

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addLight(Light light) {
        this.lights.add(light);
    }

    public void addShape(Sphere shape) {
        this.shapes.add(shape);
    }

    public Optional<Intersection> closestIntersection(Ray ray) {
        Intersection best = null;

        for (Sphere s : shapes) {
            Optional<Intersection> inter = s.intersect(ray);
            if (inter.isPresent()) {
                if (best == null || inter.get().t < best.t)
                    best = inter.get();
            }
        }
        return Optional.ofNullable(best);
    }
}
