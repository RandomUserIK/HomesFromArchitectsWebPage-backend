package sk.hfa.auth.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

class Test {
    public String odp1;
}

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminTestController {

    @GetMapping(value = "/helloWorld", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> dummyTestFunc() {
        Test test = new Test();
        test.odp1 = "Mrkvicka";
        return ResponseEntity.ok(test);
    }

}
