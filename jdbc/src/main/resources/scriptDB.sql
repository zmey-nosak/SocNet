--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.0
-- Dumped by pg_dump version 9.6.0

-- Started on 2017-02-12 11:10:34

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE postgres;
--
-- TOC entry 2293 (class 1262 OID 12401)
-- Name: postgres; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE postgres WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'Russian_Russia.1251' LC_CTYPE = 'Russian_Russia.1251';


ALTER DATABASE postgres OWNER TO postgres;

\connect postgres

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 2294 (class 1262 OID 12401)
-- Dependencies: 2293
-- Name: postgres; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON DATABASE postgres IS 'default administrative connection database';


--
-- TOC entry 2 (class 3079 OID 12387)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2296 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


--
-- TOC entry 1 (class 3079 OID 16384)
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- TOC entry 2297 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


--
-- TOC entry 3 (class 3079 OID 24591)
-- Name: lo; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS lo WITH SCHEMA public;


--
-- TOC entry 2298 (class 0 OID 0)
-- Dependencies: 3
-- Name: EXTENSION lo; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION lo IS 'Large Object maintenance';


SET search_path = public, pg_catalog;

--
-- TOC entry 234 (class 1255 OID 24794)
-- Name: f_is_exist_email(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION f_is_exist_email(p_email character varying) RETURNS boolean
LANGUAGE plpgsql
AS $$
declare
  cnt INTEGER:=0;
begin
  SELECT COUNT(1) INTO cnt FROM USERS u WHERE u.email=p_email;
  IF cnt>0 THEN
    RETURN TRUE;
  ELSE
    RETURN FALSE;
  END IF;
end;
$$;


ALTER FUNCTION public.f_is_exist_email(p_email character varying) OWNER TO postgres;

--
-- TOC entry 241 (class 1255 OID 57480)
-- Name: f_register_user(character varying, character varying, character varying, character varying, date, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION f_register_user(p_f_name character varying, p_i_name character varying, p_email character varying, p_password character varying, p_dob date, p_photo_src character varying) RETURNS integer
LANGUAGE plpgsql
AS $$
declare
  p_user_id INTEGER;
begin
  INSERT INTO USERS (email,password,f_name,i_name,dob,photo_src)
  VALUES(p_email,p_password,p_f_name,p_i_name,p_dob,p_photo_src);

  select currval(pg_get_serial_sequence('USERS', 'user_id')) into p_user_id;

  INSERT INTO user_roles(email,role_name,user_id)
  VALUES (p_email,'plain_user',p_user_id);
  RETURN p_user_id;
end;
$$;


ALTER FUNCTION public.f_register_user(p_f_name character varying, p_i_name character varying, p_email character varying, p_password character varying, p_dob date, p_photo_src character varying) OWNER TO postgres;

--
-- TOC entry 233 (class 1255 OID 32780)
-- Name: get_communication_id(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION get_communication_id(user_id_f integer, user_id_s integer) RETURNS integer
LANGUAGE plpgsql
AS $$
declare
  comm_id INTEGER:=0;
  tmp INTEGER:=0;
begin
  SELECT COUNT(1) INTO tmp FROM USER_COMMUNICATIONS;
  IF tmp=0 THEN return 1;
  ELSE
    SELECT (MAX(communication_id)+1) INTO tmp
    FROM USER_COMMUNICATIONS;

    SELECT uc.communication_id INTO comm_id
    FROM USER_COMMUNICATIONS uc
      join user_communications uc2 ON uc.communication_id=uc2.communication_id
                                      AND uc.user_id=user_id_f and uc2.user_id=user_id_s;
    RETURN CASE WHEN comm_id IS NULL THEN tmp ELSE comm_id END;
  END IF;

  EXCEPTION WHEN OTHERS THEN RETURN tmp;
END;
$$;


ALTER FUNCTION public.get_communication_id(user_id_f integer, user_id_s integer) OWNER TO postgres;

--
-- TOC entry 239 (class 1255 OID 40960)
-- Name: get_communications(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION get_communications(p_user_id integer) RETURNS refcursor
LANGUAGE plpgsql
AS $$
declare
  _communications  refcursor := '_communications';
begin
  OPEN _communications FOR
  SELECT uc.communication_id,
    last_mess_detail.message,
    last_mess_detail.user_from,
    all_users_detail.f_name,
    all_users_detail.i_name,
    all_users_detail.photo_src,
    last_corr_mess.active,
    last_mess_detail.date,
    own.photo_src own_photo_src,
    uc.user_id as partner
  FROM USER_COMMUNICATIONS uc
    JOIN USERS all_users_detail ON all_users_detail.user_id=uc.user_id
    JOIN (SELECT MAX(date) date, MAX(active) active, correspondence_id
          FROM MESSAGES
          GROUP BY correspondence_id) last_corr_mess ON last_corr_mess.correspondence_id=uc.communication_id
    JOIN MESSAGES last_mess_detail ON last_mess_detail.date=last_corr_mess.date
                                      AND last_mess_detail.correspondence_id=last_corr_mess.correspondence_id
    LEFT JOIN USERS own ON own.user_id= CASE WHEN last_mess_detail.user_from=p_user_id THEN p_user_id ELSE NULL END
  WHERE uc.communication_id IN (SELECT communication_id FROM USER_COMMUNICATIONS WHERE user_id=p_user_id) AND uc.user_id!=p_user_id
  ORDER BY  last_mess_detail.date DESC;
  RETURN _communications;
end;
$$;


ALTER FUNCTION public.get_communications(p_user_id integer) OWNER TO postgres;

--
-- TOC entry 235 (class 1255 OID 32801)
-- Name: get_messages(integer, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION get_messages(p_user_id integer, p_communication_id integer) RETURNS refcursor
LANGUAGE plpgsql
AS $$
declare
  _messages  refcursor := '_messages';
begin
  OPEN _messages FOR
  SELECT us.f_name,
    us.i_name,
    '' photo,
    '' photo_src,
    us.user_id,
    mess.message,
    mess.date,
    mess.active,
    0 group_num,
    mess.id
  FROM MESSAGES mess
    JOIN USERS us ON us.user_id=CASE WHEN mess.user_from!=p_user_id THEN mess.user_from ELSE p_user_id END
                     AND p_communication_id IN (SELECT communication_id FROM USER_COMMUNICATIONS uc WHERE uc.user_id=p_user_id)
  where mess.correspondence_id=p_communication_id;
  RETURN _messages;
end;
$$;


ALTER FUNCTION public.get_messages(p_user_id integer, p_communication_id integer) OWNER TO postgres;

--
-- TOC entry 231 (class 1255 OID 24737)
-- Name: get_user_info(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION get_user_info(p_user_id integer) RETURNS SETOF refcursor
LANGUAGE plpgsql
AS $$
declare

  _owner  refcursor := '_owner';
  _friends  refcursor := '_friends';
  _user_books refcursor := '_user_books';
begin
  OPEN _friends FOR
  SELECT  us.user_id,
    us.f_name,
    us.i_name,
    us.photo_src,
    us.email,
    us.dob
  FROM friendship fr
    JOIN users us ON us.user_id=fr.child_user_id
  WHERE fr.parent_user_id=p_user_id;
  RETURN NEXT _friends;

  OPEN _user_books FOR
  SELECT b.book_id,
    b.book_name,
    b.image_src
  FROM user_books ub JOIN books b ON ub.book_id=b.book_id
  WHERE ub.user_id=p_user_id;
  RETURN NEXT _user_books;

  OPEN _owner FOR
  SELECT  us.user_id,
    us.f_name,
    us.i_name,
    us.dob,
    us.photo_src,
    us.email
  FROM USERS us where us.user_id=p_user_id;
  RETURN NEXT _owner;

end;
$$;


ALTER FUNCTION public.get_user_info(p_user_id integer) OWNER TO postgres;

--
-- TOC entry 240 (class 1255 OID 57474)
-- Name: put_message(integer, integer, character varying, timestamp without time zone); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION put_message(user_from integer, user_to integer, message character varying, p_date timestamp without time zone) RETURNS integer
LANGUAGE plpgsql
AS $$
declare
  comm_id INTEGER;
  cnt INTEGER;
  mess_id INTEGER;
begin
  SELECT get_communication_id(user_to, user_from) INTO comm_id;
  INSERT INTO MESSAGES VALUES(comm_id,user_from,user_to,p_date,message,1)returning id into mess_id;

  SELECT COUNT(communication_id)
  INTO cnt
  FROM USER_COMMUNICATIONS
  WHERE communication_id=comm_id;

  IF cnt=0 THEN
    INSERT INTO USER_COMMUNICATIONS VALUES(user_to,comm_id);
    INSERT INTO USER_COMMUNICATIONS VALUES(user_from,comm_id);
  END IF;
  return mess_id;
end;
$$;


ALTER FUNCTION public.put_message(user_from integer, user_to integer, message character varying, p_date timestamp without time zone) OWNER TO postgres;

--
-- TOC entry 236 (class 1255 OID 57472)
-- Name: trigger_friend_req_aft_upd(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION trigger_friend_req_aft_upd() RETURNS trigger
LANGUAGE plpgsql
AS $$
DECLARE cnt integer;
BEGIN
  IF NEW.active=0 THEN
    INSERT INTO FRIENDSHIP(parent_user_id,child_user_id)
    VALUES(NEW.user_id_req,NEW.user_id_resp);

    DELETE FROM FRIEND_REQUESTS
    WHERE request_id=NEW.request_id;
  END IF;
  return NEW;
END;$$;


ALTER FUNCTION public.trigger_friend_req_aft_upd() OWNER TO postgres;

--
-- TOC entry 230 (class 1255 OID 24729)
-- Name: trigger_friendship_after_del(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION trigger_friendship_after_del() RETURNS trigger
LANGUAGE plpgsql
AS $$
DECLARE cnt integer;
BEGIN
  SELECT count(1) INTO cnt
  FROM FRIENDSHIP
  WHERE parent_user_id=OLD.child_user_id
        AND child_user_id=OLD.parent_user_id;

  IF cnt>0 THEN
    DELETE FROM FRIENDSHIP
    WHERE parent_user_id=OLD.child_user_id AND child_user_id=OLD.parent_user_id;
  END IF;
  return NEW;
END;
$$;


ALTER FUNCTION public.trigger_friendship_after_del() OWNER TO postgres;

--
-- TOC entry 232 (class 1255 OID 24732)
-- Name: trigger_friendship_after_ins(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION trigger_friendship_after_ins() RETURNS trigger
LANGUAGE plpgsql
AS $$
DECLARE cnt integer;
BEGIN
  SELECT count(1) INTO cnt
  FROM FRIENDSHIP
  WHERE parent_user_id=NEW.child_user_id
        AND child_user_id=NEW.parent_user_id;

  IF cnt=0 THEN
    INSERT INTO FRIENDSHIP(parent_user_id,child_user_id,active)
    VALUES(NEW.child_user_id,NEW.parent_user_id,0);
  END IF;
  return NEW;
END;$$;


ALTER FUNCTION public.trigger_friendship_after_ins() OWNER TO postgres;

--
-- TOC entry 238 (class 1255 OID 57448)
-- Name: trigger_friendship_after_upd(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION trigger_friendship_after_upd() RETURNS trigger
LANGUAGE plpgsql
AS $$
DECLARE cnt integer;
BEGIN
  IF NEW.active=1 THEN
    SELECT COUNT(1) INTO cnt
    FROM FRIENDSHIP f
    WHERE f.parent_user_id=NEW.child_user_id
          AND f.child_user_id=NEW.parent_user_id
          AND f.active=1;

    IF cnt=0 THEN
      UPDATE FRIENDSHIP
      SET active=1
      WHERE parent_user_id=NEW.child_user_id
            AND child_user_id=NEW.parent_user_id;
    END IF;
  END IF;
  return NEW;
END;
$$;


ALTER FUNCTION public.trigger_friendship_after_upd() OWNER TO postgres;

--
-- TOC entry 237 (class 1255 OID 49163)
-- Name: update_messages(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION update_messages(stm character varying) RETURNS boolean
LANGUAGE plpgsql
AS $$
begin
  SELECT RTRIM(stm,',') into stm;
  EXECUTE 'UPDATE MESSAGES SET active=0 WHERE id IN ('||stm||')';
  return true;
end;
$$;


ALTER FUNCTION public.update_messages(stm character varying) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 193 (class 1259 OID 16489)
-- Name: authors; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE authors (
  author_id integer NOT NULL,
  f_name character varying(50),
  i_name character varying(50),
  o_name character varying(50),
  dob date,
  country_id integer
);


ALTER TABLE authors OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 16487)
-- Name: authors_author_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE authors_author_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE authors_author_id_seq OWNER TO postgres;

--
-- TOC entry 2303 (class 0 OID 0)
-- Dependencies: 192
-- Name: authors_author_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE authors_author_id_seq OWNED BY authors.author_id;


--
-- TOC entry 203 (class 1259 OID 16557)
-- Name: book_shops; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE book_shops (
  shop_id integer NOT NULL,
  shop_name character varying(100),
  urls character varying(100)
);


ALTER TABLE book_shops OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 16555)
-- Name: book_shops_shop_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE book_shops_shop_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE book_shops_shop_id_seq OWNER TO postgres;

--
-- TOC entry 2305 (class 0 OID 0)
-- Dependencies: 202
-- Name: book_shops_shop_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE book_shops_shop_id_seq OWNED BY book_shops.shop_id;


--
-- TOC entry 195 (class 1259 OID 16502)
-- Name: books; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE books (
  book_id integer NOT NULL,
  book_name character varying(200),
  pages_count integer,
  author_id integer,
  year_name integer,
  image_src character varying,
  genre_id integer
);


ALTER TABLE books OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 16500)
-- Name: books_book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE books_book_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE books_book_id_seq OWNER TO postgres;

--
-- TOC entry 2307 (class 0 OID 0)
-- Dependencies: 194
-- Name: books_book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE books_book_id_seq OWNED BY books.book_id;


--
-- TOC entry 207 (class 1259 OID 16585)
-- Name: books_genres; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE books_genres (
  book_id integer NOT NULL,
  genre_id integer NOT NULL
);


ALTER TABLE books_genres OWNER TO postgres;

--
-- TOC entry 199 (class 1259 OID 16536)
-- Name: cities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cities (
  city_id integer NOT NULL,
  city_name character varying(100)
);


ALTER TABLE cities OWNER TO postgres;

--
-- TOC entry 198 (class 1259 OID 16534)
-- Name: cities_city_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE cities_city_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE cities_city_id_seq OWNER TO postgres;

--
-- TOC entry 2309 (class 0 OID 0)
-- Dependencies: 198
-- Name: cities_city_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE cities_city_id_seq OWNED BY cities.city_id;


--
-- TOC entry 191 (class 1259 OID 16481)
-- Name: countries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE countries (
  country_id integer NOT NULL,
  country_name character varying(100)
);


ALTER TABLE countries OWNER TO postgres;

--
-- TOC entry 190 (class 1259 OID 16479)
-- Name: countries_country_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE countries_country_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE countries_country_id_seq OWNER TO postgres;

--
-- TOC entry 2311 (class 0 OID 0)
-- Dependencies: 190
-- Name: countries_country_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE countries_country_id_seq OWNED BY countries.country_id;


--
-- TOC entry 215 (class 1259 OID 57466)
-- Name: friend_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE friend_requests (
  request_id integer NOT NULL,
  user_id_req integer NOT NULL,
  user_id_resp integer NOT NULL,
  active integer
);


ALTER TABLE friend_requests OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 57464)
-- Name: friend_requests_request_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE friend_requests_request_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE friend_requests_request_id_seq OWNER TO postgres;

--
-- TOC entry 2313 (class 0 OID 0)
-- Dependencies: 214
-- Name: friend_requests_request_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE friend_requests_request_id_seq OWNED BY friend_requests.request_id;


--
-- TOC entry 211 (class 1259 OID 24711)
-- Name: friendship; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE friendship (
  friendship_id integer NOT NULL,
  parent_user_id integer,
  child_user_id integer,
  active integer
);


ALTER TABLE friendship OWNER TO postgres;

--
-- TOC entry 210 (class 1259 OID 24709)
-- Name: friendship_friendship_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE friendship_friendship_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE friendship_friendship_id_seq OWNER TO postgres;

--
-- TOC entry 2316 (class 0 OID 0)
-- Dependencies: 210
-- Name: friendship_friendship_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE friendship_friendship_id_seq OWNED BY friendship.friendship_id;


--
-- TOC entry 206 (class 1259 OID 16574)
-- Name: genres; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE genres (
  genre_id integer NOT NULL,
  genre_name character varying(50)
);


ALTER TABLE genres OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 16572)
-- Name: genres_genre_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE genres_genre_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE genres_genre_id_seq OWNER TO postgres;

--
-- TOC entry 2319 (class 0 OID 0)
-- Dependencies: 205
-- Name: genres_genre_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE genres_genre_id_seq OWNED BY genres.genre_id;


--
-- TOC entry 201 (class 1259 OID 16544)
-- Name: libraries; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE libraries (
  library_id integer NOT NULL,
  library_name character varying(300),
  city_id integer
);


ALTER TABLE libraries OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 16542)
-- Name: libraries_library_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE libraries_library_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE libraries_library_id_seq OWNER TO postgres;

--
-- TOC entry 2321 (class 0 OID 0)
-- Dependencies: 200
-- Name: libraries_library_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE libraries_library_id_seq OWNED BY libraries.library_id;


--
-- TOC entry 189 (class 1259 OID 16476)
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE messages (
  correspondence_id integer NOT NULL,
  user_from integer,
  user_to integer,
  date timestamp without time zone,
  message character varying(5000),
  active integer,
  id integer NOT NULL
);


ALTER TABLE messages OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 32788)
-- Name: messages_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE messages_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE messages_id_seq OWNER TO postgres;

--
-- TOC entry 2323 (class 0 OID 0)
-- Dependencies: 213
-- Name: messages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE messages_id_seq OWNED BY messages.id;


--
-- TOC entry 197 (class 1259 OID 16515)
-- Name: reviews; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE reviews (
  review_id integer NOT NULL,
  user_id integer,
  book_id integer,
  review character varying(2000),
  date timestamp without time zone
);


ALTER TABLE reviews OWNER TO postgres;

--
-- TOC entry 196 (class 1259 OID 16513)
-- Name: reviews_review_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE reviews_review_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE reviews_review_id_seq OWNER TO postgres;

--
-- TOC entry 2326 (class 0 OID 0)
-- Dependencies: 196
-- Name: reviews_review_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE reviews_review_id_seq OWNED BY reviews.review_id;


--
-- TOC entry 209 (class 1259 OID 24596)
-- Name: user_books; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE user_books (
  user_id integer NOT NULL,
  book_id integer NOT NULL
);


ALTER TABLE user_books OWNER TO postgres;

--
-- TOC entry 212 (class 1259 OID 32768)
-- Name: user_communications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE user_communications (
  user_id integer NOT NULL,
  communication_id integer NOT NULL
);


ALTER TABLE user_communications OWNER TO postgres;

--
-- TOC entry 208 (class 1259 OID 24581)
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE user_roles (
  email character varying(100) NOT NULL,
  role_name character varying(15) NOT NULL,
  user_id integer
);


ALTER TABLE user_roles OWNER TO postgres;

--
-- TOC entry 188 (class 1259 OID 16450)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
  user_id integer NOT NULL,
  email character varying(100) NOT NULL,
  password character varying(100) NOT NULL,
  f_name character varying(50),
  i_name character varying(50),
  o_name character varying(50),
  dob date,
  photo bytea,
  photo_src character varying(100)
);


ALTER TABLE users OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 16448)
-- Name: users_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_user_id_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;


ALTER TABLE users_user_id_seq OWNER TO postgres;

--
-- TOC entry 2332 (class 0 OID 0)
-- Dependencies: 187
-- Name: users_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_user_id_seq OWNED BY users.user_id;


--
-- TOC entry 204 (class 1259 OID 16563)
-- Name: v_authors; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW v_authors AS
  SELECT au.author_id,
    au.f_name,
    au.i_name,
    CASE
    WHEN (au.o_name IS NULL) THEN ''::character varying
    ELSE au.o_name
    END AS o_name,
    au.dob,
    country.country_name,
    books.cnt_book
  FROM ((authors au
    LEFT JOIN countries country ON ((au.country_id = country.country_id)))
    LEFT JOIN ( SELECT b.author_id,
                  count(1) AS cnt_book
                FROM books b
                GROUP BY b.author_id) books ON ((books.author_id = au.author_id)));


ALTER TABLE v_authors OWNER TO postgres;

--
-- TOC entry 2111 (class 2604 OID 16492)
-- Name: authors author_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY authors ALTER COLUMN author_id SET DEFAULT nextval('authors_author_id_seq'::regclass);


--
-- TOC entry 2116 (class 2604 OID 16560)
-- Name: book_shops shop_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY book_shops ALTER COLUMN shop_id SET DEFAULT nextval('book_shops_shop_id_seq'::regclass);


--
-- TOC entry 2112 (class 2604 OID 16505)
-- Name: books book_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY books ALTER COLUMN book_id SET DEFAULT nextval('books_book_id_seq'::regclass);


--
-- TOC entry 2114 (class 2604 OID 16539)
-- Name: cities city_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cities ALTER COLUMN city_id SET DEFAULT nextval('cities_city_id_seq'::regclass);


--
-- TOC entry 2110 (class 2604 OID 16484)
-- Name: countries country_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY countries ALTER COLUMN country_id SET DEFAULT nextval('countries_country_id_seq'::regclass);


--
-- TOC entry 2119 (class 2604 OID 57469)
-- Name: friend_requests request_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY friend_requests ALTER COLUMN request_id SET DEFAULT nextval('friend_requests_request_id_seq'::regclass);


--
-- TOC entry 2118 (class 2604 OID 24714)
-- Name: friendship friendship_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY friendship ALTER COLUMN friendship_id SET DEFAULT nextval('friendship_friendship_id_seq'::regclass);


--
-- TOC entry 2117 (class 2604 OID 16577)
-- Name: genres genre_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY genres ALTER COLUMN genre_id SET DEFAULT nextval('genres_genre_id_seq'::regclass);


--
-- TOC entry 2115 (class 2604 OID 16547)
-- Name: libraries library_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY libraries ALTER COLUMN library_id SET DEFAULT nextval('libraries_library_id_seq'::regclass);


--
-- TOC entry 2109 (class 2604 OID 32790)
-- Name: messages id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY messages ALTER COLUMN id SET DEFAULT nextval('messages_id_seq'::regclass);


--
-- TOC entry 2113 (class 2604 OID 16518)
-- Name: reviews review_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY reviews ALTER COLUMN review_id SET DEFAULT nextval('reviews_review_id_seq'::regclass);


--
-- TOC entry 2108 (class 2604 OID 16453)
-- Name: users user_id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN user_id SET DEFAULT nextval('users_user_id_seq'::regclass);


--
-- TOC entry 2127 (class 2606 OID 16494)
-- Name: authors authors_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY authors
  ADD CONSTRAINT authors_pkey PRIMARY KEY (author_id);


--
-- TOC entry 2137 (class 2606 OID 16562)
-- Name: book_shops book_shops_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY book_shops
  ADD CONSTRAINT book_shops_pkey PRIMARY KEY (shop_id);


--
-- TOC entry 2141 (class 2606 OID 16589)
-- Name: books_genres books_genres_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY books_genres
  ADD CONSTRAINT books_genres_pkey PRIMARY KEY (book_id, genre_id);


--
-- TOC entry 2129 (class 2606 OID 16507)
-- Name: books books_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY books
  ADD CONSTRAINT books_pkey PRIMARY KEY (book_id);


--
-- TOC entry 2133 (class 2606 OID 16541)
-- Name: cities cities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cities
  ADD CONSTRAINT cities_pkey PRIMARY KEY (city_id);


--
-- TOC entry 2125 (class 2606 OID 16486)
-- Name: countries countries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY countries
  ADD CONSTRAINT countries_pkey PRIMARY KEY (country_id);


--
-- TOC entry 2151 (class 2606 OID 57471)
-- Name: friend_requests friend_requests_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY friend_requests
  ADD CONSTRAINT friend_requests_pkey PRIMARY KEY (user_id_req, user_id_resp);


--
-- TOC entry 2147 (class 2606 OID 24716)
-- Name: friendship friendship_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY friendship
  ADD CONSTRAINT friendship_pkey PRIMARY KEY (friendship_id);


--
-- TOC entry 2139 (class 2606 OID 16579)
-- Name: genres genres_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY genres
  ADD CONSTRAINT genres_pkey PRIMARY KEY (genre_id);


--
-- TOC entry 2135 (class 2606 OID 16549)
-- Name: libraries libraries_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY libraries
  ADD CONSTRAINT libraries_pkey PRIMARY KEY (library_id);


--
-- TOC entry 2123 (class 2606 OID 32798)
-- Name: messages message_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY messages
  ADD CONSTRAINT message_pk PRIMARY KEY (id);


--
-- TOC entry 2131 (class 2606 OID 16523)
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY reviews
  ADD CONSTRAINT reviews_pkey PRIMARY KEY (review_id);


--
-- TOC entry 2145 (class 2606 OID 24610)
-- Name: user_books user_books_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_books
  ADD CONSTRAINT user_books_pk PRIMARY KEY (user_id, book_id);


--
-- TOC entry 2149 (class 2606 OID 32772)
-- Name: user_communications user_communications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_communications
  ADD CONSTRAINT user_communications_pkey PRIMARY KEY (user_id, communication_id);


--
-- TOC entry 2143 (class 2606 OID 24587)
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_roles
  ADD CONSTRAINT user_roles_pkey PRIMARY KEY (email, role_name);


--
-- TOC entry 2121 (class 2606 OID 16455)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
  ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 2170 (class 2620 OID 57473)
-- Name: friend_requests trigger_friend_req_aft_upd; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_friend_req_aft_upd AFTER UPDATE ON friend_requests FOR EACH ROW EXECUTE PROCEDURE trigger_friend_req_aft_upd();


--
-- TOC entry 2167 (class 2620 OID 24730)
-- Name: friendship trigger_friendship_del_after; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_friendship_del_after AFTER DELETE ON friendship FOR EACH ROW EXECUTE PROCEDURE trigger_friendship_after_del();


--
-- TOC entry 2168 (class 2620 OID 24733)
-- Name: friendship trigger_friendship_ins_after; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_friendship_ins_after AFTER INSERT ON friendship FOR EACH ROW EXECUTE PROCEDURE trigger_friendship_after_ins();


--
-- TOC entry 2169 (class 2620 OID 57449)
-- Name: friendship trigger_friendship_upd_after; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER trigger_friendship_upd_after AFTER UPDATE ON friendship FOR EACH ROW EXECUTE PROCEDURE trigger_friendship_after_upd();


--
-- TOC entry 2152 (class 2606 OID 16495)
-- Name: authors authors_countries_country_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY authors
  ADD CONSTRAINT authors_countries_country_id_fk FOREIGN KEY (country_id) REFERENCES countries(country_id);


--
-- TOC entry 2153 (class 2606 OID 16508)
-- Name: books books_authors_author_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY books
  ADD CONSTRAINT books_authors_author_id_fk FOREIGN KEY (author_id) REFERENCES authors(author_id);


--
-- TOC entry 2155 (class 2606 OID 16590)
-- Name: books books_genres_book_id_fr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY books
  ADD CONSTRAINT books_genres_book_id_fr FOREIGN KEY (book_id) REFERENCES books(book_id);


--
-- TOC entry 2159 (class 2606 OID 16600)
-- Name: books_genres books_genres_book_id_fr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY books_genres
  ADD CONSTRAINT books_genres_book_id_fr FOREIGN KEY (book_id) REFERENCES books(book_id);


--
-- TOC entry 2154 (class 2606 OID 16580)
-- Name: books books_genres_genre_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY books
  ADD CONSTRAINT books_genres_genre_id_fk FOREIGN KEY (genre_id) REFERENCES genres(genre_id);


--
-- TOC entry 2160 (class 2606 OID 16605)
-- Name: books_genres books_genres_genre_id_fr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY books_genres
  ADD CONSTRAINT books_genres_genre_id_fr FOREIGN KEY (genre_id) REFERENCES genres(genre_id);


--
-- TOC entry 2165 (class 2606 OID 24722)
-- Name: friendship friendship_child_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY friendship
  ADD CONSTRAINT friendship_child_user_id_fk FOREIGN KEY (child_user_id) REFERENCES users(user_id);


--
-- TOC entry 2164 (class 2606 OID 24717)
-- Name: friendship friendship_parent_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY friendship
  ADD CONSTRAINT friendship_parent_user_id_fk FOREIGN KEY (parent_user_id) REFERENCES users(user_id);


--
-- TOC entry 2158 (class 2606 OID 16550)
-- Name: libraries libraries_cities_city_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY libraries
  ADD CONSTRAINT libraries_cities_city_id_fk FOREIGN KEY (city_id) REFERENCES cities(city_id);


--
-- TOC entry 2156 (class 2606 OID 16524)
-- Name: reviews reviews_books_book_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY reviews
  ADD CONSTRAINT reviews_books_book_id_fk FOREIGN KEY (book_id) REFERENCES books(book_id);


--
-- TOC entry 2157 (class 2606 OID 16529)
-- Name: reviews reviews_users_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY reviews
  ADD CONSTRAINT reviews_users_user_id_fk FOREIGN KEY (user_id) REFERENCES users(user_id);


--
-- TOC entry 2163 (class 2606 OID 24604)
-- Name: user_books user_books_book_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_books
  ADD CONSTRAINT user_books_book_id_fk FOREIGN KEY (book_id) REFERENCES books(book_id);


--
-- TOC entry 2162 (class 2606 OID 24599)
-- Name: user_books user_books_users_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_books
  ADD CONSTRAINT user_books_users_id_fk FOREIGN KEY (user_id) REFERENCES users(user_id);


--
-- TOC entry 2166 (class 2606 OID 32773)
-- Name: user_communications user_communications_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_communications
  ADD CONSTRAINT user_communications_user_id_fk FOREIGN KEY (user_id) REFERENCES users(user_id);


--
-- TOC entry 2161 (class 2606 OID 24811)
-- Name: user_roles user_roles_user_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY user_roles
  ADD CONSTRAINT user_roles_user_id_fk FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE;


--
-- TOC entry 2299 (class 0 OID 0)
-- Dependencies: 234
-- Name: f_is_exist_email(character varying); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION f_is_exist_email(p_email character varying) TO plain_user;


--
-- TOC entry 2300 (class 0 OID 0)
-- Dependencies: 241
-- Name: f_register_user(character varying, character varying, character varying, character varying, date, character varying); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION f_register_user(p_f_name character varying, p_i_name character varying, p_email character varying, p_password character varying, p_dob date, p_photo_src character varying) TO plain_user;


--
-- TOC entry 2301 (class 0 OID 0)
-- Dependencies: 240
-- Name: put_message(integer, integer, character varying, timestamp without time zone); Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON FUNCTION put_message(user_from integer, user_to integer, message character varying, p_date timestamp without time zone) TO plain_user;


--
-- TOC entry 2302 (class 0 OID 0)
-- Dependencies: 193
-- Name: authors; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE authors TO plain_user;


--
-- TOC entry 2304 (class 0 OID 0)
-- Dependencies: 203
-- Name: book_shops; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE book_shops TO plain_user;


--
-- TOC entry 2306 (class 0 OID 0)
-- Dependencies: 195
-- Name: books; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE books TO plain_user;


--
-- TOC entry 2308 (class 0 OID 0)
-- Dependencies: 199
-- Name: cities; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE cities TO plain_user;


--
-- TOC entry 2310 (class 0 OID 0)
-- Dependencies: 191
-- Name: countries; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE countries TO plain_user;


--
-- TOC entry 2312 (class 0 OID 0)
-- Dependencies: 215
-- Name: friend_requests; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE friend_requests TO plain_user;


--
-- TOC entry 2314 (class 0 OID 0)
-- Dependencies: 214
-- Name: friend_requests_request_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE friend_requests_request_id_seq TO plain_user;


--
-- TOC entry 2315 (class 0 OID 0)
-- Dependencies: 211
-- Name: friendship; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE ON TABLE friendship TO plain_user;


--
-- TOC entry 2317 (class 0 OID 0)
-- Dependencies: 210
-- Name: friendship_friendship_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE friendship_friendship_id_seq TO plain_user;


--
-- TOC entry 2318 (class 0 OID 0)
-- Dependencies: 206
-- Name: genres; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE genres TO plain_user;


--
-- TOC entry 2320 (class 0 OID 0)
-- Dependencies: 201
-- Name: libraries; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT ON TABLE libraries TO plain_user;


--
-- TOC entry 2322 (class 0 OID 0)
-- Dependencies: 189
-- Name: messages; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE messages TO plain_user;


--
-- TOC entry 2324 (class 0 OID 0)
-- Dependencies: 213
-- Name: messages_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE messages_id_seq TO plain_user;


--
-- TOC entry 2325 (class 0 OID 0)
-- Dependencies: 197
-- Name: reviews; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE reviews TO plain_user;


--
-- TOC entry 2327 (class 0 OID 0)
-- Dependencies: 196
-- Name: reviews_review_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE reviews_review_id_seq TO plain_user;


--
-- TOC entry 2328 (class 0 OID 0)
-- Dependencies: 209
-- Name: user_books; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE user_books TO plain_user;


--
-- TOC entry 2329 (class 0 OID 0)
-- Dependencies: 212
-- Name: user_communications; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE user_communications TO plain_user;


--
-- TOC entry 2330 (class 0 OID 0)
-- Dependencies: 208
-- Name: user_roles; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE user_roles TO plain_user;


--
-- TOC entry 2331 (class 0 OID 0)
-- Dependencies: 188
-- Name: users; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON TABLE users TO plain_user;


--
-- TOC entry 2333 (class 0 OID 0)
-- Dependencies: 187
-- Name: users_user_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT ALL ON SEQUENCE users_user_id_seq TO plain_user;


-- Completed on 2017-02-12 11:10:35

--
-- PostgreSQL database dump complete
--

