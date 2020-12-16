package cn.xiuminglee.drools;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;

/**
 * @author Xiuming Lee
 * 使用外部的规则文件进行判断。
 * 经测试：
 *  test_1和test_2创建的两种session都可以完成规则的匹配。
 */
public class DroolsTestWithOutFile {

    public static void main(String[] args) {
//        test_1();
        test_2();
    }

    public static void test_1() {
        // .drl文件地址
        String drlFileName = "E:\\code\\MyGitHub\\Drools\\no-config\\src\\main\\resources\\jsonObject.drl";


        KieServices kieServices = KieServices.Factory.get();

        KieResources kieResources = kieServices.getResources();
        Resource resource = kieResources.newFileSystemResource(drlFileName);


        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(resource);

        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();

        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }

        KieRepository kieRepository = kieServices.getRepository();

        KieContainer kContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());

        // 这里有些不一样。这是一个无状态的session
        StatelessKieSession statelessKieSession = kContainer.newStatelessKieSession();


        /**---------------------------------------以上创建完毕，下面进行验证-------------------------------------------------------*/

        String json = "{\n" +
                "  \"name\": \"lisi\",\n" +
                "  \"age\": 42,\n" +
                "  \"son\": [\n" +
                "    {\n" +
                "      \"name\": \"lida\",\n" +
                "      \"age\": 22\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"lier\",\n" +
                "      \"age\": 16\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"lisan\",\n" +
                "      \"age\": 10\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        JSONObject jsonObject = new JSONObject(json);

        statelessKieSession.execute(jsonObject);


        // 测试drools中数据写会操作是否成功。
        JSONArray son = jsonObject.getJSONArray("son");
        son.forEach(obj->{
            JSONObject newObj = (JSONObject) obj;
            System.out.println("name:【" + newObj.getStr("name") + "】年龄：【" +  newObj.getInt("age") + "】");
        });
    }



    public static void test_2() {
        // .drl文件地址
        String drlFileName = "E:\\code\\MyGitHub\\Drools\\no-config\\src\\main\\resources\\jsonObject.drl";


        KieServices kieServices = KieServices.Factory.get();

        KieResources kieResources = kieServices.getResources();
        Resource resource = kieResources.newFileSystemResource(drlFileName);


        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(resource);

        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();

        if (kb.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kb.getResults().toString());
        }

        KieRepository kieRepository = kieServices.getRepository();

        KieContainer kContainer = kieServices.newKieContainer(kieRepository.getDefaultReleaseId());

        // 这里有些不一样
        KieSession kieSession = kContainer.newKieSession();


        /**---------------------------------------以上创建完毕，下面进行验证-------------------------------------------------------*/

        String json = "{\n" +
                "  \"name\": \"lisi\",\n" +
                "  \"age\": 42,\n" +
                "  \"son\": [\n" +
                "    {\n" +
                "      \"name\": \"lida\",\n" +
                "      \"age\": 22\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"lier\",\n" +
                "      \"age\": 16\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"lisan\",\n" +
                "      \"age\": 10\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        JSONObject jsonObject = new JSONObject(json);

        //将数据提供给规则引擎，规则引擎会根据提供的数据进行规则匹配
        kieSession.insert(jsonObject);

        //激活规则引擎，如果规则匹配成功则执行规则 ## 只执行规则名以`rule_json_3`开头的
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_json_3"));


        // 测试drools中数据写会操作是否成功。
        JSONArray son = jsonObject.getJSONArray("son");
        son.forEach(obj->{
            JSONObject newObj = (JSONObject) obj;
            System.out.println("name:【" + newObj.getStr("name") + "】年龄：【" +  newObj.getInt("age") + "】");
            System.out.println("flag的值为：" + newObj.getBool("flag"));
        });

        //关闭会话
        kieSession.dispose();
    }

}
