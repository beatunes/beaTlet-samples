// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-plugindescriptor.html
import com.tagtraum.core.app.*

class PluginDescriptorDemo implements Plugin {

    def PluginDescriptor getPluginDescriptor() {
        PluginDescriptor pd = new PluginDescriptor()
        pd.setId("com.yourdomain.someid.groovy")
        pd.setName("Our pretty cool Groovy beaTlet")
        pd.setDescription("And here we describe what it does ...")
        pd.setLicenseName("LGPL")
        pd.setVersion(new Version("1.0.0"))
        pd.setMinBeaTunesVersion(new Version("3.5.0"))
        pd.setMaxBeaTunesVersion(new Version("4.9.9"))
        // etc.
        return pd
    }
}

