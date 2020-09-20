package com.pjhu.whoami;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class WhoamiController {

    @GetMapping("/whoami")
    public String whoami() {
        return "Jinhu PENG";
    }
}
