# Sample beaTlet for beaTunes 5.x
# More info at https://www.beatunes.com/en/beatlet-keytextrenderer.html
require 'java'

java_import com.tagtraum.audiokern.key.Key
java_import com.tagtraum.beatunes.KeyTextRenderer

class CustomKeyRenderer

# Implement KeyTextRenderer interface
include Java::com.tagtraum.beatunes.KeyTextRenderer

    # Create a textual representation for a key object.
    def toKeyString(key)
        # key.ordinal is a number starting with C Major = 0 and A Minor = 0,
        # then following the order in the Circle of Fifths.
        # Let's shift by 8 and make sure 0 is converted to 12.
        i = (key.ordinal + 8) % 12
        i = i == 0 ? 12 : i
        ab = key.isMajor ? "A" : "B"
        # create the final string
        return "#{i}#{ab}"
    end

    # Create a tooltip representation for a key object.
    # This may also include html-tags.
    def toToolTip(key)
        return toKeyString(key)
    end

    # Short name of this renderer. To be used in the user interface.
    def getName()
        return "Custom.rb"
    end

end

