import abc
from obstacle import Obstacle

class Circle(Obstacle):

    def __init__(self, center, radius, color=(255, 255, 255)):

        self.center = center
        self.radius = radius
        self.color = color

    def contains(self, point):

        return ((point.x - center.x)**2 + (point.y - center.y)**2) < radius**2

    def to_screen_coordinates(self, screen_height):
        """Converts rectangle to coordinates matching that of a computer screen. X increases going right, Y increases downwards"""

        self.center.y = screen_height - self.center.y
