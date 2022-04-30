from utils import fix_path


def to_csv(path, df):
    path = fix_path(path)

    for name, group in df.groupby(['Season']):
        temp = group.sort_values('Score', ascending=False)
        temp.to_csv(path + "scoreboard-"+name+".csv", index=True, mode='a')
