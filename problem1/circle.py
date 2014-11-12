import abc
from obstacle import Obstacle

class Circle(Obstacle):

    def __init__(self, center, radius, color=(255, 255, 255)):

        self.center = center
        self.radius = radius
        self.color = color

    def contains(self, point):

        return (((point.x - self.center.x)**2 + (point.y - self.center.y)**2) < self.radius**2)

    def on_boundary(self, point):

        distance = ((point.x - self.center.x)**2 + (point.y - self.center.y)**2)
        print "DIFFERENCE: {0}".format(distance - (self.radius)**2)
        return abs(distance - self.radius**2) < 40

    def to_screen_coordinates(self, screen_height):
        """Converts rectangle to coordinates matching that of a computer screen. X increases going right, Y increases downwards"""

        self.center.y = screen_height - self.center.y
