import pandas as pd
from utils import fix_date


def to_scoreboard(df):

    df = fix_date(df)

    # make two entries separating home and away
    home_results = pd.DataFrame(df[['HomeTeam', 'H', 'D', 'FTHG', 'FTAG', 'Date', 'HST', 'season']])
    home_results['Score'] = home_results['H'] + home_results['D']
    home_results.rename(columns={'HomeTeam': 'Team', 'FTHG': 'Goals', 'FTAG': 'GoalsAgainst', 'HST': 'ShotsOnTarget',
                                 'season': 'Season'},
                        inplace=True)
    home_results = home_results.drop(['H', 'D'], axis=1)

    away_results = pd.DataFrame(df[['AwayTeam', 'A', 'D', 'FTAG', 'FTHG', 'Date', 'AST', 'season']])
    away_results['Score'] = away_results['A'] + away_results['D']
    away_results.rename(columns={'AwayTeam': 'Team', 'FTAG': 'Goals', 'FTHG': 'GoalsAgainst', 'AST': 'ShotsOnTarget',
                                 'season': 'Season'},
                        inplace=True)
    away_results = away_results.drop(['A', 'D'], axis=1)

    scoreboard = pd.concat([home_results, away_results])
    scoreboard = scoreboard.groupby(['Season', 'Team']).sum()
    scoreboard['GoalsRatio'] = scoreboard['Goals'] / scoreboard['ShotsOnTarget']

    return scoreboard
