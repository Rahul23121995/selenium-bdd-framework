package com.showcase.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.monte.media.Format;
import org.monte.media.Registry;
import org.monte.media.math.Rational;
import org.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static org.monte.media.FormatKeys.*;
import static org.monte.media.VideoFormatKeys.*;

/**
 * Utility class to record test executions visually in AVI format using Monte Screen Recorder.
 * Wraps AWT configurations safely to avoid HeadlessExceptions in CI pipelines.
 */
public class ScreenRecorderUtil extends ScreenRecorder {
    private static final Logger log = LogManager.getLogger(ScreenRecorderUtil.class);
    private static ScreenRecorder screenRecorder;
    private final String name;

    public ScreenRecorderUtil(GraphicsConfiguration cfg, Rectangle captureArea, Format fileFormat,
                              Format screenFormat, Format mouseFormat, Format audioFormat, File movieFolder, String name)
            throws IOException, AWTException {
        super(cfg, captureArea, fileFormat, screenFormat, mouseFormat, audioFormat, movieFolder);
        this.name = name;
    }

    @Override
    protected File createMovieFile(Format fileFormat) throws IOException {
        if (!movieFolder.exists()) {
            movieFolder.mkdirs();
        } else if (!movieFolder.isDirectory()) {
            throw new IOException("\"" + movieFolder + "\" is not a directory.");
        }
        // Custom name matching the scenario
        String cleanName = name.replaceAll("[^a-zA-Z0-9_-]", "_");
        return new File(movieFolder, cleanName + "." + Registry.getInstance().getExtension(fileFormat));
    }

    public static void startRecording(String scenarioName) {
        // Safe check for headless systems (like CI/CD servers) where no display exist
        if (GraphicsEnvironment.isHeadless()) {
            log.warn("System is running in Headless environment. Skipping video recording.");
            return;
        }

        try {
            log.info("Starting visual screen recording for scenario: {}", scenarioName);
            File file = new File("./target/recordings/");
            
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle captureSize = new Rectangle(0, 0, screenSize.width, screenSize.height);

            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getDefaultConfiguration();

            screenRecorder = new ScreenRecorderUtil(gc, captureSize,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE,
                            CompressorNameKey, ENCODING_AVI_TECHSMITH_SCREEN_CAPTURE, DepthKey, 24, FrameRateKey, Rational.valueOf(15),
                            QualityKey, 1.0f, KeyFrameIntervalKey, 15 * 60),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, "black", FrameRateKey, Rational.valueOf(30)),
                    null, file, scenarioName);
            screenRecorder.start();
        } catch (Exception e) {
            log.error("Failed to start automated screen recording. Details: ", e);
        }
    }

    public static void stopRecording() {
        if (GraphicsEnvironment.isHeadless() || screenRecorder == null) {
            return;
        }

        try {
            log.info("Stopping screen recording.");
            screenRecorder.stop();
        } catch (Exception e) {
            log.error("Failed to cleanly stop screen recording. Details: ", e);
        } finally {
            screenRecorder = null;
        }
    }
}
