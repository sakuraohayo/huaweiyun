package javaweb.remember;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;

@SpringBootApplication
public class RememberApplication {

    public static void main(String[] args) {
        // 下边一行是直接启动
        // SpringApplication.run(RememberApplication.class, args);
        // 下边是启动时同时将进程号写入pid文件
        SpringApplication app = new SpringApplication(RememberApplication.class);
        app.addListeners(new ApplicationPidFileWriter("remember.pid"));
        app.run(args);
    }

}
