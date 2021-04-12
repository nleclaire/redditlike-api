package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationExistException;
import com.sei.redditlikeapi.exception.InformationForbidden;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.Article;
import com.sei.redditlikeapi.model.Comment;
import com.sei.redditlikeapi.model.Topic;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.repository.ArticleRepository;
import com.sei.redditlikeapi.repository.CommentRepository;
import com.sei.redditlikeapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private UtilityService utility = new UtilityService();

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ArticleRepository articleRepository;

    public List<Comment> getComments(){
        return commentRepository.findAll();
    }

    public Comment createComment(Long topicId, Long articleId, Comment commentObject){
        Optional<Topic> topic = topicRepository.findById(topicId);
        Optional<Article> article = articleRepository.findById(articleId);
        if (topic.isPresent() && article.isPresent()){
            commentObject.setArticle(article.get());
            commentObject.setUser(utility.getAuthenticatedUser());
            return commentRepository.save(commentObject);
        } else {
            throw new InformationNotFoundException("Cannot find topic or article");
        }
    }

    public Comment updateComment(Long topicId, Long articleId, Long commentId, Comment commentObject){
        Optional<Topic> topic = topicRepository.findById(topicId);
        Optional<Article> article = articleRepository.findById(articleId);
        Comment comment = commentRepository.findById(commentId).get();

        if (topic.isPresent() && article.isPresent()){
            if (commentRepository.findById(commentId).isPresent()){
                comment.setTextContent(commentObject.getTextContent());
                return commentRepository.save(comment);
            } else {
                throw new InformationNotFoundException("Cannot find topic or article");
            }
        } else {
            throw new InformationNotFoundException("Cannot find topic or article");
        }
    }

    public void deleteComment(Long topicId, Long articleId, Long commentId){
        User user = utility.getAuthenticatedUser();

        if (commentRepository.findById(commentId).isPresent()){
            Comment comment = commentRepository.findById(commentId).get();

            if(utility.isUserAdmin(user) || comment.getUser() == user){
                Optional<Topic> topic = topicRepository.findById(topicId);
                Optional<Article> article = articleRepository.findById(articleId);

                if (topic.isPresent() && article.isPresent()){
                    commentRepository.deleteById(commentId);
                }
            } else {
                throw new InformationForbidden("You must be the original poster or an admin to delete this comment!");
            }
        } else {
            throw new InformationNotFoundException("Comment with id " + commentId + " not found!");
        }

    }

}
