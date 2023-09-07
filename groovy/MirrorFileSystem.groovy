// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html


import javax.swing.Action
import java.awt.event.ActionEvent
import com.tagtraum.core.app.*
import com.tagtraum.audiokern.*
import com.tagtraum.beatunes.Property
import com.tagtraum.beatunes.BeaTunes
import com.tagtraum.beatunes.MainWindow
import com.tagtraum.beatunes.library.*
import com.tagtraum.beatunes.library.filesystem.*
import com.tagtraum.beatunes.action.*
import java.nio.file.*
import java.util.*
import java.awt.Cursor
import org.slf4j.*

class MirrorFileSystem extends BaseAction {

    static log = LoggerFactory.getLogger("MirrorFileSystem.groovy")

    def String getId() {
        "Groovy.MirrorFileSystem"
    }

    def void init() {
        putValue(Action.NAME, "Mirror File System")
    }

    /**
     * Install the action in both the Tools menu and the context
     * menu of the main table.
     */
    def ActionLocation[] getActionLocations() {
        [new AbsoluteActionLocation(BeaTunesUIRegion.TOOL_MENU, AbsoluteActionLocation.LAST)]
    }

    def void actionPerformed(ActionEvent actionEvent) {
        if (log.isInfoEnabled()) log.info("Starting...")
        BeaTunes application = getApplication()
        MediaLibrary library = application.getMediaLibrary()
        MainWindow mainWindow = application.getMainWindow()
        mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {

            // get collection root, will fail for iTunes/Music
            FileSystemLibraryDescriptor descriptor = (FileSystemLibraryDescriptor)library.getLibraryDescriptor()
            Path root = descriptor.getRoots()[0]
            if (log.isDebugEnabled()) log.debug("Found root: ${root}")

            // iterate through collection and remember folders and songs
            PlayList list = getApplication().getMediaLibrary().getLibrary();
            Iterable songs = list.getSongsOrderedBy(Property.LOCATION_PROPERTY.getFullyQualifiedName(), PlayList.SortDirection.ASCENDING);
            Map folders = new HashMap()
            for (AudioSong s : songs) {
                Path path = s.getFile()
                Path relativePath = root.relativize(path)
                Path relativeFolder = relativePath.getParent()
                List songsInFolder = folders.get(relativeFolder)
                if (songsInFolder == null) {
                    songsInFolder = new ArrayList()
                    folders.put(relativeFolder, songsInFolder)
                }
                songsInFolder.add(s.getId())
            }

            // create main folder ("mirror")
            if (log.isDebugEnabled()) log.debug("Creating mirror folder...")
            PlayList mirror = library.createFolder(list.getId())
            mirror.setName("Mirror")
            library.store(mirror);

            Map createdPlayLists = new HashMap()

            for (Path p : folders.keySet()) {
                int count = p.getNameCount()

                // create folders
                PlayList l = mirror
                for (int i=0; i<count-1; i++) {
                    String f = p.getName(i).toString()

                    String path = ""
                    for (int j=0; j<(i+1); j++) path += ("/" + p.getName(j).toString())

                    if (log.isDebugEnabled()) log.debug("Checking folder ${path} (for ${p})")

                    if (!createdPlayLists.containsKey(path)) {
                        if (log.isDebugEnabled()) log.debug("For path ${path} creating folder named ${f}")
                        PlayList folder = library.createFolder(l.getId())
                        folder.setName(f)
                        library.store(folder)
                        createdPlayLists.put(path, folder)
                        l = folder
                    } else {
                        l = createdPlayLists.get(path)
                    }
                }

                // create playlist
                String playListName = p.getName(count-1)
                if (!createdPlayLists.containsKey(p.toString())) {
                    if (log.isDebugEnabled()) log.debug("For path ${p} creating playlist named ${playListName}")
                    PlayList newList  = library.createPlayList(l.getId(), playListName)
                    library.store(newList)
                    createdPlayLists.put(p.toString(), newList)
                    l = newList
                    // mainWindow.getPlayListTree().addPlaylist(l);
                } else {
                    l = createdPlayLists.get(p.toString())
                }

                // fill playlist
                l.setSongIds(folders.get(p))
            }
            if (log.isInfoEnabled()) log.info("Done.")
        } finally {
            mainWindow.setCursor(null);
        }
        library.refresh(false, true, false);
    }
}
