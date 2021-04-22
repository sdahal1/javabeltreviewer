package com.instructorrob.beltreviewer.models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name="groupz")
public class Group {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	
	@NotEmpty(message="Group name cannot be empty")
	@Size(min=2, message ="Group name must be at least 2 char")
    private String name;
    
	
	@NotEmpty(message="Group description cannot be empty")
	@Size(min=10, max=1000, message ="Group description must be at least 10 char and less than 1000")
    private String description;
    
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="creator_id")
    private User creator;

	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="vp_id")
    private User vp;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "users_groups_members", 
        joinColumns = @JoinColumn(name = "group_id"), 
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> members;
	
	
    public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public User getCreator() {
		return creator;
	}



	public void setCreator(User creator) {
		this.creator = creator;
	}



	public User getVp() {
		return vp;
	}



	public void setVp(User vp) {
		this.vp = vp;
	}



	public List<User> getMembers() {
		return members;
	}



	public void setMembers(List<User> members) {
		this.members = members;
	}



	public Date getCreatedAt() {
		return createdAt;
	}



	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}



	public Date getUpdatedAt() {
		return updatedAt;
	}



	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}



	@Column(updatable=false)
    private Date createdAt;
    private Date updatedAt;
    
    
    
    public Group() {
    	
    }
    
    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }
    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = new Date();
    }
    

}
