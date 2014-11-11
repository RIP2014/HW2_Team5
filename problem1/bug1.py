import abc
import math
from bug import Bug
from state import State
from point import Point
from vector import Vector2D

class BugOne(Bug):

    def __init__(self, obstacles, start, goal):

        self.found_closest_point = False
        self.circumnavigation_end_point = None
        self.circumnavigation_closest_point = None
        super(BugOne, self).__init__(obstacles, start, goal)
     
    def move(self):

        if self.state == State.Circumnavigation:

            if self.position == self.circumnavigation_end_point and not self.found_closest_point:
                self.circumnavigation_end_point = self.circumnavigation_closest_point
                self.found_closest_point = True
                self.visited_points = []

            elif self.position == self.circumnavigation_end_point and self.found_closest_point:

                self.state = State.Motion
                self.direction = Vector2D.from_points(self.position, self.goal)
                self.direction.normalize()
                self.found_closest_point = False



            elif not self.on_obstacle() or self.hasVisited():

                # go back one step, and turn 90 degrees
                print "circumnavigating but not on obstacle"
                self.position.x -= self.direction.x
                self.position.y -= self.direction.y
                self.visited_points.pop(len(self.visited_points)-1)
                print "am i now on object again? {0}".format(self.on_obstacle())

                new_heading = math.radians(round(math.degrees(math.atan2(self.direction.y, self.direction.x))) + 90)
                print "     OLD HEADING -> {0}"
                print "     NEW HEADING -> {0}".format(new_heading)
                self.direction = Vector2D(math.cos(new_heading), math.sin(new_heading))
                #self.direction.rotate(math.pi/2)

            else:

                if self.position.distance_to(self.goal) < self.circumnavigation_closest_point.distance_to(self.goal):
                    self.circumnavigation_closest_point = Point(self.position.x, self.position.y)

                self.visited_points.append(Point(self.position.x, self.position.y))
                self.position.x += self.direction.x
                self.position.y += self.direction.y
                self.distance_traveled += 1


        elif not self.on_obstacle():

            self.visited_points.append(Point(self.position.x, self.position.y))
            self.position.x += self.direction.x
            self.position.y += self.direction.y
            self.updateHeading()
            self.distance_traveled += 1
            self.state = State.Motion
            print "not on obstacle!"

        else:

                # new obstacle..begin circumnavigation
                new_heading = math.radians(round(math.degrees(math.atan2(self.direction.y, self.direction.x))) + 90)
                print "     NEW HEADING -> {0}".format(new_heading)
                self.direction = Vector2D(math.cos(new_heading), math.sin(new_heading))
                #self.direction.rotate(math.pi/2)
                self.visited_points.append(Point(self.position.x, self.position.y))
                # set 'end point' and variable for 'closest point'
                self.circumnavigation_end_point = Point(self.position.x, self.position.y)
                self.circumnavigation_closest_point = Point(self.position.x, self.position.y)
                self.position.x += 2 * self.direction.x
                self.position.y += 2 * self.direction.y
                self.distance_traveled += 1
                self.state = State.Circumnavigation

    def _circumnavigate_obstacle(self, obstacle, goal):

        # go around obstacle

        # keep track of closest point
        return False


