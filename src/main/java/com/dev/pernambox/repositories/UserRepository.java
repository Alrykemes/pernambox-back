package com.dev.pernambox.repositories;

import com.dev.pernambox.domain.user.User;
import com.dev.pernambox.domain.user.dtos.StatsUsersResponseDto;
import com.dev.pernambox.domain.user.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.password = :password WHERE u.id = :userId")
    int updatePasswordById(@Param("userId") UUID userId,@Param("password") String password);

    @Query("SELECT u FROM User u")
    Page<User> getAll(Pageable pageable);

    @Query("""
    SELECT u FROM User u
    WHERE (:name IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (
        (:active = TRUE AND u.active = true)
        OR (:deactive = TRUE AND u.active = false)
        OR (:active = FALSE AND :deactive = FALSE)
      )
      AND (
        (:onlyUsers = TRUE AND u.role = com.dev.pernambox.domain.user.enums.Role.USER)
        OR (:onlyAdmins = TRUE AND u.role = com.dev.pernambox.domain.user.enums.Role.ADMIN
                                AND u.role = com.dev.pernambox.domain.user.enums.Role.ADMIN_MASTER)
        OR (:onlyUsers = FALSE AND :onlyAdmins = FALSE)
      )
""")
    Page<User> getUsersByNameWithFilter(
            @Param("name") String name,
            @Param("active") Boolean active,
            @Param("deactive") Boolean deactive,
            @Param("onlyUsers") Boolean onlyUsers,
            @Param("onlyAdmins") Boolean onlyAdmins,
            Pageable pageable);

    @Query("""
            SELECT new com.dev.pernambox.domain.user.dtos.StatsUsersResponseDto(
                            COUNT(u),
                            SUM(CASE WHEN u.active = true THEN 1 ELSE 0 END),
                            SUM(CASE WHEN u.active = false THEN 1 ELSE 0 END),
                            SUM(CASE WHEN u.role = com.dev.pernambox.domain.user.enums.Role.ADMIN THEN 1 ELSE 0 END)
                        )
                        FROM User u
            """)
    StatsUsersResponseDto getUsersStats();

    @Query("""
            SELECT u FROM User u WHERE u.role = "ADMIN_MASTER"
            """)
    List<User> getAllAdminsMasters();

    @Query("""
            SELECT u FROM User u WHERE u.role = "ADMIN"
            """)
    List<User> getAllAdmins();

    boolean existsByCpfEquals(String cpf);

    boolean existsByPhoneEquals(String phone);

    boolean existsByEmailEquals(String email);

    boolean existsByCpfAndIdNot(String cpf, UUID id);

    boolean existsByPhoneAndIdNot(String phone, UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);
}
