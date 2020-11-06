package cn.itrip.text;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class freeMarkertest {
    public static void main(String[] args) {
        List list = new ArrayList();
        list.add("lhy");
        list.add("zgd");
        list.add("ljh");
        list.add("zxl");
        list.add("yzx");
        Map map = new HashMap();
        map.put("lhy", list);
        try {
            Configuration configuration = new Configuration();
            configuration.setDefaultEncoding("UTF-8");
            configuration.setDirectoryForTemplateLoading(new File("E:\\itripbackend\\itripbeans\\src\\main\\resources"));
            Template template = configuration.getTemplate("demo.flt");
            template.process(map, new FileWriter("F:\\a.html"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}
