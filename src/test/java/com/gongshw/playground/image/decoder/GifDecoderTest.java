package com.gongshw.playground.image.decoder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

/**
 * @author gongshiwei
 */
public class GifDecoderTest {
    @Test
    public void extract() throws Exception {
        GifDecoder gifDecoder = new GifDecoder();
        int frames = 44;
        File gifFile = new File(getClass().getResource("test.gif").getFile());
        File targetDir = gifFile.getParentFile();
        gifDecoder.extract(gifFile, targetDir);
        for (int i = 0; i < frames; i++) {
            File gifFrameFileI = new File(targetDir.getAbsolutePath() + "/test.gif." + i + ".gif");
            assertTrue(gifFrameFileI.exists());
            gifFrameFileI.deleteOnExit();
        }
        File gifFrameFileNonExist = new File(targetDir.getAbsolutePath() + "/test.gif." + frames + ".gif");
        assertFalse(gifFrameFileNonExist.exists());
    }
}
