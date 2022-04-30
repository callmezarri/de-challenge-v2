import argparse

from utils import load_data
from transformation.scoreboard import to_scoreboard
from transformation.statistics import best_scorer, best_goal_ratio, most_scored
from writer import scoreboard as sw, statistic as stw


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument("path", help="path of the json files")
    parser.add_argument("output", help="path to save json files")
    args = parser.parse_args()

    # Extract
    df = load_data(args.path)

    # Transform
    scoreboard = to_scoreboard(df)

    bestScorer = best_scorer(scoreboard)
    mostScored = most_scored(scoreboard)
    bestGoalRatio = best_goal_ratio(scoreboard)

    # Load
    sw.to_csv(args.output, scoreboard)
    stw.to_csv(args.output, "best_scorer", bestScorer)
    stw.to_csv(args.output, "most_scored", mostScored)
    stw.to_csv(args.output, "best_goal_ratio", bestGoalRatio)

