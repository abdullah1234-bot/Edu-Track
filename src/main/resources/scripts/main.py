import time
import pandas as pd
import class_object as co
from class_object import Class
import json
import sys

fileName = "D:/sda project/project/project/src/main/resources/scripts/timetable.xlsx"
# def main():
#     # Check if a filename was provided
#     if len(sys.argv) > 1:
#         fileName = sys.argv[1]
#     else:
#         print("[-] No filename provided.", file=sys.stderr)
#         sys.exit(1)
# Read the timetable.xlsx file
try:
    timeTable = pd.ExcelFile(fileName)
except FileNotFoundError:
    print(f"[-] I couldn't find the file {fileName}.", file=sys.stderr)
    sys.exit(1)
except Exception as e:
    print(f"[-] An error occurred while reading the Excel file: {e}", file=sys.stderr)
    traceback.print_exc()
    sys.exit(1)

# Function to check if given string is a day name
def is_weekday(sheetName):
    weekdays = [
        "MONDAY",
        "TUESDAY",
        "WEDNESDAY",
        "THURSDAY",
        "FRIDAY",
        "SATURDAY",
        "SUNDAY",
    ]
    return sheetName.strip().upper() in weekdays

# Collect all class data
all_classes = []

# Loop over each sheet
for sheet in timeTable.sheet_names:
    # For sheets whose names are weekdays
    if is_weekday(sheet):
        dayClasses = []

        # Read complete excel sheet including the title columns/rows in this variable
        completeExcelSheet = timeTable.parse(sheet)
        completeExcelSheetArray = completeExcelSheet.to_numpy()

        # Read excel sheet excluding the title columns/rows in this variable
        dayTimeTable = timeTable.parse(sheet, header=3, index_col=0)
        classesArray = dayTimeTable.to_numpy()

        # Loop over every cell
        for i in range(len(classesArray)):
            row = classesArray[i]
            for j in range(len(row)):
                classData = row[j]

                # If the current slot is a valid class
                if co.isValidClassData(classData):
                    # Reading slot time and classroom and slot number (index)
                    classroom = completeExcelSheetArray[i + 3][0]
                    timeSlot = completeExcelSheetArray[1][j + 1]
                    index = completeExcelSheetArray[0][j + 1]

                    # Extracting more info from the class cell
                    instructor = str(classData).split("\n")[1].strip()
                    course = str(classData).split("\n")[0].strip()

                    # (This next part accounts for the merged cells used for lab classes)
                    # Checking if current cell is a lab class
                    if co.isLabClassData(classData):
                        # Update the timeslot duration to extend to 3 slots
                        # Get the timeslot for the last lab slot
                        labLastTimeSlot = completeExcelSheetArray[1][(j + 1) + 2]
                        # New timeslot = the starting time of the first slot and the ending time of the last slot
                        timeSlot = f"{timeSlot.split('-')[0].strip()}-{labLastTimeSlot.split('-')[1].strip()}"

                    # Create a class object using the classData
                    currentClass = Class(
                        time_slot=timeSlot,
                        instructor=instructor,
                        room=classroom,
                        weekday=sheet,  # sheet name is weekday
                        index=index,
                        course=course,
                    )

                    # Append class data to the list
                    all_classes.append(currentClass.to_dict())

# Output the data as JSON
print(json.dumps(all_classes))
