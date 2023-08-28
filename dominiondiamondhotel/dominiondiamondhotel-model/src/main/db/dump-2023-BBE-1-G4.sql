--
-- PostgreSQL database dump
--

-- Dumped from database version 11.16 (Debian 11.16-0+deb10u1)
-- Dumped by pg_dump version 14.2

-- Started on 2023-07-27 08:29:53

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--DROP DATABASE "2023-BBE-1-G4";
--
-- TOC entry 3078 (class 1262 OID 206205)
-- Name: 2023-BBE-1-G4; Type: DATABASE; Schema: -; Owner: -
--

--CREATE DATABASE "2023-BBE-1-G4" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE = 'en_US.UTF-8';


\connect -reuse-previous=on "dbname='2023-BBE-1-G4'"

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 3 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: -
--

--CREATE SCHEMA public;


--
-- TOC entry 3079 (class 0 OID 0)
-- Dependencies: 3
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON SCHEMA public IS 'standard public schema';


SET default_tablespace = '';

--
-- TOC entry 214 (class 1259 OID 230353)
-- Name: allergens; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.allergens (
    id integer NOT NULL,
    name character varying(255) NOT NULL
);


--
-- TOC entry 213 (class 1259 OID 230351)
-- Name: allergens_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.allergens_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3080 (class 0 OID 0)
-- Dependencies: 213
-- Name: allergens_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.allergens_id_seq OWNED BY public.allergens.id;


--
-- TOC entry 205 (class 1259 OID 209997)
-- Name: bookings; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.bookings (
    id integer NOT NULL,
    entry_date date NOT NULL,
    exit_date date NOT NULL,
    hotel_id integer,
    customer_id integer,
    check_in timestamp without time zone,
    room_id integer,
    check_out timestamp without time zone,
    cleaning integer,
    facilities integer,
    pricequality integer,
    comm character varying(255),
    mean numeric(4,2),
    expenses numeric DEFAULT 0 NOT NULL
);


--
-- TOC entry 204 (class 1259 OID 209995)
-- Name: bookings_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.bookings_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3081 (class 0 OID 0)
-- Dependencies: 204
-- Name: bookings_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.bookings_id_seq OWNED BY public.bookings.id;


--
-- TOC entry 222 (class 1259 OID 262630)
-- Name: country_iso; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.country_iso (
    id integer NOT NULL,
    iso character varying(2) NOT NULL,
    name character varying(255) NOT NULL
);


--
-- TOC entry 221 (class 1259 OID 262628)
-- Name: country_iso_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.country_iso_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3082 (class 0 OID 0)
-- Dependencies: 221
-- Name: country_iso_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.country_iso_id_seq OWNED BY public.country_iso.id;


--
-- TOC entry 201 (class 1259 OID 206895)
-- Name: customers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.customers (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    lastname1 character varying(255) NOT NULL,
    lastname2 character varying(255),
    phone character(14) NOT NULL,
    idnumber character(9) NOT NULL,
    mail character varying(255) NOT NULL,
    idtype_id integer NOT NULL
);


--
-- TOC entry 200 (class 1259 OID 206893)
-- Name: customer_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.customer_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3083 (class 0 OID 0)
-- Dependencies: 200
-- Name: customer_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.customer_id_seq OWNED BY public.customers.id;


--
-- TOC entry 211 (class 1259 OID 226762)
-- Name: hist_rooms; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hist_rooms (
    id integer NOT NULL,
    change_date date NOT NULL,
    room_id integer,
    state_id integer
);


--
-- TOC entry 210 (class 1259 OID 226760)
-- Name: hist_rooms_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hist_rooms_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3084 (class 0 OID 0)
-- Dependencies: 210
-- Name: hist_rooms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.hist_rooms_id_seq OWNED BY public.hist_rooms.id;


--
-- TOC entry 197 (class 1259 OID 206663)
-- Name: hotels; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.hotels (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    zip_id integer,
    rating numeric(10,2),
    popularity integer,
    totalrooms integer DEFAULT 0 NOT NULL
);


--
-- TOC entry 196 (class 1259 OID 206661)
-- Name: hotels_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hotels_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3085 (class 0 OID 0)
-- Dependencies: 196
-- Name: hotels_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.hotels_id_seq OWNED BY public.hotels.id;


--
-- TOC entry 207 (class 1259 OID 210122)
-- Name: iddocumenttypes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.iddocumenttypes (
    id integer NOT NULL,
    idtype character varying(8) NOT NULL,
    CONSTRAINT iddocumenttypes_idtype_check CHECK (((idtype)::text = ANY ((ARRAY['PASSPORT'::character varying, 'DNI'::character varying])::text[])))
);


--
-- TOC entry 206 (class 1259 OID 210120)
-- Name: iddocumenttypes_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.iddocumenttypes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3086 (class 0 OID 0)
-- Dependencies: 206
-- Name: iddocumenttypes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.iddocumenttypes_id_seq OWNED BY public.iddocumenttypes.id;


--
-- TOC entry 224 (class 1259 OID 267589)
-- Name: orders; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.orders (
    id integer NOT NULL,
    booking_id integer NOT NULL,
    product_id integer NOT NULL,
    products_list character varying(15),
    checked boolean DEFAULT false
);


--
-- TOC entry 223 (class 1259 OID 267587)
-- Name: orders_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.orders_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3087 (class 0 OID 0)
-- Dependencies: 223
-- Name: orders_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.orders_id_seq OWNED BY public.orders.id;


--
-- TOC entry 209 (class 1259 OID 215674)
-- Name: postalcodes; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.postalcodes (
    id integer NOT NULL,
    zip integer NOT NULL,
    iso_id integer NOT NULL
);


--
-- TOC entry 208 (class 1259 OID 215672)
-- Name: postalcodes_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.postalcodes_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3088 (class 0 OID 0)
-- Dependencies: 208
-- Name: postalcodes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.postalcodes_id_seq OWNED BY public.postalcodes.id;


--
-- TOC entry 216 (class 1259 OID 230361)
-- Name: product_types; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.product_types (
    id integer NOT NULL,
    name character varying(255) NOT NULL
);


--
-- TOC entry 215 (class 1259 OID 230359)
-- Name: product_types_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.product_types_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3089 (class 0 OID 0)
-- Dependencies: 215
-- Name: product_types_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.product_types_id_seq OWNED BY public.product_types.id;


--
-- TOC entry 218 (class 1259 OID 230369)
-- Name: products; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.products (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    producttype_id integer,
    price numeric NOT NULL
);


