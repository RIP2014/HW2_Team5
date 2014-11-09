import abc
import pygame.rect
from obstacle import Obstacle

class Rectangle(Obstacle):

    def __init__(self, left, top, width, height):

        self.left = left
        self.top = top
        self.width = width
        self.height = height

    def contains(self, point):

        return pygame.rect.Rect(self.left, self.top, self.width, self.height).collidepoint(point.x, point.y)
