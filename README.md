# redditlike-api

## An API for sharing articles on different topics of interest.

A RESTful API written in Java made for sharing articles.

### Supported HTTP requests and their path mappings

| HTTP REQUEST | PATH | FUNCTIONALITY |
| ------------ | ---- | ------------- |
| GET | /api/topics | List all topics |
| POST | /api/topics | Create new topics |
| GET | /api/topics/{topicId} | Get a single topic w/ id |
| PUT | /api/topics/{topicId} | Update a single topic w/ id |
| DELETE | /api/topics/{topicId} | Delete a single topic w/ id |
| GET | /api/topics/{topicId}/articles | Get all articles for single topic w/ id |
| POST | /api/topics/{topicId}/articles | Create a new article in the given topic |
| GET | /api/topics/{topicId}/articles/<span style="color:orange">{articleId}</span> | List single article in specified topic by Id |
| PUT | /api/topics/{topicId}/articles/<span style="color:orange">{articleId}</span> | Update single article in specified topic by Id |
| DELETE | /api/topics/{topicId}/articles/<span style="color:orange">{articleId}</span> | Delete single article in specified topic by Id |
| POST | /auth/users/register | Registers a new user |
| POST | /auth/users/login | Logs a user in |

---

# Examples!

Feeling hungry? Let's create a `Lunch` topic to discuss some of your favorite things to eat.

## Registering a User

Before we can get around to creating topics, you'll need to register a user first.

At this point we'll assume that you have your dev environment up and running.
Go ahead and find your way to your friendly neighborhood Postman and open up a new **POST** request.

Make sure you're pointing towards http://localhost:9092/auth/users/register and send the following in the body of the request.
Quickly now! There's lunch to be discussed.

```json
{
	"userName" : "LunchLover",
	"emailAddress" : "Lunchman.leclaire@email.com",
	"password" : "12345"
}
```

Make sure you have selected raw and JSON for your Body.

If you get an error saying "Unsupported Media Type", navigate to 
your Headers section. In the Key section, add a Key field of `Content-Type`
and a Value of `application/json`. 

If you did it right you should see a response like the one below,
stating that you created the object. **Success!** 

```json
{
    "id": 2,
    "userName": "LunchLover",
    "emailAddress": "Lunchman.leclaire@email.com",
    "userProfile": null,
    "topicList": null,
    "articleList": null,
    "commentList": null
}
```

We're now one step closer to discussing how much we love lunch!

---

## Authentication

Now that you're registered, you can log in to receive a JWT token used for authentication.

Change your endpoint to http://localhost:9092/auth/users/login and send the same
content as you did to sign up in the body (again, formatted as JSON).

```json
{
	"userName" : "LunchLover",
	"emailAddress" : "Lunchman.leclaire@email.com",
	"password" : "12345"
}
```

Once you do, you should see a response like so:

```json
{
    "jwt": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJMdW5jaG1hbi5sZWNsYWlyZUBlbWFpbC5jb20iLCJleHAiOjE2MTgyMTA3OTIsImlhdCI6MTYxODE3NDc5Mn0.Ct76ylDVo2L7PGdpOWDvZaHCUZSjQdUMo3y9jVAH-Js"
}
```

Great! Now we can use this to verify to the server that we are who we say we are.

### **Let's talk about lunch**

Finally we're all set up to start talking about lunch. I'm starving!

Point your Postman to http://localhost:9092/api/topics/new and type (or copy/paste) the
information below in the Body section of your request

```json
{
    "title" : "Lunch",
    "description" : "My favorite meal of the day!"
}
```

Hold your horses!! Don't even think about sending it yet. I know you knew this was too good to be true. And it is.

Before we can access this endpoint, we need send along our JWT token so the server can verify that we are
who we say we are.

Back in the Headers section of your request, add a field of type `Authorization`
and in the Value section add `Bearer YOUR_JWT_TOKEN_HERE`

Finally, we're ready to talk about Lunch. Hit Send.

```json
{
    "id": 4,
    "name": "Lunch",
    "description": "My favorite meal of the day!",
    "articleList": null
}
```
## Success!

If you've made it up to this point, you should be able to navigate the rest of the API with no problem.

Happy chatting!


---

### GET /api/topics
- no parameters required

### POST /api/topics
```json
{
  "title" : "Lunch",
  "description" : "A place to talk about lunch"
}
```
Note that an id will automatically be generated for you when you
send a POST request

### GET /api/topics/{topicId}

- need to pass specified topic Id in the URL

### PUT /api/topics/{topicId}
```json
{
  "title" : "Updated title",
  "description" : "Updated description"
}
```

### DELETE /api/topics/{topicId}

- Not necessary to pass a body, just an Id in the URL

### GET /api/topics/{topicId}/articles

- Not necessary to pass a body

### POST /api/topics/{topicId}/articles/{articleId}

```json
{
  "title" : "some title",
  "textContent" : "some text"
}
```
Note that it will automatically generate an Id for you

### GET /api/topics/{topicId}/articles/{articleId}

- Not necessary to have a body

### PUT /api/topics/{topicId}/articles/{articleId}

```json
{
  "title" : "updated title",
  "textContent" : "updated text"
}
```

### DELETE /api/topics/{topicId}/articles/{articleId}

- Delete the specified article

