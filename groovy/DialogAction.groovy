// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html
import javax.swing.*
import java.awt.event.ActionEvent
import com.tagtraum.core.app.*
import com.tagtraum.beatunes.MessageDialog
import com.tagtraum.beatunes.action.*

class DialogAction extends BaseAction {

    def String getId() {
        "Groovy.DialogAction"
    }

    def void init() {
        putValue(Action.NAME, "DialogAction")
    }

    def ActionLocation[] getActionLocations() {
        [new AbsoluteActionLocation(BeaTunesUIRegion.TOOL_MENU,
            AbsoluteActionLocation.LAST)]
    }

    def void actionPerformed(ActionEvent actionEvent) {
        new MessageDialog(
            getApplication().getMainWindow(),
            "DialogAction",
            JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.DEFAULT_OPTION
        ).showDialog()
    }
}

