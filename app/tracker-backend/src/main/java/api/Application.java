package api;

@SpringBootApplication
@RestController
public class Application {
    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @RequestMapping("/")
    public String home()
    {
        return "Hello World!";
    }
}