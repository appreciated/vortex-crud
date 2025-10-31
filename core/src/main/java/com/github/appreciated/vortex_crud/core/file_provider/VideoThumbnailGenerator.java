package com.github.appreciated.vortex_crud.core.file_provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for generating video thumbnails using FFmpeg.
 * Extracts the first frame of a video and saves it as a JPEG thumbnail.
 */
@Service
public class VideoThumbnailGenerator {

    private static final Logger log = LoggerFactory.getLogger(VideoThumbnailGenerator.class);
    private static final String FFMPEG_COMMAND = "ffmpeg";

    /**
     * Generates a thumbnail from the first frame of a video file.
     *
     * @param videoPath     Path to the source video file
     * @param thumbnailPath Path where the thumbnail should be saved (should end with .jpg)
     * @return true if thumbnail generation was successful, false otherwise
     */
    public boolean generateThumbnail(Path videoPath, Path thumbnailPath) {
        return generateThumbnail(videoPath, thumbnailPath, 0.0);
    }

    /**
     * Generates a thumbnail from a specific timestamp in a video file.
     *
     * @param videoPath      Path to the source video file
     * @param thumbnailPath  Path where the thumbnail should be saved (should end with .jpg)
     * @param timeInSeconds  Timestamp in seconds from which to extract the frame
     * @return true if thumbnail generation was successful, false otherwise
     */
    public boolean generateThumbnail(Path videoPath, Path thumbnailPath, double timeInSeconds) {
        if (!isFFmpegAvailable()) {
            log.warn("FFmpeg is not available. Thumbnail generation will be skipped.");
            return false;
        }

        List<String> command = new ArrayList<>();
        command.add(FFMPEG_COMMAND);
        command.add("-ss");
        command.add(String.valueOf(timeInSeconds));
        command.add("-i");
        command.add(videoPath.toString());
        command.add("-vframes");
        command.add("1");
        command.add("-q:v");
        command.add("2");
        command.add("-f");
        command.add("image2");
        command.add("-y"); // Overwrite output file if exists
        command.add(thumbnailPath.toString());

        try {
            Process process = new ProcessBuilder(command)
                    .redirectErrorStream(true)
                    .start();

            // Read output for logging
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.debug("FFmpeg: {}", line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                log.info("Successfully generated thumbnail: {} from video: {}", thumbnailPath, videoPath);
                return true;
            } else {
                log.error("FFmpeg exited with code {} while generating thumbnail for: {}", exitCode, videoPath);
                return false;
            }

        } catch (IOException e) {
            log.error("Failed to execute FFmpeg command for thumbnail generation: {}", videoPath, e);
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thumbnail generation was interrupted for: {}", videoPath, e);
            return false;
        }
    }

    /**
     * Checks if FFmpeg is available on the system.
     *
     * @return true if FFmpeg is available, false otherwise
     */
    public boolean isFFmpegAvailable() {
        try {
            Process process = new ProcessBuilder(FFMPEG_COMMAND, "-version")
                    .redirectErrorStream(true)
                    .start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (IOException | InterruptedException e) {
            log.debug("FFmpeg is not available on this system", e);
            return false;
        }
    }
}