package horand.servlet.study;

import javax.servlet.ServletContextEvent;  
import javax.servlet.ServletContextListener;  
  
public class ContextListener implements ServletContextListener {  
      
    java.util.Timer timer = Time.getSingle();  
      
    public void contextInitialized(ServletContextEvent event) {  
        timer = new java.util.Timer(true);  
        event.getServletContext().log("��ʱ��������");  
        //System.out.println("*******************��ʱ��������");  
        timer.schedule(new MyTask(event.getServletContext()), 0, 30*1000);  
        //System.out.println("********************�Ѿ����������ȱ�");  
        event.getServletContext().log("�Ѿ����������ȱ�");  
    }  
    public void contextDestroyed(ServletContextEvent event) {  
        timer.cancel();  
        //System.out.println("*************��ʱ������");  
        event.getServletContext().log("��ʱ������");  
    }  
}  
class Time{  
    private Time() {}  
    private static java.util.Timer timer = null;  
    public static java.util.Timer getSingle() {  
        if(timer == null){  
            timer = new java.util.Timer();  
        }  
        return timer;  
    }  
}  