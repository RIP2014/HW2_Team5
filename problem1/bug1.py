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



            elif self.in_obstacle() or not self.on_obstacle_boundary() or self.hasVisited():

                # go back one step, and turn 90 degrees
                print "circumnavigating but not on boundary of  obstacle, or inside.or repeat point"
                self.position.x -= self.direction.x
                self.position.y -= self.direction.y
                self.visited_points.pop(len(self.visited_points)-1)

                new_heading = math.radians(round(math.degrees(math.atan2(self.direction.y, self.direction.x))) + 90)
                print "     OLD HEADING -> {0}"
                print "     NEW HEADING -> {0}".format(new_heading)
                self.direction = Vector2D(math.cos(new_heading), math.sin(new_heading))
                #self.direction.rotate(math.pi/2)

            else:

                print "normal circumnavigate"
                if self.position.distance_to(self.goal) < self.circumnavigation_closest_point.distance_to(self.goal):
                    self.circumnavigation_closest_point = Point(self.position.x, self.position.y)

                self.visited_points.append(Point(self.position.x, self.position.y))
                self.position.x += self.direction.x
                self.position.y += self.direction.y
                self.distance_traveled += 1


        elif not self.in_obstacle() and not self.on_obstacle_boundary():

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

class BugOneB(Bug):

    def __init__(self, obstacles, start, goal):

        self.circumnavigation_travel = 0
        self.circumnavigation_perimeter = 0
        self.circumnavigation_start = None
        self.circumnavigation_obstacle = None
        self.found_closest_point = False
        self.circumnavigation_end_point = None
        self.circumnavigation_closest_point = None
        super(BugOneB, self).__init__(obstacles, start, goal)

    def scan_for_obstacles(self):
    
        close_obstacle = None
        for obstacle in self.obstacles:

            if obstacle.on_boundary(self.position):

                close_obstacle = obstacle

        return close_obstacle

    def move(self):

        if self.state == State.Motion:

            scan_result = self.scan_for_obstacles()

            if scan_result == None:

                self.visited_points.append(Point(self.position.x, self.position.y))
                self.position.x += self.direction.x
                self.position.y += self.direction.y
                self.updateHeading()
                self.distance_traveled += 1

            else:

                # in range of an obstacle
                self.circumnavigation_obstacle = scan_result
                self.state = State.Circumnavigation
                self.found_closest_point = False
                self.circumnavigation_end_point = Point(self.position.x, self.position.y)
                self.circumnavigation_closest_point = Point(self.position.x, self.position.y)
                self.circumnavigation_travel = 0
                self.circumnavigation_perimeter = 2 * math.pi * self.circumnavigation_obstacle.radius
                self.circumnavigation_start = Point(self.position.x, self.position.y)
                self.move_tangent_line()
        
        else:

            # circumnavigating

            if self.circumnavigation_travel > self.circumnavigation_perimeter:
                self.position.x = self.circumnavigation_start.x
                self.position.y = self.circumnavigation_start.y
                self.circumnavigation_travel = 0
                
            if self.position == self.circumnavigation_end_point and not self.found_closest_point:
                self.circumnavigation_end_point = self.circumnavigation_closest_point
                self.found_closest_point = True
                self.visited_points = []

            elif self.position == self.circumnavigation_end_point and self.found_closest_point:

                self.state = State.Motion
                self.direction = Vector2D.from_points(self.position, self.goal)
                self.direction.normalize()
                self.circumnavigation_travel = 0
                self.circumnavigation_perimeter = 0
                self.circumnavigation_start = None
                self.circumnavigation_obstacle = None
                self.found_closest_point = False
                self.circumnavigation_end_point = None
                self.circumnavigation_closest_point = None

            else:

                # normal circumnavigation
                self.move_tangent_line()


    def move_tangent_line(self):

        print "IN TANGENT LINE"
        slope_to_center = (-self.circumnavigation_obstacle.center.y + self.position.y) / (-self.circumnavigation_obstacle.center.x + self.position.x)
        tangent_line_slope = (-1/slope_to_center)
        if (self.position.y > self.circumnavigation_obstacle.center.y):
            self.direction = Vector2D(1, tangent_line_slope)

        elif (self.position.y < self.circumnavigation_obstacle.center.y and self.position.x < self.circumnavigation_obstacle.center.x):
            self.direction = Vector2D(-1, abs(tangent_line_slope))

        else:
            self.direction = Vector2D(-1, -abs(tangent_line_slope))
        self.direction.normalize()
        self.visited_points.append(Point(self.position.x, self.position.y))
        self.distance_traveled += 1
        self.circumnavigation_travel += 1
        if (self.position.distance_to(self.goal) < self.circumnavigation_closest_point.distance_to(self.goal)):
            self.circumnavigation_closest_point = Point(self.position.x, self.position.y)
        self.position.x += self.direction.x
        self.position.y += self.direction.y

