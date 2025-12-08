package com.imt.raytracing;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.imt.raytracing.imaging.Renderer;
import com.imt.raytracing.parsing.SceneFileParser;
import com.imt.raytracing.raytracer.scene.Scene;

/**
 * Utility class to compare two BufferedImage type images.
 * Necessary because JUnit does not have a native image comparator.
 */
class ImageComparisonUtil {
    // Max color tolerance per pixel (on a 0-255 scale)
    private static final int MAX_COLOR_DIFFERENCE = 10; 
    // Maximum percentage of pixels allowed to be in error
    private static final double MAX_PIXEL_ERROR_PERCENT = 0.5; 

    /**
     * Compares two images pixel by pixel.
     * @param img1 The reference image (expected).
     * @param img2 The generated image (actual).
     * @return true if the images are similar within the defined tolerance.
     */
    public static boolean compare(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            System.err.println("Inconsistent image dimensions.");
            return false;
        }

        int width = img1.getWidth();
        int height = img1.getHeight();
        int totalPixels = width * height;
        int errorCount = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);

                int r1 = (rgb1 >> 16) & 0xFF;
                int g1 = (rgb1 >> 8) & 0xFF;
                int b1 = (rgb1) & 0xFF;

                int r2 = (rgb2 >> 16) & 0xFF;
                int g2 = (rgb2 >> 8) & 0xFF;
                int b2 = (rgb2) & 0xFF;
                
                // Calculate cumulative color difference
                int colorDiff = Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);

                if (colorDiff > MAX_COLOR_DIFFERENCE * 3) { 
                    errorCount++;
                }
            }
        }

        double errorPercent = (double) errorCount / totalPixels * 100;
        
        if (errorPercent > MAX_PIXEL_ERROR_PERCENT) {
            System.err.printf("Image too different. Error: %.2f%% of incorrect pixels (> %.2f%%).%n", 
                              errorPercent, MAX_PIXEL_ERROR_PERCENT);
            return false;
        }

        return true;
    }
}


public class RaytracingIntegrationTest {

    private static final String RESOURCE_BASE_PATH = "src/test/resources/imagetest/";
    
    @TempDir
    File tempDir; 
    
    static Stream<Arguments> sceneFilesProvider() {
        return Stream.of(
            Arguments.of("tp61-dir.test", "tp61-dir.png"),
            Arguments.of("tp62-1.test", "tp62-1.png"),
            Arguments.of("tp61.test", "tp61.png"),
            Arguments.of("tp51-diffuse.test", "tp51-diffuse.png"),
            Arguments.of("tp51-specular.test", "tp51-specular.png")
        );
    }

    /**
     * Parameterized integration test method running the full rendering pipeline 
     * for multiple scene files.
     * * @param sceneFileName The name of the input scene file (e.g., "tp61-dir.test").
     * @param expectedImageName The name of the reference image (e.g., "tp61-dir.png").
     */
    @ParameterizedTest(name = "Test rendering scene: {0}")
    @MethodSource("sceneFilesProvider")
    void testRenderingPipeline_MatchesReference(String sceneFileName, String expectedImageName) throws Exception {
        
        String sceneFilePath = RESOURCE_BASE_PATH + sceneFileName;
        String expectedImagePath = RESOURCE_BASE_PATH + expectedImageName;
        
        File generatedFile = new File(tempDir, sceneFileName.replace(".test", "-generated.png"));

        BufferedImage expectedImage = ImageIO.read(new File(expectedImagePath));
        assertNotNull(expectedImage, "The expected image file was not found: " + expectedImagePath);

        SceneFileParser parser = new SceneFileParser();
        Scene scene = parser.parse(sceneFilePath);

        scene.setOutput(generatedFile.getAbsolutePath());

        Renderer renderer = new Renderer();
        BufferedImage generatedImage = renderer.render(scene);

        ImageIO.write(generatedImage, "png", generatedFile);

        BufferedImage actualImage = ImageIO.read(generatedFile);

        assertTrue(ImageComparisonUtil.compare(expectedImage, actualImage), 
                   "The rendered image does not match the reference image for scene: " + sceneFileName);
    }
}