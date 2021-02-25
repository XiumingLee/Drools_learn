package cn.xiuminglee.drools.test;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.myspace.test_general_data.GeneralDataEntity;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @author Xiuming Lee
 */
public class DroolsJsonTest {


    KieServices kieServices;
    KieContainer kieClasspathContainer;
    //会话对象，用于和规则引擎交互
    KieSession kieSession;


    @Before
    public void buildKie(){
        kieServices = KieServices.Factory.get();
        kieClasspathContainer = kieServices.getKieClasspathContainer();
        //会话对象，用于和规则引擎交互
        kieSession = kieClasspathContainer.newKieSession();
    }

    @Test
    public void jsonTest_1(){
        String json = "{\n" +
                "  \"name\": \"lisi\",\n" +
                "  \"age\": 42,\n" +
                "  \"parent\": {\n" +
                "    \"name\": \"李时珍\",\n" +
                "    \"age\": 98\n" +
                "  }\n" +
                "}";

        JSONObject jsonObject = new JSONObject(json);

        //将数据提供给规则引擎，规则引擎会根据提供的数据进行规则匹配
        kieSession.insert(jsonObject);

        //激活规则引擎，如果规则匹配成功则执行规则
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();


    }

    @Test
    public void jsonTest_2(){
        String json = "{\n" +
                "  \"name\": \"lisi\",\n" +
                "  \"age\": 42,\n" +
                "  \"parent\": {\n" +
                "    \"name\": \"李时珍\",\n" +
                "    \"age\": 68,\n" +
                "    \"parent\": {\n" +
                "      \"name\": \"李天罡\",\n" +
                "      \"age\": 90,\n" +
                "      \"parent\": {\n" +
                "        \"name\": \"李元霸\",\n" +
                "        \"age\": 111\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        JSONObject jsonObject = new JSONObject(json);

        //将数据提供给规则引擎，规则引擎会根据提供的数据进行规则匹配
        kieSession.insert(jsonObject);

        //激活规则引擎，如果规则匹配成功则执行规则 ## 只执行规则名以`rule_json_2`开头的
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_json_2"));
        //关闭会话
        kieSession.dispose();


    }


    @Test
    public void jsonTest_3(){
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

        //激活规则引擎，如果规则匹配成功则执行规则 ## 只执行规则名以`rule_json_2`开头的
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_json_3"));

        // 测试drools中数据写会操作是否成功。
        JSONArray son = jsonObject.getJSONArray("son");
        son.forEach(obj->{
            JSONObject newObj = (JSONObject) obj;
            System.out.println("name:【" + newObj.getStr("name") + "】年龄：【" +  newObj.getInt("age") + "】");
        });

        //关闭会话
        kieSession.dispose();


    }


    @Test
    public void jsonTest_4(){
        String json = "{\n" +
                "  \"name\": \"李四\",\n" +
                "  \"age\": 42,\n" +
                "  \"parent\": {\n" +
                "    \"name\": \"李时珍\",\n" +
                "    \"age\": 68,\n" +
                "    \"parent\": {\n" +
                "      \"name\": \"李天罡\",\n" +
                "      \"age\": 90,\n" +
                "      \"parent\": {\n" +
                "        \"name\": \"李元霸\",\n" +
                "        \"age\": 111\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        JSONObject jsonObject = new JSONObject(json);

        //将数据提供给规则引擎，规则引擎会根据提供的数据进行规则匹配
        kieSession.insert(jsonObject);

        //激活规则引擎，如果规则匹配成功则执行规则 ## 只执行规则名以`rule_json_2`开头的
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("rule_json_4"));
        //关闭会话
        kieSession.dispose();


    }


    /**
     * Drools Web Server 数据测试
     */
    @Test
    public void jsonTest_5(){
        String json = "{\n" +
                "    \"flag\": \"\",\n" +
                "    \"data\": {\n" +
                "        \"a_desc\": \"敏感文件操作日志，主机ip地址：172.168.2.241，责任人：win7，文件名：20180814_31.txt，发现方式：文件刻录\",\n" +
                "        \"logtime_format\": \"2018-3-14 20:22:33\",\n" +
                "        \"op_procn\": \"\",\n" +
                "        \"gid\": \"54a47bda-d271-4884-bb25-062799bb0c94\",\n" +
                "        \"obj_id\": \"\",\n" +
                "        \"logtime_hour\": 20,\n" +
                "        \"sub_id\": \"8CD3A377-CAF0-48D5-9074-60CC44536213\",\n" +
                "        \"src_dev_id\": \"\",\n" +
                "        \"pc_ip\": \"172.168.2.241\",\n" +
                "        \"sec_level\": \"\",\n" +
                "        \"dst_dev_id\": \"\",\n" +
                "        \"a_type\": \"210100011\",\n" +
                "        \"dst_dev_t\": \"\",\n" +
                "        \"a_result\": \"\",\n" +
                "        \"sub_type\": \"\",\n" +
                "        \"src_dev_t\": \"\",\n" +
                "        \"source_app\": \"mg_1\",\n" +
                "        \"obj_type\": \"\",\n" +
                "        \"op_type\": \"Cd\",\n" +
                "        \"logtime\": 1537341336000,\n" +
                "        \"pc_id\": \"8CD3A377-CAF0-48D5-9074-60CC44536213\"\n" +
                "    },\n" +
                "    \"ext\": {}\n" +
                "}";

        JSONObject jsonObject = new JSONObject(json);
        GeneralDataEntity generalDataEntity = jsonObject.toBean(GeneralDataEntity.class);

        //将数据提供给规则引擎，规则引擎会根据提供的数据进行规则匹配
        kieSession.insert(generalDataEntity);

        //激活规则引擎，如果规则匹配成功则执行规则 ## 只执行规则名以`rule_json_2`开头的
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("general_rule_001"));
        //关闭会话
        kieSession.dispose();

        JSONObject jsonObject1 = new JSONObject(generalDataEntity);

        System.out.println(jsonObject1);


    }
}
