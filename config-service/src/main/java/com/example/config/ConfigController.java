package com.example.config;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/configs")
class ConfigController {

  private final Map<String, String> store = Map.of(
      "es.feature.quotes", "on",
      "de.feature.quotes", "off");

  @GetMapping("/{key}")
  ResponseEntity<?> get(@PathVariable String key) {
    String value = store.get(key);
    if (value == null) {
      ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "No config named " + key);
      pd.setTitle("Config not found");
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }
    return ResponseEntity.ok()
        .eTag(Integer.toHexString(value.hashCode()))
        .body(Map.of(key, value));
  }
}
