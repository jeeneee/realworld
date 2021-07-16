package com.jeeneee.realworld.user.domain;

import com.jeeneee.realworld.common.domain.BaseTimeEntity;
import com.jeeneee.realworld.common.exception.IllegalParameterException;
import com.jeeneee.realworld.user.exception.DuplicatedFollowException;
import com.jeeneee.realworld.user.exception.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String image;

    private String bio;

    @ManyToMany
    @JoinTable(name = "user_follows",
        joinColumns = @JoinColumn(name = "following_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private final List<User> follows = new ArrayList<>();

    @Builder
    private User(Long id, String email, String username, String password, String image,
        String bio) {
        validateParams(email, username, password);
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.image = image;
        this.bio = bio;
    }

    private void validateParams(String email, String username, String password) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(username) ||
            !StringUtils.hasText(password)) {
            throw new IllegalParameterException();
        }
    }

    public static User create(String email, String username, String password) {
        return new User(null, email, username, password, null, null);
    }

    public void update(String email, String username, String password, String image, String bio) {
        validateParams(email, username, password);
        this.email = email;
        this.username = username;
        this.password = password;
        this.image = image;
        this.bio = bio;
    }

    public void follow(User target) {
        if (followed(target)) {
            throw new DuplicatedFollowException("The user you have already followed.");
        }
        follows.add(target);
    }

    public void unfollow(User target) {
        if (!followed(target)) {
            throw new UserNotFoundException();
        }
        follows.remove(target);
    }

    public boolean followed(User target) {
        return follows.contains(target);
    }
}
