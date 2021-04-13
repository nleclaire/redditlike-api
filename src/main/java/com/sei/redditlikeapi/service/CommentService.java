package com.sei.redditlikeapi.service;

import com.sei.redditlikeapi.exception.InformationForbidden;
import com.sei.redditlikeapi.exception.InformationNotFoundException;
import com.sei.redditlikeapi.model.Comment;
import com.sei.redditlikeapi.model.User;
import com.sei.redditlikeapi.repository.ArticleRepository;
import com.sei.redditlikeapi.repository.CommentRepository;
import com.sei.redditlikeapi.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    private UtilityService utility = new UtilityService();

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private ArticleRepository articleRepository;

//    //ONLY ADMIN can access all comment at the same time
//    public List<Comment> getComments(){
//        User user = utility.getAuthenticatedUser();
//        if (utility.isUserAdmin(user))
//            return commentRepository.findAll();
//        else
//            throw new InformationForbidden("Not an admin");
//    }
//
//    //ONLY ADMIN can access comment through ID within all comments
//    public Comment getComment(Long commentID){
//        utility.errorIfRepositoryElementNotExistById(commentRepository,commentID,"Comment");
//        return commentRepository.findById(commentID).get();
//    }

    //Public USER
    public List<Comment> getArticleComments(Long topicId, Long articleId){
        utility.errorIfRepositoryElementNotExistById(topicRepository,topicId, "Topic");
        utility.errorIfRepositoryElementNotExistById(articleRepository,articleId,"Article");
        if (articleRepository.findById(articleId).get().getTopic().getId() != topicId)
            throw new InformationNotFoundException("Article with ID " + articleId + " is in a different topic");
        return articleRepository.findById(articleId).get().getCommentList();
    }

    //Public USER
    public Comment getArticleComment(Long topicId, Long articleId, Long commentId){
        utility.errorIfRepositoryElementNotExistById(topicRepository,topicId, "Topic");
        utility.errorIfRepositoryElementNotExistById(articleRepository,articleId,"Article");
        utility.errorIfRepositoryElementNotExistById(commentRepository,commentId,"Comment");
        if (commentRepository.findById(commentId).get().getArticle().getId() == articleId)
            return commentRepository.findById(commentId).get();
        else
            throw new InformationNotFoundException("Comment with ID " + commentId + " is in a different article");
    }

    //Authenticated USER
    public Comment createComment(Long topicId, Long articleId, Comment commentObject){
        User currentUser = utility.getAuthenticatedUser();
        utility.errorIfRepositoryElementNotExistById(topicRepository,topicId, "Topic");
        utility.errorIfRepositoryElementNotExistById(articleRepository,articleId,"Article");
        if (articleRepository.findById(articleId).get().getTopic().getId() != topicId)
            throw new InformationNotFoundException("Article with ID " + articleId + " is in a different topic");
        commentObject.setArticle(articleRepository.findById(articleId).get());
        commentObject.setUser(currentUser);
        commentObject.setDateCreated(new Date(System.currentTimeMillis()));
        return commentRepository.save(commentObject);
    }

    //Authenticated USER, only his comment
    public Comment updateComment(Long topicId, Long articleId, Long commentId, Comment commentObject){
        User currentUser = utility.getAuthenticatedUser();
        utility.errorIfRepositoryElementNotExistById(topicRepository,topicId, "Topic");
        utility.errorIfRepositoryElementNotExistById(articleRepository,articleId,"Article");
        utility.errorIfRepositoryElementNotExistById(commentRepository,commentId,"Comment");
        if (articleRepository.findById(articleId).get().getTopic().getId() != topicId)
            throw new InformationNotFoundException("Article with ID " + articleId + " is in a different topic");
        if (commentRepository.findById(commentId).get().getArticle().getId() != articleId)
            throw new InformationNotFoundException("Comment with ID " + commentId + " is in a different article");
        Comment currentComment = commentRepository.findById(commentId).get();
        if (currentComment.getUser().getId() != currentUser.getId())
            throw new InformationNotFoundException("Comment with ID " + commentId + " belongs to a different user");
        currentComment.setTextContent(commentObject.getTextContent());
        return commentRepository.save(currentComment);
    }

    //Authenticated USER, only his comment; ADMIN; User who owns article
    public void deleteComment(Long topicId, Long articleId, Long commentId){
        User currentUser = utility.getAuthenticatedUser();
        utility.errorIfRepositoryElementNotExistById(topicRepository,topicId, "Topic");
        utility.errorIfRepositoryElementNotExistById(articleRepository,articleId,"Article");
        utility.errorIfRepositoryElementNotExistById(commentRepository,commentId,"Comment");
        if (articleRepository.findById(articleId).get().getTopic().getId() != topicId)
            throw new InformationNotFoundException("Article with ID " + articleId + " is in a different topic");
        if (commentRepository.findById(commentId).get().getArticle().getId() != articleId)
            throw new InformationNotFoundException("Comment with ID " + commentId + " is in a different article");
        Comment currentComment = commentRepository.findById(commentId).get();
        if ((currentComment.getUser().getId() == currentUser.getId())|| utility.isUserAdmin(currentUser)
            || (currentUser.getId() == currentComment.getArticle().getUser().getId()))
            commentRepository.deleteById(commentId);
        else
            throw new InformationForbidden("You must be the original poster or an admin to delete this comment!");
    }
}
