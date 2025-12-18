/* ================================ Clean up ================================ */

DROP TABLE IF EXISTS background CASCADE;
DROP TABLE IF EXISTS class CASCADE;
DROP TABLE IF EXISTS race CASCADE;
DROP TABLE IF EXISTS level_reward CASCADE;
DROP TABLE IF EXISTS language CASCADE;
DROP TABLE IF EXISTS proficiency CASCADE;
DROP TABLE IF EXISTS skill CASCADE;
DROP TABLE IF EXISTS armour CASCADE;
DROP TABLE IF EXISTS tool CASCADE;
DROP TABLE IF EXISTS feat CASCADE;
DROP TABLE IF EXISTS asm CASCADE;
DROP TABLE IF EXISTS modifier_source CASCADE;
DROP TABLE IF EXISTS supply_language CASCADE;
DROP TABLE IF EXISTS supply_proficiency CASCADE;
DROP TABLE IF EXISTS supply_feat CASCADE;
DROP TABLE IF EXISTS supply_asm CASCADE;

/* ================================= Tables ================================= */

CREATE TABLE IF NOT EXISTS background (
	id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
	description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS class_template (
	id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
	description VARCHAR(255),
	hit_points VARCHAR(255),
	hit_dice VARCHAR(255),
	parent_id INT REFERENCES class_template(id) CHECK (parent_id != id)
);

CREATE TABLE IF NOT EXISTS race (
	id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
	description VARCHAR(255),
	age VARCHAR(255),
	alignment VARCHAR(255),
	size VARCHAR(255),
	speed INT DEFAULT 30 CHECK (speed > 0),
	parent_id INT REFERENCES race(id) CHECK (parent_id != id)
);

CREATE TABLE IF NOT EXISTS level_reward (
	id SERIAL PRIMARY KEY,
	class_id INT NOT NULL REFERENCES class_template(id),
    level INT NOT NULL CHECK (level BETWEEN 1 AND 20),
	UNIQUE (class_id, level)
);

/* ------------------------ Available  Modifications ------------------------ */

CREATE TABLE IF NOT EXISTS language (
	id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
	description VARCHAR(255),
	script VARCHAR(255),
	exotic BOOLEAN NOT NULL DEFAULT false
	-- UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS proficiency (
	id SERIAL PRIMARY KEY,
	kind TEXT NOT NULL CHECK (kind in (
		'skill', 'tool', 'armour')),
	ref_id INT NOT NULL, -- Tables referenced
	UNIQUE (kind, ref_id)
);

CREATE TABLE IF NOT EXISTS skill (
	id SERIAL PRIMARY KEY,
	kind TEXT NOT NULL CHECK (kind in (
		'ACROBATICS', 'ANIMAL_HANDLING', 'ARCANA', 'ATHLETICS', 'DECEPTION',
		'HISTORY', 'INSIGHT', 'INTIMIDATION', 'INVESTIGATION', 'MEDICINE',
		'NATURE', 'PERCEPTION', 'PERFORMANCE', 'PERSUASION', 'RELIGION',
		'SLEIGHT_OF_HAND', 'STEALTH', 'SURVIVAL')),
	UNIQUE (kind)
);

CREATE TABLE IF NOT EXISTS armour (
	id SERIAL PRIMARY KEY,
	kind TEXT NOT NULL CHECK (kind in (
		'LIGHT', 'MEDIUM', 'HEAVY', 'SHIELD')),
	UNIQUE (kind)
);

CREATE TABLE IF NOT EXISTS tool (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255),
	kind TEXT NOT NULL CHECK (kind in (
		'ARTISAN_TOOLS', 'GAMING_SET', 'MUSICAL_INSTRUMENT', 'MISCELLANEOUS'))
	-- UNIQUE (name, kind)
);

CREATE TABLE IF NOT EXISTS feat (
	id SERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255) NOT NULL
	-- TODO: abilityScoreModifiers, proficiencies, choices
);

CREATE TABLE IF NOT EXISTS asm (
	id SERIAL PRIMARY KEY,
	ability TEXT NOT NULL CHECK (ability in (
		'STR', 'DEX', 'CON', 'INT', 'WIS', 'CHA')),
	value INT NOT NULL CHECK (value <> 0)
);

/* =========================== Association Tables =========================== */

CREATE TABLE IF NOT EXISTS modifier_source (
	id SERIAL PRIMARY KEY,
	kind TEXT NOT NULL CHECK (kind in (
		'background', 'race', 'level_reward', 'feat')),
	ref_id INT NOT NULL,
	UNIQUE (kind, ref_id)
);

CREATE TABLE IF NOT EXISTS supply_language (
	source_id INT NOT NULL REFERENCES modifier_source(id) ON DELETE CASCADE,
	language_id INT NOT NULL REFERENCES language(id),
	PRIMARY KEY(source_id, language_id)
);

CREATE TABLE IF NOT EXISTS supply_proficiency (
	source_id INT NOT NULL REFERENCES modifier_source(id) ON DELETE CASCADE,
	proficiency_id INT NOT NULL REFERENCES proficiency(id),
	PRIMARY KEY(source_id, proficiency_id)
);

CREATE TABLE IF NOT EXISTS supply_feat (
	source_id INT NOT NULL REFERENCES modifier_source(id) ON DELETE CASCADE,
	feat_id INT NOT NULL REFERENCES feat(id),
	PRIMARY KEY(source_id, feat_id)
);

CREATE TABLE IF NOT EXISTS supply_asm (
	source_id INT NOT NULL REFERENCES modifier_source(id) ON DELETE CASCADE,
	asm_id INT NOT NULL REFERENCES asm(id),
	PRIMARY KEY(source_id, asm_id)
);
