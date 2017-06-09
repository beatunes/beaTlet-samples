# Sample beaTlet for beaTunes 5.x
# More info at https://www.beatunes.com/en/beatlet-playlistexporter.html
from com.tagtraum.beatunes.library import PlayListExporter

# This example should use the codecs module, but due to
# http://bugs.jython.org/issue1722
# this isn't possible.

class HTMLPlayListExporter(PlayListExporter):

    # Lowercase file extension (without the '.').
    def getFileExtension(self):
        return "html"

    # Due to http://bugs.jython.org/issue2403 the interface
    # default methods getId() and getDescription()
    # cannot be overridden. :-(

    # Exports the given playlist to the given file.
    #
    # file to write to
    # playlist to export
    # progressListener that lets you report... well, progress
    def export(self, file, playList, progressListener):
        f = None
        try:
            f = open(file.toString(), 'w')
            f.write("<html>\n<body>\n<h1>%s</h1>\n<ol>\n" % (playList.getName().encode('UTF-8')))
            for song in playList.getSongs():
                f.write("<li>%s</li>\n" % (song.getName().encode('UTF-8')))
            f.write("</ol>\n</body>\n</html>\n")
        finally:
            if (f is not None):
                f.close()
