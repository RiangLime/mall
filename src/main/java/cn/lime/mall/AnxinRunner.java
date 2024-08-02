package cn.lime.mall;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;

/**
 * @author riang
 */
@SpringBootApplication
@MapperScan({"cn.lime.core.mapper","cn.lime.mall.mapper"})
@Slf4j
@EnableScheduling
public class AnxinRunner {

	@SneakyThrows
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AnxinRunner.class);
		ConfigurableApplicationContext application = app.run(args);
		Environment env = application.getEnvironment();
		String contextPath = env.getProperty("server.servlet.context-path");
		if (contextPath == null) {
			contextPath = "";
		}
		if (contextPath.startsWith("/")) {
			contextPath = contextPath.substring(1);
		}
		if (contextPath.endsWith("/")) {
			contextPath = contextPath.substring(0, contextPath.length() - 1);
		}
		if (!contextPath.isBlank()) {
			contextPath = "/" + contextPath;
		}
		log.info("""

                        ----------------------------------------------------------
                        \tApplication '{}' is running! Access URLs:
                        \tLocal: \t\thttp://localhost:{}{}
                        \tExternal: \thttp://{}:{}{}
                        \tDoc: \thttp://{}:{}{}/doc.html
                        ----------------------------------------------------------""",
				env.getProperty("spring.application.name"),
				env.getProperty("server.port"),
				contextPath,
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"),
				contextPath,
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"),
				contextPath
		);
	}

}
