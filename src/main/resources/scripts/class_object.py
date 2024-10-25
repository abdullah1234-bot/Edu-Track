# class_object.py
import re

# A class object for each slot
class Class:
    def __init__(self, time_slot, instructor, course, room, weekday, index):
        self.time_slot = time_slot
        self.instructor = instructor
        self.course = course
        self.room = room
        self.weekday = weekday
        self.index = index

    def has_in_course(self, pattern):
        return pattern in self.course

    def to_dict(self):
        return {
            "time_slot": self.time_slot,
            "instructor": self.instructor,
            "course": self.course,
            "room": self.room,
            "weekday": self.weekday,
            "index": self.index,
        }

def isValidClassData(classDataString):
    # Checks if the cell is a class slot
    return isinstance(classDataString, str) and "\n" in classDataString.strip()

def isLabClassData(classDataString):
    # Checks if the word "LAB" exists in the cell
    return "lab" in re.split(r"\s|-", classDataString.lower())

def displayClassObjectList(classObjectList, patternList):
    pass  # Not needed for JSON output
