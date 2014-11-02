import pygame.rect

import Point

class Obstacle(object):

    def __init__(self, shape):
        self.shape = shape

    def collides_with_point(point):
        return self.shape.collidepoint(point.x, point.y)
