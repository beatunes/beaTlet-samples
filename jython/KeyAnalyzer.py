# Sample beaTlet for beaTunes 4.x
# More info at https://www.beatunes.com/en/beatlet-songpropertyanalyzer.html
from com.tagtraum.audiokern import AudioClip
from com.tagtraum.audiokern import SongPropertyAnalyzer
from com.tagtraum.jipes.audio import Mono

# Simple song property analyzer.
class KeyAnalyzer(SongPropertyAnalyzer):

    # Name of this analyzer. Please include a version number.
    def getName(self):
        return "Jython KeyAnalyzer 1.0.0"

    # How much audio do we need?
    def getRequiredClip(self, audioFileFormat):
        # AudioClip takes start and stop times in ms
        return AudioClip(0, 120000)

    # Create a new pipeline. See Jipes for details.
    def createPipeline(self):
        # Mono does not compute a key. It's merely a stand-in for a real
        # com.tagtraum.jipes.SignalPipeline
        # The id of the processor that computes the key, must be "key".
        return Mono()

    # The property you want to change.
    # This corresponds to com.tagtraum.audiokern.AudioSong properties.
    def getPropertyName(self):
        return "key"

