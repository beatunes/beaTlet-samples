# Sample beaTlet for beaTunes 6.x
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

    # The sample rate we would like the audio to have, in Hz.
    def getPreferredSampleRate()
        return 44100.0
    end

    # The number of channels we would like the audio to have.
    def getPreferredChannels()
        return 1
    end

    # How much audio do we need? Return nil, if you need the whole file.
    # durationInSeconds is the length of the song.
    def getRequiredClip(durationInSeconds)
        # AudioClip takes start and stop times in ms
        return AudioClip.new(0, 120000)
    end

    # Create a new pipeline. See Jipes for details.
    # This must return a new pipeline every time it is called.
    def createPipeline(audioFormat, durationInSeconds)
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

