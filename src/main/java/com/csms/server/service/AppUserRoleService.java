package com.csms.server.service;

import com.csms.server.dao.AppUserRoleDao;
import com.csms.server.dao.RolePermissionDao;
import com.csms.server.dto.AppUserRoleDto;
import com.csms.server.exception.ObjectDoesNotExistException;
import com.csms.server.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserRoleService {
    private final AppUserRoleDao appUserRoleDao;
    private final RolePermissionDao rolePermissionDao;

    @Autowired
    public AppUserRoleService(AppUserRoleDao appUserRoleDao, RolePermissionDao rolePermissionDao){
        this.appUserRoleDao = appUserRoleDao;
        this.rolePermissionDao = rolePermissionDao;
    }

    public void validate(final AppUserRoleDto appUserRoleDto) throws ValidationException {

    }

    public long insert(AppUserRoleDto appUserRoleDto) throws ValidationException {
        validate(appUserRoleDto);
        return appUserRoleDao.insert(appUserRoleDto);
    }

    public AppUserRoleDto get(long idAppUserRole) {
        try{
            return appUserRoleDao.get(idAppUserRole);
        } catch (Exception ignored){
            return null;
        }
    }

    public List<AppUserRoleDto> getAll() {
        return appUserRoleDao.getAll();
    }

    public void update(AppUserRoleDto appUserRoleDto) throws ValidationException, ObjectDoesNotExistException {
        validate(appUserRoleDto);
        appUserRoleDao.update(appUserRoleDto);
    }

    public void delete(long idAppUserRole) throws ObjectDoesNotExistException {
        appUserRoleDao.delete(idAppUserRole);
    }

    public void getAppUserRole(){

    }

    public void getRolePermissions(long idAppUserRole){

    }
}
