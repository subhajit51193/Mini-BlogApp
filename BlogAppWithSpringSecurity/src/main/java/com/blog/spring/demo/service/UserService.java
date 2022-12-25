package com.blog.spring.demo.service;

import java.util.List;

import com.blog.spring.demo.exceptions.PostException;
import com.blog.spring.demo.exceptions.UserException;
import com.blog.spring.demo.models.Comment;
import com.blog.spring.demo.models.Post;

public interface UserService {

	public String createPost(Post post) throws UserException;
	
	public List<Post> getAllPost() throws UserException;
	
	public String deletePost(Integer postId) throws UserException, PostException;// later
	
	public String addComment(Comment comment,Integer postId) throws PostException, UserException;
	
	public List<Comment> getAllComments(Integer postId) throws UserException, PostException;
	
}
