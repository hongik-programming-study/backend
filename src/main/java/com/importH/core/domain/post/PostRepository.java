package com.importH.core.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface PostRepository extends JpaRepository<Post,Long > {

    Optional<Post> findByIdAndType(Long postId, int type);

    List<Post> findAllByType(int type);
}
