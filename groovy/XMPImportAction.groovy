// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html
import org.slf4j.LoggerFactory
import javax.swing.*
import java.awt.Toolkit
import java.awt.event.*
import java.io.*
import com.tagtraum.core.app.*
import com.tagtraum.audiokern.*
import com.tagtraum.beatunes.library.*
import com.tagtraum.beatunes.action.*
import com.tagtraum.beatunes.songtable.SongPropertyChangeListener

import javax.xml.*
import javax.xml.parsers.*
import org.w3c.dom.*
import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.*

/*
 * Simple beaTlet that attempts to find Adobe XMP metadata
 * and imports it.
 */
class XMPImportAction extends BaseAction {

    static dynamicMediaNamespace = "http://ns.adobe.com/xmp/1.0/DynamicMedia/"
    static dublinCoreNamespace = "http://purl.org/dc/elements/1.1/"
    static rdfNamespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    static xmpMarker = "<x:xmpmeta"

    static log = LoggerFactory.getLogger("XMPImportAction.groovy")

    def String getId() {
        "Groovy.XMPImportAction"
    }

    def void init() {
        setEnabled(false)
        putValue(Action.NAME, "Import XMP")
        // enable this action, if one or more items are selected from the given tables
        bindListeners(BaseAction.SelectionCount.ONE_OR_MORE,
            BaseAction.EnabledView.SONG_TABLE, BaseAction.EnabledView.MATCH_TABLE)
    }

    def ActionLocation[] getActionLocations() {
        [new AbsoluteActionLocation(BeaTunesUIRegion.FILE_MENU,
            AbsoluteActionLocation.LAST)]
    }

    def void actionPerformed(ActionEvent actionEvent) {
        final library = getApplication().getiTunesMusicLibrary()
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance()
        factory.setNamespaceAware(true)
        // we're being super sloppy, so that we don't have to extract just he XMP from the file
        factory.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true)
        getSelectedSongIds().each{ id ->
            log.warn "Attempting to import XMP metadata from file for ${id}"

            try {
                // "AudioSong" is the interface we typically use to read and write song properties
                // It allows manipulation of *both* the database and the file at the same time.
                final AudioSong song = library.getSong(id)
                final File file = song.getFile()
                if (file != null) {
                    log.debug "Searching for \"${xmpMarker}\" in ${file} for ${song}"
                    PushbackInputStream stream = null
                    try {
                        stream = new PushbackInputStream(new BufferedInputStream(new FileInputStream(file)), 10)
                        def mark = xmpMarker.getBytes("ASCII")
                        if (search(stream, mark)) {
                            stream.unread(mark)
                            log.debug "Importing values from file ${file} for ${song}"
                            def builder = factory.newDocumentBuilder()
                            builder.setErrorHandler(new DefaultHandler() {
                                // just ignore errors—if things go South, we're
                                // simply not going to find anything.
                                def void fatalError(SAXParseException exception) throws SAXException {}
                            })
                            Document document = builder.parse(stream)
                            String artist = getDynamicMediaValue(document, "artist")
                            String album = getDynamicMediaValue(document, "album")
                            String genre = getDynamicMediaValue(document, "genre")
                            int trackNumber = 0
                            try {
                                String trackNumberString = getValue(document, "trackNumber")
                                if (trackNumberString != null) {
                                    trackNumber = Integer.parseInt(trackNumberString)
                                }
                            } catch (Exception e) {
                                log.warn("Failed to parse track number.", e)
                            }
                            String name = null
                            // cumbersome navigation to get title... :-(
                            NodeList list = ((NodeList) document.getElementsByTagNameNS(dublinCoreNamespace, "title"))
                            if (list.getLength() > 0) {
                                NodeList altElements = list.item(0).getElementsByTagNameNS(rdfNamespace, "Alt")
                                if (altElements.getLength() > 0) {
                                    name = altElements.item(0).getTextContent().trim()
                                }
                            }
                            // make sure, changes are displayed in the UI
                            song.addPropertyChangeListener(new SongPropertyChangeListener((JTable)getFocusOwner(), song))
                            // Only overwrite stuff, if we actually found something.
                            if (name != null) song.setName(name)
                            if (artist != null) song.setArtist(artist)
                            if (album != null) song.setAlbum(album)
                            if (genre != null) song.setGenre(genre)
                            if (trackNumber != 0) song.setTrackNumber(trackNumber)
                        }
                    } finally {
                        if (stream != null)  {
                            stream.close()
                        }
                    }
                } else {
                    log.warn "Failed to find file for ${song}"
                }
            } catch (Exception e) {
                log.error("Failed to import XMP data from file ${id}", e)
            }
        }
    }

    def String getDynamicMediaValue(Document document, String dynamicMediaElementName) {
        NodeList list = ((NodeList) document.getElementsByTagNameNS(dynamicMediaNamespace, dynamicMediaElementName))
        return list.getLength() == 0 ? null : list.item(0).getTextContent().trim()
    }

    // Knuth-Morris-Pratt algorithm adapted from
    // https://github.com/twitter/elephant-bird/blob/master/core/src/main/java/com/twitter/elephantbird/util/StreamSearcher.java
    def boolean search(InputStream stream, byte[] pattern) throws IOException {
        byte[] pattern_ = Arrays.copyOf(pattern, pattern.length)
        int[] borders_ = new int[pattern_.length + 1]

        // pre process
        int i = 0
        int j = -1
        borders_[i] = j
        while (i < pattern_.length) {
            while (j >= 0 && pattern_[i] != pattern_[j]) {
                j = borders_[j]
            }
            borders_[++i] = ++j
        }

        // main routine
        long bytesRead = 0
        int b
        int k = 0
        while ((b = stream.read()) != -1) {
            bytesRead++
            while (k >= 0 && (byte)b != pattern_[k]) {
                k = borders_[k]
            }
            // Move to the next character in the pattern.
            ++k
            // If we've matched up to the full pattern length, we found it.  Return,
            // which will automatically save our position in the InputStream at the point immediately
            // following the pattern match.
            if (k == pattern_.length) {
                return true
            }
        }
        // No dice, Note that the stream is now completely consumed.
        return false
    }
}
