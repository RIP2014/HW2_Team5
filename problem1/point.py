import math

class Point(object):

    def __init__(self, x, y):

        self.x = x
        self.y = y

    def __str__(self):

        return "Point: {0} x, {1} y".format(self.x, self.y)

    def __eq__(self, point):

        return self.x == point.x and self.y == point.y

    def distance_to(self, point):

        return math.sqrt(math.pow(point.x - self.x, 2) + math.pow(point.y - self.y, 2))
