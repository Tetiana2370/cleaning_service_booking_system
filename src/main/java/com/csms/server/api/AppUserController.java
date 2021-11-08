package com.csms.server.api;

import com.csms.server.api.response.ResponseBody;
import com.csms.server.dto.AppUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.csms.server.service.AppUserService;

import java.util.List;

@Controller
@RestController
@RequestMapping("/com/csms/server/api/app_user")
public class AppUserController {

    AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public ResponseEntity<ResponseBody<List<AppUserDto>>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseBody<>(appUserService.getAll()));
    }

    @GetMapping(path="{idAppUser}")
    public ResponseEntity<ResponseBody<AppUserDto>> get(@PathVariable("idAppUser") Long idAppUser){
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseBody<>(appUserService.get(idAppUser))) ;
    }

    @PostMapping
    public ResponseEntity<ResponseBody<Long>> insert(@RequestBody AppUserDto appUserDto) {
        try {
            Long idAppUser = appUserService.insert(appUserDto);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBody<>(idAppUser));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBody<>(e));
        }
    }

    @DeleteMapping(path="{idAppUser}")
    public ResponseEntity<ResponseBody<Boolean>> delete(@PathVariable("idAppUser") Long idAppUser){
        try {
            appUserService.delete(idAppUser);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBody<>(Boolean.TRUE));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBody<>(e));
        }
    }

    @PutMapping(path="{idAppUser}")
    public ResponseEntity<ResponseBody<AppUserDto>> update(@RequestBody AppUserDto appUserDto){
        try {
            appUserService.update(appUserDto);
            appUserDto = appUserService.get(appUserDto.idAppUser);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBody<>(appUserDto));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseBody<>(e));
        }
    }

}
