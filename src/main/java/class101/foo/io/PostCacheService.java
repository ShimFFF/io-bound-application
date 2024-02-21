package class101.foo.io;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PostCacheService {

    @Autowired
    PostRepository postRepository;

    private Page<Post> firstPostPage; // 최신 글 20개를 저장하는 변수

    @Scheduled(cron = "* * * * * *") // 매 초마다 실행
    public void updateFirstPostPage() {
        // 최신 글 20개를 firstPostPage에 저장
        firstPostPage = postRepository.findAll(PageRequest.of(0, 20, Sort.by("id").descending()));
    }

    public Page<Post> getFirstPostPage() {
        return this.firstPostPage; // firstPostPage 반환
    }
}
