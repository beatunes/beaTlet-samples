# Sample beaTlet for beaTunes 5.x
# More info at https://www.beatunes.com/en/beatlet-songpropertyanalyzer.html
require 'java'

java_import com.tagtraum.jipes.audio.Mono
java_import com.tagtraum.audiokern.AudioClip

# Simple song property analyzer.
class KeyAnalyzer

    # Implement SongPropertyAnalyzer interface
    include Java::com.tagtraum.audiokern.SongPropertyAnalyzer

    # Name of this analyzer. Please include a version number.
    def getName()
        return "JRuby KeyAnalyzer 1.0.0"
    end

    # How much audio do we need?
    def getRequiredClip(audioFileFormat)
        # AudioClip takes start and stop times in ms
        return AudioClip.new(0, 120000)
    end

    # Create a new pipeline. See Jipes for details.
    def createPipeline()
        # Mono does not compute a key. It's merely a stand-in for a real
        # com.tagtraum.jipes.SignalPipeline
        # The id of the processor that computes the key, must be "key".
        return Mono.new()
    end

    # The property you want to change.
    # This corresponds to com.tagtraum.audiokern.AudioSong properties.
    def getPropertyName()
        return "key"
    end
end

