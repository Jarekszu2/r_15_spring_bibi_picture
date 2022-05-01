package jarek.biblioteka.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/test/")
public class TestController {

    @GetMapping(path = "/go")
    public String test() {
        return "fragmentsTest";
    }

    @GetMapping(path = "/fragments")
    public String fragments() {
        return "fragments";
    }
}
