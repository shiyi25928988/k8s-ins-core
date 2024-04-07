package yi.shi.ssh;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import yi.shi.ssh.cli.CommandLineOptions;
import yi.shi.ssh.k8s.file.ConfigFileProcessor;
import yi.shi.ssh.k8s.file.ConfigInfo;
import yi.shi.ssh.k8s.file.ExportConfigFile;
import yi.shi.ssh.shell.SshOutputBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class Main {

    public static void main(String...args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter helper = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(CommandLineOptions.getOptions(), args);
            if(cmd.hasOption("h") || cmd.getOptions().length == 0){
                helper.printHelp("help", CommandLineOptions.getOptions());
            }
            if(cmd.hasOption("i")){
                String configFile = cmd.getOptionValue("install");
                File file  = new File(configFile);
                ConfigInfo configInfo = ConfigFileProcessor.loadConfigFile(file);
                if(cmd.hasOption("v")){
                    String version = cmd.getOptionValue("version");
                    ConfigFileProcessor.processConfig(configInfo, version);
                }
                if(!cmd.hasOption("v")){
                    ConfigFileProcessor.processConfig(configInfo, "1.28.2");
                }
            }
            if(cmd.hasOption("t")){
                ExportConfigFile.exportTemplateFile();
            }
        } catch (ParseException e) {
            helper.printHelp("help", CommandLineOptions.getOptions());
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
