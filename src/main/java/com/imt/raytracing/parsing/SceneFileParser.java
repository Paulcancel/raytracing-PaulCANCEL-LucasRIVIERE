package com.imt.raytracing.parsing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.geometry.shape.Sphere;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Camera;
import com.imt.raytracing.raytracer.light.DirectionalLight;
import com.imt.raytracing.raytracer.light.PointLight;
import com.imt.raytracing.raytracer.scene.Scene;

public class SceneFileParser {

    public Scene parse(String filename) throws Exception {

        Scene scene = new Scene();

        List<Point> vertexes = new ArrayList<>();
        Color diffuse = new Color(1,1,1);

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;
            while ((line = br.readLine()) != null) {

                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] tok = line.split("\\s+");

                switch (tok[0]) {

                    case "size":
                        scene.setWidth(Integer.parseInt(tok[1]));
                        scene.setHeight(Integer.parseInt(tok[2]));
                        break;

                    case "output":
                        scene.setOutput(tok[1]);
                        break;

                    case "camera":
                        Point eye = new Point(
                                Double.parseDouble(tok[1]),
                                Double.parseDouble(tok[2]),
                                Double.parseDouble(tok[3]));

                        Point lookAt = new Point(
                                Double.parseDouble(tok[4]),
                                Double.parseDouble(tok[5]),
                                Double.parseDouble(tok[6]));

                        Vector up = new Vector(
                                Double.parseDouble(tok[7]),
                                Double.parseDouble(tok[8]),
                                Double.parseDouble(tok[9]));

                        double fov = Double.parseDouble(tok[10]);

                        scene.setCamera(new Camera(eye, lookAt, up, fov));
                        break;

                    case "ambient":
                        scene.setAmbient(new Color(
                                Double.parseDouble(tok[1]),
                                Double.parseDouble(tok[2]),
                                Double.parseDouble(tok[3])));
                        break;

                    case "diffuse":
                        diffuse = new Color(
                                Double.parseDouble(tok[1]),
                                Double.parseDouble(tok[2]),
                                Double.parseDouble(tok[3]));
                        break;

                    case "sphere":
                        scene.getShapes().add(new Sphere(
                                new Point(
                                        Double.parseDouble(tok[1]),
                                        Double.parseDouble(tok[2]),
                                        Double.parseDouble(tok[3])),
                                Double.parseDouble(tok[4]),
                                diffuse
                        ));
                        break;

                    default:
                        System.out.println("Instruction inconnue : " + tok[0]);
                }
            }
        }

        return scene;
    }
}