import abc
from circle import Circle
from point import Point
from vector import Vector2D
from state import State

class Bug(object):
    __metaclass__ = abc.ABCMeta

    def __init__(self, obstacles, start, goal):

        self.obstacles = obstacles 
        self.position = start
        self.goal = goal
        self.direction = Vector2D.from_points(start, goal)
        self.direction.normalize()
        self.distance_traveled = 0
        self.visited_points = []
        self.state = State.Motion 
        self.euclidean_distance = self.position.distance_to(self.goal)

    def competitive_ratio(self):

        return self.distance_traveled / self.euclidean_distance

    def at_goal(self):

        return self.position == self.goal

    @abc.abstractmethod
    def move(self):
        """Moves the bug 'one unit of space' according to the chosen algorithm"""

    def updateHeading(self):

        self.direction = Vector2D.from_points(self.position, self.goal)
        self.direction.normalize()
    
    def update(self):

        if not self.at_goal():
            self.move()

    def near_edge(self):

        for x in xrange(3):
    
            for obstacle in self.obstacles:

                if obstacle.contains(Point(self.position.x + (x * self.direction.x), self.position.y + (x * self.direction.y))):

                    return True

        return False


    def in_obstacle(self):

        for obstacle in self.obstacles:
            if obstacle.contains(self.position):
                return True 

        return False
        
    def on_obstacle_boundary(self):

        for obstacle in self.obstacles:
            if obstacle.on_boundary(self.position):
                return True

        return False

    def hasVisited(self):

        for visited in self.visited_points:

            if visited.x == self.position.x and visited.y == self.position.y:

                return True

        return False

    def get_drawable(self):
        
        return Circle(Point(self.position.x, self.position.y), 1)


