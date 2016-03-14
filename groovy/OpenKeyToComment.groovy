// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-songanalysistask.html
import com.tagtraum.audiokern.*
import com.tagtraum.audiokern.key.*
import com.tagtraum.beatunes.analysis.*
import org.slf4j.*

import javax.persistence.Entity

/**
 * Copies tonal open key info to the comments field.
 * THIS TASK REQUIRES beaTunes 3.5.17 OR LATER!!!
 */
// =============================================================================== //
// It is *essential* for this class to be annotated as Entity.                     //
// Otherwise it will not be saved in the analysis queue and cannot be processed.   //
// Annotations are only available in Java and Groovy, but not in JRuby or Jython.  //
// =============================================================================== //
@Entity
class OpenKeyToComment extends SongAnalysisTask {

    static log = LoggerFactory.getLogger("OpenKeyToComment.groovy")
    static String KEY_START_MARKER = "KEY:"
    static String KEY_END_MARKER = ";"


    def OpenKeyToComment() {
        // this task does not take long - therefore we ignore it in per task progress bars
        setProgressRelevant(false)
    }

    /**
     * Returns a verbose description of the task in HTML format. This is shown in the
     * Analysis Options dialog (left pane).
     *
     * @return verbose HTML description.
     */
    def String getDescription() {
        "<h1>OpenKey To Comment</h1><p>Copies the tonal key (if it exists) to the comment field.</p>"
    }

    /**
     * This will be the displayed name of the analysis task.
     *
     * @return HTML string
     */
    def String getName() {
        "<html>Copy open key<br>to comment field</html>"
    }

    /**
     * This is where the actual work occurs. This method is called by beaTunes when
     * this task is processed in the analysis/task queue.
     *
     * @throws AnalysisException if something goes wrong.
     */
    def void runBefore(Task task) throws AnalysisException {
        // check whether we can skip this step altogether
        if (skip()) {
            if (log.isDebugEnabled()) log.debug("Skipped " + getSong())
            return
        }
        // get the song object
        AudioSong song = getSong()
        // get the new comment
        String newComments = getNewComments(song)
        if (log.isDebugEnabled()) log.debug("Setting new comments to: " + newComments)
        // store new comment - the new value is automatically persisted and the UI is updated.
        song.setComments(newComments)
    }

    /**
     * Indicates, whether this task can be skipped.
     *
     * @return true or false
     */
    def boolean skip() {
        AudioSong song = getSong()
        String comments = song.getComments()
        Key commentsKey = getKey(comments)
        Key key = song.getKey()
        boolean skip = commentsKey != null && commentsKey.equals(key)
        if (log.isDebugEnabled()) log.debug("Skipping " + song + " ...")
        skip
    }

    /**
     * Creates a new comment string.
     *
     * @param song song
     * @return new comment (with key, if the song has a key)
     */
    private def String getNewComments(AudioSong song) {
        String comments = song.getComments() == null ? "" : song.getComments()
        if (hasCommentsKey(comments)) {
            comments = removeCommentsKey(comments)
        }
        if (song.getKey() != null) {
            comments = addCommentsKey(comments, song.getKey())
        }
        comments
    }

    /**
     * Indicates whether this comment contains a key.
     *
     * @param comments comment
     * @return true, if the comment contains a key
     */
    private def boolean hasCommentsKey(String comments) {
        getKey(comments) != null
    }

    /**
     * Extracts a key out of a comment string.
     *
     * @param comments comment
     * @return key or null, if not found
     */
    private def Key getKey(String comments) {
        Key keyObject
        if (comments == null || comments.length() < KEY_START_MARKER.length() + KEY_END_MARKER.length()) {
            keyObject = null
        } else {
            int start = comments.indexOf(KEY_START_MARKER)
            if (start == -1) {
                keyObject = null
            } else {
                int end = comments.indexOf(KEY_END_MARKER, start)
                if (end == -1) {
                    keyObject = null
                } else {
                    String key = comments.substring(start + KEY_START_MARKER.length(), end)
                    // parseKeyCode() parses OpenKey notation keys
                    keyObject = KeyFactory.parseKeyCode(key)
                }
            }
        }
        keyObject
    }

    /**
     * Removes a key from a comment string.
     *
     * @param comments comment
     * @return comment without the key
     */
    private def String removeCommentsKey(String comments) {
        int start = comments.indexOf(KEY_START_MARKER)
        int end = comments.indexOf(KEY_END_MARKER, start)
        if (comments.length() > end) return comments.substring(0, start) + comments.substring(end+1)
        comments.substring(0, start)
    }

    /**
     * Adds a key to a comment.
     *
     * @param comments comment
     * @param key key
     * @return new comment with key
     */
    private def String addCommentsKey(final String comments, final Key key) {
        comments + KEY_START_MARKER + key.getOpenKeyCode() + KEY_END_MARKER
    }

}

