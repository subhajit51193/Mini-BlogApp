package com.blog.spring.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import com.blog.spring.demo.exceptions.PostException;
import com.blog.spring.demo.exceptions.UserException;
import com.blog.spring.demo.models.Comment;
import com.blog.spring.demo.models.Post;
import com.blog.spring.demo.models.User;
import com.blog.spring.demo.payload.response.UserInfoResponse;
import com.blog.spring.demo.repository.CommentRepository;
import com.blog.spring.demo.repository.PostRepository;
import com.blog.spring.demo.repository.UserRepository;
import com.blog.spring.demo.security.jwt.JwtUtils;

import net.bytebuddy.implementation.bind.MethodDelegationBinder.ParameterBinding.Anonymous;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	JwtUtils jwtUtils;
	
	
	@Override
	public String createPost(Post post) throws UserException {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();

		Optional<User> opt = userRepository.findByUsername(currentUsername);
		if (opt.isPresent()) {
			User user = opt.get();
			post.setCreationDate(LocalDateTime.now());
			post.setUser(user);
			user.getPosts().add(post);
			postRepository.save(post);
			userRepository.save(user);
			return "Congrats!! "+authentication.getName()+" Your post has been added";
		}
		else {
			throw new UserException("Please login first to use service!!!");
		}
		
	}

	@Override//have to create another endpoint where all the posts in the app should,be visisble
	public List<Post> getAllPost() throws UserException {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername = authentication.getName();
			Optional<User> opt = userRepository.findByUsername(currentUsername);
			if (opt.isEmpty()) {
				throw new UserException("User not found");
			}
			else {
				User user = opt.get();
				List<Post> posts = user.getPosts();
				return posts;
			}
		}
		throw new UserException("Please login and try again!!!");
	}

	@Override
	public String deletePost(Integer postId) throws UserException, PostException {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername = authentication.getName();
			Optional<User> opt = userRepository.findByUsername(currentUsername);
			if (opt.isEmpty()) {
				throw new UserException("User not found...");
			}
			else {
				User user = opt.get();
				List<Post> posts = user.getPosts();
				for (Post pst : posts) {
					if (pst.getPostId() == postId) {
						postRepository.delete(pst);
						return "Your post has been deleteed";
					}
				}
				throw new PostException("No post found");
			}
		}
		throw new UserException("Please login and try again...");
	}

	@Override
	public String addComment(Comment comment, Integer postId) throws PostException, UserException {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		Optional<User> opt = userRepository.findByUsername(currentUsername);
		if (opt.isPresent()) {
			User user = opt.get();
			Optional<Post> optPost = postRepository.findById(postId);
			if (optPost.isPresent()) {
				Post post = optPost.get();
				comment.setCreationDate(LocalDateTime.now());
				comment.setPost(post);
				comment.setUser(user);
				post.getComments().add(comment);
				commentRepository.save(comment);
				postRepository.save(post);
				userRepository.save(user);
				return "Comment has been added to post";
			}
			else {
				throw new PostException("Post not found!!");
			}
		}
		throw new UserException("Please login and try again!!!...");
	}

	@Override//create this endpoint without authentication as everybody should see all comemnts in any post 
	public List<Comment> getAllComments(Integer postId) throws UserException, PostException {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUsername = authentication.getName();
			Optional<User> opt = userRepository.findByUsername(currentUsername);
			if (opt.isEmpty()) {
				throw new UserException("User not found please try again....");
			}
			else {
				User user = opt.get();
				Optional<Post> optPost = postRepository.findById(postId);
				if (optPost.isEmpty()) {
					throw new PostException("Post not found");
				}
				else {
					Post post = optPost.get();
					List<Comment> comments = post.getComments();
					return comments;
				}
			}
		}
		throw new UserException("Please login first");
	}

	
}
