// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-songpropertyanalyzer.html
import com.tagtraum.audiokern.*
import com.tagtraum.jipes.*
import com.tagtraum.jipes.audio.*
import javax.sound.sampled.AudioFileFormat

// Simple song property analyzer.
class KeyAnalyzer implements SongPropertyAnalyzer {

    // Name of this analyzer. Please include a version number.
    def String getName() {
        return "Groovy KeyAnalyzer 1.0.0"
    }

    // How much audio do we need?
    def AudioClip getRequiredClip(AudioFileFormat audioFileFormat) {
        // AudioClip takes start and stop times in ms
        return new AudioClip(0, 120000)
    }

    // Create a new pipeline. See Jipes for details.
    def SignalProcessor createPipeline() {
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

