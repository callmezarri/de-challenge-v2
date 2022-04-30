DROP TABLE match_data IF EXISTS;
DROP TABLE scoreboard IF EXISTS;
DROP TABLE most_scored IF EXISTS;
DROP TABLE best_scorer IF EXISTS;
DROP TABLE best_ratio IF EXISTS;

CREATE TABLE match_data  (
    team VARCHAR(20) NOT NULL,
    score SMALLINT NOT NULL,
    date DATE NOT NULL,
    season CHAR(5) NOT NULL,
    goals SMALLINT NOT NULL,
    goals_against SMALLINT NOT NULL,
    shots_target SMALLINT NOT NULL,
    PRIMARY KEY (team, season, date)
);

CREATE TABLE scoreboard (
    team VARCHAR(20) NOT NULL,
    score SMALLINT NOT NULL DEFAULT 0,
    matches_played SMALLINT NOT NULL DEFAULT 0,
    goals_favor SMALLINT NOT NULL DEFAULT 0,
    goals_against SMALLINT NOT NULL DEFAULT 0,
    goal_difference SMALLINT NOT NULL DEFAULT 0,
    shots_on_target SMALLINT NOT NULL DEFAULT 0,
    goal_percentage FLOAT NOT NULL DEFAULT 0.0,
    season CHAR(5) NOT NULL,
    PRIMARY KEY (team, season)
);

CREATE TABLE most_scored (
    most_goals_against_team VARCHAR(20) NOT NULL,
    most_goals_against_number SMALLINT DEFAULT 0 NOT NULL,
    season char(5) NOT NULL,
    PRIMARY KEY (season, most_goals_against_team, most_goals_against_number)
);

CREATE TABLE best_scorer (
    most_goals_team VARCHAR(20) NOT NULL,
    most_goals_number SMALLINT DEFAULT 0 NOT NULL,
    season char(5) NOT NULL,
    PRIMARY KEY (season, most_goals_team, most_goals_number)
);

CREATE TABLE best_ratio (
    best_goals_ratio_team VARCHAR(20) NOT NULL,
    best_goals_ratio_number DOUBLE DEFAULT 0 NOT NULL,
    season char(5) NOT NULL,
    PRIMARY KEY (season, best_goals_ratio_team, best_goals_ratio_number)
);