--
-- TOC entry 220 (class 1259 OID 260697)
-- Name: products_allergens; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.products_allergens (
    id integer NOT NULL,
    product_id integer NOT NULL,
    allergen_id integer NOT NULL
);


--
-- TOC entry 219 (class 1259 OID 260695)
-- Name: products_allergens_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.products_allergens_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3090 (class 0 OID 0)
-- Dependencies: 219
-- Name: products_allergens_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.products_allergens_id_seq OWNED BY public.products_allergens.id;


--
-- TOC entry 217 (class 1259 OID 230367)
-- Name: products_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.products_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3091 (class 0 OID 0)
-- Dependencies: 217
-- Name: products_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;


--
-- TOC entry 199 (class 1259 OID 206823)
-- Name: rooms; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.rooms (
    id integer NOT NULL,
    number integer,
    hotel_id integer,
    state_id integer DEFAULT 1
);


--
-- TOC entry 198 (class 1259 OID 206821)
-- Name: rooms_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rooms_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3092 (class 0 OID 0)
-- Dependencies: 198
-- Name: rooms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rooms_id_seq OWNED BY public.rooms.id;


--
-- TOC entry 203 (class 1259 OID 207373)
-- Name: states; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.states (
    id integer NOT NULL,
    state character varying(20) NOT NULL,
    CONSTRAINT states_state_check CHECK (((state)::text = ANY ((ARRAY['free'::character varying, 'busy'::character varying, 'maintenance'::character varying, 'cleaning'::character varying])::text[])))
);


--
-- TOC entry 202 (class 1259 OID 207371)
-- Name: states_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.states_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3093 (class 0 OID 0)
-- Dependencies: 202
-- Name: states_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.states_id_seq OWNED BY public.states.id;


--
-- TOC entry 212 (class 1259 OID 226784)
-- Name: view_histroom_hotelid; Type: VIEW; Schema: public; Owner: -
--

CREATE VIEW public.view_histroom_hotelid AS
 SELECT hr.id AS hr_id,
    hr.change_date,
    hr.room_id,
    hr.state_id,
    r.hotel_id
   FROM (public.hist_rooms hr
     JOIN public.rooms r ON ((hr.room_id = r.id)));


--
-- TOC entry 2873 (class 2604 OID 230356)
-- Name: allergens id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.allergens ALTER COLUMN id SET DEFAULT nextval('public.allergens_id_seq'::regclass);


--
-- TOC entry 2867 (class 2604 OID 210000)
-- Name: bookings id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bookings ALTER COLUMN id SET DEFAULT nextval('public.bookings_id_seq'::regclass);


--
-- TOC entry 2877 (class 2604 OID 262633)
-- Name: country_iso id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.country_iso ALTER COLUMN id SET DEFAULT nextval('public.country_iso_id_seq'::regclass);


--
-- TOC entry 2864 (class 2604 OID 206898)
-- Name: customers id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customers ALTER COLUMN id SET DEFAULT nextval('public.customer_id_seq'::regclass);


--
-- TOC entry 2872 (class 2604 OID 226765)
-- Name: hist_rooms id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hist_rooms ALTER COLUMN id SET DEFAULT nextval('public.hist_rooms_id_seq'::regclass);


--
-- TOC entry 2860 (class 2604 OID 206666)
-- Name: hotels id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hotels ALTER COLUMN id SET DEFAULT nextval('public.hotels_id_seq'::regclass);


--
-- TOC entry 2869 (class 2604 OID 210125)
-- Name: iddocumenttypes id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iddocumenttypes ALTER COLUMN id SET DEFAULT nextval('public.iddocumenttypes_id_seq'::regclass);


--
-- TOC entry 2878 (class 2604 OID 267592)
-- Name: orders id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders ALTER COLUMN id SET DEFAULT nextval('public.orders_id_seq'::regclass);


--
-- TOC entry 2871 (class 2604 OID 215677)
-- Name: postalcodes id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.postalcodes ALTER COLUMN id SET DEFAULT nextval('public.postalcodes_id_seq'::regclass);


--
-- TOC entry 2874 (class 2604 OID 230364)
-- Name: product_types id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_types ALTER COLUMN id SET DEFAULT nextval('public.product_types_id_seq'::regclass);


--
-- TOC entry 2875 (class 2604 OID 230372)
-- Name: products id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);


--
-- TOC entry 2876 (class 2604 OID 260700)
-- Name: products_allergens id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products_allergens ALTER COLUMN id SET DEFAULT nextval('public.products_allergens_id_seq'::regclass);


--
-- TOC entry 2862 (class 2604 OID 206826)
-- Name: rooms id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rooms ALTER COLUMN id SET DEFAULT nextval('public.rooms_id_seq'::regclass);


--
-- TOC entry 2865 (class 2604 OID 207376)
-- Name: states id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.states ALTER COLUMN id SET DEFAULT nextval('public.states_id_seq'::regclass);


--
-- TOC entry 3062 (class 0 OID 230353)
-- Dependencies: 214
-- Data for Name: allergens; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.allergens VALUES (1, 'Celery');
INSERT INTO public.allergens VALUES (2, 'Gluten');
INSERT INTO public.allergens VALUES (3, 'Crustaceans');
INSERT INTO public.allergens VALUES (4, 'Eggs');
INSERT INTO public.allergens VALUES (5, 'Fish');
INSERT INTO public.allergens VALUES (6, 'Lupin');
INSERT INTO public.allergens VALUES (7, 'Milk');
INSERT INTO public.allergens VALUES (8, 'Mollusc');
INSERT INTO public.allergens VALUES (9, 'Mustard');
INSERT INTO public.allergens VALUES (10, 'Nuts');
INSERT INTO public.allergens VALUES (11, 'Peanuts');
INSERT INTO public.allergens VALUES (12, 'Sesame seeds');
INSERT INTO public.allergens VALUES (13, 'Soya');
INSERT INTO public.allergens VALUES (14, 'Sulphites');
INSERT INTO public.allergens VALUES (15, 'Meat');


