package yi.shi.ssh.k8s.file;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ExportConfigFile {

    private static String exportPath = System.getProperty("user.dir");

    public static void exportTemplateFile() throws IOException {
        File file = new File(exportPath.concat(File.separator).concat("k8s-cluster-config.json"));
        ConfigInfo configInfo = new ConfigInfo();
        List<NodeInfo> list = new ArrayList<>();
        list.add(new NodeInfo());
        configInfo.setList(list);
        FileUtils.writeStringToFile(file, JSON.toJSONString(configInfo, JSONWriter.Feature.PrettyFormat), Charset.forName("UTF-8"));
        exportYamlTemplateFile("/deployment/deployment.yaml");
        exportYamlTemplateFile("/service/service.yaml");
        exportYamlTemplateFile("/ingress/ingress.yaml");
    }

    public static void exportYamlTemplateFile(String path) throws IOException {
        File file = new File(exportPath.concat(File.separator).concat(path));
        String content = IOUtils.toString(ExportConfigFile.class.getResourceAsStream(path), Charset.forName("UTF-8"));
        FileUtils.writeStringToFile(file, content, Charset.forName("UTF-8"));
    }
}
