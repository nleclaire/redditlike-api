package com.sei.redditlikeapi.controller;

import com.sei.redditlikeapi.model.Comment;
import com.sei.redditlikeapi.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/topics/{topicId}/articles/{articleId}")
public class CommentsController {
    @Autowired
    private CommentService commentService;

    @GetMapping("/comments")
    public List<Comment> getComments(@PathVariable Long topicId, @PathVariable Long articleId){
        return commentService.getComments();
    }

    @PostMapping("/comments")
    public Comment createComment(@PathVariable Long topicId, @PathVariable Long articleId, @RequestBody Comment commentObject){
        return commentService.createComment(topicId, articleId, commentObject);
    }

    @PutMapping("/comments/{commentId}")
    public Comment updateComment(@PathVariable Long topicId, @PathVariable Long articleId, @PathVariable Long commentId, @RequestBody Comment commentObject){
        return commentService.updateComment(topicId, articleId, commentId, commentObject);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<HashMap> deleteComment(@PathVariable Long topicId, @PathVariable Long articleId, @PathVariable Long commentId){
        commentService.deleteComment(topicId, articleId, commentId);
        HashMap response = new HashMap();
        response.put("Response", "Comment with id " + commentId + " has been deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