--
-- TOC entry 3054 (class 0 OID 209997)
-- Dependencies: 205
-- Data for Name: bookings; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.bookings VALUES (8, '2023-09-15', '2023-09-30', 2, 20, '2023-07-17 13:25:34.373755', 26, '2023-07-17 13:25:38.950856', 7, 7, 7, 'Good price and good service', 7.00, 0);
INSERT INTO public.bookings VALUES (9, '2023-09-15', '2023-09-30', 3, 5, '2023-07-17 13:27:13.817253', 28, '2023-07-17 13:27:18.597033', 7, 7, 7, 'Good´s price ', 7.00, 0);
INSERT INTO public.bookings VALUES (10, '2023-09-15', '2023-09-20', 3, 10, '2023-07-17 13:28:04.658239', 29, '2023-07-17 13:28:08.546717', 5, 5, 5, 'Very expensive', 5.00, 0);
INSERT INTO public.bookings VALUES (11, '2023-08-15', '2023-08-20', 4, 17, '2023-07-17 13:29:50.684694', 31, '2023-07-17 13:29:54.845304', 5, 5, 5, 'Very expensive', 5.00, 0);
INSERT INTO public.bookings VALUES (12, '2023-08-15', '2023-08-28', 4, 4, '2023-07-17 13:30:29.914211', 32, '2023-07-17 13:30:33.750971', 9, 9, 9, 'Amazing service and good´s price', 9.00, 0);
INSERT INTO public.bookings VALUES (23, '2023-11-05', '2023-11-15', 1, 25, '2023-07-26 10:58:02.946729', 4, '2023-07-26 11:04:09.635848', 7, 5, 3, 'Price could be better', 5.00, 0);
INSERT INTO public.bookings VALUES (13, '2023-08-15', '2023-09-05', 4, 8, '2023-07-17 13:33:27.187019', 33, '2023-07-17 13:33:30.236821', 9, 9, 9, 'Amazing service', 9.00, 0);
INSERT INTO public.bookings VALUES (15, '2023-10-15', '2023-11-05', 5, 18, '2023-07-17 13:36:17.252712', 35, '2023-07-17 13:36:20.59089', 9, 9, 9, 'Amazing service', 9.00, 0);
INSERT INTO public.bookings VALUES (16, '2023-11-15', '2023-12-05', 5, 3, '2023-07-17 13:37:48.61406', 36, '2023-07-17 13:37:52.946207', 3, 3, 3, 'Bad service', 3.00, 0);
INSERT INTO public.bookings VALUES (17, '2023-11-15', '2023-12-05', 1, 9, '2023-07-17 13:38:52.920185', 1, '2023-07-17 13:38:58.175369', 3, 3, 3, 'Bad service', 3.00, 0);
INSERT INTO public.bookings VALUES (6, '2023-10-15', '2023-10-23', 2, 8, '2023-07-17 13:19:08.257211', 26, '2023-07-17 13:19:15.470716', 7, 8, 8, 'Good price and pretty', 7.67, 0);
INSERT INTO public.bookings VALUES (7, '2023-08-15', '2023-08-30', 2, 12, '2023-07-17 13:20:54.938345', 27, '2023-07-17 13:21:01.838678', 5, 5, 6, 'It´s ok', 5.33, 0);
INSERT INTO public.bookings VALUES (1, '2023-05-30', '2023-06-05', 1, 2, '2023-06-01 00:00:00', 1, '2023-06-02 11:14:21.826229', 10, 7, 9, 'Ubicación correcta.', 8.67, 0);
INSERT INTO public.bookings VALUES (18, '2023-11-05', '2023-11-15', 1, 13, '2023-07-17 13:39:56.012921', 4, '2023-07-21 11:58:17.963117', 7, 5, 3, 'Bad price', 5.00, 0);


--
-- TOC entry 3070 (class 0 OID 262630)
-- Dependencies: 222
-- Data for Name: country_iso; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.country_iso VALUES (1, 'ES', 'Spain');
INSERT INTO public.country_iso VALUES (2, 'DK', 'Denmark');
INSERT INTO public.country_iso VALUES (4, 'BG', 'Bulgaria');
INSERT INTO public.country_iso VALUES (7, 'BZ', 'Belice');
INSERT INTO public.country_iso VALUES (12, 'CO', 'Colombia');
INSERT INTO public.country_iso VALUES (13, 'CR', 'Costa Rica');
INSERT INTO public.country_iso VALUES (14, 'CU', 'Cuba');
INSERT INTO public.country_iso VALUES (15, 'CV', 'Cabo Verde');
INSERT INTO public.country_iso VALUES (23, 'GE', 'Georgia');
INSERT INTO public.country_iso VALUES (25, 'GT', 'Guatemala');
INSERT INTO public.country_iso VALUES (26, 'HN', 'Honduras');
INSERT INTO public.country_iso VALUES (30, 'ID', 'Indonesia');
INSERT INTO public.country_iso VALUES (31, 'IN', 'India');
INSERT INTO public.country_iso VALUES (32, 'IQ', 'Iraq');
INSERT INTO public.country_iso VALUES (3, 'BE', 'Belgium');
INSERT INTO public.country_iso VALUES (5, 'BR', 'Brazil');
INSERT INTO public.country_iso VALUES (6, 'BY', 'Belarus');
INSERT INTO public.country_iso VALUES (8, 'BJ', 'Benin');
INSERT INTO public.country_iso VALUES (9, 'BT', 'Bhutan');
INSERT INTO public.country_iso VALUES (10, 'BW', 'Botwana');
INSERT INTO public.country_iso VALUES (11, 'CF', 'Central African Republic');
INSERT INTO public.country_iso VALUES (16, 'CY', 'Cyprus');
INSERT INTO public.country_iso VALUES (17, 'CZ', 'Czech');
INSERT INTO public.country_iso VALUES (18, 'ET', 'Ethiopia');
INSERT INTO public.country_iso VALUES (19, 'FI', 'Finland');
INSERT INTO public.country_iso VALUES (20, 'FJ', 'Fiji');
INSERT INTO public.country_iso VALUES (21, 'FR', 'France');
INSERT INTO public.country_iso VALUES (22, 'GA', 'Gabon');
INSERT INTO public.country_iso VALUES (24, 'GR', 'Greece');
INSERT INTO public.country_iso VALUES (27, 'HR', 'Croatia');
INSERT INTO public.country_iso VALUES (28, 'HT', 'Haiti');
INSERT INTO public.country_iso VALUES (29, 'HU', 'Hungary');
INSERT INTO public.country_iso VALUES (33, 'IR', 'Iran');
INSERT INTO public.country_iso VALUES (34, 'IS', 'Iceland');


