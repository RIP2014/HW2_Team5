import math

class Point(object):

    def __init__(self, x, y):

        self.x = x
        self.y = y

    def __str__(self):

        return "Point: {0} x, {1} y".format(self.x, self.y)

    def __eq__(self, point):

        return self.distance_to(point) <= 1 # approximation as sometimes point gets in loop of going back and forth around goal

    def distance_to(self, point):

        return math.sqrt((point.x - self.x)**2 + (point.y - self.y)**2)

    def to_screen_coordinates(self, screen_size):

        self.y = screen_size - self.y
