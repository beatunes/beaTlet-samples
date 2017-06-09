// Sample beaTlet for beaTunes 5.x
// More info at https://www.beatunes.com/en/beatlet-getting-started.html

// Define the Java type java.lang.System via
// the Nashorn extension "Java.type" ...
var System = Java.type("java.lang.System");
// ...and use it to print to the standard error stream.
System.err.println("Hello World");
