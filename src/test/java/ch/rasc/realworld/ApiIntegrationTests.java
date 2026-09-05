package ch.rasc.realworld;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest(properties = "debug=false")
@AutoConfigureMockMvc
class ApiIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private DSLContext dsl;

	@BeforeEach
	void deleteTestData() {
		this.dsl.deleteFrom(APP_USER).execute();
	}

	@Test
	void validationErrorsUseTheRealWorldEnvelope() throws Exception {
		this.mockMvc.perform(post("/api/users").contextPath("/api").contentType(MediaType.APPLICATION_JSON).content("""
				{"user":{"email":"bad","password":"","username":""}}
				"""))
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.errors.email").exists())
			.andExpect(jsonPath("$.errors.password").value(contains("can't be empty")))
			.andExpect(jsonPath("$.errors.username").value(contains("can't be empty")));
	}

	@Test
	void articleWorkflowHandlesDuplicateTitlesAndTags() throws Exception {
		String token = registerAndGetToken();
		String article = """
				{"article":{"title":"Café & Spring!","description":"Description","body":"Body","tagList":["java","java"]}}
				""";

		this.mockMvc
			.perform(post("/api/articles").contextPath("/api")
				.header("Authorization", "Token " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(article))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.article.slug").value("cafe-spring"))
			.andExpect(jsonPath("$.article.body").value("Body"))
			.andExpect(jsonPath("$.article.tagList", hasSize(1)));

		this.mockMvc
			.perform(post("/api/articles").contextPath("/api")
				.header("Authorization", "Token " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(article))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.article.slug").value("cafe-spring-2"));

		this.mockMvc.perform(get("/api/articles").contextPath("/api"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.articlesCount").value(2))
			.andExpect(jsonPath("$.articles[0].body").doesNotExist());

		this.mockMvc.perform(get("/api/articles/cafe-spring").contextPath("/api"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.article.body").value("Body"));

		updateArticleTags(token, "cafe-spring", "[\"spring\",\" spring \"]");
		updateArticleTags(token, "cafe-spring-2", "[\"spring\"]");

		this.mockMvc.perform(get("/api/tags").contextPath("/api"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.tags", contains("spring")));

		this.mockMvc
			.perform(put("/api/user").contextPath("/api")
				.header("Authorization", "Token " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{"user":{"bio":null,"image":null}}
						"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.user.bio").value(nullValue()))
			.andExpect(jsonPath("$.user.image").value(nullValue()));
	}

	@Test
	void rejectsInvalidPaginationAndMalformedAuthorizationHeader() throws Exception {
		this.mockMvc.perform(get("/api/articles").contextPath("/api").queryParam("offset", "-1"))
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.errors.offset").exists());

		this.mockMvc.perform(get("/api/user").contextPath("/api").header("Authorization", "Token"))
			.andExpect(status().isUnauthorized());
	}

	private String registerAndGetToken() throws Exception {
		String response = this.mockMvc
			.perform(post("/api/users").contextPath("/api")
				.contentType(MediaType.APPLICATION_JSON)
				.content(
						"""
								{"user":{"email":"test@example.com","password":"secret","username":"tester","bio":"bio","image":"image"}}
								"""))
			.andExpect(status().isCreated())
			.andReturn()
			.getResponse()
			.getContentAsString();
		return JsonPath.read(response, "$.user.token");
	}

	private void updateArticleTags(String token, String slug, String tagList) throws Exception {
		this.mockMvc
			.perform(put("/api/articles/{slug}", slug).contextPath("/api")
				.header("Authorization", "Token " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"article\":{\"tagList\":" + tagList + "}}"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.article.tagList", contains("spring")));
	}

}
