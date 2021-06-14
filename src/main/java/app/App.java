package app;

import app.config.AppConfig;
import app.services.Notifier;
import app.util.ExceptionUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        final Notifier notifier = ctx.getBean(Notifier.class);

        final Thread notificationThread = new Thread(() -> {
            try {
                while (true) {
                    notifier.notifyAll("This is a test notification");
                    Thread.sleep(10000);
                }
            } catch (InterruptedException e) {
                ExceptionUtil.toRuntimeException(e);
            }
        });

        notificationThread.start();
    }
}
