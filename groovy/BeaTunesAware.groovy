// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html
import com.tagtraum.core.app.*

class BeaTunesAware implements ApplicationComponent {

    ApplicationComponent application

    def void setApplication(application) { this.application = application }
    def ApplicationComponent getApplication() { application }

    def void init() { System.err.println "init" }
    def void shutdown() { System.err.println "shutdown" }
    def String getId() { "Groovy.beaTunes.aware" }
}

