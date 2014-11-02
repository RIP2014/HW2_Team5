import abc

class Bug(object):
    __metaclass__ = abc.ABCMeta

    def __init__(self, world, start, goal):

        self.world = world
        self.position = start
        self.goal = goal
        self.distanceTraveled = 0

    def at_goal(self):

        return self.position == self.goal

    @abc.abstractmethod
    def move(self):
        """Moves the bug 'one unit of space' according to the chosen algorithm"""
    
    def _circumnavigate_obstacle(self, obstacle, goal):

        # go around obstacle

        # keep track of closest point
        return False
