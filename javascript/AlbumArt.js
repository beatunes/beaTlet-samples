// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-songcontextview.html
// Only works in conjunction with AlbumArtShowHideAction.js

/*
 * These type vars basically act as imports for Java classes.
 */
var Insets = Java.type("java.awt.Insets");
var GridBagLayout = Java.type("java.awt.GridBagLayout");
var GridBagConstraints = Java.type("java.awt.GridBagConstraints");

var Action = Java.type("javax.swing.Action");
var SwingConstants = Java.type("javax.swing.SwingConstants");
var ImageIcon = Java.type("javax.swing.ImageIcon");
var JPanel = Java.type("javax.swing.JPanel");
var JLabel = Java.type("javax.swing.JLabel");

var ImageScaler = Java.type("com.tagtraum.core.image.ImageScaler");
var SongContextView = Java.type("com.tagtraum.beatunes.songtable.SongContextView");

// set up the layout
var component = new JPanel(new GridBagLayout());
var image = new JLabel();
var album = new JLabel();
var artist = new JLabel();
var gbc = new GridBagConstraints();

image.setVerticalAlignment(SwingConstants.TOP);
album.setVerticalAlignment(SwingConstants.TOP);
artist.setVerticalAlignment(SwingConstants.TOP);
gbc.fill = GridBagConstraints.VERTICAL;
gbc.anchor = GridBagConstraints.NORTHWEST;
gbc.gridx = 0;
gbc.gridy = 0;
gbc.weighty = 2;
gbc.gridheight = 2;
component.add(image, gbc);
gbc.fill = GridBagConstraints.HORIZONTAL;
gbc.anchor = GridBagConstraints.NORTHWEST;
gbc.gridx = 1;
gbc.gridy = 0;
gbc.gridheight = 1;
gbc.weighty = 0;
gbc.insets = new Insets(0, 10, 0, 10);
component.add(album, gbc);
gbc.fill = GridBagConstraints.BOTH;
gbc.anchor = GridBagConstraints.NORTHWEST;
gbc.gridx = 1;
gbc.gridy = 1;
gbc.weightx = 2;
gbc.weighty = 2;
gbc.gridheight = 1;
component.add(artist, gbc);

// ApplicationComponent is an interface, which we can
// implement via "new" (much like an anonymous inner Java class).
// The resulting instance is stored in the "beatlet" variable.
var beatlet = new SongContextView() {

    application: null,

    // Is called when this view should be updated.
    update: function(song) {
        // check for null!
        if (song != null) {
            var albumArt = song.getImage()
            if (albumArt != null) {
                image.setIcon(new ImageIcon(ImageScaler.scale(albumArt, 300, 300)));
            } else {
                image.setIcon(null);
            }
            album.setText("<html><font size='+3'>"
                + song.getAlbum() + "</font></html>");
            artist.setText("<html><font color='#555555' size='-1'>by "
                + song.getArtist() + "</font></html>");
        } else {
            image.setIcon(null);
            album.setText(null);
            artist.setText(null);
        }
    },

    // Every SongContextView needs to be accompanied by a
    // SongContextComponentShowHideAction.
    // Return the action's id here.
    getShowHideActionId: function() {
        return "javascript.albumart.showhide";
    },

    // The visual component to be shown in this view.
    getComponent: function() {
        return component;
    },

    /*
     * The application object is injected by beaTunes
     * right after this script has been eval'd.
     */
    setApplication: function(application) {
        this.application = application;
    },

    /*
     * beaTunes application object.
     */
    getApplication: function() {
        return this.application;
    },

    /*
     * This id is referenced in the corresponding SongContextView.
     */
    getId: function() {
        return "javascript.albumart";
    },

    init: function() {
    },

    shutdown: function() {
    }

}

// Put "beatlet" into the last line, so that it is returned
// to beaTunes when this script is eval'd.
beatlet;
