# csc413-zombiegame
<img src="ZombieGame/resources/vfx/title.png" width="300">

| Student Information |                 |
|:-------------------:|-----------------|
|  Student Name       | Roxana del Toro |
|  Student Email      | rxdt@sfsu.edu   |


## Purpose of jar Folder 
The jar folder will store the built jar `jar/tankgame-rxdt.jar`

`THIS FOLDER CAN NOT BE DELETED OR MOVED`

# Required Information when Submitting Tank Game

## Version of Java Used: 24.0.1

## IDE used: 
### IntelliJ IDEA 2025.1.2 (Ultimate Edition) on Macbook Pro

## Steps to Import project into IDE:
`git clone git@github.com:csc413-SFSU-SU2025/tankgame-rxdt.git`

## Steps to Manually Build Project:
```
>  cd tankgame-rxdt
>  mkdir -p out
>  javac -d out $(find ZombieGame/src -name "*.java")
>  cp -r ZombieGame/resources/* out/
```
## Steps to Build the JAR
```
>  mkdir -p jar
>  jar cfm jar/tankgame-rxdt.jar manifest.txt -C out .
```
## Steps to Run Project:
```
>  java -cp out zombiegame.Launcher
```
## Alternate steps to run the project - run the JAR directly
```
>  java -jar jar/tankgame-rxdt.jar
```

## Controls to play Zombie Wars:

|               | Player 1 - Green | Player 2 - Red |
|---------------|------------------|----------------|
|  Forward      | W                | up arrow       |
|  Backward     | S                | down arrow     |
|  Rotate left  | A                | left arrow     |
|  Rotate Right | D                | right arrow    |
|  Shoot        | space            | enter          |

## Power Ups
#### HEALTH
Eat a brain and gain 35 health points <br>
<img src="ZombieGame/resources/vfx/health_brain_powerup.png" width="100"> 

#### SHIELD
Pick up a shield and become invincible to an opponent's bullets <br>
<img src="ZombieGame/resources/vfx/shield_injection_powerup.png" width="100"> 

#### SPEED
3X your speed with a battery boost <br>
<img src="ZombieGame/resources/vfx/speed_potion_powerup.png" width="100"> 

#### LASER BEAMS
Damage your opponent even more with laser ammo!<br>
<img src="ZombieGame/resources/vfx/ammo.png" width="50">

## Breakable Walls - Flowers 
<img src="ZombieGame/resources/vfx/sunflower.png" width="50"><img src="ZombieGame/resources/vfx/daisies.png" width="50"><img src="ZombieGame/resources/vfx/blue_flowers.png" width="50"><img src="ZombieGame/resources/vfx/roses.png" width="50">

## Non-breakable Walls - Trees and Shrubs
<img src="ZombieGame/resources/vfx/bush.png" width="100"><img src="ZombieGame/resources/vfx/log.png" width="75"><img src="ZombieGame/resources/vfx/trees.png" width="50">

## Extra!
####
If you don't move your zombie for more than 30 seconds it will grumble and start breathing heavily.
#### Secret Code
Green Zombie 1 Easter Egg `W W S S A D A D B A`<br>
Red Zombie 2 Easter Egg `↑ ↑ ↓ ↓ ← → ← → B A`