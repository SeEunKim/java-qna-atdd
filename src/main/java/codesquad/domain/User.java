package codesquad.domain;

import codesquad.UnAuthorizedException;
import codesquad.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import support.domain.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class User extends AbstractEntity {
    public static final GuestUser GUEST_USER = new GuestUser();

    @Size(min = 3, max = 20)
    @Column(unique = true, nullable = false, length = 20)
    @JsonProperty
    private String userId;

    @Size(min = 6, max = 20)
    @Column(nullable = false, length = 20)
    @JsonIgnore
    private String password;

    @Size(min = 3, max = 20)
    @Column(nullable = false, length = 20)
    @JsonProperty
    private String name;

    @Column(length = 50)
    @JsonProperty
    private String email;

    public User() {
    }

    public User(String userId, String password, String name, String email) {
        this(0L, userId, password, name, email);
    }

    public User(long id, String userId, String password, String name, String email) {
        super(id);
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    private boolean matchUserId(String userId) {
        return this.userId.equals(userId);
    }

    public void update(User loginUser, User target) {
        if (!matchUserId(loginUser.getUserId())) {
            throw new UnAuthorizedException();
        }

        if (!matchPassword(target.getPassword())) {
            return;
        }

        name = target.name;
        email = target.email;
    }

    public boolean matchUser(User user) {
        return user.equals(this);
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    public UserDto toUserDto() {
        return new UserDto(userId, password, name, email);
    }

    @JsonIgnore
    public boolean isGuestUser() {
        return false;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, password);
    }

    private static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
