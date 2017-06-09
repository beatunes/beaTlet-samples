// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-playlistexporter.html
import java.io.File
import com.tagtraum.core.ProgressListener
import com.tagtraum.beatunes.library.PlayListExporter
import com.tagtraum.beatunes.library.PlayList

/**
 * Simple exporter for the WPL playlist format supported
 * by Windows Media Player and SONOS.
 */
class WPLPlayListExporter implements PlayListExporter {

    // Lowercase file extension (without the '.').
    def String getFileExtension() {
        "wpl"
    }

    // Very short description for this exporter.
    // Starting with beaTunes 4.6, this method is optional.
    def String getDescription() {
        "WPL"
    }

    // Lets you provide an id for this exporter.
    // The id may be used for referring to an instance of this exporter
    // in persistent configuration files.
    def String getId() {
        "groovy.wplplaylistexporter"
    }

    // Exports the given playlist to the given file.
    //
    // file to write to
    // playlist to export
    // progressListener that lets you report... well, progress
    def void export(File file, PlayList playList, ProgressListener progressListener) {
        file.withWriter { f ->
            f.writeLine("<?wpl version=\"1.0\"?>\n<smil>\n  <head>\n    <meta name=\"Generator\" content=\"beaTunes4 groovy.wplplaylistexporter\"/>\n"
                + "    <meta name=\"ItemCount\" content=\"${playList.getUnfilteredSize()}\"/>\n"
                + "    <title>${toXML(playList.getName())}</title>\n"
                + "  </head>\n  <body>\n    <seq>")
            playList.getSongs().each() { song ->
                f.writeLine("      <media src=\"${song.getFile()}\"/>")
            }
            f.writeLine("    </seq>\n  </body>\n</smil>\n")
        }
    }

    def static String toXML(final String s) {
        if (s == null) return ""
        int length = s.length()
        // Give the string buffer enough room for a couple of escaped characters
        final StringBuilder result = new StringBuilder(length+12)
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i)
            switch (c) {
                case '\r':
                    result.append("&#x0D")
                    break
                case 14:
                    // impossible
                    break
                case 15:
                    // impossible
                    break
                case 16:
                    // impossible
                    break
                case 17:
                    // impossible
                    break
                case 18:
                    // impossible
                    break
                case 19:
                    // impossible
                    break
                case 20:
                    // impossible
                    break
                case 21:
                    // impossible
                    break
                case 22:
                    // impossible
                    break
                case 23:
                    // impossible
                    break
                case 24:
                    // impossible
                    break
                case 25:
                    // impossible
                    break
                case 26:
                    // impossible
                    break
                case 27:
                    // impossible
                    break
                case 28:
                    // impossible
                    break
                case 29:
                    // impossible
                    break
                case 30:
                    // impossible
                    break
                case 31:
                    // impossible
                    break
                case ' ':
                    result.append(' ')
                    break
                case '!':
                    result.append('!')
                    break
                case '"':
                    result.append('"')
                    break
                case '#':
                    result.append('#')
                    break
                case '$':
                    result.append('$')
                    break
                case '%':
                    result.append('%')
                    break
                case '&':
                    result.append("&amp")
                    break
                case '\'':
                    result.append('\'')
                    break
                case '(':
                    result.append('(')
                    break
                case ')':
                    result.append(')')
                    break
                case '*':
                    result.append('*')
                    break
                case '+':
                    result.append('+')
                    break
                case ',':
                    result.append(',')
                    break
                case '-':
                    result.append('-')
                    break
                case '.':
                    result.append('.')
                    break
                case '/':
                    result.append('/')
                    break
                case '0':
                    result.append('0')
                    break
                case '1':
                    result.append('1')
                    break
                case '2':
                    result.append('2')
                    break
                case '3':
                    result.append('3')
                    break
                case '4':
                    result.append('4')
                    break
                case '5':
                    result.append('5')
                    break
                case '6':
                    result.append('6')
                    break
                case '7':
                    result.append('7')
                    break
                case '8':
                    result.append('8')
                    break
                case '9':
                    result.append('9')
                    break
                case ':':
                    result.append(':')
                    break
                case '':
                    result.append('')
                    break
                case '<':
                    result.append("&lt")
                    break
                case '=':
                    result.append('=')
                    break
                case '>':
                    result.append("&gt")
                    break
                default:
                    result.append(c)
            }
        }
        return result.toString()
    }
}
