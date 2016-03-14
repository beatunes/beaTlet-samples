# Sample beaTlet for beaTunes 4.x
# More info at https://www.beatunes.com/en/beatlet-getting-started.html
require 'java'

java_import javax.swing.Action
java_import javax.swing.JOptionPane

java_import com.tagtraum.core.app.ActionLocation
java_import com.tagtraum.core.app.AbsoluteActionLocation

java_import com.tagtraum.beatunes.action.BeaTunesUIRegion
java_import com.tagtraum.beatunes.action.BaseAction
java_import com.tagtraum.beatunes.MessageDialog

# An action that does nothing, but to show a simple message box.
# The corresponding menu item can be found in the 'Tools' menu.

# All actions in beaTunes subclass com.tagtraum.beatunes.action.BaseAction
class DialogAction < BaseAction

    # Unique id
    def getId
        "JRuby.DialogAction"
    end

    # Is called by beaTunes as part of the lifecycle after instantiation.
    # At this point all other plugins are instantiated and registered.
    # We use this to set the menu item's (i.e. the action's) name.
    def init
        putValue(Action::NAME, "DialogAction")
    end

    # Define, where in the UI the Action should appear.
    # You can define multiple locations in an array. Here, we
    # only request to be the last item in the Tool menu.
    # If other Actions do the same thing, the last one wins.
    def getActionLocations
        # "to_java()" converts the Ruby array into a Java array of the given type
        [AbsoluteActionLocation.new(BeaTunesUIRegion::TOOL_MENU,
            AbsoluteActionLocation::LAST)].to_java(ActionLocation)
    end

    # React to a click on the menu item.
    # We show a simple dialog with the main window as the dialog's parent.
    def actionPerformed(actionEvent)
        # getApplication is defined in the super class, which is an
        # ApplicationComponent. The application in turn has a main window (a JFrame subclass).
        MessageDialog.new(
            getApplication.getMainWindow,       # parent window
            "DialogAction",                     # message
            JOptionPane::INFORMATION_MESSAGE,   # type of message dialog
            JOptionPane::DEFAULT_OPTION         # what buttons to show
        ).showDialog
    end

end

