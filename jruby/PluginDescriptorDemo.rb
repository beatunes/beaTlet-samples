# Sample beaTlet for beaTunes 5.x
# More info at https://www.beatunes.com/en/beatlet-plugindescriptor.html
require 'java'

java_import com.tagtraum.core.app.Plugin
java_import com.tagtraum.core.app.PluginDescriptor
java_import com.tagtraum.core.app.Version

class PluginDescriptorDemo

    include Java::com.tagtraum.core.app.Plugin

    def getPluginDescriptor()
        pd = PluginDescriptor.new()
        pd.setId("com.yourdomain.someid.rb")
        pd.setName("Our pretty cool JRuby beaTlet")
        pd.setDescription("And here we describe what it does ...")
        pd.setLicenseName("LGPL")
        pd.setVersion(Version.new("1.0.0"))
        pd.setMinBeaTunesVersion(Version.new("3.5.0"))
        pd.setMaxBeaTunesVersion(Version.new("4.9.9"))
        # etc.
        return pd
    end
end

