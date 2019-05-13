// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html
import javax.swing.*
import java.awt.event.ActionEvent
import java.awt.FileDialog
import java.awt.Cursor
import java.text.SimpleDateFormat
import java.io.InputStreamReader
import java.io.FileInputStream
import java.nio.file.Paths
import com.tagtraum.core.OperatingSystem
import com.tagtraum.core.app.*
import com.tagtraum.beatunes.MessageDialog
import com.tagtraum.beatunes.action.*
import com.tagtraum.audiokern.AudioSong
import com.tagtraum.audiokern.AudioSongLocation
import javax.xml.stream.*
import javax.xml.stream.events.*
import javax.xml.namespace.*
import java.util.Collections
import org.slf4j.*

class ImportTraktorDatesAndCounts  extends BaseAction {

    static log = LoggerFactory.getLogger("ImportTraktorDatesAndCounts.groovy")

    def String getId() {
        "Groovy.ImportTraktorDatesAndCounts"
    }

    def void init() {
        putValue(Action.NAME, "Import Traktor Dates, Counts & Comment 2")
    }

    def ActionLocation[] getActionLocations() {
        [new AbsoluteActionLocation(BeaTunesUIRegion.TOOL_MENU,
            AbsoluteActionLocation.LAST)]
    }

    def void actionPerformed(ActionEvent actionEvent) {
        int res = new MessageDialog(
            getApplication().getMainWindow(),
            "<html><b>Do you really want to import Traktor dates, counts, and comment 2?</b><br><br>"
            + "This will overwrite all existing dates, counts, and the custom1 field.</html>",
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION
        ).showDialog()
        if (res != JOptionPane.YES_OPTION) return

        // show dialog that lets user select the collection.nml file
        final File collection = getCollectionNML()
        if (collection == null) return
        // this can take a little while. set wait cursor (blue wheel)
        getApplication().getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR))
        // iterate over <ENTRY>,<LOCATION>,<INFO> tags, but not in EDT
        def t = new Thread({
            XMLInputFactory factory = null
            XMLEventReader reader = null
            try {
                factory = XMLInputFactory.newInstance()
                reader = factory.createXMLEventReader(new InputStreamReader(new FileInputStream(collection), "UTF-8"))
                String location = null
                while(reader.hasNext()) {
                    XMLEvent event = reader.next()
                    if (event.isStartElement()) {
                        StartElement element = event.asStartElement()
                        if (element.getName().getLocalPart().equals("LOCATION")) {
                            location = getLocation(element)
                        }
                        if (location != null && element.getName().getLocalPart().equals("INFO")) {
                            importInfo(element, location)
                            location = null
                        }
                    }
                }
                // let user know we're done in EDT
                SwingUtilities.invokeLater({
                    // reset to regular cursor
                    getApplication().getMainWindow().setCursor(null)
                    new MessageDialog(
                        getApplication().getMainWindow(),
                        "Done.",
                        JOptionPane.INFORMATION_MESSAGE,
                        JOptionPane.DEFAULT_OPTION
                    ).showDialog()
                } as Runnable)
            } catch (Exception e) {
                // report errod in EDT
                SwingUtilities.invokeLater({
                    // reset to regular cursor
                    getApplication().getMainWindow().setCursor(null)
                    new MessageDialog(
                        getApplication().getMainWindow(),
                        "An error occurred: " + e.toString(),
                        JOptionPane.ERROR_MESSAGE,
                        JOptionPane.DEFAULT_OPTION
                    ).showDialog()
                } as Runnable)
            } finally {
                if (reader != null) reader.close()
            }
        } as Runnable)
        t.start()
    }

    def boolean importInfo(StartElement element, String location) {
        AudioSong song = null
        try {
            Attribute playcount = element.getAttributeByName(new QName("PLAYCOUNT"))
            Attribute lastPlayed = element.getAttributeByName(new QName("LAST_PLAYED"))
            // probably for historic reasons, Traktor calls comment 2 "rating"
            // its .nml file.
            // we map its contents to the field custom1.
            Attribute rating = element.getAttributeByName(new QName("RATING"))
            if (playcount != null || lastPlayed != null || rating != null) {
                song = getSong(location)
                if (song != null) {
                    if (lastPlayed != null) {
                        log.debug("Setting playdate for " + song + ": " + lastPlayed.getValue())
                        song.setPlayDateUTC(new SimpleDateFormat("yyyy/M/d").parse(lastPlayed.getValue()))
                    }
                    if (playcount != null) {
                        log.debug("Setting playcount for " + song + ": " + playcount.getValue())
                        song.setPlayCount(Integer.valueOf(playcount.getValue()))
                    }
                    if (rating != null) {
                        log.debug("Setting rating/comment2/custom1 for " + song + ": " + rating.getValue())
                        song.setCustom1(rating.getValue())
                    }
                } else {
                    log.info("Failed to find song for location " + location)
                }
            }
        } catch(Exception e) {
            log.error("Failed to import info for " + song, e)
        }
    }

    def String getLocation(StartElement element) {
        String dir = element.getAttributeByName(new QName("DIR")).getValue()
        String file = element.getAttributeByName(new QName("FILE")).getValue()
        String volume = element.getAttributeByName(new QName("VOLUME")).getValue()
        //System.out.println(volume + dir + file)
        if (OperatingSystem.isMac()) {
            return "/Volumes/" + volume + (dir.replace("/:", "/")) + file
        } else {
            return volume + (dir.replace("/:", "/")) + file
        }
    }

    def AudioSong getSong(String location) {
        AudioSongLocation audioSongLocation = new AudioSongLocation(Paths.get(location))
        List<AudioSong> songs = getApplication().getMediaLibrary().getSongsWithProperties(Collections.singletonMap("location", audioSongLocation.getLocation()))
        if (songs.isEmpty() && audioSongLocation.isFile() && audioSongLocation.getLocation().contains("/localhost/")) {
            songs = getApplication().getMediaLibrary().getSongsWithProperties(Collections.singletonMap("location", audioSongLocation.getLocation().replace("localhost", "")))
        }
        return songs.isEmpty() ? null : songs.get(0)
    }

    def File getCollectionNML() {
        final FileDialog dialog = new FileDialog(getApplication().getMainWindow(),
                "Please select the collection.nml file")
        dialog.setFilenameFilter({d, f-> f ==~ /.*.nml/ } as FilenameFilter)
        dialog.setDirectory(System.getProperty("user.home") + "/Documents/Native Instruments/")
        dialog.setMode(FileDialog.LOAD)
        dialog.setVisible(true)
        return dialog.getFile() == null ? null : dialog.getFiles()[0]
    }
}
