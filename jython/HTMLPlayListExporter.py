# Sample beaTlet for beaTunes 4.x
# More info at https://www.beatunes.com/en/beatlet-playlistexporter.html
from com.tagtraum.beatunes.library import PlayListExporter

# This example should use the codecs module, but due to
# http://bugs.jython.org/issue1722
# this isn't possible.

class HTMLPlayListExporter(PlayListExporter):

    # Lowercase file extension (without the '.').
    def getFileExtension(self):
        return "html"

    # Very short description for this exporter.
    # Starting with beaTunes 4.6, this method is optional.
    def getDescription(self):
        return "HTML"

    # Lets you provide an id for this exporter.
    # The id may be used for referring to an instance of this exporter
    # in persistent configuration files.
    def getId(self):
        return "jython.htmlplaylistexporter"

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
