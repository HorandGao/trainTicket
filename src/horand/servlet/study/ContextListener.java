package horand.servlet.study;

import javax.servlet.ServletContextEvent;  
import javax.servlet.ServletContextListener;  
  
public class ContextListener implements ServletContextListener {  
      
    java.util.Timer timer = Time.getSingle();  
      
    public void contextInitialized(ServletContextEvent event) {  
        timer = new java.util.Timer(true);  
        timer.schedule(new MyTask(event.getServletContext()), 0, 30*1000);  
    }  
    public void contextDestroyed(ServletContextEvent event) {  
        timer.cancel();  
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