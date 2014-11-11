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
        self.state = None

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


    def on_obstacle(self):

        collision = False
        for obstacle in self.obstacles:

            # do 4 courners checking here
            #upper_right_corner = Point(self.position.x + 1, self.position.y)
            #lower_left_corner = Point(self.position.x, self.position.y - 1)
            #lower_right_corner = Point(self.position.x + 1, self.position.y -1)
            #if obstacle.contains(self.position) or obstacle.contains(upper_right_corner) or obstacle.contains(lower_left_corner) or obstacle.contains(lower_right_corner):
            if obstacle.on_boundary(self.position):
                collision = True
            if obstacle.contains(self.position):
                return False

        return collision 
        
    def hasVisited(self):

        for visited in self.visited_points:

            if visited.x == self.position.x and visited.y == self.position.y:

                return True

        return False

    def get_drawable(self):
        
        return Circle(Point(self.position.x, self.position.y), 1)


