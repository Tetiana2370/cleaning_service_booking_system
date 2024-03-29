package com.csms.server.service;

import com.csms.server.dao.AppUserDao;
import com.csms.server.dto.AppUserDto;
import com.csms.server.exception.ObjectDoesNotExistException;
import com.csms.server.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserDao appUserDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserService(AppUserDao appUserDao, PasswordEncoder passwordEncoder){
        this.appUserDao = appUserDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void validate(final AppUserDto appUserDto) throws ValidationException {

    }

    public long insert(AppUserDto appUserDto) throws ValidationException {
        validate(appUserDto);
        return appUserDao.insert(appUserDto);
    }

    public AppUserDto get(long idAppUser) {
        try{
            return appUserDao.get(idAppUser);
        } catch (Exception ignored){
            return null;
        }
    }

    public List<AppUserDto> getAll() {
        return appUserDao.getAll();
    }

    public void update(AppUserDto appUserDto) throws ObjectDoesNotExistException, ValidationException {
        validate(appUserDto);
        appUserDao.update(appUserDto);
    }

    public void delete(long idAppUser) throws ObjectDoesNotExistException {
        appUserDao.delete(idAppUser);
    }
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserDto appUserDto = appUserDao.getByUsername(username);
        if(appUserDto != null) {
            return appUserDto;
        } else {
            throw new UsernameNotFoundException(String.format("Username %s not found", username));
        }
    }
}
