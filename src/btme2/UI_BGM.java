/*
 * The MIT License
 *
 * Copyright 2024 barackobama.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package btme2;

import java.io.File; 
import java.io.IOException; 
import java.util.Random;

import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem; 
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException; 

/**
 *
 * @author barackobama
 */
public class UI_BGM {
    Clip clip;
    AudioInputStream audioInputStream; 
    
    String musics[] = { "src/btme2/music/ledstorm.wav", "src/btme2/music/acid_god.wav", "src/btme2/music/bubblegum.wav", "src/btme2/music/i need a gus  astro.wav",
                        "src/btme2/music/sirens.wav", "src/btme2/music/a piece of magicmix.wav"
    };
    
    int rnd = new Random().nextInt(musics.length);
    String filepath = musics[rnd];;
    
    public UI_BGM()
        throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        System.out.println(new File(filepath).getAbsoluteFile());
        audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
