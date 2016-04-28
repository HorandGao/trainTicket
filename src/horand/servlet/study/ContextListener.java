package horand.servlet.study;

import javax.servlet.ServletContextEvent;  
import javax.servlet.ServletContextListener;  
  
public class ContextListener implements ServletContextListener {  
      
    java.util.Timer timer = Time.getSingle();  
      
    public void contextInitialized(ServletContextEvent event) {  
        timer = new java.util.Timer(true);  
        event.getServletContext().log("定时器已启动");  
        //System.out.println("*******************定时器已启动");  
        timer.schedule(new MyTask(event.getServletContext()), 0, 30*1000);  
        //System.out.println("********************已经添加任务调度表");  
        event.getServletContext().log("已经添加任务调度表");  
    }  
    public void contextDestroyed(ServletContextEvent event) {  
        timer.cancel();  
        //System.out.println("*************定时器销毁");  
        event.getServletContext().log("定时器销毁");  
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