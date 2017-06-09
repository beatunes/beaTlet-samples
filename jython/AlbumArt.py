# Sample beaTlet for beaTunes 5.x
# Works only in conjunction with AlbumArtShowHideAction.py
# More info at https://www.beatunes.com/en/beatlet-songcontextview.html
from java.awt import GridBagLayout
from java.awt import GridBagConstraints
from java.awt import Insets
from javax.swing import ImageIcon
from javax.swing import JPanel
from javax.swing import JLabel
from javax.swing import SwingConstants
from com.tagtraum.core.image import ImageScaler
from com.tagtraum.beatunes.songtable import SongContextView

# Simple song context view (below main song table in the UI) that shows
# the selected song's artwork along with album title and artist name.
# To work, this class requires a companion class, a SongContextComponentShowHideAction.
class AlbumArt(SongContextView):

    def __init__(self):
        # set up the layout
        self.__component = JPanel(GridBagLayout())
        self.__image = JLabel()
        self.__album = JLabel()
        self.__artist = JLabel()
        self.__application = None
        self.__image.setVerticalAlignment(SwingConstants.TOP)
        self.__album.setVerticalAlignment(SwingConstants.TOP)
        self.__artist.setVerticalAlignment(SwingConstants.TOP)
        gbc = GridBagConstraints()
        gbc.fill = GridBagConstraints.VERTICAL
        gbc.anchor = GridBagConstraints.NORTHWEST
        gbc.gridx = 0
        gbc.gridy = 0
        gbc.weighty = 2
        gbc.gridheight = 2
        self.__component.add(self.__image, gbc)
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.anchor = GridBagConstraints.NORTHWEST
        gbc.gridx = 1
        gbc.gridy = 0
        gbc.gridheight = 1
        gbc.weighty = 0
        gbc.insets = Insets(0, 10, 0, 10)
        self.__component.add(self.__album, gbc)
        gbc.fill = GridBagConstraints.BOTH
        gbc.anchor = GridBagConstraints.NORTHWEST
        gbc.gridx = 1
        gbc.gridy = 1
        gbc.weightx = 2
        gbc.weighty = 2
        gbc.gridheight = 1
        self.__component.add(self.__artist, gbc)


    # Is called when this view should be updated.
    def update(self, song):
        # check for None!
        if (song != None):
            albumArt = song.getImage()
            if (albumArt != None):
                self.__image.setIcon(ImageIcon(ImageScaler.scale(albumArt, 300, 300)));
            else:
                self.__image.setIcon(None);

            self.__album.setText("<html><font size='+3'>" + song.getAlbum() + "</font></html>");
            self.__artist.setText("<html><font color='#555555' size='-1'>by " + song.getArtist() + "</font></html>");
        else:
            self.__image.setIcon(None);
            self.__album.setText(None);
            self.__artist.setText(None);



    # Every SongContextView needs to be accompanied by a
    # SongContextComponentShowHideAction.
    # Return the action's id here.
    def getShowHideActionId(self):
        return "jython.albumart.showhide"


    # The visual component to be shown in this view.
    def getComponent(self):
        return self.__component


    def setApplication(self, application):
        self.__application = application


    def getApplication(self):
        return self.__application


    def getId(self):
        return "jython.albumart"


    def init(self):
        pass

    def shutdown(self):
        pass
