package com.csms.server.dao;

import com.csms.server.dto.AppUserRoleDto;
import com.csms.server.exception.ObjectDoesNotExistException;
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
public class AppUserRoleDao {
    public static final String ID_APP_USER_ROLE = "idAppUserRole";
    public static final String ADMIN = "admin";
    public static final String NAME = "name";
    public static final String ACTIVE = "active";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public AppUserRoleDao(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insert(AppUserRoleDto appUserRoleDto) {
        String sql = "insert into app_user_role (" +
                "name, " +
                "admin, " +
                "active " +
                ") values (" +
                ":name, " +
                ":admin, " +
                ":active " +
                ") returning id_app_user_role;";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(NAME, appUserRoleDto.name, Types.VARCHAR)
                .addValue(ADMIN, appUserRoleDto.admin, Types.INTEGER)
                .addValue(ACTIVE, appUserRoleDto.active ? 1 : 0, Types.INTEGER);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, parameters, holder);
        return Objects.requireNonNull(holder.getKey()).longValue();
    }

    public AppUserRoleDto get(long idAppUserRole) throws ObjectDoesNotExistException {
        validateIfExists(idAppUserRole);
        String sql = "select * from app_user_role where id_app_user_role = :idAppUserRole ";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_APP_USER_ROLE,  idAppUserRole, Types.NUMERIC);

        return jdbcTemplate.queryForObject(sql, parameters, this::mapRow);
    }

    public List<AppUserRoleDto> getAll(){
        String sql = "select * from app_user_role";

        return jdbcTemplate.query(sql, this::mapRow);
    }

    public void delete(long idAppUserRole) throws ObjectDoesNotExistException {
        validateIfExists(idAppUserRole);
        String sql = "delete from app_user_role where id_app_user_role = :idAppUserRole";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_APP_USER_ROLE,  idAppUserRole, Types.NUMERIC);

        jdbcTemplate.update(sql, parameters);
    }

    public void deleteAll(){
        String sql = "delete from app_user_role";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        jdbcTemplate.update(sql, parameters);
    }

    public void update(AppUserRoleDto appUserRoleDto) throws ObjectDoesNotExistException {
        validateIfExists(appUserRoleDto.idAppUserRole);
        String sql = "update app_user_role set " +
                "name = :name, " +
                "admin = :admin, " +
                "active = :active " +
                "where id_app_user_role = :idAppUserRole ";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_APP_USER_ROLE,  appUserRoleDto.idAppUserRole, Types.NUMERIC)
                .addValue(NAME, appUserRoleDto.name, Types.VARCHAR)
                .addValue(ADMIN,  appUserRoleDto.admin ? 1 : 0, Types.INTEGER)
                .addValue(ACTIVE, appUserRoleDto.active ? 1 : 0, Types.INTEGER);

        jdbcTemplate.update(sql, parameters);
    }

    public AppUserRoleDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        AppUserRoleDto appUserRoleDto = new AppUserRoleDto();
        appUserRoleDto.idAppUserRole = rs.getLong("id_app_user_role");

        appUserRoleDto.name = rs.getString("name");
        appUserRoleDto.admin = rs.getInt("admin") == 1;
        appUserRoleDto.active = rs.getInt("active") == 1;

        return appUserRoleDto;
    }

    public void validateIfExists(long idAppUserRole) throws ObjectDoesNotExistException {
        String sql = "select count(*) from app_user_role where id_app_user_role = :idAppUserRole ";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_APP_USER_ROLE, idAppUserRole, Types.NUMERIC);

        boolean exists;
        exists = jdbcTemplate.queryForObject(sql, parameters, (rs, rowNum) -> rs.getInt("count") == 1);

        if (!exists) {
            throw new ObjectDoesNotExistException("AppUserRole", idAppUserRole);
        }
    }
}
