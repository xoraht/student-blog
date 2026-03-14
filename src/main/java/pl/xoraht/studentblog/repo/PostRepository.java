package pl.xoraht.studentblog.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.xoraht.studentblog.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}