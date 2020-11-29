package api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @RequestMapping("/home")
    public String home()
    {
        return "Hello World!";
    }

    @RequestMapping("*")
    public String error() {
        return "Error in finding the correct mapping";
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}