class BugTwo(Bug):

    def __init__(self, obstacles, start, goal):

        self.jump_points = []
        slope_vector = Vector2D.from_points(start, goal)
        if slope_vector.x == 0:
            self.slope = None
        else:
            self.slope = slope_vector.y/slope_vector.x
        self.point = Point(goal.x, goal.y)
        super(BugTwo, self).__init__(obstacles, start, goal)

    def on_m_line(self):

        if not self.slope:
            return self.position.x == self.point.x
        else:
            y_side = self.position.y - self.point.y
            x_side = self.slope * (self.position.x - self.point.x)

        print "M-LINE ??? --> {0}".format(y_side == x_side)
        return y_side == x_side

    def point_on_boundaries(self, point):

        for obstacle in self.obstacles:
            if obstacle.contains(point) or obstacle.on_boundary(point):
                return True

        return False

    def clear_path(self):

        # get vector from point to goal
        heading_to_goal = Vector2D.from_points(self.position, self.goal)
        heading_to_goal.normalize()
        point = Point(self.position.x + heading_to_goal.x, self.position.y + heading_to_goal.y)
        # check next point if in obstacle
        print "clear_path ??? {0}".format(not self.point_on_boundaries(point))
        return not self.point_on_boundaries(point)

    def move(self):

        if self.state == State.Circumnavigation:

            if (self.on_m_line()) and self.clear_path() and not (self.position.x, self.position.y) in self.jump_points:

                self.state = State.Motion
                self.direction = Vector2D.from_points(self.position, self.goal)
                self.direction.normalize()
                self.jump_points.append((self.position.x, self.position.y))
                self.position.x += self.direction.x
                self.position.y += self.direction.y

            elif self.in_obstacle() or not self.on_obstacle_boundary() or self.hasVisited():

                # go back one step, and turn 90 degrees
                print "circumnavigating but not on boundary of  obstacle, or inside.or repeat point"
                self.position.x -= self.direction.x
                self.position.y -= self.direction.y
                self.visited_points.pop(len(self.visited_points)-1)

                new_heading = math.radians(round(math.degrees(math.atan2(self.direction.y, self.direction.x))) + 90)
                print "     NEW HEADING -> {0}".format(new_heading)
                self.direction = Vector2D(math.cos(new_heading), math.sin(new_heading))

            else:

                print "normal circumnavigate"
                self.visited_points.append(Point(self.position.x, self.position.y))
                self.position.x += self.direction.x
                self.position.y += self.direction.y
                self.distance_traveled += 1


        elif not self.in_obstacle() and not self.on_obstacle_boundary():

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
                self.position.x += 2 * self.direction.x
                self.position.y += 2 * self.direction.y
                self.distance_traveled += 1
                self.state = State.Circumnavigation


class BugTwoB(Bug):

    def __init__(self, obstacles, start, goal):

        self.jump_points = []
        slope_vector = Vector2D.from_points(start, goal)
        if slope_vector.x == 0:
            self.slope = None
        else:
            self.slope = slope_vector.y/slope_vector.x
        self.point = Point(goal.x, goal.y)
        self.circumnavigation_obstacle = None
        super(BugTwoB, self).__init__(obstacles, start, goal)

    def on_m_line(self):

        if not self.slope:
            return self.position.x == self.point.x
        else:
            y_side = self.position.y - self.point.y
            x_side = self.slope * (self.position.x - self.point.x)
        return y_side - x_side < 2

    def point_on_boundaries(self, point):

        for obstacle in self.obstacles:
            if obstacle.contains(point) or obstacle.on_boundary(point):
                return True

        return False

    def clear_path(self):

        # get vector from point to goal
        heading_to_goal = Vector2D.from_points(self.point, self.goal)
        #heading_to_goal.normalize()
        point = Point(self.position.x + heading_to_goal.x, self.position.y + heading_to_goal.y)
        # check next point if in obstacle
        print "clear_path ??? {0}".format(not self.point_on_boundaries(point))
        return not self.point_on_boundaries(point)

    def scan_for_obstacles(self):
    
        close_obstacle = None
        for obstacle in self.obstacles:

            if obstacle.on_boundary(self.position):

                close_obstacle = obstacle

        return close_obstacle

    def move(self):

        if self.state == State.Motion:

            scan_result = self.scan_for_obstacles()

            if scan_result == None:

                self.visited_points.append(Point(self.position.x, self.position.y))
                self.position.x += self.direction.x
                self.position.y += self.direction.y
                self.updateHeading()
                self.distance_traveled += 1

            else:

                # in range of an obstacle
                self.circumnavigation_obstacle = scan_result
                self.state = State.Circumnavigation
                self.move_tangent_line()
        
        else:

            # circumnavigating


            # check for on m-line
            if self.on_m_line() and self.clear_path():
                self.visited_points = []
                self.state = State.Motion
                self.direction = Vector2D.from_points(self.position, self.goal)
                self.direction.normalize()
                self.circumnavigation_obstacle = None

            else:

                # normal circumnavigation
                self.move_tangent_line()


    def move_tangent_line(self):

        print "IN TANGENT LINE"
        slope_to_center = (-self.circumnavigation_obstacle.center.y + self.position.y) / (-self.circumnavigation_obstacle.center.x + self.position.x)
        tangent_line_slope = (-1/slope_to_center)
        if (self.position.y > self.circumnavigation_obstacle.center.y):
            self.direction = Vector2D(1, tangent_line_slope)

        elif (self.position.y < self.circumnavigation_obstacle.center.y and self.position.x < self.circumnavigation_obstacle.center.x):
            self.direction = Vector2D(-1, abs(tangent_line_slope))

        else:
            self.direction = Vector2D(-1, -abs(tangent_line_slope))
            
        self.direction.normalize()
        self.visited_points.append(Point(self.position.x, self.position.y))
        self.distance_traveled += 1
        self.position.x += self.direction.x
        self.position.y += self.direction.y


