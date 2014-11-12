import pygame
from pygame.locals import *
from sys import exit
from time import sleep
from bug1 import BugOne, BugOneB
from point import Point
from circle import Circle

white = (255,255,255)
red = (255, 0, 0)
green = (0,255,0)
screen_size = (250, 250)
bugStart = Point(10, 190)
goal = Point(210, 175)
goalCircle = Circle(Point(goal.x, goal.y), 1)
obstacle1 = Circle(Point(50, 190), 20)
obstacle2 = Circle(Point(150,175), 35)
obstacles = [obstacle1, obstacle2]

bug = BugOneB(obstacles, bugStart, goal)
screen = pygame.display.set_mode(screen_size, 0, 32)

while not bug.at_goal():
    
    for event in pygame.event.get():
        if event.type == QUIT:
            exit()
            
    screen.fill((0,0,0))
    for obstacle in obstacles:
        pygame.draw.circle(screen, white, (obstacle.center.x, obstacle.center.y), obstacle.radius)


    bug.update()
    bugCircle = bug.get_drawable()
    pygame.draw.circle(screen, red, (int(bugCircle.center.x), int(bugCircle.center.y)), bugCircle.radius)
    pygame.draw.circle(screen, green, (goalCircle.center.x, goalCircle.center.y), 1)

    print "bug {0}x {1}y".format(bug.position.x, bug.position.y)
    print "goal {0}x {1}y".format(goal.x, goal.y)
    sleep(.1)
    pygame.display.update()

print "******* Competitive Ratio ********"
print bug.competitive_ratio()
