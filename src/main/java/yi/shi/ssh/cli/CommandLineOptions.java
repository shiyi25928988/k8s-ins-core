package yi.shi.ssh.cli;

/**
 * @author yshi
 *
 */
public class CommandLineOptions {
	
	private static final org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
	private static final boolean hasArg = true;
	private static final boolean notHasArgs = false;
	
	static {
		options.addOption("h", "help", notHasArgs, "Helper.");
		options.addOption("i", "install", hasArg, "with the config json file.");
		options.addOption("t", "template", notHasArgs, "generate k8s config template file.");
		//..... add options here
	}
	
	/**
	 * @return
	 */
	public static org.apache.commons.cli.Options getOptions(){
		return CommandLineOptions.options;
	}
	
}
