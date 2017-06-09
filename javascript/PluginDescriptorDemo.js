// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-plugindescriptor.html

var Plugin = Java.type("com.tagtraum.core.app.Plugin");
var PluginDescriptor = Java.type("com.tagtraum.core.app.PluginDescriptor");
var Version = Java.type("com.tagtraum.core.app.Version");

var beatlet = new Plugin() {
    getPluginDescriptor: function() {
        var pd = new PluginDescriptor();
        pd.setId("com.yourdomain.someid.rb");
        pd.setName("Our pretty cool JavaScript beaTlet");
        pd.setDescription("And here we describe what it does ...");
        pd.setLicenseName("LGPL");
        pd.setVersion(new Version("1.0.0"));
        pd.setMinBeaTunesVersion(new Version("3.5.0"));
        pd.setMaxBeaTunesVersion(new Version("4.9.9"));
        // etc.
        return pd;
    }
}

beatlet;