--
-- TOC entry 3050 (class 0 OID 206895)
-- Dependencies: 201
-- Data for Name: customers; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.customers VALUES (2, 'Paco', 'Miranda', 'López', '+34600000000  ', '87654321A', 'pacomiranda@gmail.com', 2);
INSERT INTO public.customers VALUES (3, 'Jose Manuel', 'Gamez', 'Arranz', '+34708757380  ', '23614641N', '1hmecst4u0@lycos.co.uk', 2);
INSERT INTO public.customers VALUES (4, 'Carla', 'Cruz', 'Salvador', '+34748958360  ', '08700140E', '2x6gmmprm@scientist.com', 2);
INSERT INTO public.customers VALUES (5, 'Ramon', 'Perea', 'Moran', '+34763455421  ', '49645958K', 'evy08e3a@witty.com', 2);
INSERT INTO public.customers VALUES (8, 'Begoña', 'Rueda', 'Espejo', '+34629584746  ', '40856442R', 'un109pgzd@lycos.de', 2);
INSERT INTO public.customers VALUES (9, 'Laura', 'Arjona', 'Del Valle', '+34663471576  ', '43347550W', 'l61gj8eg8@btinternet.com', 2);
INSERT INTO public.customers VALUES (10, 'Eulalia', 'De La Rosa', 'Tapia', '+34694447039  ', '25369237F', 'mh7n2vt79@whoever.com', 2);
INSERT INTO public.customers VALUES (11, 'Jonathan', 'Peralta', 'Nieto', '+34738899033  ', '04506373Y', '5ouqrjymz@gmail.com', 2);
INSERT INTO public.customers VALUES (12, 'Modesto', 'Muñoz', 'Puig', '+34705578083  ', '96719549H', '7lf2d5p1v@earthling.net', 2);
INSERT INTO public.customers VALUES (13, 'Anna', 'Figueroa', 'Cobos', '+34641114406  ', '47994763L', 'ppso2y6bx@unforgettable.com', 2);
INSERT INTO public.customers VALUES (14, 'Khadija', 'Del Pozo', 'Nicolas', '+34646968006  ', '93729335B', 'ukt7gd30@scientist.com', 2);
INSERT INTO public.customers VALUES (15, 'Armando', 'Romero', 'Arranz', '+34766688429  ', '19612889F', 'otg5movufx@blu.it', 2);
INSERT INTO public.customers VALUES (16, 'Jose Ramon', 'Paredes', 'Ochoa', '+34680280043  ', '58587741W', 'upy6k2u91@usa.com', 2);
INSERT INTO public.customers VALUES (17, 'Faustino', 'Manzano', 'Barrio', '+34681174923  ', '18968489K', 'r0fqbrlbr@teacher.com', 2);
INSERT INTO public.customers VALUES (18, 'Teodoro', 'Bermudez', 'Lazaro', '+34630084219  ', '65092417W', 'qfievtdor@lycos.de', 2);
INSERT INTO public.customers VALUES (19, 'Jose Manuel', 'Asensio', 'Muñoz', '+34674486808  ', '67665674L', 'rp94vro0@techie.com', 2);
INSERT INTO public.customers VALUES (20, 'Alvaro', 'Manzano', 'Prieto', '+34659429669  ', '38785175E', 'riiplq2kxwc@caramail.com', 2);
INSERT INTO public.customers VALUES (21, 'Pilar', 'Carvajal', 'Parra', '+34632223231  ', '50556384S', '0k4nnpvwqq@yahoo.es', 2);
INSERT INTO public.customers VALUES (25, 'Fernando', 'Diaz', 'Alonso', '+34644257396  ', '80033380G', 'nanoalonso14@gmail.com', 2);


--
-- TOC entry 3060 (class 0 OID 226762)
-- Dependencies: 211
-- Data for Name: hist_rooms; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.hist_rooms VALUES (9, '2023-07-17', 1, 4);
INSERT INTO public.hist_rooms VALUES (12, '2023-07-17', 26, 2);
INSERT INTO public.hist_rooms VALUES (13, '2023-07-17', 26, 4);
INSERT INTO public.hist_rooms VALUES (14, '2023-07-17', 27, 2);
INSERT INTO public.hist_rooms VALUES (15, '2023-07-17', 27, 4);
INSERT INTO public.hist_rooms VALUES (16, '2023-07-17', 26, 2);
INSERT INTO public.hist_rooms VALUES (17, '2023-07-17', 26, 4);
INSERT INTO public.hist_rooms VALUES (18, '2023-07-17', 28, 2);
INSERT INTO public.hist_rooms VALUES (19, '2023-07-17', 28, 4);
INSERT INTO public.hist_rooms VALUES (20, '2023-07-17', 29, 2);
INSERT INTO public.hist_rooms VALUES (21, '2023-07-17', 29, 4);
INSERT INTO public.hist_rooms VALUES (22, '2023-07-17', 31, 2);
INSERT INTO public.hist_rooms VALUES (23, '2023-07-17', 31, 4);
INSERT INTO public.hist_rooms VALUES (24, '2023-07-17', 32, 2);
INSERT INTO public.hist_rooms VALUES (25, '2023-07-17', 32, 4);
INSERT INTO public.hist_rooms VALUES (26, '2023-07-17', 33, 2);
INSERT INTO public.hist_rooms VALUES (27, '2023-07-17', 33, 4);
INSERT INTO public.hist_rooms VALUES (28, '2023-07-17', 34, 2);
INSERT INTO public.hist_rooms VALUES (29, '2023-07-17', 34, 4);
INSERT INTO public.hist_rooms VALUES (30, '2023-07-17', 35, 2);
INSERT INTO public.hist_rooms VALUES (31, '2023-07-17', 35, 4);
INSERT INTO public.hist_rooms VALUES (32, '2023-07-17', 36, 2);
INSERT INTO public.hist_rooms VALUES (33, '2023-07-17', 36, 4);
INSERT INTO public.hist_rooms VALUES (34, '2023-07-17', 1, 2);
INSERT INTO public.hist_rooms VALUES (35, '2023-07-17', 1, 4);
INSERT INTO public.hist_rooms VALUES (36, '2023-07-17', 4, 2);
INSERT INTO public.hist_rooms VALUES (37, '2023-07-17', 4, 4);
INSERT INTO public.hist_rooms VALUES (38, '2023-07-21', 4, 4);
INSERT INTO public.hist_rooms VALUES (39, '2023-07-24', 5, 2);
INSERT INTO public.hist_rooms VALUES (40, '2023-07-24', 37, 4);
INSERT INTO public.hist_rooms VALUES (41, '2023-07-24', 36, 1);
INSERT INTO public.hist_rooms VALUES (42, '2023-07-24', 5, 4);
INSERT INTO public.hist_rooms VALUES (43, '2023-07-24', 37, 4);
INSERT INTO public.hist_rooms VALUES (44, '2023-07-24', 1, 2);
INSERT INTO public.hist_rooms VALUES (45, '2023-07-24', 37, 4);
INSERT INTO public.hist_rooms VALUES (46, '2023-07-24', 37, 1);
INSERT INTO public.hist_rooms VALUES (47, '2023-07-24', 1, 4);
INSERT INTO public.hist_rooms VALUES (48, '2023-07-26', 4, 2);
INSERT INTO public.hist_rooms VALUES (49, '2023-07-26', 37, 4);
INSERT INTO public.hist_rooms VALUES (50, '2023-07-26', 37, 1);
INSERT INTO public.hist_rooms VALUES (51, '2023-07-26', 4, 4);


