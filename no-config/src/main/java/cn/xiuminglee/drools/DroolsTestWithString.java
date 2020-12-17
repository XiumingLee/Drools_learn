package cn.xiuminglee.drools;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.drools.core.impl.InternalKnowledgeBase;
import org.drools.core.impl.KnowledgeBaseFactory;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieRepository;
import org.kie.api.builder.Message;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderError;
import org.kie.internal.builder.KnowledgeBuilderErrors;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;

/**
 * @author Xiuming Lee
 * 使用字符串测试。
 * 经测试：
 *  在7.1x和7.2x版本中可以使用，在7.4x版本中报空指针错误。
 */
public class DroolsTestWithString {

    public static void main(String[] args) {
//        test_1();
        test_2();
    }

    public static void test_1() {
        // 规则字符串。
        String drlStr = "package json.object\n" +
                "import cn.hutool.json.JSONObject\n" +
                "rule \"rule_json_2\"\n" +
                "    when\n" +
                "        $currentUser:JSONObject(getInt(\"age\") > 18)\n" +
                "        $parent:JSONObject(getInt(\"age\") > 60) from $currentUser.getJSONObject(\"parent\")\n" +
                "        $parent_1:JSONObject(getInt(\"age\") > 80) from $parent.getJSONObject(\"parent\")\n" +
                "        $parent_2:JSONObject(getInt(\"age\") > 100) from $parent_1.getJSONObject(\"parent\")\n" +
                "    then\n" +
                "        System.out.println(\"当前用户：【\" + $currentUser.getStr(\"name\") + \"】年龄为：【\" + $currentUser.getInt(\"age\") + \"】\");\n" +
                "        System.out.println(\"当前用户的父亲：【\" + $parent.getStr(\"name\") + \"】年龄为：【\" + $parent.getInt(\"age\") + \"】\");\n" +
                "        System.out.println(\"当前用户的父亲的父亲：【\" + $parent_1.getStr(\"name\") + \"】年龄为：【\" + $parent_1.getInt(\"age\") + \"】\");\n" +
                "        System.out.println(\"当前用户的父亲的父亲的父亲：【\" + $parent_2.getStr(\"name\") + \"】年龄为：【\" + $parent_2.getInt(\"age\") + \"】\");\n" +
                "end\n" +
                "\n" +
                "\n" +
                "rule \"rule_json_3_1\"\n" +
                "    when\n" +
                "        $currentUser:JSONObject(getInt(\"age\") > 40)\n" +
                "        $son_1:JSONObject(getInt(\"age\") < 18) from $currentUser.getJSONArray(\"son\")\n" +
                "    then\n" +
                "        System.out.println(\"当前用户：【\" + $currentUser.getStr(\"name\") + \"】年龄为：【\" + $currentUser.getInt(\"age\") + \"】\");\n" +
                "        System.out.println(\"当前用户的儿子：【\" + $son_1.getStr(\"name\") + \"】年龄为：【\" + $son_1.getInt(\"age\") + \"】\");\n" +
                "\n" +
                "        // 符合条件后，设置其值\n" +
                "        $son_1.set(\"age\",100);\n" +
                "        $son_1.set(\"flag\",true);\n" +
                "end";


        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();

        kb.add(ResourceFactory.newByteArrayResource(drlStr.getBytes()), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kb.getErrors();
        for (KnowledgeBuilderError error : errors) {
            System.err.println(error);
        }

        InternalKnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addPackages(kb.getKnowledgePackages());

        StatelessKieSession statelessKieSession = kBase.newStatelessKieSession();


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
            System.out.println("使用字符串做测试：--------------------------------------------------------------------");
            System.out.println("name:【" + newObj.getStr("name") + "】年龄：【" +  newObj.getInt("age") + "】");
        });
    }



    public static void test_2() {
        // 规则字符串。
        String drlStr = "package json.object\n" +
                "import cn.hutool.json.JSONObject\n" +
                "rule \"rule_json_2\"\n" +
                "    when\n" +
                "        $currentUser:JSONObject(getInt(\"age\") > 18)\n" +
                "        $parent:JSONObject(getInt(\"age\") > 60) from $currentUser.getJSONObject(\"parent\")\n" +
                "        $parent_1:JSONObject(getInt(\"age\") > 80) from $parent.getJSONObject(\"parent\")\n" +
                "        $parent_2:JSONObject(getInt(\"age\") > 100) from $parent_1.getJSONObject(\"parent\")\n" +
                "    then\n" +
                "        System.out.println(\"当前用户：【\" + $currentUser.getStr(\"name\") + \"】年龄为：【\" + $currentUser.getInt(\"age\") + \"】\");\n" +
                "        System.out.println(\"当前用户的父亲：【\" + $parent.getStr(\"name\") + \"】年龄为：【\" + $parent.getInt(\"age\") + \"】\");\n" +
                "        System.out.println(\"当前用户的父亲的父亲：【\" + $parent_1.getStr(\"name\") + \"】年龄为：【\" + $parent_1.getInt(\"age\") + \"】\");\n" +
                "        System.out.println(\"当前用户的父亲的父亲的父亲：【\" + $parent_2.getStr(\"name\") + \"】年龄为：【\" + $parent_2.getInt(\"age\") + \"】\");\n" +
                "end\n" +
                "\n" +
                "\n" +
                "rule \"rule_json_3_1\"\n" +
                "    when\n" +
                "        $currentUser:JSONObject(getInt(\"age\") > 40)\n" +
                "        $son_1:JSONObject(getInt(\"age\") < 18) from $currentUser.getJSONArray(\"son\")\n" +
                "    then\n" +
                "        System.out.println(\"当前用户：【\" + $currentUser.getStr(\"name\") + \"】年龄为：【\" + $currentUser.getInt(\"age\") + \"】\");\n" +
                "        System.out.println(\"当前用户的儿子：【\" + $son_1.getStr(\"name\") + \"】年龄为：【\" + $son_1.getInt(\"age\") + \"】\");\n" +
                "\n" +
                "        // 符合条件后，设置其值\n" +
                "        $son_1.set(\"age\",100);\n" +
                "        $son_1.set(\"flag\",true);\n" +
                "end";


        KnowledgeBuilder kb = KnowledgeBuilderFactory.newKnowledgeBuilder();

        kb.add(ResourceFactory.newByteArrayResource(drlStr.getBytes()), ResourceType.DRL);
        KnowledgeBuilderErrors errors = kb.getErrors();
        for (KnowledgeBuilderError error : errors) {
            System.err.println(error);
        }

        InternalKnowledgeBase kBase = KnowledgeBaseFactory.newKnowledgeBase();
        kBase.addPackages(kb.getKnowledgePackages());
        KieSession kieSession = kBase.newKieSession();


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
            System.out.println("使用字符串测试“-----------------------------------------------------------------------");
            System.out.println("name:【" + newObj.getStr("name") + "】年龄：【" +  newObj.getInt("age") + "】");
            System.out.println("flag的值为：" + newObj.getBool("flag"));
        });

        //关闭会话
        kieSession.dispose();
    }

}
