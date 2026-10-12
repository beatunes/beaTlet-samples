# beaTlet-samples

beaTlet code samples for [beaTunes](https://www.beatunes.com/) 6, in Groovy and JRuby.

To install, simply place a script in the beaTunes [plugins directory](https://help.beatunes.com/kb/plugins/how-to-install-a-plugin).

For more info about beaTunes plugin development in general, please see [here](https://www.beatunes.com/en/beatunes-plugin-api.html).
To get started with beaTlets, check out the [getting started](https://www.beatunes.com/en/beatlet-getting-started.html) page.

## JavaScript and Jython are no longer supported

beaTunes 6 runs beaTlets written in **Groovy** and **JRuby** only. JavaScript and Jython
beaTlets are not loaded anymore, so their samples have been removed from this branch.

If you still use beaTunes 5, you can find all samples as they were for that version,
including the JavaScript and Jython ones, under the tag
[beaTunes-5](https://github.com/beatunes/beaTlet-samples/tree/beaTunes-5).

## Moving a beaTlet from beaTunes 5 to 6

The samples here have been updated to the beaTunes 6 API. If you have beaTlets of your own,
these are the changes they most likely need:

- **Files are `java.nio.file.Path` objects**, not `java.io.File`. This affects
  `PlayListExporter.export(Path, PlayList, boolean, ProgressListener)` and `AudioSong.getFile()`.
- **`PlayList` is `com.tagtraum.audiokern.PlayList`**, not `com.tagtraum.beatunes.library.PlayList`.
- **`@Entity` is `jakarta.persistence.Entity`**, not `javax.persistence.Entity`.
- **`SongPropertyAnalyzer`** additionally asks for `getPreferredSampleRate()` and
  `getPreferredChannels()`. `createPipeline` takes the audio format and the duration,
  and `getRequiredClip` takes the duration in seconds.
- **To show changes of a song in the user interface right away**, call
  `SongPropertyChangeListener.installIfAbsent(getApplication(), song)`. The listener cannot
  be created with `new` anymore.
- **The executor for writes to Music/iTunes** is
  `getMediaLibrary().getFileWriteCoordinator().getTunesExecutor()`.

Help and support is available at https://help.beatunes.com/

Enjoy.
