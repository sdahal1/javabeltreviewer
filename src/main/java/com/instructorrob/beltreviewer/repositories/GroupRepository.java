package com.instructorrob.beltreviewer.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.instructorrob.beltreviewer.models.Group;

@Repository
public interface GroupRepository extends CrudRepository<Group, Long>{

}
