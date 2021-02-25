package cn.xiuminglee.drools.entity;

import cn.hutool.json.JSONObject;
import org.drools.core.io.impl.UrlResource;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author lxm
 */
public class RemoteTest {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String url = "http://172.168.3.213:8888/kie-drools-wb/maven2/com/myspace/test_general_data/1.0.0/test_general_data-1.0.0.jar";
        KieServices ks = KieServices.Factory.get();
        KieRepository kr = ks.getRepository();
        UrlResource urlResource = (UrlResource)ks.getResources().newUrlResource(url);
        urlResource.setUsername("admin");
        urlResource.setPassword("admin");
        urlResource.setBasicAuthentication("enabled");
        InputStream inputStream = urlResource.getInputStream();
        KieModule kieModule = kr.addKieModule(ks.getResources().newInputStreamResource(inputStream));
        KieContainer kieContainer = ks.newKieContainer(kieModule.getReleaseId());
        KieSession kieSession = kieContainer.newKieSession();

        // 通过反射的方式，加载远程的数据格式。
        ClassLoader classLoader = kieContainer.getClassLoader();
        Class<?> aClass = classLoader.loadClass("com.myspace.test_general_data.GeneralDataEntity");



        String json = "{\n" +
                "    \"flag\": \"\",\n" +
                "    \"data\": {\n" +
                "        \"gid\": \"54a47bda-d271-4884-bb25-062799bb0c94\",\n" +
                "        \"logtime\": 1537341336000,\n" +
                "        \"logtime_format\": \"2018-3-14 20:22:33\",\n" +
                "        \"logtime_hour\": 20,\n" +
                "        \"a_result\": \"\",\n" +
                "        \"a_desc\": \"敏感文件操作日志，主机ip地址：172.168.2.241，责任人：win7，文件名：20180814_31.txt，发现方式：文件刻录\",\n" +
                "        \"source_app\": \"mg_1\",\n" +
                "        \"sub_type\": \"\",\n" +
                "        \"sub_id\": \"8CD3A377-CAF0-48D5-9074-60CC44536213\",\n" +
                "        \"obj_type\": \"\",\n" +
                "        \"obj_id\": \"\",\n" +
                "        \"pc_id\": \"8CD3A377-CAF0-48D5-9074-60CC44536213\",\n" +
                "        \"pc_ip\": \"172.168.2.241\",\n" +
                "        \"op_procn\": \"\",\n" +
                "        \"op_type\": \"Cd\",\n" +
                "        \"sec_level\": \"\",\n" +
                "        \"srcfn\": \"20180814_31.txt\",\n" +
                "        \"dstfn\": \"\",\n" +
                "        \"srcfid\": \"\",\n" +
                "        \"dstfid\": \"\",\n" +
                "        \"src_dev_t\": \"\",\n" +
                "        \"src_dev_id\": \"\",\n" +
                "        \"dst_dev_t\": \"\",\n" +
                "        \"dst_dev_id\": \"\",\n" +
                "        \"a_type\": \"210100011\"\n" +
                "    },\n" +
                "    \"ext\": {}\n" +
                "}";



        JSONObject jsonObject = new JSONObject(json);
        Object generalData = jsonObject.toBean(aClass);


        kieSession.insert(generalData);
        int i = kieSession.fireAllRules();

        JSONObject jsonGeneralData = new JSONObject(generalData);
        System.out.println("共执行了" + i + "条规则");
        System.out.println("修改后的结果" + jsonGeneralData.toString());
    }
}
