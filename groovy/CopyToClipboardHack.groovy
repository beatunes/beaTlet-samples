// Sample beaTlet for beaTunes 5.x
// Fixes on issue on Mac, where a file copied to the clipboard
// cannot be pasted into Finder (and some other apps)
import javax.swing.*
import java.awt.event.*
import java.awt.*
import com.tagtraum.core.app.*
import com.tagtraum.beatunes.action.*
import com.tagtraum.audiokern.*
import java.nio.file.Path

class CopyToClipboardHack extends BaseAction {

    def String getId() {
        "Groovy.CopyToClipboardHack"
    }

    def void init() {
        putValue(Action.NAME, "Copy Hack")
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() | InputEvent.ALT_MASK))
        bindListeners(BaseAction.SelectionCount.ONE, BaseAction.EnabledView.SONG_TABLE, BaseAction.EnabledView.MATCH_TABLE)
    }

    def ActionLocation[] getActionLocations() {
        [new AbsoluteActionLocation(BeaTunesUIRegion.EDIT_MENU,
            AbsoluteActionLocation.LAST)]
    }

    def void actionPerformed(ActionEvent actionEvent) {
        AudioSong song = getSelectedSong()
        Path file = song.getFile()
        if (file != null) {
            copyToClipboard(file)
        }
    }

    def void copyToClipboard(Path filepath) {
        String[] cmd = ["osascript", "-e", "tell app \"Finder\" to set the clipboard to ( POSIX file \"" + filepath + "\" )"]
        try {
            Runtime.getRuntime().exec(cmd)
        } catch (Exception ex) {
            ex.printStackTrace()
        }
    }
}
