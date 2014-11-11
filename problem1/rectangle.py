import abc
import pygame.rect
from obstacle import Obstacle
from vector import Vector2D
from point import Point

class Rectangle(Obstacle):

    def __init__(self, left, top, width, height):

        self.left = left
        self.top = top
        self.width = width
        self.height = height

    def contains(self, point):

        #return pygame.rect.Rect(self.left, self.top, self.width, self.height).collidepoint(point.x, point.y)
        
        # p = point, a,b,d = corners on rectangles
        top_left = Point(self.left, self.top)
        top_right = Point(self.left + self.width, self.top)
        bottom_left = Point(self.left, self.top + self.height)
        tLeft_to_point = Vector2D.from_points(top_left, point)
        tLeft_to_tRight = Vector2D.from_points(top_left, top_right)
        tLeft_to_bLeft = Vector2D.from_points(top_left, bottom_left)

        return (0 < tLeft_to_point.dot(tLeft_to_tRight) and tLeft_to_point.dot(tLeft_to_tRight) <  tLeft_to_tRight.dot(tLeft_to_tRight)) and (0 < tLeft_to_point.dot(tLeft_to_bLeft) and tLeft_to_point.dot(tLeft_to_bLeft) < tLeft_to_bLeft.dot(tLeft_to_bLeft))

    def on_boundary(self, point):

        on_top_side = point.y == self.top and point.x >= self.left and point.x <= (self.left + self.width)
        on_bottom_side = point.y == (self.top + self.height) and point.x >= self.left and point.x <= (self.left + self.width)
        on_left_side = point.x == self.left and point.y <= (self.top + self.height) and point.y >= self.top
        on_right_side = point.x == (self.left + self.width) and point.y <= (self.top + self.height) and point.y >= self.top
        return on_top_side or on_bottom_side or on_left_side or on_right_side
    def get_drawable(self):

        return pygame.rect.Rect(self.left, self.top, self.width, self.height)

    def to_screen_coordinates(self, screen_height):
        """Converts rectangle to coordinates matching that of a computer screen. X increases going right, Y increases downwards"""
        
        self.top = screen_height - self.top
