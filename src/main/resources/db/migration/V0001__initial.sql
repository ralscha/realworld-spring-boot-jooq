create table app_user (
  id bigint not null auto_increment,
  username varchar(255),
  password varchar(255),
  email varchar(255),
  bio text,
  image varchar(511),
  primary key(id),
  unique(username),
  unique(email)
);

create table article (
  id bigint not null auto_increment,
  user_id bigint,
  slug varchar(255),
  title varchar(255),
  description text,
  body text,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  primary key(id),
  unique(slug),
  foreign key (user_id) references app_user(id) ON DELETE CASCADE
);

create table article_favorite (
  article_id bigint not null,
  user_id bigint not null,
  primary key(article_id, user_id),
  foreign key (article_id) references article(id) ON DELETE CASCADE,
  foreign key (user_id) references app_user(id) ON DELETE CASCADE
);

create table follow (
  user_id bigint not null,
  follow_id bigint not null,
  primary key(user_id, follow_id),  
  foreign key (user_id) references app_user(id) ON DELETE CASCADE,
  foreign key (follow_id) references app_user(id) ON DELETE CASCADE
);

create table tag (
  id bigint not null auto_increment,
  name varchar(255),
  primary key(id)
);

create table article_tag (
  article_id bigint not null,
  tag_id bigint not null,
  primary key(article_id, tag_id),  
  foreign key (article_id) references article(id) ON DELETE CASCADE,
  foreign key (tag_id) references tag(id) ON DELETE CASCADE  
);

create table comment (
  id bigint not null auto_increment,
  body text,
  article_id bigint not null,
  user_id bigint not null,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  primary key(id),
  foreign key (user_id) references app_user(id) ON DELETE CASCADE,
  foreign key (article_id) references article(id) ON DELETE CASCADE
);
