// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-songanalysistask.html
import java.awt.Color;
import com.tagtraum.audiokern.*
import com.tagtraum.beatunes.analysis.*
import org.slf4j.*

import javax.persistence.Entity

/**
 * Copies color information to the comments field.
 */
// =============================================================================== //
// It is *essential* for this class to be annotated as Entity.                     //
// Otherwise it will not be saved in the analysis queue and cannot be processed.   //
// Annotations are only available in Java and Groovy, but not in JRuby or Jython.  //
// =============================================================================== //
@Entity
class ColorToComment extends SongAnalysisTask {

    static log = LoggerFactory.getLogger("ColorToComment.groovy")
    static String COLOR_START_MARKER = "Color:"
    static String COLOR_END_MARKER = ";"


    def ColorToComment() {
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
        "<h1>Color To Comment</h1><p>Copies the color (if it exists) to the comment field using hex HSB notation.</p>"
    }

    /**
     * This will be the displayed name of the analysis task.
     *
     * @return HTML string
     */
    def String getName() {
        "<html>Copy color<br>to comment field</html>"
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
        Color commentsColor = getColor(comments)
        // do a roundtrip to 0-16 HSB notation
        Color color = song.getColor() == null ? null : parseColor(toString(song.getColor()))
        boolean skip = commentsColor != null && commentsColor.equals(color)
        if (log.isDebugEnabled()) log.debug("Skipping " + song + " ...")
        skip
    }

    /**
     * Creates a new comment string.
     *
     * @param song song
     * @return new comment (with color, if the song has a color)
     */
    private def String getNewComments(AudioSong song) {
        String comments = song.getComments() == null ? "" : song.getComments() + " "
        if (hasCommentsColor(comments)) {
            comments = removeCommentsColor(comments)
        }
        if (song.getColor() != null) {
            comments = addCommentsColor(comments, song.getColor())
        }
        comments
    }

    /**
     * Indicates whether this comment contains a color.
     *
     * @param comments comment
     * @return true, if the comment contains a color
     */
    private def boolean hasCommentsColor(String comments) {
        getColor(comments) != null
    }

    /**
     * Extracts a color out of a comment string.
     *
     * @param comments comment
     * @return color or null, if not found
     */
    private def Color getColor(String comments) {
        Color colorObject
        if (comments == null || comments.length() < COLOR_START_MARKER.length() + COLOR_END_MARKER.length()) {
            colorObject = null
        } else {
            int start = comments.indexOf(COLOR_START_MARKER)
            if (start == -1) {
                colorObject = null
            } else {
                int end = comments.indexOf(COLOR_END_MARKER, start)
                if (end == -1) {
                    colorObject = null
                } else {
                    String color = comments.substring(start + COLOR_START_MARKER.length(), end)
                    colorObject = parseColor(color)
                }
            }
        }
        colorObject
    }

    /**
     * Removes a color from a comment string.
     *
     * @param comments comment
     * @return comment without the color
     */
    private def String removeCommentsColor(String comments) {
        int start = comments.indexOf(COLOR_START_MARKER)
        int end = comments.indexOf(COLOR_END_MARKER, start)
        if (comments.length() > end) return comments.substring(0, start) + comments.substring(end+1)
        comments.substring(0, start).trim()
    }

    /**
     * Adds a color to a comment.
     *
     * @param comments comment
     * @param color color
     * @return new comment with color
     */
    private def String addCommentsColor(final String comments, final Color color) {
        comments + COLOR_START_MARKER + toString(color) + COLOR_END_MARKER
    }

    private def Color parseColor(final String colorString) {
        final float h = Integer.valueOf(colorString.substring(0, 1), 16) / 15f
        final float s = Integer.valueOf(colorString.substring(1, 2), 16) / 15f
        final float b = Integer.valueOf(colorString.substring(2, 3), 16) / 15f
        return new Color(Color.HSBtoRGB(h, s, b))
    }

    private def String toString(final Color color) {
        final float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        int h = (int)(hsb[0] * 15 + 0.5)
        int s = (int)(hsb[1] * 15 + 0.5)
        int b = (int)(hsb[2] * 15 + 0.5)

        String hex = Integer.toHexString(h) + Integer.toHexString(s) + Integer.toHexString(b)
        hex
    }

}
