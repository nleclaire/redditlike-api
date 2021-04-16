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

    // PUBLIC USER has access, returns the list of all comments related to the article in a topic
    // Will also check if data is set properly and article is exactly inside the needed topic
    public List<Comment> getArticleComments(Long topicId, Long articleId){
        utility.errorIfRepositoryElementNotExistById(topicRepository,topicId, "Topic");
        utility.errorIfRepositoryElementNotExistById(articleRepository,articleId,"Article");
        if (articleRepository.findById(articleId).get().getTopic().getId() != topicId)
            throw new InformationNotFoundException("Article with ID " + articleId + " is in a different topic");
        return commentRepository.findByParentCommentIsNullAndArticleId(articleId);
    }

    // PUBLIC USER has access, returns a single comment related to the article in a topic
    // Will also check if data is set properly and article is exactly inside the needed topic
    public Comment getArticleComment(Long topicId, Long articleId, Long commentId){
        utility.errorIfRepositoryElementNotExistById(topicRepository,topicId, "Topic");
        utility.errorIfRepositoryElementNotExistById(articleRepository,articleId,"Article");
        utility.errorIfRepositoryElementNotExistById(commentRepository,commentId,"Comment");
        if (commentRepository.findById(commentId).get().getArticle().getId() == articleId)
            return commentRepository.findById(commentId).get();
        else
            throw new InformationNotFoundException("Comment with ID " + commentId + " is in a different article");
    }

    // AUTHENTICATED USER has access, creates new comment in the article within a topic
    // Will also check if data is set properly and article is exactly inside the needed topic
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

    // AUTHENTICATED USER has access to his comment only, updates existing comment of the user
    // Will also check if data is set properly and if comment belongs to a different user
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

    // AUTHENTICATED USER can delete his comment only OR ADMIN can delete all comments,
    // Will also check if data is set properly and if comment belongs to a different user
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

    //Public USER has access, returns the list of all child comments of a particular comment
    //Will also check if data is set properly
    public List<Comment> getChildComments(Long topicId, Long articleId, Long commentId){
        return this.getArticleComment(topicId,articleId,commentId).getChildrenComments();
    }


    // AUTHENTICATED USER has access, creates new child comment (REPLY) to an existing comment
    // Will also check if data is set properly
    public Comment createChildComment(Long topicId, Long articleId,
                                      Long commentId, Comment commentObject) {
        Comment parent = this.getArticleComment(topicId,articleId,commentId);
        commentObject.setParentComment(parent);
        Comment currentNewComment = this.createComment(topicId,articleId,commentObject);
        return commentRepository.save(parent);
    }
}
