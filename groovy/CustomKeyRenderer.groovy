// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-keytextrenderer.html
import com.tagtraum.audiokern.key.Key
import com.tagtraum.beatunes.KeyTextRenderer

class CustomKeyRenderer implements KeyTextRenderer {

    /**
     * Create a textual representation for a Key object.
     *
     * @param key key
     * @return textual representation
     */
    def String toKeyString(Key key) {
        // key.ordinal() is a number starting with C Major = 0 and A Minor = 0,
        // then following the order in the Circle of Fifths.
        // Let's shift by 8 and make sure 0 is converted to 12.
        int i = (key.ordinal() + 8) % 12
        i = i == 0 ? 12 : i
        String ab = key.isMinor() ? "A" : "B"
        // create the final string
        return "$i$ab"
    }

    /**
     * Create a tooltip representation for a key object.
     * This may also include html-tags.
     *
     * @param key key
     * @return tooltip representation
     */
    def String toToolTip(Key key) {
        toKeyString(key)
    }

    /**
     * Short name of this renderer. To be used in the user interface.
     *
     * @return name
     */
    def String getName() {
        "Custom.groovy"
    }

}
