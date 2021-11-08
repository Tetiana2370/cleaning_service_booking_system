package com.csms.server.dao;

import com.csms.server.dto.AppUserRoleDto;
import com.csms.server.dto.RolePermissionDto;
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
public class RolePermissionDao {
    public static final String ID_ROLE_PERMISSION = "idAppUserRole";
    public static final String ID_ROLE = "idRole";
    public static final String ID_PERMISSION = "idPermission";
    public static final String READ = "read";
    public static final String WRITE = "write";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public RolePermissionDao(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insert(RolePermissionDto rolePermissionDto) {
        String sql = "insert into role_permission (" +
                "id_role, " +
                "id_persmission, " +
                "read, " +
                "write " +
                ") values (" +
                ":idRole, " +
                ":idPermission, " +
                ":read, " +
                ":write " +
                ") returning id_role_permission;";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_ROLE, rolePermissionDto.idRole, Types.NUMERIC)
                .addValue(ID_PERMISSION, rolePermissionDto.idPermission, Types.NUMERIC)
                .addValue(READ, rolePermissionDto.read ? 1 : 0, Types.INTEGER)
                .addValue(WRITE, rolePermissionDto.write ? 1 : 0, Types.INTEGER);

        KeyHolder holder = new GeneratedKeyHolder();

        jdbcTemplate.update(sql, parameters, holder);
        return Objects.requireNonNull(holder.getKey()).longValue();
    }

    public RolePermissionDto get(long idRolePermission) throws ObjectDoesNotExistException {
        validateIfExists(idRolePermission);
        String sql = "select * from role_pesmission where id_role_pesmission = :idRolePermission ";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_ROLE_PERMISSION,  idRolePermission, Types.NUMERIC);

        return jdbcTemplate.queryForObject(sql, parameters, this::mapRow);
    }

    public List<RolePermissionDto> getAll(){
        String sql = "select * from role_permission";

        return jdbcTemplate.query(sql, this::mapRow);
    }

    public void delete(long idRolePermission) throws ObjectDoesNotExistException {
        validateIfExists(idRolePermission);
        String sql = "delete from role_permission where id_role_permission = :idAppUserRole";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_ROLE_PERMISSION,  idRolePermission, Types.NUMERIC);

        jdbcTemplate.update(sql, parameters);
    }

    public void deleteAll(){
        String sql = "delete from role_permission";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        jdbcTemplate.update(sql, parameters);
    }

    public void update(RolePermissionDto rolePermissionDto) throws ObjectDoesNotExistException {
        validateIfExists(rolePermissionDto.idRolePermission);
        String sql = "update role_permission set " +
                "id_role = :idRole, " +
                "read = :read, " +
                "write = :write " +
                "where id_role_permission = :idRolePermission ";

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_ROLE_PERMISSION,  rolePermissionDto.idRolePermission, Types.NUMERIC)
                .addValue(ID_ROLE, rolePermissionDto.idRole, Types.NUMERIC)
                .addValue(ID_PERMISSION, rolePermissionDto.idPermission, Types.NUMERIC)
                .addValue(READ,  rolePermissionDto.read ? 1 : 0, Types.INTEGER)
                .addValue(WRITE, rolePermissionDto.write ? 1 : 0, Types.INTEGER);

        jdbcTemplate.update(sql, parameters);
    }

    public RolePermissionDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        RolePermissionDto rolePermissionDto = new RolePermissionDto();
        rolePermissionDto.idRolePermission = rs.getLong("id_role_permission");
        rolePermissionDto.idRole = rs.getLong("id_role");
        rolePermissionDto.idPermission = rs.getLong("id_permission");
        rolePermissionDto.read = rs.getInt("read") == 1;
        rolePermissionDto.write = rs.getInt("write") == 1;

        return rolePermissionDto;
    }

    public void validateIfExists(long idRolePermission) throws ObjectDoesNotExistException {
        String sql = "select count(*) from role_permission where id_role_permission = :idRolePermission ";
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue(ID_ROLE_PERMISSION, idRolePermission, Types.NUMERIC);

        boolean exists;
        exists = jdbcTemplate.queryForObject(sql, parameters, (rs, rowNum) -> rs.getInt("count") == 1);

        if (!exists) {
            throw new ObjectDoesNotExistException("RolePermission", idRolePermission);
        }
    }
}
