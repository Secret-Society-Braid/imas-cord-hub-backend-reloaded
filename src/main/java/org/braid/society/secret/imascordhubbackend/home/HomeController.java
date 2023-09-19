package org.braid.society.secret.imascordhubbackend.home;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
public class HomeController {

  private final HomeService service;

  @GetMapping()
  public Map<String, String> home() {
    return this.service.home();
  }
}
