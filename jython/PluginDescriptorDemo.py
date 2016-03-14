# Sample beaTlet for beaTunes 4.x
# More info at https://www.beatunes.com/en/beatlet-plugindescriptor.html
from com.tagtraum.core.app import Plugin, PluginDescriptor, Version

class PluginDescriptorDemo(Plugin):

    def getPluginDescriptor(self):
        pd = PluginDescriptor()
        pd.setId("com.yourdomain.someid.py")
        pd.setName("Our pretty cool Jython beaTlet")
        pd.setDescription("And here we describe what it does ...")
        pd.setLicenseName("LGPL")
        pd.setVersion(Version("1.0.0"))
        pd.setMinBeaTunesVersion(Version("3.5.0"))
        pd.setMaxBeaTunesVersion(Version("4.9.9"))
        # etc.
        return pd

