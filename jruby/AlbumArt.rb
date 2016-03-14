# Sample beaTlet for beaTunes 4.x
# More info at https://www.beatunes.com/en/beatlet-songcontextview.html
# Only works in conjuntion with AlbumArtShowHideAction.rb
require 'java'

java_import javax.swing.SwingConstants
java_import javax.swing.JLabel
java_import javax.swing.JPanel
java_import javax.swing.ImageIcon
java_import java.awt.GridBagLayout
java_import java.awt.GridBagConstraints
java_import java.awt.Image
java_import java.awt.Insets
java_import com.tagtraum.core.image.ImageScaler;

# Simple song context view (below main song table in the UI) that shows
# the selected song's artwork along with album title and artist name.
# To work, this class requires a companion class, a SongContextComponentShowHideAction.
class AlbumArt
        
    include Java::com.tagtraum.beatunes.songtable.SongContextView
    
    def initialize 
        # set up the layout
        @component = JPanel.new(GridBagLayout.new())
        @image = JLabel.new()
        @album = JLabel.new()
        @artist = JLabel.new()
        @image.setVerticalAlignment(SwingConstants::TOP)
        @album.setVerticalAlignment(SwingConstants::TOP)
        @artist.setVerticalAlignment(SwingConstants::TOP)
        gbc = GridBagConstraints.new()
        gbc.fill = GridBagConstraints::VERTICAL
        gbc.anchor = GridBagConstraints::NORTHWEST
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weighty = 2
        gbc.gridheight = 2
        @component.add(@image, gbc)
        gbc.fill = GridBagConstraints::HORIZONTAL
        gbc.anchor = GridBagConstraints::NORTHWEST
        gbc.gridx = 1
        gbc.gridy = 0
        gbc.gridheight = 1
        gbc.weighty = 0
        gbc.insets = Insets.new(0, 10, 0, 10)
        @component.add(@album, gbc)
        gbc.fill = GridBagConstraints::BOTH
        gbc.anchor = GridBagConstraints::NORTHWEST
        gbc.gridx = 1
        gbc.gridy = 1
        gbc.weightx = 2
        gbc.weighty = 2
        gbc.gridheight = 1
        @component.add(@artist, gbc)
    end
    
    # Is called when this view should be updated.
    def update(song) 
        # check for nil!
        if (!song.nil?) 
            albumArt = song.getImage()
            if (!albumArt.nil?) 
                @image.setIcon(ImageIcon.new(ImageScaler.scale(albumArt, 300, 300)));
            else 
                @image.setIcon(nil);
            end
            @album.setText("<html><font size='+3'>#{song.getAlbum}</font></html>");
            @artist.setText("<html><font color='#555555' size='-1'>by #{song.getArtist}</font></html>");
        else 
            @image.setIcon(nil);
            @album.setText(nil);
            @artist.setText(nil);
        end
    end

    # Every SongContextView needs to be accompanied by a
    # SongContextComponentShowHideAction.
    # Return the action's id here.
    def getShowHideActionId() 
        "jruby.albumart.showhide"
    end

    # The visual component to be shown in this view.
    def getComponent() 
        @component
    end

    def setApplication(application) 
        @application = application
    end

    def getApplication() 
        @application
    end

    def getId() 
        "jruby.albumart"
    end

    def init() 
    end

    def shutdown() 
    end
end

