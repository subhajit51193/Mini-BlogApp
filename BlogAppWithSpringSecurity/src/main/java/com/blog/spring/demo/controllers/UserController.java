package com.blog.spring.demo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blog.spring.demo.exceptions.PostException;
import com.blog.spring.demo.exceptions.UserException;
import com.blog.spring.demo.models.Comment;
import com.blog.spring.demo.models.Post;
import com.blog.spring.demo.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/user/createPost")
	public ResponseEntity<String> createPost(@RequestBody Post post) throws UserException{
		String result = userService.createPost(post);
		return new ResponseEntity<String>(result,HttpStatus.CREATED);
	}
	
	@GetMapping("/user/allpost")
	public ResponseEntity<List<Post>> getAllPosts() throws UserException {
		List<Post> posts = userService.getAllPost();
		return new ResponseEntity<List<Post>>(posts,HttpStatus.OK);
	}
	
	@PostMapping("/user/addcomment/{id}")
	public ResponseEntity<String> addComment(@RequestBody Comment comment,@PathVariable("id") Integer postId) throws PostException, UserException{
		String result = userService.addComment(comment, postId);
		return new ResponseEntity<String>(result,HttpStatus.OK);
	}
	
	@GetMapping("/user/allcomment/{id}")
	public ResponseEntity<List<Comment>> getAllComment(@PathVariable("id") Integer postId) throws UserException, PostException{
		List<Comment> comments = userService.getAllComments(postId);
		return new ResponseEntity<List<Comment>>(comments,HttpStatus.OK);
	}
	
	@DeleteMapping("/user/deletepost/{id}")
	public ResponseEntity<String> deletePost(@PathVariable("id") Integer postId) throws UserException, PostException{
		String res  = userService.deletePost(postId);
		return new ResponseEntity<String>(res,HttpStatus.OK);
	}
}
