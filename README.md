# redditlike-api

## An API for sharing articles on different topics of interest.

A RESTful API written in Java made for sharing articles.

### Supported HTTP requests and their path mappings

| HTTP REQUEST | PATH | FUNCTIONALITY |
| ------------ | ---- | ------------- |
| GET | /api/topics | Lists all topics |
| POST | /api/topics | Create new topics |
| GET | /api/topics/<span style="color:orange">{topicId}</span> | Get a single topic w/ id |
| PUT | /api/topics/<span style="color:orange">{topicId}</span> | Update a single topic w/ id |
| DELETE | /api/topics/<span style="color:orange">{topicId}</span> | Delete a single topic w/ id |
| GET | /api/topics/<span style="color:orange">{topicId}</span>/articles | Get all articles for single topic w/ id |
| POST | /api/topics/<span style="color:orange">{topicId}</span>/articles | Create a new article in the given topic |
| GET | /api/topics/<span style="color:orange">{topicId}</span>/articles/<span style="color:orange">{articleId}</span> | List single article in specified topic by Id |
| PUT | /api/topics/<span style="color:orange">{topicId}</span>/articles/<span style="color:orange">{articleId}</span> | Update single article in specified topic by Id |
| DELETE | /api/topics/<span style="color:orange">{topicId}</span>/articles/<span style="color:orange">{articleId}</span> | Delete single article in specified topic by Id |
| POST | /auth/users/register | Registers a new user |
| POST | /auth/users/login | Logs a user in |
