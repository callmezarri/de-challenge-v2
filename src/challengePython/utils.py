from os import listdir
import json
import pandas as pd
import re
import datetime

pattern = re.compile(r"^\d{4}-(0[1-9]|1[012])-(0[1-9]|[12]\d|3[01])$")


def get_seasons_from_file_name(file_name):
    return str(file_name[7:9]) + "-" + str(file_name[9:11])


def load_data(path):
    df = pd.DataFrame()
    path = fix_path(path)
    files = listdir(path)
    files = [x for x in files if '.json' in x]
    for file in files:
        f = open(path + file)
        data = json.load(f)
        aux = pd.json_normalize(data)
        aux['season'] = get_seasons_from_file_name(file)
        aux['H'] = 0
        aux['A'] = 0
        aux['D'] = 0
        aux.loc[aux["FTR"] == "H", "H"] = 3
        aux.loc[aux["FTR"] == "A", "A"] = 3
        aux.loc[aux["FTR"] == "D", "D"] = 1
        df = pd.concat([df, aux])
    return df


def attempt_to_fix_date(date):
    if pattern.match(date):
        return date
    else:
        try:
            return datetime.datetime.strptime(date, "%d/%m/%Y")
        except ValueError:
            return datetime.datetime.strptime(date, "%d/%m/%y")


def fix_date(df):
    df['Date'] = df['Date'].apply(attempt_to_fix_date).astype('datetime64[ns]')
    return df


def fix_path(path):
    if path[-1] != '/':
        path = path + '/'
    return path

