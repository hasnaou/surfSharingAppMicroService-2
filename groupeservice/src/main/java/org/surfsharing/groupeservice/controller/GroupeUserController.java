package org.surfsharing.groupeservice.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.surfsharing.groupeservice.dto.GroupeDto;
import org.surfsharing.groupeservice.dto.GroupeUserDto;
import org.surfsharing.groupeservice.dto.UserDto;
import org.surfsharing.groupeservice.entity.Groupe;
import org.surfsharing.groupeservice.service.IGroupeService;
import org.surfsharing.groupeservice.service.IGroupeUserService;
import org.surfsharing.groupeservice.userClient.UserClient;

import java.util.List;

@RestController
@RequestMapping("/api/v1/grp/groupeuser")
@RequiredArgsConstructor
public class GroupeUserController {

    private final UserClient userClient;
    @Autowired
    private IGroupeService groupeService;
    @Autowired
    private IGroupeUserService groupeUserService;
    /////////////////////////////////////////////
    //work
    @CircuitBreaker(name = "userserice",fallbackMethod = "UserFullBack")
    @PostMapping()
    public ResponseEntity<Object> ajouterGroupeUser(@RequestBody GroupeUserDto groupeUserDto, HttpServletRequest request) {
        long adminid = Long.parseLong(request.getHeader("hid"));
        GroupeDto groupeDto = groupeService.getGroupeById(groupeUserDto.getGroupeId());
        UserDto userDto = userClient.findUserById(groupeUserDto.getUserId());
        if(userDto == null)
        {
            return new ResponseEntity<>("User not exist!", HttpStatus.BAD_REQUEST);
        }
        if(!groupeDto.getAdminId().toString().equals(Long.toString(adminid)))
        {
            return new ResponseEntity<>("you cant add user to this groupe", HttpStatus.BAD_REQUEST);
        }

        GroupeUserDto result = groupeUserService.ajouterGroupeUser(groupeUserDto);
        if (result == null) {
            return new ResponseEntity<>("User is already in the group", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.CREATED);

    }
    @CircuitBreaker(name = "userserice",fallbackMethod = "UserFullBack")
    //work
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteGroupeUser(@RequestBody GroupeUserDto groupeUserDto, HttpServletRequest request) {
        long adminid = Long.parseLong(request.getHeader("hid"));
        GroupeDto groupeDto = groupeService.getGroupeById(groupeUserDto.getGroupeId());
        UserDto userDto = userClient.findUserById(groupeUserDto.getUserId());
        if(userDto == null)
        {
            return new ResponseEntity<>("User not exist!", HttpStatus.BAD_REQUEST);
        }
        if(groupeDto.getAdminId() != adminid)
        {
            return new ResponseEntity<>("you cant delete user to this groupe", HttpStatus.BAD_REQUEST);
        }
        groupeUserService.deleteGroupeUser(groupeUserDto);
        return ResponseEntity.ok("User with id " + groupeUserDto.getUserId() + " was deleted successfully");
    }
    ////////////////////////////////////////////
    @GetMapping("/{id}")
    public ResponseEntity<GroupeUserDto> getGroupeUserById(@PathVariable Long id) {
        GroupeUserDto groupeUser = groupeUserService.getGroupesUsersById(id);
        if (groupeUser != null) {
            return new ResponseEntity<>(groupeUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<GroupeUserDto> updateGroupeUser(@PathVariable Long id, @RequestBody GroupeUserDto groupeUserDto) {
        GroupeUserDto updatedGroupeUser = groupeUserService.updateGroupeUser(groupeUserDto, id);
        if (updatedGroupeUser != null) {
            return new ResponseEntity<>(updatedGroupeUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping
    public ResponseEntity<List<GroupeUserDto>> getGroupesUsers() {
        List<GroupeUserDto> groupesUsers = groupeUserService.getGroupesUsers();
        return new ResponseEntity<>(groupesUsers, HttpStatus.OK);
    }
    @GetMapping("/getUsersInGroup/{groupId}")
    public ResponseEntity<List<GroupeUserDto>> getUsersInGroup(@PathVariable Long groupId) {
        List<GroupeUserDto> usersInGroup = groupeUserService.getUsersInGroup(groupId);
        return new ResponseEntity<>(usersInGroup, HttpStatus.OK);
    }

    public ResponseEntity<Object> UserFullBack(Exception e){
        return new ResponseEntity<>("User Service is down",HttpStatus.OK);
    }

}