--
-- TOC entry 3046 (class 0 OID 206663)
-- Dependencies: 197
-- Data for Name: hotels; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.hotels VALUES (5, 'Vila Garic', 12, 7.00, 3, 3);
INSERT INTO public.hotels VALUES (1, 'NYX Hotel Madrid', 1, 5.42, 4, 5);
INSERT INTO public.hotels VALUES (2, 'Riu Touraged', 14, 6.67, 3, 2);
INSERT INTO public.hotels VALUES (3, 'Ibis Oviedo', 4, 6.00, 2, 3);
INSERT INTO public.hotels VALUES (4, 'Generator Paris', 8, 7.67, 3, 3);


--
-- TOC entry 3056 (class 0 OID 210122)
-- Dependencies: 207
-- Data for Name: iddocumenttypes; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.iddocumenttypes VALUES (1, 'PASSPORT');
INSERT INTO public.iddocumenttypes VALUES (2, 'DNI');


--
-- TOC entry 3072 (class 0 OID 267589)
-- Dependencies: 224
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.orders VALUES (1, 1, 50, '[1, 2, 3]', true);
INSERT INTO public.orders VALUES (2, 1, 51, '[1, 2, 3]', true);
INSERT INTO public.orders VALUES (3, 1, 52, '[1, 2]', true);
INSERT INTO public.orders VALUES (4, 1, 50, '[1, 2, 3]', true);
INSERT INTO public.orders VALUES (5, 1, 51, '[1, 2, 3]', true);
INSERT INTO public.orders VALUES (6, 1, 52, '[1, 2]', true);
INSERT INTO public.orders VALUES (7, 1, 50, '[1, 2, 3]', false);
INSERT INTO public.orders VALUES (8, 1, 51, '[1, 2, 3]', false);
INSERT INTO public.orders VALUES (9, 1, 52, '[1, 2]', true);
INSERT INTO public.orders VALUES (10, 1, 50, '[1, 2, 3]', false);
INSERT INTO public.orders VALUES (11, 1, 51, '[1, 2, 3]', false);
INSERT INTO public.orders VALUES (12, 1, 52, '[1, 2]', true);
INSERT INTO public.orders VALUES (13, 1, 50, '[1, 2, 3]', false);
INSERT INTO public.orders VALUES (14, 1, 51, '[1, 2, 3]', false);
INSERT INTO public.orders VALUES (15, 1, 52, '[1, 2]', false);
INSERT INTO public.orders VALUES (16, 1, 50, '[1, 2, 3]', false);
INSERT INTO public.orders VALUES (17, 1, 51, '[1, 2, 3]', false);
INSERT INTO public.orders VALUES (18, 1, 52, '[1, 2]', false);
INSERT INTO public.orders VALUES (25, 23, 50, '[1, 2, 3]', false);
INSERT INTO public.orders VALUES (26, 23, 51, '[1, 2, 3]', false);
INSERT INTO public.orders VALUES (27, 23, 52, '[1, 2]', false);


--
-- TOC entry 3058 (class 0 OID 215674)
-- Dependencies: 209
-- Data for Name: postalcodes; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.postalcodes VALUES (1, 28015, 1);
INSERT INTO public.postalcodes VALUES (2, 36950, 1);
INSERT INTO public.postalcodes VALUES (3, 15001, 1);
INSERT INTO public.postalcodes VALUES (4, 33200, 1);
INSERT INTO public.postalcodes VALUES (5, 110110, 12);
INSERT INTO public.postalcodes VALUES (6, 10200, 14);
INSERT INTO public.postalcodes VALUES (7, 1000, 3);
INSERT INTO public.postalcodes VALUES (8, 70123, 21);
INSERT INTO public.postalcodes VALUES (9, 101, 34);
INSERT INTO public.postalcodes VALUES (10, 110001, 31);
INSERT INTO public.postalcodes VALUES (11, 241, 22);
INSERT INTO public.postalcodes VALUES (12, 43232, 27);
INSERT INTO public.postalcodes VALUES (13, 2, 19);
INSERT INTO public.postalcodes VALUES (14, 7425, 15);


--
-- TOC entry 3064 (class 0 OID 230361)
-- Dependencies: 216
-- Data for Name: product_types; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.product_types VALUES (1, 'Drink');
INSERT INTO public.product_types VALUES (2, 'Snack');
INSERT INTO public.product_types VALUES (3, 'Starter dish');
INSERT INTO public.product_types VALUES (4, 'First dish');
INSERT INTO public.product_types VALUES (5, 'Second dish');
INSERT INTO public.product_types VALUES (6, 'Dessert');
INSERT INTO public.product_types VALUES (7, 'Kids dish');
INSERT INTO public.product_types VALUES (8, 'Kids dessert');
INSERT INTO public.product_types VALUES (9, 'Menú');


