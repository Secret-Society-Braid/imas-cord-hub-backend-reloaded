package org.braid.society.secret.imascordhubbackend;

import static com.google.common.truth.Truth.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
class ImascordhubbackendApplicationTests {

  @Test
  void contextLoads() {
    assertThat(true).isTrue();
  }

  @Test
  void testGetAllServers(@Autowired MockMvc mvc) throws Exception {
    mvc
      .perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/servers"))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json"));
  }

  @Test
  void testFindServers(@Autowired MockMvc mvc) throws Exception {
    mvc
      .perform(MockMvcRequestBuilders.request(HttpMethod.GET,
        "/servers/find/9bffce54-d95e-4f51-b8d1-8b68aaea605a"))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json"));
  }

  @Test
  void unknownServers(@Autowired MockMvc mvc) throws Exception {
    mvc
        .perform(MockMvcRequestBuilders.request(HttpMethod.GET,
            "/servers/find/someunknownid"))
        .andExpect(status().isNotFound());
  }

  @Test
  void testGetAllFansites(@Autowired MockMvc mvc) throws Exception {
    mvc
      .perform(MockMvcRequestBuilders.request(HttpMethod.GET, "/fansites"))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json"));
  }

  @Test
  void testFindFansites(@Autowired MockMvc mvc) throws Exception {
    mvc
      .perform(MockMvcRequestBuilders.request(HttpMethod.GET,
        "/fansites/find/10422c16-d21c-444f-9897-11e2f2546357"))
      .andExpect(status().isOk())
      .andExpect(content().contentType("application/json"));
  }

  @Test
  void unknownFansites(@Autowired MockMvc mvc) throws Exception {
    mvc
      .perform(MockMvcRequestBuilders.request(HttpMethod.GET,
        "/fansites/find/someunknownid"))
      .andExpect(status().isNotFound());
  }
}
