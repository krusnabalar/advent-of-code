#!/bin/bash

# Specify the range of days
for ((day=1; day<=25; day++)); do
    # Format the day with leading zero if necessary
    formatted_day=$(printf "%02d" $day)

    # Define folder and file names
    folder_name="day$formatted_day"
    file_name="Day$formatted_day.java"
    package_statement="import java.nio.file.Files;"

    # Check if the file exists before attempting to modify
    if [ -e "$folder_name/$file_name" ]; then
        # Change the first line in the file
        sed -i "" "1s/.*/$package_statement/" "$folder_name/$file_name"
        echo "First line changed in $folder_name/$file_name"
    else
        echo "File not found: $folder_name/$file_name"
    fi
done