--
-- TOC entry 3066 (class 0 OID 230369)
-- Dependencies: 218
-- Data for Name: products; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.products VALUES (1, 'Coca-Cola 1L', 'A bottle of Coca-Cola 1L', 1, 3.50);
INSERT INTO public.products VALUES (2, 'Water 1L', 'A bottle of water 1L', 1, 1.50);
INSERT INTO public.products VALUES (3, 'Salad', 'Salad with lettuce, tomatoes and onions', 4, 5.00);
INSERT INTO public.products VALUES (9, 'Fruit', 'Fresh fruit', 6, 2.99);
INSERT INTO public.products VALUES (11, 'Cinnamon Roll', 'Sweet baked dough filled with a cinnamon-sugar filling', 6, 6.99);
INSERT INTO public.products VALUES (12, 'Grilled Fingerlings', 'Grilled potatoes with a Western flair served with sauce of choice', 5, 16.99);
INSERT INTO public.products VALUES (17, 'Fennel and aragula salad', 'With shavced ricotta and lemon oil vinaigrette', 4, 12.99);
INSERT INTO public.products VALUES (22, 'Mizuna Salad', 'Truffle vinaigrette, murcott tangerine, caciotta al tartufo', 4, 12.99);
INSERT INTO public.products VALUES (16, 'Saffron-Tomato Seafood Stew', 'With prawns, mussels, calamari, market catch', 5, 16.99);
INSERT INTO public.products VALUES (21, 'Tako Salad', 'Octopuss, daikon, kimchi vinaigrette', 4, 12.99);
INSERT INTO public.products VALUES (18, 'Corn Soup', 'Dungenesss crab gratin, chipotle gelée', 4, 12.99);
INSERT INTO public.products VALUES (19, 'Caesar', 'Romaine, anchovies, croutons', 4, 12.99);
INSERT INTO public.products VALUES (5, 'Banana Split', 'Fresh banana sliced in half and topped with scoops of vanilla, strawberry, and chocolate ice cream, and various sweet syrups and garnishes', 6, 6.99);
INSERT INTO public.products VALUES (6, 'Cheese Cake', 'Thick, creamy filling of cheese, eggs, and sugar over a thinner crust and topped with sweet', 6, 6.99);
INSERT INTO public.products VALUES (7, 'Chocolate Ice Cream', 'Frozen dessert consisting of milk, cream, melted chocolate, and sugar flavored with cocoa powder and additional mix-ins', 6, 6.99);
INSERT INTO public.products VALUES (8, 'Fruit Cake', 'Made with candied or dried fruit, nuts, and spices, and optionally soaked in spirits', 6, 6.99);
INSERT INTO public.products VALUES (10, 'Carrot Cake', 'Sweet and moist spice cake, full of cut carrots and toasted nuts, and covered in cream cheese icing', 6, 6.99);
INSERT INTO public.products VALUES (13, 'Roasted Acron Squash', 'Spicy-sweet, soft wedges topped with musshrooms, onions and cheese', 5, 16.99);
INSERT INTO public.products VALUES (15, 'Prime Rib', 'Garlic, mustard rub, horseradish', 5, 16.99);
INSERT INTO public.products VALUES (20, 'Iceberg Lettuce', 'Blue cheese, smoked bacon, fuji apple', 4, 12.99);
INSERT INTO public.products VALUES (23, 'Baby Bok Choy', 'Applewood smoked bacon, toasted madacamia nuts', 4, 12.99);
INSERT INTO public.products VALUES (14, 'Day Boat Scallops', 'With fennel, prosciutto, black bean sauce', 5, 16.99);
INSERT INTO public.products VALUES (24, 'Shrimp Scampi', 'Peeled prawns, garlic and olive oil.', 4, 12.50);
INSERT INTO public.products VALUES (25, 'Rice with lobster', 'Rice with lobster, fish soup and prawns', 5, 25.00);
INSERT INTO public.products VALUES (26, 'Stuffed Brown Crab', 'Brown crab stuffed with peppers, onions and butter', 5, 19.99);
INSERT INTO public.products VALUES (27, 'Grilled shrimp', 'carabinieri, coarse salt and olive oil', 4, 18.99);
INSERT INTO public.products VALUES (28, 'Crab Cake', 'Crab cake with mozzarella, garlic, onions and bread', 5, 15.99);
INSERT INTO public.products VALUES (32, 'Grilled razor', 'Fresh razors, chopped garlic, chopped parsley, extra virgin olive oil and sea salt ', 4, 22.50);
INSERT INTO public.products VALUES (31, 'Beef Escalope ', 'Beef Escalope with fries or salad', 7, 8.99);
INSERT INTO public.products VALUES (33, 'Tortilla', 'Tortilla with or without onion', 7, 5.99);
INSERT INTO public.products VALUES (34, 'Fries with eggs and sausages', 'Fries with eggs and sausages', 7, 5.99);
INSERT INTO public.products VALUES (36, 'Macaroons', 'Macaroons with tomate sauce and chopped meat ', 7, 6.99);
INSERT INTO public.products VALUES (35, 'Strawberry ice cream', 'Strawberry ice cream ', 8, 2.99);
INSERT INTO public.products VALUES (37, 'Vanilla ice cream', 'Vanilla ice cream', 8, 2.99);
INSERT INTO public.products VALUES (38, 'Chocolate ice cream', 'Chocolate ice cream', 8, 2.99);
INSERT INTO public.products VALUES (39, 'Grilled monkfish with herbs potatoes', 'Grilled monkfish with herbed potatoes, olive oil, garlic and dried chilli', 5, 35.70);
INSERT INTO public.products VALUES (4, 'Spicy potatoes', 'Potatoes with brava sauce ', 3, 4.99);
INSERT INTO public.products VALUES (40, 'Sautéed chickpeas', 'Sautéed chickpeas', 4, 7.40);
INSERT INTO public.products VALUES (41, 'Rice with seitan, peas, mushrooms and garlic', 'Rice with seitan, peas, mushrooms and garlic', 4, 8.20);
INSERT INTO public.products VALUES (42, 'Bean stew', 'Bean stew', 5, 9.50);
INSERT INTO public.products VALUES (43, 'Quinoa paella', 'Quinoa paella with red and green peppers and peas', 5, 10.99);
INSERT INTO public.products VALUES (44, 'Pasta salad with vegetable sausages', 'Pasta salad with vegetable sausages', 4, 8.50);
INSERT INTO public.products VALUES (45, 'Baked seitan', 'Baked seitan with onion and mushrooms', 5, 7.20);
INSERT INTO public.products VALUES (64, 'Prueba', 'Prueba de comentario', 4, 9.99);
INSERT INTO public.products VALUES (50, 'Vegan menu', 'First dishes: Salad, Rice with seitan, peas, mushrooms and garlic, Fennel and aragula salad; Second dishes: Quinoa paella, Baked seitan, Bean stew; Desserts: Cinnamon Roll, Fruit, Carrot Cake; ', 9, 12.5);
INSERT INTO public.products VALUES (49, 'Vegetarian menu', 'First dishes: Fennel and aragula salad, Baby Bok Choy, Pasta salad with vegetable sausages; Second dishes: Bean stew, Stuffed Brown Crab, Quinoa paella; Desserts: Banana Split, Cheese Cake, Carrot Cake; ', 9, 12.5);
INSERT INTO public.products VALUES (52, 'Children''s menu', 'First dishes: Fries with eggs and sausages, Tortilla, Beef Escalope ; Desserts: Chocolate ice cream, Vanilla ice cream, Strawberry ice cream; ', 9, 7.95);
INSERT INTO public.products VALUES (60, 'Cannelloni', 'Cannelloni with meat and vegetables', 4, 9.99);
INSERT INTO public.products VALUES (46, 'Menu of the day', 'First dishes: Rice with seitan, peas, mushrooms and garlic, Mizuna Salad, Pasta salad with vegetable sausages; Second dish: Roasted Acron Squash, Grilled Fingerlings, Baked seitan; Desserts: Banana Split, Fruit Cake, Chocolate Cake ; ', 9, 14.50);
INSERT INTO public.products VALUES (63, 'Chocolate Cake ', 'A delicious chocolate cake', 6, 5.99);
INSERT INTO public.products VALUES (51, 'Seafood menu', 'First dishes: Rice with seitan, peas, mushrooms and garlic, Pasta salad with vegetable sausages, Tako Salad; Second dishes: Day Boat Scallops, Rice with lobster, Bean stew; Desserts: Cheese Cake, Chocolate Ice Cream, Carrot Cake; ', 9, 39.5);


