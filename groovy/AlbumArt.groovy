// Sample beaTlet for beaTunes 4.x
// Works only in conjunction with AlbumArtShowHideAction.groovy
// More info at https://www.beatunes.com/en/beatlet-songcontextview.html
import javax.swing.*
import java.awt.*
import com.tagtraum.core.app.*
import com.tagtraum.core.image.*
import com.tagtraum.audiokern.AudioSong
import com.tagtraum.beatunes.*
import com.tagtraum.beatunes.songtable.SongContextView

// Simple song context view (below main song table in the UI) that shows
// the selected song's artwork along with album title and artist name.
// To work, this class requires a companion class, a SongContextComponentShowHideAction.
class AlbumArt implements SongContextView {

    BeaTunes application
    JComponent component
    JLabel image
    JLabel album
    JLabel artist

    AlbumArt() {
        // set up the layout
        component = new JPanel(new GridBagLayout())
        image = new JLabel()
        album = new JLabel()
        artist = new JLabel()
        image.setVerticalAlignment(SwingConstants.TOP)
        album.setVerticalAlignment(SwingConstants.TOP)
        artist.setVerticalAlignment(SwingConstants.TOP)
        GridBagConstraints gbc = new GridBagConstraints()
        gbc.fill = GridBagConstraints.VERTICAL
        gbc.anchor = GridBagConstraints.NORTHWEST
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weighty = 2
        gbc.gridheight = 2
        component.add(image, gbc)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.anchor = GridBagConstraints.NORTHWEST
        gbc.gridx = 1
        gbc.gridy = 0
        gbc.gridheight = 1
        gbc.weighty = 0
        gbc.insets = new Insets(0, 10, 0, 10)
        component.add(album, gbc)
        gbc.fill = GridBagConstraints.BOTH
        gbc.anchor = GridBagConstraints.NORTHWEST
        gbc.gridx = 1
        gbc.gridy = 1
        gbc.weightx = 2
        gbc.weighty = 2
        gbc.gridheight = 1
        component.add(artist, gbc)
    }

    // Is called when this view should be updated.
    def void update(AudioSong song) {
        // check for null!
        if (song != null) {
            Image albumArt = song.getImage()
            if (albumArt != null) {
                image.setIcon(new ImageIcon(ImageScaler.scale(albumArt, 300, 300)))
            } else {
                image.setIcon(null)
            }
            album.setText("<html><font size='+3'>" + song.getAlbum() + "</font></html>")
            artist.setText("<html><font color='#555555' size='-1'>by " + song.getArtist() + "</font></html>")
        } else {
            image.setIcon(null)
            album.setText(null)
            artist.setText(null)
        }
    }

    // Every SongContextView needs to be accompanied by a
    // SongContextComponentShowHideAction.
    // Return the action's id here.
    def String getShowHideActionId() {
        "groovy.albumart.showhide"
    }

    // The visual component to be shown in this view.
    def JComponent getComponent() {
        component
    }

    def void setApplication(ApplicationComponent application) {
        this.application = (BeaTunes) application
    }

    def BeaTunes getApplication() {
        application
    }

    def String getId() {
        "groovy.albumart"
    }

    def void init() {
    }

    def void shutdown() {
    }
}
