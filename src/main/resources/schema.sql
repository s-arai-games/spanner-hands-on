
/* user */
CREATE TABLE user (
  user_id INT64 NOT NULL,
  name STRING(MAX),
) PRIMARY KEY (user_id)

/* user_favorite */
CREATE TABLE user_favorite (
  user_id INT64 NOT NULL,
  favorite_id INT64 NOT NULL,
  favorite_thing STRING(MAX),
) PRIMARY KEY (user_id, favorite_id),
INTERLEAVE IN PARENT user ON DELETE CASCADE