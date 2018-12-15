package codesquad.web;

import codesquad.domain.QuestionRepository;
import codesquad.domain.User;
import com.sun.xml.internal.bind.v2.TODO;
import org.junit.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;
import support.test.HtmlFormDataBuilder;

import static org.slf4j.LoggerFactory.getLogger;

public class QuestionAcceptanceTest extends AcceptanceTest {
    private static final Logger log = getLogger(QuestionAcceptanceTest.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void questionsForm() {
        ResponseEntity<String> response = template().getForEntity("/questions/form", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("body : {}", response.getBody());
    }

    @Test
    public void create_login() throws Exception {
        User loginUser = defaultUser();
        HttpEntity<MultiValueMap<String, Object>> request =
                HtmlFormDataBuilder.urlEncodeForm()
                        .addParameter("title", "title")
                        .addParameter("contents", "contents")
                        .build();
        ResponseEntity<String> response = basicAuthTemplate(loginUser).postForEntity("/questions", request, String.class);

        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
        softly.assertThat(questionRepository.findByTitle("title").isPresent()).isTrue();
        softly.assertThat(response.getHeaders().getLocation().getPath()).startsWith("/");
    }

    @Test
    public void create_no_login() {
        HttpEntity<MultiValueMap<String, Object>> request =
                HtmlFormDataBuilder.urlEncodeForm()
                        .addParameter("title", "title")
                        .addParameter("contents", "contents")
                        .build();
        ResponseEntity<String> response = template().postForEntity("/questions", request, String.class);

        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void updateForm_no_login() {
        ResponseEntity<String> response = template().getForEntity("/questions/updateForm", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void updateForm_login() {
        User loginUser = defaultUser();
        ResponseEntity<String> response = basicAuthTemplate(loginUser).getForEntity("/questions/updateForm", String.class);
        softly.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        softly.assertThat(response.getHeaders().getLocation().getPath().startsWith("/questions/updateForm"));
    }
}