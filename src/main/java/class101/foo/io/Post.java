package class101.foo.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_id_seq")
    @SequenceGenerator(name="post_id_seq", sequenceName = "post_id_seq", allocationSize = 1)
    private Long id;
    private String content;
}
