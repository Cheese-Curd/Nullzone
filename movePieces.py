import os
import shutil

directory = r"./src/main/resources/data/nullzone/structures/nbt/abandoned_offices/"

for filename in os.listdir(directory):
    file_path = os.path.join(directory, filename)

    if os.path.isfile(file_path):
        name_without_ext = os.path.splitext(filename)[0]
        new_folder_path = os.path.join(directory, name_without_ext)
        os.makedirs(new_folder_path, exist_ok=True)
        shutil.move(file_path, os.path.join(new_folder_path, filename))

print("All files moved into their own folders!")
input("Press enter to dismiss.")
