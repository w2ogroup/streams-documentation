streams-provider-twitter

Purpose

  Module connects to twitter APIs, collects events, and passes each message downstream.

EndPoints

  * Timeline - supported, tested
  * Sample - supported, tested
  * Firehose - supported, not tested
  * Site - not currently supported

Normalization

  Optionally, module can convert messages to ActivityStreams format

  * Tweets [TwitterJsonTweetActivitySerializer]
  * Retweets [TwitterJsonRetweetActivitySerializer]

[TwitterJsonTweetActivitySerializer]: TwitterJsonTweetActivitySerializer

  TwitterJsonTweetActivitySerializer.class serializes tweets like this:

  ![TwitterJsonTweetActivitySerializer.png](TwitterJsonTweetActivitySerializer.png)

[TwitterJsonRetweetActivitySerializer]: TwitterJsonRetweetActivitySerializer

  TwitterJsonRetweetActivitySerializer.class serializes retweets like this:

  ![TwitterJsonRetweetActivitySerializer.png](TwitterJsonRetweetActivitySerializer.png)