package codesquad.web;

import codesquad.domain.Question;
import codesquad.domain.User;
import codesquad.security.LoginUser;
import codesquad.service.QnaService;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;

@RestController
@RequestMapping("/api/questions")
public class ApiQuestionController {
    private static final Logger log = getLogger(ApiQuestionController.class);

    @Resource(name = "qnaService")
    private QnaService qnaService;

    @PostMapping("")
    public ResponseEntity<Void> create(@LoginUser User loginUser, @Valid @RequestBody Question question) {

        log.debug("##create, User : {}, Question : {}", loginUser, question);

        Question savedQuestion = qnaService.create(loginUser, question);
        log.debug("savedQuestion : {}", savedQuestion);


        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/api/questions/" + savedQuestion.getId()));
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Question get(@PathVariable Long id) {
        return qnaService.findById(id);
    }

    @PutMapping("/{id}")
    public Question update(@LoginUser User loginUser, @PathVariable long id, @Valid @RequestBody Question updateQuestion) {

        return qnaService.update(loginUser, id, updateQuestion);

    }
}
