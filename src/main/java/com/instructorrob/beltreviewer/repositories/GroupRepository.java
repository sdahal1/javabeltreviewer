package com.instructorrob.beltreviewer.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.instructorrob.beltreviewer.models.Group;
import com.instructorrob.beltreviewer.models.User;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long>{
	List<Group> findBymembersNotContaining(User u);

}
