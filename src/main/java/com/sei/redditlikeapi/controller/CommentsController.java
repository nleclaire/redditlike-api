package com.sei.redditlikeapi.controller;

import com.sei.redditlikeapi.model.Comment;
import com.sei.redditlikeapi.service.CommentService;
import com.sei.redditlikeapi.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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


}
