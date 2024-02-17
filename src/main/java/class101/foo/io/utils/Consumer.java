package class101.foo.io.utils;

import class101.foo.io.Post;
import class101.foo.io.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PostRepository postRepository;

    @RabbitListener(queues = "CREATE_POST_QUEUE") // 메시지 큐 이름, 해당 큐에 메시지가 들어오면 해당 메소드가 실행된다.
    public void handler(String message) throws JsonProcessingException {
        Post post = objectMapper.readValue(message, Post.class); // 메시지를 Post 객체로 변환
        postRepository.save(post); // 변환된 Post 객체를 저장
    }
}
