package cn.xiuminglee.drools.test;

import cn.xiuminglee.drools.entity.Order;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.core.io.impl.UrlResource;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.KieRepository;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Xiuming Lee
 */
public class DroolsTest {

    @Test
    public void test1() {
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieClasspathContainer = kieServices.getKieClasspathContainer();
        //会话对象，用于和规则引擎交互
        KieSession kieSession = kieClasspathContainer.newKieSession();

        //构造订单对象，设置原始价格，由规则引擎根据优惠规则计算优惠后的价格
        Order order = new Order();
        order.setOriginalPrice(210D);

        //将数据提供给规则引擎，规则引擎会根据提供的数据进行规则匹配
        kieSession.insert(order);

        //激活规则引擎，如果规则匹配成功则执行规则
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();

        System.out.println("优惠前原始价格：" + order.getOriginalPrice() +
                "，优惠后价格：" + order.getRealPrice());
    }

    @Test
    public void Remote() throws IOException {
        String url = "http://10.0.5.141:8080/kie-drools-wb/maven2/demo/test_web_drools/1.0/test_web_drools-1.0.jar";
        ReleaseIdImpl releaseId = new ReleaseIdImpl("demo", "test_web_drools", "LATEST");
        KieServices ks = KieServices.Factory.get();
        KieRepository kr = ks.getRepository();
        UrlResource urlResource = (UrlResource) ks.getResources().newUrlResource(url);
        urlResource.setUsername("drools");
        urlResource.setPassword("admin");
        urlResource.setBasicAuthentication("enabled");
        InputStream is = urlResource.getInputStream();
        KieModule kModule = kr.addKieModule(ks.getResources().newInputStreamResource(is));
        KieContainer kContainer = ks.newKieContainer(kModule.getReleaseId());
        KieSession kieSession = kContainer.newKieSession();

//        demo.test_web_drools.Person p=new demo.test_web_drools.Person();
//        p.setAge(50);
//        p.setName("张三");
//        kieSession.insert(p);
        int i = kieSession.fireAllRules();
        System.out.print("共执行了" + i + "条规则");
//        System.out.print("修改后的结果" + p.getName());
    }
}
