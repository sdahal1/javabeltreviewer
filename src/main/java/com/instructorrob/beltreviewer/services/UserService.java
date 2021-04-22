package com.instructorrob.beltreviewer.services;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.instructorrob.beltreviewer.models.Group;
import com.instructorrob.beltreviewer.models.User;
import com.instructorrob.beltreviewer.models.UserGroupMembers;
import com.instructorrob.beltreviewer.repositories.GroupRepository;
import com.instructorrob.beltreviewer.repositories.UserGroupMembersRepository;
import com.instructorrob.beltreviewer.repositories.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserGroupMembersRepository userGroupMembersRepository;
    
    public UserService(UserRepository userRepository, GroupRepository groupRepository, UserGroupMembersRepository userGroupMembersRepository   ) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userGroupMembersRepository = userGroupMembersRepository;
    }
    
    // register user and hash their password
    public User registerUser(User user) {
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        user.setEmail(user.getEmail().toLowerCase());
        return userRepository.save(user);

    }
    
    // find user by email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // find user by id
    public User findUserById(Long id) {
    	return userRepository.findById(id).orElse(null);
    }
    
    // authenticate user
    public boolean authenticateUser(String email, String password) {
        // first find the user by email
        User user = userRepository.findByEmail(email);
        // if we can't find it by email, return false
        if(user == null) {
            return false;
        } else {
            // if the passwords match, return true, else, return false
            if(BCrypt.checkpw(password, user.getPassword())) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    public List<User> findAllUsers(){
    		return (List<User>)this.userRepository.findAll();	
    }
    
    
    public Group createGroup(Group group) {
    		return this.groupRepository.save(group);
    }
    
    public List<Group> findAllGroups(){
    		return (List<Group>)this.groupRepository.findAll();
    }
    
    public Group findAGroup(Long id) {
    		return this.groupRepository.findById(id).orElse(null);
    }
    
    public Group updateAGroup(Group group) {
    		return this.groupRepository.save(group);
    }
    
    public void deleteGroup(Group group) {
    		this.groupRepository.delete(group);
    }
    
    public UserGroupMembers createAssociation(UserGroupMembers ugm) {
    	
    		return this.userGroupMembersRepository.save(ugm);
    }
    
    
    public User leaveGroup(User u) {
    		return this.userRepository.save(u);
    }
    
    public List<Group> findGroupsNotBelongingTo(User u){
    		return this.groupRepository.findBymembersNotContaining(u);
    }
    
    
}








