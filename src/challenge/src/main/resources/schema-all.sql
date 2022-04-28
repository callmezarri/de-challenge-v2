DROP TABLE match IF EXISTS;

CREATE TABLE match_data  (
    team VARCHAR(20) NOT NULL,
    score SMALLINT NOT NULL,
    date VARCHAR(20) NOT NULL,
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

CREATE TABLE season_statistics (
    most_goals_team VARCHAR(20),
    most_goals_number SMALLINT DEFAULT 0,
    most_goals_against_team VARCHAR(20),
    most_goals_against_number SMALLINT DEFAULT 0,
    best_goals_ratio_team VARCHAR(20) ,
    best_goals_ratio_number DOUBLE DEFAULT 0,
    season char(5) NOT NULL,
    PRIMARY KEY (season)
);