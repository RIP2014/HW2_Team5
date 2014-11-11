import math
import numpy as np

class Vector2D(object):

    def __init__(self, x, y):

        self.x = x
        self.y = y

    def __str__(self):

        return "Vector: ({0}, {1})".format(self.x, self.y)

    def __add__(self, vector):

        return Vector2D(self.x + vector.x, self.y + vector.y)

    def __sub__(self, vector):

        return Vector2D(self.x - vector.x, self.y - vector.y)

    def __neg__(self):

        return Vector2D(-self.x, -self.y)

    def rotate(self, angle):

        cos = round(math.cos(angle))
        sin = round(math.sin(angle))
        rotational_matrix = np.matrix(((cos, -sin), (sin, cos)))
        old_vector = np.matrix("{0};{1}".format(self.x, self.y))
        new_vector = (rotational_matrix * old_vector).tolist()
        self.x = new_vector[0][0]
        self.y = new_vector[1][0]

        

    def dot(self, vector):

        return (self.x * vector.x) + (self.y * vector.y)

    @classmethod
    def from_points(cls, p1, p2):

        return Vector2D(p2.x - p1.x, p2.y - p1.y)

    def get_magnitude(self):

        return math.sqrt(self.x**2 + self.y**2)

    def normalize(self):

        magnitude = self.get_magnitude()
        self.x /= magnitude
        self.y /= magnitude
