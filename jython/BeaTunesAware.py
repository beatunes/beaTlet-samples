# Sample beaTlet for beaTunes 4.x
# More info at https://www.beatunes.com/en/beatlet-getting-started.html
import com.tagtraum.core.app
import sys

class BeaTunesAware(com.tagtraum.core.app.ApplicationComponent):

    def __init__(self):
        self.__application = None

    def setApplication(self, application):
        self.__application = application

    def getApplication(self):
        return self.__application

    def init(self):
        print >> sys.stderr, "init"

    def shutdown(self):
        print >> sys.stderr, "shutdown"

    def getId(self):
        return "Jython.beaTunes.aware"

