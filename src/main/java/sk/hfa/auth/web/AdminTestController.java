package sk.hfa.auth.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminTestController {

    @GetMapping("/helloWorld")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> dummyTestFunc() {
        return ResponseEntity.ok("Hello from /api/admin/helloWorld");
    }

}
