// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html
import java.awt.Toolkit
import java.awt.event.*
import java.text.NumberFormat
import javax.swing.*
import javax.swing.table.*
import javax.swing.event.TableModelListener
import com.tagtraum.core.app.*
import com.tagtraum.audiokern.*
import com.tagtraum.audiokern.bpm.*
import com.tagtraum.audiokern.key.*
import com.tagtraum.beatunes.songtable.renderer.*
import com.tagtraum.beatunes.MessageDialog
import com.tagtraum.beatunes.action.*
import com.tagtraum.beatunes.BeaTunes

class KeyAtBPM extends BaseAction {

    def String getId() {
        "Groovy.KeyAtBPM"
    }

    def void init() {
        putValue(Action.NAME, "Show key at BPM")
        // tooltip
        putValue(Action.SHORT_DESCRIPTION, "Shows a table of keys at different BPMs.")
        // register keyboard shortcut Command-K/CTRL-K
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_K,
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()))
        // Only enable this action, when exactly one song is selected
        bindListeners(BaseAction.SelectionCount.ONE,
            BaseAction.EnabledView.SONG_TABLE,
            BaseAction.EnabledView.MATCH_TABLE,
            BaseAction.EnabledView.DETAIL_VIEW,
            BaseAction.EnabledView.SIMPLE_SONG_TABLE)
    }

    def ActionLocation[] getActionLocations() {
        // install this action in the song context menu (select song, right-click)
        [new AbsoluteActionLocation(BeaTunesUIRegion.SONG_CONTEXT_MENU, AbsoluteActionLocation.LAST),
        // install in Tools menu, to make sure the keyboard shortcut actually works.
        new AbsoluteActionLocation(BeaTunesUIRegion.TOOL_MENU, AbsoluteActionLocation.LAST)]
    }

    def void actionPerformed(ActionEvent actionEvent) {
        AudioSong song = getSelectedSong();
        if (song.getTempo() == null) {
            new MessageDialog(
                getApplication().getMainWindow(),
                "The BPM for the selected song is unknown.",
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.DEFAULT_OPTION
            ).showDialog()
        } else if (song.getKey() == null) {
            new MessageDialog(
                getApplication().getMainWindow(),
                "The key for the selected song is unknown.",
                JOptionPane.ERROR_MESSAGE,
                JOptionPane.DEFAULT_OPTION
            ).showDialog()
        } else {
            JTable table = new JTable(new BPMShiftModel(getApplication(), song));
            table.setDefaultRenderer(Tempo.class, new TempoRenderer())
            table.setDefaultRenderer(Key.class, new KeyRenderer(getApplication()))
            table.setPreferredScrollableViewportSize(table.getPreferredSize())
            MessageDialog dialog = new MessageDialog(
                getApplication().getMainWindow(),
                "How the key would change when played<br>at a given BPM without keylock:",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                new JScrollPane(table)
            )
            String artist = song.getArtist() != null && !song.getArtist().isEmpty()
                ? song.getArtist()
                : (song.getAlbumArtist() != null && !song.getAlbumArtist().isEmpty()
                    ? song.getAlbumArtist()
                    : "<" + getApplication().localize("unknown") + ">")
            NumberFormat format = NumberFormat.getInstance()
            format.setMaximumFractionDigits(1)
            format.setMinimumFractionDigits(1)
            dialog.setTitle("\"" + song.getName()
                + "\" - " + artist
                + " - " + format.format(song.getTempo().getTempo(TempoUnit.BPM))
                + " " + getApplication().localize("BPM")
                + " " + getApplication().getGeneralPreferences().getKeyTextRenderer().toKeyString(song.getKey()))
            dialog.showDialog()
        }
    }

    /**
     * Renderer for the Tempo class so that we can do some formatting.
     */
    class TempoRenderer extends DefaultTableCellRenderer {

        NumberFormat format

        public TempoRenderer() {
            format = NumberFormat.getInstance()
            format.setMaximumFractionDigits(1)
            format.setMinimumFractionDigits(1)
            setHorizontalAlignment(SwingConstants.RIGHT)
        }

        def void setValue(Object obj) {
            Tempo tempo = (Tempo)obj
            setText(format.format(tempo.getTempo(TempoUnit.BPM)))
        }
    }

    /**
     * Renderer for the Key class so that we can do some formatting,
     * in particular honor the KeyTextRenderer configured in the preferences.
     */
    class KeyRenderer extends DefaultTableCellRenderer {

        BeaTunes application

        public KeyRenderer(BeaTunes application) {
            this.application = application
        }

        def void setValue(Object obj) {
            Key key = (Key)obj
            setIcon(KeyTableCellRenderer.getIcon(key))
            setText(application.getGeneralPreferences().getKeyTextRenderer().toKeyString(key))
        }
    }

    /**
     * Model for the table that is going to be displayed.
     */
    class BPMShiftModel implements TableModel {

        BeaTunes application
        Tempo baseTempo
        Key baseKey

        public BPMShiftModel(BeaTunes application, AudioSong song) {
            this.application = application
            this.baseTempo = song.getTempo()
            this.baseKey = song.getKey()
        }

        def int getRowCount() {
            11
        }

        def int getColumnCount() {
            2
        }

        def String getColumnName(int col) {
            switch (col) {
                case 0: return application.localize("BPM")
                case 1: return application.localize("Key")
            }
            null
        }

        def Class<?> getColumnClass(int col) {
            switch (col) {
                case 0: return Tempo.class
                case 1: return Key.class
            }
            null
        }

        def boolean isCellEditable(int row, int col) {
            false
        }

        def Object getValueAt(int row, int col) {
            int halfToneShifts = row-5
            if (col == 0) {
                return new Tempo((float)(baseTempo.getTempo(TempoUnit.BPM)
                    * Math.pow(2, halfToneShifts / 12f)), TempoUnit.BPM)
            }
            else if (col == 1) {
                Tone newTonic = Tone.values()[(baseKey.getTonic().ordinal() + halfToneShifts) % 12];
                if (baseKey.isMajor()) return newTonic.getMajorKey()
                else return newTonic.getMinorKey()
            }
        }

        def void setValueAt(Object object, int row, int col) {
            // not editable
        }

        def void addTableModelListener(TableModelListener l) {
            // model does not change
        }

        def void removeTableModelListener(TableModelListener l) {
            // model does not change
        }
    }
}
