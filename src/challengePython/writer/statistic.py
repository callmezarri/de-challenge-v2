from utils import fix_path


def to_csv(path, statistic_name, df):
    path = fix_path(path)
    df.to_csv(path + statistic_name + ".csv", index=True, mode='a')
