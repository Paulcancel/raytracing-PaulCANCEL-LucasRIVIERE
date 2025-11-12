package com.imt.raytracing;

import java.util.ArrayList;
import java.util.List;

import com.imt.raytracing.shape.Shape;

public class Scene {
    private int width;
    private int height;
    private Camera camera;
    private String output = "output.png";
    private Color ambient = new Color();
    private List<Light> lights = new ArrayList<>();
    private List<Shape> shapes = new ArrayList<>();
}
