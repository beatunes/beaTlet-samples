# Sample beaTlet for beaTunes 5.x
# More info at https://www.beatunes.com/en/beatlet-keytextrenderer.html
from com.tagtraum.audiokern.key import Key
from com.tagtraum.beatunes import KeyTextRenderer

class CustomKeyRenderer(KeyTextRenderer):

    # Create a textual representation for a Key object.
    def toKeyString(self, key):
        # key.ordinal is a number starting with C Major = 0 and A Minor = 0,
        # then following the order in the Circle of Fifths.
        # Let's shift by 8 and make sure 0 is converted to 12.
        i = (key.ordinal() + 8) % 12
        i = 12 if i==0 else i
        # i = i == 0 ? 12 : i
        ab = "A" if key.isMinor() else "B"
        # create the final string
        return str(i) + ab

    # Create a tooltip representation for a key object.
    # This may also include html-tags.
    def toToolTip(self, key):
        return self.toKeyString(key)

    # Short name of this renderer. To be used in the user interface.
    def getName(self):
        return "Custom.py"
