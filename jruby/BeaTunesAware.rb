# Sample beaTlet for beaTunes 5.x
# More info at https://www.beatunes.com/en/beatlet-getting-started.html
class BeaTunesAware

    include Java::com.tagtraum.core.app.ApplicationComponent

    def setApplication(application)
        @application = application
    end

    def ApplicationComponent getApplication
        @application
    end

    def init
        $stderr.puts "init"
    end

    def shutdown
        $stderr.puts "shutdown"
    end

    def getId
        "JRuby.beaTunes.aware"
    end

end

