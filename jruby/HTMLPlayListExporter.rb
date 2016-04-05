# Sample beaTlet for beaTunes 4.x
# More info at https://www.beatunes.com/en/beatlet-playlistexporter.html
require 'java'

class HTMLPlayListExporter

    # The interface we need to implement.
    include Java::com.tagtraum.beatunes.library.PlayListExporter

    # Lowercase file extension (without the '.').
    def getFileExtension()
        "html"
    end

    # Very short description for this exporter.
    # Starting with beaTunes 4.6, this method is optional.
    def getDescription()
        "HTML"
    end

    # Lets you provide an id for this exporter.
    # The id may be used for referring to an instance of this exporter
    # in persistent configuration files.
    def getId()
        "jruby.htmlplaylistexporter"
    end

    # Exports the given playlist to the given file.
    #
    # file to write to
    # playlist to export
    # progressListener that lets you report... well, progress
    def export(file, playList, progressListener)
        f = nil
        begin
            f = File.new(file.toString, "w")
            f.write("<html>\n<body>\n<h1>#{playList.getName}</h1>\n<ol>\n")
            playList.getSongs.each{ |song|
                f.write("<li>#{song.getName}</li>\n");
            }
            f.write("</ol>\n</body>\n</html>\n")
        ensure
            if (!f.nil?)
                f.close
            end
        end
    end
end
