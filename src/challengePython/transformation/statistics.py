
def best_scorer(df):
    idx = df.groupby(['Season'])['Goals'].transform(max) == df['Goals']
    return df[idx]['Goals']


def most_scored(df):
    idx = df.groupby(['Season'])['GoalsAgainst'].transform(max) == df['GoalsAgainst']
    return df[idx]['GoalsAgainst']


def best_goal_ratio(df):
    idx = df.groupby(['Season'])['GoalsRatio'].transform(max) == df['GoalsRatio']
    return df[idx]['GoalsRatio']
