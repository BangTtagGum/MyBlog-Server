package com.sparta.myblogserver.repository.post;

import com.sparta.myblogserver.entity.domain.dto.post.PostReq;
import com.sparta.myblogserver.entity.domain.dto.post.PostRes;
import com.sparta.myblogserver.entity.domain.entity.Post;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcPostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long save(Post post) {
        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO post (title, content, author, password) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, post.getTitle());
                    preparedStatement.setString(2, post.getContent());
                    preparedStatement.setString(3, post.getAuthor());
                    preparedStatement.setString(4, post.getPassword());
                    return preparedStatement;
                },
                keyHolder);

        return keyHolder.getKey().longValue();

    }

    @Override
    public List<PostRes> findAll() {
        String sql = "SELECT * FROM post";

        return jdbcTemplate.query(sql, new RowMapper<PostRes>() {
            @Override
            public PostRes mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoRes 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                String author = rs.getString("author");
                Date createdAt = rs.getDate("createdAt");
                Date modifiedAt = rs.getDate("modifiedAt");
                return PostRes.builder()
                        .id(id)
                        .title(title)
                        .content(content)
                        .author(author)
                        .createdAt(createdAt)
                        .modifiedAt(modifiedAt)
                        .build();
            }
        });
    }

    @Override
    public Post findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM post WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Post post = Post.builder()
                        .id(Long.valueOf(resultSet.getString("id")))
                        .title(resultSet.getString("title"))
                        .content(resultSet.getString("content"))
                        .author(resultSet.getString("author"))
                        .password(resultSet.getString("password"))
                        .build();
                post.setCreatedAt(resultSet.getDate("createdAt"));
                post.setModifiedAt(resultSet.getDate("modifiedAt"));
                return post;
            } else {
                return null;
            }
        }, id);
    }

    @Override
    public Long update(Long id, PostReq postReq) {
        String sql = "UPDATE post SET title = ?, content = ?, author = ? WHERE id = ?";
        jdbcTemplate.update(sql, postReq.getTitle(), postReq.getContent(), postReq.getAuthor(), id);
        return id;
    }

    @Override
    public Long delete(Long id) {
        String sql = "DELETE FROM post WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return id;
    }

}
