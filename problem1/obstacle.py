import abc

class Obstacle(object):
    __metaclass__ = abc.ABCMeta

    @abc.abstractmethod
    def contains(self, point):
        """Returns whether the given point is inside the obstacle"""
