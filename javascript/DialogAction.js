// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html

/*
 * These type vars basically act as imports for Java classes.
 */
var Action = Java.type("javax.swing.Action");
var JOptionPane = Java.type("javax.swing.JOptionPane");
var ActionLocations = Java.type("com.tagtraum.core.app.ActionLocation[]");
var AbsoluteActionLocation = Java.type("com.tagtraum.core.app.AbsoluteActionLocation");
var BeaTunesUIRegion = Java.type("com.tagtraum.beatunes.action.BeaTunesUIRegion");
var MessageDialog = Java.type("com.tagtraum.beatunes.MessageDialog");
var BaseAction = Java.type("com.tagtraum.beatunes.action.BaseAction");

// BaseAction is an abstract class.
// This allows us to subclass it with "new".
// The resulting subclass instance is stored in the "beatlet" variable.
var beatlet = new BaseAction() {

    /*
     * Unique id.
     */
    getId: function() {
        return "Javascript.DialogAction";
    },

    /*
     * Is called by beaTunes as part of the lifecycle after instantiation.
     * At this point all other plugins are instantiated and registered.
     * We use this to set the menu item's (i.e. the action's) name.
     */
    init: function() {
        beatletSuper.putValue(Action.NAME, "DialogAction");
    },

    /*
     * Define, where in the UI the Action should appear.
     * You can define multiple locations in an array. Here, we
     * only request to be the last item in the Tool menu.
     * If other Actions do the same thing, the last one wins.
     * Note, that we use the Nashorn extension "Java.to", to
     * create a Java array.
     */
    getActionLocations: function() {
        return Java.to([new AbsoluteActionLocation(BeaTunesUIRegion.TOOL_MENU,
            AbsoluteActionLocation.LAST)], ActionLocations);
    },

    /*
     * React to a click on the menu item.
     * We show a simple dialog with the main window as the dialog's parent.
     */
    actionPerformed: function(actionEvent) {
        // getApplication is defined in the super class, which is an
        // ApplicationComponent. The application in turn has a main
        // window (a JFrame subclass).
        new MessageDialog(
            beatletSuper.getApplication().getMainWindow(), // parent window
            "DialogAction",                                 // message
            JOptionPane.INFORMATION_MESSAGE,                // type of message dialog
            JOptionPane.DEFAULT_OPTION                      // what buttons to show
        ).showDialog();
    }
}

// Find super class of beatlet, so that we can call methods on it
// in the "actionPerformed" function.
var beatletSuper = Java.super(beatlet);

// Put "beatlet" into the last line, so that it is returned to beaTunes
// when this script is eval'd.
beatlet;
