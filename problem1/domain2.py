import pygame
from pygame.locals import *
from sys import exit, argv
from time import sleep
from bug1 import BugOne, BugTwo
from circle import Circle
from point import Point
from rectangle import Rectangle


def simulate(bugNum):

    white = (255,255,255)
    red = (255, 0, 0)
    green = (0,255,0)
    screen_size = (250, 250)
    bugStart = Point(45, 73) # actual Y is 157
    goal = Point(45, 213)

    #= [(0, 147, 5, 147), (0, 147,40,7), (0,119,35,7), (0,91,35,7), (0,63,35,7), (0,35,35,7), (0,7,80,7),(20,49,60,7), (20, 77, 60,7), (20,105,60,7), (20,133,60,7), (75,133,5,133)]
    #rectangleData = [(20, 83, 5, 147),
    #(25, 83, 35, 7),
    #(25, 111, 30, 7),
    #(25, 139, 30, 7),
    #(25, 167, 30, 7),
    #(25, 195, 30, 7),
    #(25, 223, 70, 7),
    #(40, 181, 55, 7),
    #(40, 153, 55, 7),
    #(40, 125, 55, 7),
    #(40, 97, 55, 7),
    #(95, 97, 5, 133)]

    rectangleData = [(20, 83, 5, 147),
    (20, 83, 40, 7),
    (20, 111, 35, 7),
    (20, 139, 35, 7),
    (20, 167, 35, 7),
    (20, 195, 35, 7),
    (20, 223, 80, 7),
    (40, 181, 60, 7),
    (40, 153, 60, 7),
    (40, 125, 60, 7),
    (40, 97, 60, 7),
    (95, 97, 5, 133)]


    obstacles = []
    #obstacle_visuals = []

    for rec in rectangleData:

        rectangle_object = Rectangle(rec[0], rec[1], rec[2], rec[3])
        obstacles.append(rectangle_object)

    #for rec in rectangleData:

    #    rectangle_object = Rectangle(rec[0], rec[1], rec[2], rec[3])
    #    rectangle_object.to_screen_coordinates(screen_size[1])
        # for visual purposes, center objects
    #    rectangle_object.top -= 20
    #    rectangle_object.left += 20
    #    obstacle_visuals.append(rectangle_object)

    if (bugNum == 1):
        bug = BugOne(obstacles, bugStart, goal)
    else:
        bug = BugTwo(obstacles, bugStart, goal)
    screen = pygame.display.set_mode(screen_size, 0, 32)


    goalCircle = Circle(Point(goal.x, goal.y), 1)
    #goalCircle.to_screen_coordinates(screen_size[1])
    #goalCircle.center.x += 20

    while not bug.at_goal():

        for event in pygame.event.get():
            if event.type == QUIT:
                exit()


        screen.fill((0,0,0))
        for obstacle in obstacles:
            pygame.draw.rect(screen, white, obstacle.get_drawable())
        bug.update()
        bugCircle = bug.get_drawable()
    #    bugCircle.to_screen_coordinates(screen_size[1])
    #    bugCircle.center.y -= 20
    #    bugCircle.center.x += 20
        pygame.draw.circle(screen, red, (int(bugCircle.center.x), int(bugCircle.center.y)), bugCircle.radius)
        pygame.draw.circle(screen, green, (goalCircle.center.x, goalCircle.center.y), 1)
        print "bug {0}x {1}y".format(bug.position.x, bug.position.y)
        print "goal {0}x {1}y".format(goal.x, goal.y)
        sleep(.01)
        pygame.display.update()

    print "******* Competitive Ratio ********"
    print bug.competitive_ratio()

if __name__ == "__main__":
    simulate(int(argv[1]))

