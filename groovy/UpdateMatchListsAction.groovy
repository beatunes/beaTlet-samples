// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html
import javax.swing.*
import java.awt.event.ActionEvent
import org.slf4j.LoggerFactory
import com.tagtraum.core.app.*
import com.tagtraum.beatunes.MessageDialog
import com.tagtraum.beatunes.action.*
import com.tagtraum.beatunes.library.MatchListInfo;
import com.tagtraum.beatunes.matchlist.MatchListCreator;

class UpdateMatchListsAction extends BaseAction {

    static log = LoggerFactory.getLogger("UpdateMatchListsAction.groovy")
    boolean cancelled = false

    def String getId() {
        "Groovy.UpdateMatchListsAction"
    }

    def void init() {
        putValue(Action.NAME, "Update MatchLists")
    }

    def ActionLocation[] getActionLocations() {
        [new AbsoluteActionLocation(BeaTunesUIRegion.TOOL_MENU,
            AbsoluteActionLocation.LAST)]
    }

    def void actionPerformed(ActionEvent actionEvent) {
        int result = new MessageDialog(
            getApplication().getMainWindow(),
            "Do you really want to update all MatchLists?",
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.YES_NO_OPTION
        ).showDialog()

        if (result == JOptionPane.YES_OPTION) {
            cancelled = false
            def playlists = getApplication().getMediaLibrary().getPlayLists()
            playlists.each {
                if (it.isSpecialPlayList() && !cancelled) {
                    final matchList = it
                    log.info "Found MatchList ${matchList}, attempting to update..."
                    MatchListInfo matchListInfo = (MatchListInfo) it.getSpecialPlayListInfo()
                    final MatchListCreator creator = new MatchListCreator(matchListInfo, getApplication()) {
                        def void onTryAgain() {
                            log.info "Failed to update MatchList ${matchList}"
                            // TODO: Show message.
                        }

                        def void onDone() {
                            // TODO: Show message.
                        }

                        def void onCancelled() {
                            log.info "Cancelled update of MatchList ${matchList}"
                            cancel()
                            // TODO: Show message.
                        }

                    };
                    creator.create();
                }
            }
        }
    }

    def cancel() {
        cancelled = true
    }
}
