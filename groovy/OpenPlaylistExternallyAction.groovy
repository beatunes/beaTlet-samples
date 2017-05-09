// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html
import org.slf4j.LoggerFactory
import java.awt.Toolkit
import java.io.File
import java.awt.event.ActionEvent
import java.awt.event.*
import java.awt.Desktop
import javax.swing.*
import com.tagtraum.core.app.*
import com.tagtraum.beatunes.library.*
import com.tagtraum.beatunes.MessageDialog
import com.tagtraum.beatunes.action.*

/**
 * Simple beaTlet that lets you export playlists and open
 * them in the external default player.
 */
class OpenPlaylistExternallyAction extends BaseAction {

    static log = LoggerFactory.getLogger("OpenPlaylistExternallyAction.groovy")

    def String getId() {
        "Groovy.OpenPlaylistExternallyAction"
    }

    def void init() {
        putValue(Action.NAME, "Open Playlist Externally")
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))
    }

    def ActionLocation[] getActionLocations() {
        [new AbsoluteActionLocation(BeaTunesUIRegion.FILE_MENU,
            AbsoluteActionLocation.LAST)]
    }

    def void actionPerformed(ActionEvent actionEvent) {
        try {
            final exporters = getApplication().getPluginManager().getImplementations(PlayListExporter.class)
            // find the right exporter by searching for the desired file extension
            final format = "m3u"
            //final format = "m3u8"
            //final format = "pls"
            //final format = "xspf"
            final exporter = exporters.findResult { it.getFileExtension().equals(format) ? it : null }
            final file = File.createTempFile("beaTunes_OpenPlaylistExternally_", "." + format)
            final playlist = getApplication().getMainWindow().getPlayListTree().getSelectedPlayList()
            log.info("Exporting playlist ${playlist} to ${file}")
            exporter.export(file, playlist, null)
            log.info("Opening ${file} in external program.")
            Desktop.getDesktop().open(file)
        } catch (Exception e) {
            log.error("Failed to export and open playlist.", e);
            new MessageDialog(
                getApplication().getMainWindow(),
                "<html>Failed to export and play playlist.<br><br>${e.toString()}</html>",
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.DEFAULT_OPTION
            ).showDialog()
        }
    }
}
