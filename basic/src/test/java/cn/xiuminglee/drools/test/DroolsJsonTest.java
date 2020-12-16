package cn.xiuminglee.drools.test;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
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
}