--
-- TOC entry 3068 (class 0 OID 260697)
-- Dependencies: 220
-- Data for Name: products_allergens; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.products_allergens VALUES (1, 5, 7);
INSERT INTO public.products_allergens VALUES (3, 6, 7);
INSERT INTO public.products_allergens VALUES (4, 6, 4);
INSERT INTO public.products_allergens VALUES (5, 7, 7);
INSERT INTO public.products_allergens VALUES (6, 8, 10);
INSERT INTO public.products_allergens VALUES (7, 10, 10);
INSERT INTO public.products_allergens VALUES (8, 10, 7);
INSERT INTO public.products_allergens VALUES (9, 13, 7);
INSERT INTO public.products_allergens VALUES (10, 18, 3);
INSERT INTO public.products_allergens VALUES (11, 19, 5);
INSERT INTO public.products_allergens VALUES (12, 16, 8);
INSERT INTO public.products_allergens VALUES (13, 20, 7);
INSERT INTO public.products_allergens VALUES (14, 20, 15);
INSERT INTO public.products_allergens VALUES (15, 21, 8);
INSERT INTO public.products_allergens VALUES (16, 23, 15);
INSERT INTO public.products_allergens VALUES (17, 23, 10);
INSERT INTO public.products_allergens VALUES (18, 24, 3);
INSERT INTO public.products_allergens VALUES (19, 25, 3);
INSERT INTO public.products_allergens VALUES (20, 26, 3);
INSERT INTO public.products_allergens VALUES (21, 26, 7);
INSERT INTO public.products_allergens VALUES (22, 27, 3);
INSERT INTO public.products_allergens VALUES (23, 28, 3);
INSERT INTO public.products_allergens VALUES (24, 28, 7);
INSERT INTO public.products_allergens VALUES (25, 31, 15);
INSERT INTO public.products_allergens VALUES (26, 32, 8);
INSERT INTO public.products_allergens VALUES (27, 33, 4);
INSERT INTO public.products_allergens VALUES (28, 34, 15);
INSERT INTO public.products_allergens VALUES (29, 34, 4);
INSERT INTO public.products_allergens VALUES (30, 35, 7);
INSERT INTO public.products_allergens VALUES (31, 36, 15);
INSERT INTO public.products_allergens VALUES (32, 37, 7);
INSERT INTO public.products_allergens VALUES (33, 38, 7);
INSERT INTO public.products_allergens VALUES (34, 51, 3);
INSERT INTO public.products_allergens VALUES (35, 51, 8);
INSERT INTO public.products_allergens VALUES (42, 60, 4);
INSERT INTO public.products_allergens VALUES (43, 60, 7);
INSERT INTO public.products_allergens VALUES (44, 60, 15);
INSERT INTO public.products_allergens VALUES (48, 12, 15);
INSERT INTO public.products_allergens VALUES (49, 15, 15);
INSERT INTO public.products_allergens VALUES (50, 16, 3);
INSERT INTO public.products_allergens VALUES (52, 39, 5);
INSERT INTO public.products_allergens VALUES (53, 64, 4);
INSERT INTO public.products_allergens VALUES (54, 64, 7);
INSERT INTO public.products_allergens VALUES (55, 64, 15);


--
-- TOC entry 3048 (class 0 OID 206823)
-- Dependencies: 199
-- Data for Name: rooms; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.rooms VALUES (31, 155, 4, 1);
INSERT INTO public.rooms VALUES (32, 92, 4, 1);
INSERT INTO public.rooms VALUES (33, 333, 4, 1);
INSERT INTO public.rooms VALUES (26, 83, 2, 1);
INSERT INTO public.rooms VALUES (28, 502, 3, 1);
INSERT INTO public.rooms VALUES (29, 233, 3, 1);
INSERT INTO public.rooms VALUES (36, 255, 5, 1);
INSERT INTO public.rooms VALUES (34, 20, 5, 1);
INSERT INTO public.rooms VALUES (35, 105, 5, 1);
INSERT INTO public.rooms VALUES (5, 77, 1, 4);
INSERT INTO public.rooms VALUES (1, 1, 1, 4);
INSERT INTO public.rooms VALUES (37, 999, 5, 1);
INSERT INTO public.rooms VALUES (4, 595, 1, 4);
INSERT INTO public.rooms VALUES (30, 404, 3, 1);
INSERT INTO public.rooms VALUES (27, 43, 2, 1);


--
-- TOC entry 3052 (class 0 OID 207373)
-- Dependencies: 203
-- Data for Name: states; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.states VALUES (1, 'free');
INSERT INTO public.states VALUES (2, 'busy');
INSERT INTO public.states VALUES (3, 'maintenance');
INSERT INTO public.states VALUES (4, 'cleaning');


--
-- TOC entry 3094 (class 0 OID 0)
-- Dependencies: 213
-- Name: allergens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.allergens_id_seq', 16, true);


--
-- TOC entry 3095 (class 0 OID 0)
-- Dependencies: 204
-- Name: bookings_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.bookings_id_seq', 23, true);


--
-- TOC entry 3096 (class 0 OID 0)
-- Dependencies: 221
-- Name: country_iso_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.country_iso_id_seq', 34, true);


--
-- TOC entry 3097 (class 0 OID 0)
-- Dependencies: 200
-- Name: customer_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.customer_id_seq', 25, true);


--
-- TOC entry 3098 (class 0 OID 0)
-- Dependencies: 210
-- Name: hist_rooms_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.hist_rooms_id_seq', 51, true);


--
-- TOC entry 3099 (class 0 OID 0)
-- Dependencies: 196
-- Name: hotels_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.hotels_id_seq', 5, true);


--
-- TOC entry 3100 (class 0 OID 0)
-- Dependencies: 206
-- Name: iddocumenttypes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.iddocumenttypes_id_seq', 2, true);


