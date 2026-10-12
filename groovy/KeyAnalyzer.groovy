// Sample beaTlet for beaTunes 6.x
// More info at https://www.beatunes.com/en/beatlet-songpropertyanalyzer.html
import com.tagtraum.audiokern.*
import com.tagtraum.jipes.*
import com.tagtraum.jipes.audio.*
import javax.sound.sampled.AudioFormat

// Simple song property analyzer.
class KeyAnalyzer implements SongPropertyAnalyzer {

    // Name of this analyzer. Please include a version number.
    def String getName() {
        return "Groovy KeyAnalyzer 1.0.0"
    }

    // The sample rate we would like the audio to have, in Hz.
    def float getPreferredSampleRate() {
        return 44100f
    }

    // The number of channels we would like the audio to have.
    def int getPreferredChannels() {
        return 1
    }

    // How much audio do we need? Return null, if you need the whole file.
    // durationInSeconds is the length of the song.
    def AudioClip getRequiredClip(float durationInSeconds) {
        // AudioClip takes start and stop times in ms
        return new AudioClip(0, 120000)
    }

    // Create a new pipeline. See Jipes for details.
    // This must return a new pipeline every time it is called.
    def SignalProcessor createPipeline(AudioFormat audioFormat, float durationInSeconds) {
        // Mono does not compute a key. It's merely a stand-in for a real
        // com.tagtraum.jipes.SignalPipeline
        // The id of the processor that computes the key, must be "key".
        return new Mono()
    }

    // The property you want to change.
    // This corresponds to com.tagtraum.audiokern.AudioSong properties.
    def String getPropertyName() {
        "key"
    }
}

