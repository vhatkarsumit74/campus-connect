CREATE TYPE role_enum AS ENUM ('STUDENT','ADMIN');
CREATE TYPE exam_enum AS ENUM ('CET','JEE');
CREATE TYPE ticket_status_enum AS ENUM ('OPEN','IN_PROGRESS','RESOLVED');

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email TEXT UNIQUE NOT NULL,
                       password_hash TEXT NOT NULL,
                       first_name TEXT,
                       last_name TEXT,
                       age SMALLINT,
                       marks_12th DECIMAL(5,2),
                       cet_percentile DECIMAL(5,2),
                       jee_percentile DECIMAL(5,2),
                       role role_enum NOT NULL DEFAULT 'STUDENT',
                       verified BOOLEAN NOT NULL DEFAULT false,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE colleges (
                          id BIGSERIAL PRIMARY KEY,
                          name TEXT NOT NULL,
                          city TEXT, state TEXT,
                          fees INT,
                          website TEXT, phone TEXT,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                          updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE programs (
                          id BIGSERIAL PRIMARY KEY,
                          college_id BIGINT NOT NULL REFERENCES colleges(id) ON DELETE CASCADE,
                          name TEXT NOT NULL,
                          degree TEXT NOT NULL,
                          duration_years SMALLINT NOT NULL
);

CREATE TABLE cutoffs (
                         id BIGSERIAL PRIMARY KEY,
                         program_id BIGINT NOT NULL REFERENCES programs(id) ON DELETE CASCADE,
                         exam exam_enum NOT NULL,
                         year SMALLINT NOT NULL,
                         percentile_cutoff DECIMAL(5,2) NOT NULL
);

CREATE TABLE blog_posts (
                            id BIGSERIAL PRIMARY KEY,
                            title TEXT NOT NULL,
                            slug TEXT UNIQUE NOT NULL,
                            summary TEXT,
                            content TEXT NOT NULL,
                            author_name TEXT,
                            created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                            updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE blog_comments (
                               id BIGSERIAL PRIMARY KEY,
                               post_id BIGINT NOT NULL REFERENCES blog_posts(id) ON DELETE CASCADE,
                               user_id BIGINT REFERENCES users(id),
                               author_name TEXT,
                               body TEXT NOT NULL,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE contact_tickets (
                                 id BIGSERIAL PRIMARY KEY,
                                 created_by_user_id BIGINT REFERENCES users(id),
                                 college_id BIGINT REFERENCES colleges(id),
                                 subject TEXT NOT NULL,
                                 description TEXT NOT NULL,
                                 status ticket_status_enum NOT NULL DEFAULT 'OPEN',
                                 created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                                 updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE contact_messages (
                                  id BIGSERIAL PRIMARY KEY,
                                  ticket_id BIGINT NOT NULL REFERENCES contact_tickets(id) ON DELETE CASCADE,
                                  author_user_id BIGINT REFERENCES users(id),
                                  body TEXT NOT NULL,
                                  created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE bookmarks (
                           user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                           college_id BIGINT NOT NULL REFERENCES colleges(id) ON DELETE CASCADE,
                           created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                           PRIMARY KEY (user_id, college_id)
);

CREATE TABLE password_reset_tokens (
                                       id BIGSERIAL PRIMARY KEY,
                                       user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                       token_hash TEXT NOT NULL,
                                       expires_at TIMESTAMPTZ NOT NULL,
                                       used BOOLEAN NOT NULL DEFAULT false,
                                       created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
