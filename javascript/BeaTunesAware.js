// Sample beaTlet for beaTunes 4.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html

/*
 * These type vars basically act as imports for Java classes.
 */
var System = Java.type("java.lang.System");
var ApplicationComponent = Java.type("com.tagtraum.core.app.ApplicationComponent");

// ApplicationComponent is an interface, which we can
// implement via "new" (much like an anonymous inner Java class).
// The resulting instance is stored in the "beatlet" variable.
var beatlet = new ApplicationComponent() {

    application: null,

    /*
     * The application object is injected by beaTunes
     * right after this script has been eval'd.
     */
    setApplication: function(application) {
        this.application = application;
    },

    /*
     * beaTunes application object.
     */
    getApplication: function() {
        return this.application;
    },

    /*
     * Is called by beaTunes as part of the lifecycle after instantiation.
     * At this point all other plugins are instantiated and registered.
     */
    init: function() {
        System.err.println("init");
    },

    /*
     * Is called by beaTunes as part of the lifecycle during shutdown.
     */
    shutdown: function() {
        System.err.println("shutdown");
    },

    /*
     * Unique id.
     */
    getId: function() {
        return "Javascript.beatunes.aware";
    }
}

// Put "beatlet" into the last line, so that it is returned
// to beaTunes when this script is eval'd.
beatlet;
