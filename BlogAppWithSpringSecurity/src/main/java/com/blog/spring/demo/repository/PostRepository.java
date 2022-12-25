package com.blog.spring.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.spring.demo.models.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{

}