--
-- TOC entry 3101 (class 0 OID 0)
-- Dependencies: 223
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.orders_id_seq', 27, true);


--
-- TOC entry 3102 (class 0 OID 0)
-- Dependencies: 208
-- Name: postalcodes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.postalcodes_id_seq', 14, true);


--
-- TOC entry 3103 (class 0 OID 0)
-- Dependencies: 215
-- Name: product_types_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.product_types_id_seq', 9, true);


--
-- TOC entry 3104 (class 0 OID 0)
-- Dependencies: 219
-- Name: products_allergens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.products_allergens_id_seq', 55, true);


--
-- TOC entry 3105 (class 0 OID 0)
-- Dependencies: 217
-- Name: products_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.products_id_seq', 64, true);


--
-- TOC entry 3106 (class 0 OID 0)
-- Dependencies: 198
-- Name: rooms_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.rooms_id_seq', 40, true);


--
-- TOC entry 3107 (class 0 OID 0)
-- Dependencies: 202
-- Name: states_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.states_id_seq', 7, true);


--
-- TOC entry 2897 (class 2606 OID 230358)
-- Name: allergens allergens_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.allergens
    ADD CONSTRAINT allergens_pkey PRIMARY KEY (id);


--
-- TOC entry 2889 (class 2606 OID 210002)
-- Name: bookings bookings_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_pkey PRIMARY KEY (id);


--
-- TOC entry 2905 (class 2606 OID 262635)
-- Name: country_iso country_iso_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.country_iso
    ADD CONSTRAINT country_iso_pkey PRIMARY KEY (id);


--
-- TOC entry 2885 (class 2606 OID 206903)
-- Name: customers customer_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customer_pkey PRIMARY KEY (id);


--
-- TOC entry 2895 (class 2606 OID 226767)
-- Name: hist_rooms hist_rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hist_rooms
    ADD CONSTRAINT hist_rooms_pkey PRIMARY KEY (id);


--
-- TOC entry 2881 (class 2606 OID 206820)
-- Name: hotels hotels_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hotels
    ADD CONSTRAINT hotels_pk PRIMARY KEY (id);


--
-- TOC entry 2891 (class 2606 OID 210128)
-- Name: iddocumenttypes iddocumenttypes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.iddocumenttypes
    ADD CONSTRAINT iddocumenttypes_pkey PRIMARY KEY (id);


--
-- TOC entry 2907 (class 2606 OID 267595)
-- Name: orders orders_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);


--
-- TOC entry 2893 (class 2606 OID 215679)
-- Name: postalcodes postalcodes_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.postalcodes
    ADD CONSTRAINT postalcodes_pkey PRIMARY KEY (id);


--
-- TOC entry 2899 (class 2606 OID 230366)
-- Name: product_types product_types_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.product_types
    ADD CONSTRAINT product_types_pkey PRIMARY KEY (id);


--
-- TOC entry 2903 (class 2606 OID 260702)
-- Name: products_allergens products_allergens_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products_allergens
    ADD CONSTRAINT products_allergens_pkey PRIMARY KEY (id);


--
-- TOC entry 2901 (class 2606 OID 230377)
-- Name: products products_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);


--
-- TOC entry 2883 (class 2606 OID 206828)
-- Name: rooms rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (id);


--
-- TOC entry 2887 (class 2606 OID 207379)
-- Name: states states_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.states
    ADD CONSTRAINT states_pkey PRIMARY KEY (id);


--
-- TOC entry 2913 (class 2606 OID 210008)
-- Name: bookings bookings_customer_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES public.customers(id);


--
-- TOC entry 2912 (class 2606 OID 210003)
-- Name: bookings bookings_hotel_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(id);


--
-- TOC entry 2914 (class 2606 OID 213637)
-- Name: bookings bookings_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bookings
    ADD CONSTRAINT bookings_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(id);


--
-- TOC entry 2911 (class 2606 OID 210129)
-- Name: customers customers_idtype_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customers_idtype_id_fkey FOREIGN KEY (idtype_id) REFERENCES public.iddocumenttypes(id);


--
-- TOC entry 2916 (class 2606 OID 226768)
-- Name: hist_rooms hist_rooms_room_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hist_rooms
    ADD CONSTRAINT hist_rooms_room_id_fkey FOREIGN KEY (room_id) REFERENCES public.rooms(id);


--
-- TOC entry 2917 (class 2606 OID 226773)
-- Name: hist_rooms hist_rooms_state_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hist_rooms
    ADD CONSTRAINT hist_rooms_state_id_fkey FOREIGN KEY (state_id) REFERENCES public.states(id);


--
-- TOC entry 2908 (class 2606 OID 215680)
-- Name: hotels hotels_zip_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.hotels
    ADD CONSTRAINT hotels_zip_id_fkey FOREIGN KEY (zip_id) REFERENCES public.postalcodes(id);


--
-- TOC entry 2921 (class 2606 OID 267596)
-- Name: orders orders_booking_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_booking_id_fkey FOREIGN KEY (booking_id) REFERENCES public.bookings(id);


--
-- TOC entry 2922 (class 2606 OID 267601)
-- Name: orders orders_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 2915 (class 2606 OID 262649)
-- Name: postalcodes postalcodes_iso_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.postalcodes
    ADD CONSTRAINT postalcodes_iso_id_fkey FOREIGN KEY (iso_id) REFERENCES public.country_iso(id);


--
-- TOC entry 2920 (class 2606 OID 260708)
-- Name: products_allergens products_allergens_allergen_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products_allergens
    ADD CONSTRAINT products_allergens_allergen_id_fkey FOREIGN KEY (allergen_id) REFERENCES public.allergens(id);


--
-- TOC entry 2919 (class 2606 OID 260703)
-- Name: products_allergens products_allergens_product_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products_allergens
    ADD CONSTRAINT products_allergens_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- TOC entry 2918 (class 2606 OID 230378)
-- Name: products products_producttype_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_producttype_id_fkey FOREIGN KEY (producttype_id) REFERENCES public.product_types(id);


--
-- TOC entry 2909 (class 2606 OID 206829)
-- Name: rooms rooms_hotel_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_hotel_id_fkey FOREIGN KEY (hotel_id) REFERENCES public.hotels(id);


--
-- TOC entry 2910 (class 2606 OID 207380)
-- Name: rooms rooms_state_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_state_id_fkey FOREIGN KEY (state_id) REFERENCES public.states(id);


-- Completed on 2023-07-27 08:29:58

--
-- PostgreSQL database dump complete
--

