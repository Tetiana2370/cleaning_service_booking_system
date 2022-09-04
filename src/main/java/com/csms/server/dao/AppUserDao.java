package com.csms.server.dao;

import com.csms.server.dto.AppUserDto;
import com.csms.server.exception.ObjectDoesNotExistException;
import com.csms.server.security.PasswordConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Objects;

@Repository
public class AppUserDao
{
    public static final String ID_APP_USER = "idAppUser";
    public static final String ID_APP_USER_ROLE = "idAppUserRole";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String EMAIL_ADDRESS = "emailAddress";
    public static final String ACTIVE = "active";
    public static final String VERSION = "version";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PasswordConfig passwordConfig;

    @Autowired
    public AppUserDao(NamedParameterJdbcTemplate jdbcTemplate, PasswordConfig passwordConfig)
    {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordConfig = passwordConfig;
    }

    public long insert(AppUserDto appUserDto)
    {
        String sql = "insert into app_user (" +
                "id_app_user_role, " +
                "username, " +
                "password, " +
                "first_name, " +
                "last_name, " +
                "phone_number, " +
                "email_address, " +
                "active " +
                ") values (" +
                ":idAppUserRole, " +
                ":username, " +
                ":password, " +
                ":firstName, " +
                ":lastName, " +
                ":phoneNumber, " +
                ":emailAddress, " +
                ":active " +
                ") returning id_app_user;";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_APP_USER_ROLE, appUserDto.idAppUserRole, Types.NUMERIC)
                .addValue(FIRST_NAME, appUserDto.firstName, Types.VARCHAR)
                .addValue(USERNAME, appUserDto.username, Types.VARCHAR)
                .addValue(PASSWORD, passwordConfig.passwordEncoder().encode(appUserDto.getPassword()), Types.VARCHAR)
                .addValue(LAST_NAME, appUserDto.lastName, Types.VARCHAR)
                .addValue(PHONE_NUMBER, appUserDto.phoneNumber, Types.VARCHAR)
                .addValue(EMAIL_ADDRESS, appUserDto.emailAddress, Types.VARCHAR)
                .addValue(ACTIVE, appUserDto.active ? 1 : 0, Types.INTEGER);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, parameters, holder);
        return Objects.requireNonNull(holder.getKey()).longValue();
    }

    public AppUserDto get(long idAppUser) throws ObjectDoesNotExistException
    {
        validateIfExists(idAppUser);
        String sql = "select * from app_user where id_app_user = :idAppUser ";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_APP_USER, idAppUser, Types.NUMERIC);

        return jdbcTemplate.queryForObject(sql, parameters, this::mapRow);
    }

    public AppUserDto getByUsername(String username)
    {
        String sql = "select * from app_user where username = :username ";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(USERNAME, username, Types.VARCHAR);

        return jdbcTemplate.queryForObject(sql, parameters, this::mapRow);
    }

    public List<AppUserDto> getAll()
    {
        String sql = "select * from app_user";

        return jdbcTemplate.query(sql, this::mapRow);
    }

    public List<AppUserDto> getAll(boolean active)
    {
        String sql = "select * from app_user where active = :active";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ACTIVE, active ? 1 : 0, Types.NUMERIC);

        return jdbcTemplate.query(sql, parameters, this::mapRow);
    }

    public void delete(long idAppUser) throws ObjectDoesNotExistException
    {
        validateIfExists(idAppUser);
        String sql = "delete from app_user where id_app_user = :idAppUser";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_APP_USER, idAppUser, Types.NUMERIC);

        jdbcTemplate.update(sql, parameters);
    }

    public void deleteAll()
    {
        String sql = "delete from app_user";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        jdbcTemplate.update(sql, parameters);
    }

    public void update(AppUserDto appUserDto) throws ObjectDoesNotExistException
    {
        validateIfExists(appUserDto.idAppUser);
        String sql = "update app_user set " +
                "id_app_user_role = :idAppUserRole, " +
                "first_name = :firstName, " +
                "last_name = :lastName, " +
                "phone_number = :phoneNumber, " +
                "email_address = :emailAddress, " +
                "active = :active, " +
                "version = version + 1 " +
                "where id_app_user = :idAppUser ";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_APP_USER, appUserDto.idAppUser, Types.NUMERIC)
                .addValue(ID_APP_USER_ROLE, appUserDto.idAppUserRole, Types.NUMERIC)
                .addValue(FIRST_NAME, appUserDto.firstName, Types.VARCHAR)
                .addValue(LAST_NAME, appUserDto.lastName, Types.VARCHAR)
                .addValue(PHONE_NUMBER, appUserDto.phoneNumber, Types.VARCHAR)
                .addValue(EMAIL_ADDRESS, appUserDto.emailAddress, Types.VARCHAR)
                .addValue(ACTIVE, appUserDto.active ? 1 : 0, Types.INTEGER);

        jdbcTemplate.update(sql, parameters);
    }

    public AppUserDto mapRow(ResultSet rs, int rowNum) throws SQLException
    {

        AppUserDto appUserDto = new AppUserDto();
        appUserDto.idAppUser = rs.getLong("id_app_user");
        appUserDto.idAppUserRole = rs.getLong("id_app_user_role");
        appUserDto.username = rs.getString("username");
        appUserDto.version = rs.getInt("version");
        appUserDto.active = rs.getInt("active") == 1;
        appUserDto.firstName = rs.getString("first_name");
        appUserDto.lastName = rs.getString("last_name");
        appUserDto.phoneNumber = rs.getString("phone_number");
        appUserDto.emailAddress = rs.getString("email_address");
        appUserDto.password = rs.getString("password");
        appUserDto.failedLoginAttempts = rs.getInt("failed_login_attempts");

        return appUserDto;
    }

    public void validateIfExists(long idAppUser) throws ObjectDoesNotExistException
    {
        String sql = "select count(*) from app_user where id_app_user = :idAppUser ";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_APP_USER, idAppUser, Types.NUMERIC);

        boolean exists;
        exists = jdbcTemplate.queryForObject(sql, parameters, (rs, rowNum) -> rs.getInt("count") == 1);

        if (!exists) {
            throw new ObjectDoesNotExistException("AppUser", idAppUser);
        }
    }
}
