package class101.foo.io;

import class101.foo.io.utils.Producer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostRepository postRepository;
    private final Producer producer;
    private final ObjectMapper objectMapper;
    private final PostCacheService postCacheService;

    // 1. 글을 작성한다.
    @PostMapping("/post")
    public Post createPost(@RequestBody Post post) throws JsonProcessingException {
        String jsonPost = objectMapper.writeValueAsString(post); // Post 객체를 JSON 문자열로 변환
        producer.sendTo(jsonPost); // 변환된 JSON 문자열을 메시지 큐에 전송
        return post;
    }

    // 2-1. 글 목록을 조회한다. (페이징 x)
    @GetMapping("/posts")
    public List<Post> getPostList() {
        return postRepository.findAll();
    }
    
    // 2-2 글 목록을 페이징하여 반환
    @GetMapping("/posts/page")
    public Page<Post> getPostListPaging(@RequestParam(defaultValue = "1") Integer page) {
        if (page.equals(1)) { // 첫 페이지일 경우 캐시에서 가져옴
            return postCacheService.getFirstPostPage();
        } else { // 첫 페이지가 아닐 경우 DB에서 가져옴
            return postRepository.findAll(PageRequest.of(page - 1, 20, Sort.by("id").descending()));
        }
    }
    
    // 3. 글 번호로 조회
    @GetMapping("/posts/{id}}")
    public Post getPostById(@PathVariable Long id) {
        return postRepository.findById(id).get();
    }
    
    // 4. 글 내용으로 검색 -> 해당 내용이 포함된 모든 글
    @GetMapping("/posts/search")
    public List<Post> getPostByContent(@RequestParam String content) {
        return postRepository.findByContentContaining(content);
    }

}
