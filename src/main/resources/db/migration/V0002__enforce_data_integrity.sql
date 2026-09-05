-- Consolidate tags created by older versions before enforcing uniqueness.
-- [jooq ignore start]
merge into article_tag (article_id, tag_id) key (article_id, tag_id)
select distinct article_tag.article_id, canonical.id
from article_tag
join tag original on original.id = article_tag.tag_id
join tag canonical on canonical.name = original.name
where canonical.id = (select min(candidate.id) from tag candidate where candidate.name = original.name);

delete from article_tag
where tag_id not in (select min(id) from tag group by name);

delete from tag
where id not in (select min(id) from tag group by name);

delete from tag
where id not in (select tag_id from article_tag);

delete from follow where user_id = follow_id;
-- [jooq ignore stop]

alter table app_user alter column username set not null;
alter table app_user alter column password set not null;
alter table app_user alter column email set not null;

alter table article alter column user_id set not null;
alter table article alter column slug set not null;
alter table article alter column title set not null;
alter table article alter column description set not null;
alter table article alter column body set not null;

alter table tag alter column name set not null;
alter table tag add constraint tag_name_unique unique (name);

alter table comment alter column body set not null;

alter table follow add constraint follow_not_self check (user_id <> follow_id);
